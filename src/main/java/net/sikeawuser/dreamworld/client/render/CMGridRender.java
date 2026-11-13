package net.sikeawuser.dreamworld.client.render;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import net.sikeawuser.dreamworld.DreamMod;
import net.sikeawuser.dreamworld.registry.DMDimensions;

@Mod.EventBusSubscriber(modid = DreamMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CMGridRender {

    private static final float FLOOR_RED_Y = -15.0F;
    private static final float FLOOR_BLUE_Y = -30.0F;
    private static final float CEILING_RED_Y = 90.0F;
    private static final float CEILING_BLUE_Y = 150.0F;

    private static final int HALF = 2048;
    private static final float GRID_FOG_START = 150.0F;
    private static final float GRID_FOG_END = 1800.0F;

    private static final float FAR_PLANE = 4000.0F;

    private static final int RED_SPACING = 16;
    private static final int BLUE_SPACING = 16;
    private static final int BLUE_OFFSET = 4;
    private static final int RECENTER_SNAP = 32;
    private static final float WIDEN = 1.0F / 300.0F;

    private record Style(float r, float g, float b, float coreHalf, float glowHalf, float coreAlpha, float shoulderAlpha) {}

    private static final Style RED = new Style(1.00F, 0.15F, 0.25F, 0.30F, 0.65F, 0.60F, 0.20F);

    private static final Style BLUE = new Style(0.20F, 0.35F, 1.00F, 0.25F, 0.55F, 0.50F, 0.18F);

    private static VertexBuffer redBuffer;
    private static VertexBuffer blueBuffer;

    @SubscribeEvent
    public static void onRenderGrid(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) {return;}

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || !mc.level.dimension().equals(DMDimensions.DREAMWORLD)) {return;}

        // Built once, on first entry (expect a brief one-time hitch).
        if (redBuffer == null) {
            redBuffer = buildGrid(RED, RED_SPACING, 0);
            blueBuffer = buildGrid(BLUE, BLUE_SPACING, BLUE_OFFSET);
        }

        Vec3 camPos = event.getCamera().getPosition();
        double snappedX = Math.floor(camPos.x / RECENTER_SNAP) * RECENTER_SNAP;
        double snappedZ = Math.floor(camPos.z / RECENTER_SNAP) * RECENTER_SNAP;

        PoseStack poseStack = event.getPoseStack();
        Matrix4f projection = farProjection(event.getProjectionMatrix());

        // Save vanilla's fog state so terrain fog is never disturbed.
        float prevStart = RenderSystem.getShaderFogStart();
        float prevEnd = RenderSystem.getShaderFogEnd();
        float[] prevColor = RenderSystem.getShaderFogColor().clone();
        FogShape prevShape = RenderSystem.getShaderFogShape();

        RenderSystem.setShaderFogStart(GRID_FOG_START);
        RenderSystem.setShaderFogEnd(GRID_FOG_END);
        // Fade to BLACK: the sky behind is black, so far ribbons vanish.
        RenderSystem.setShaderFogColor(0.0F, 0.0F, 0.0F, 1.0F);
        RenderSystem.setShaderFogShape(FogShape.SPHERE);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getRendertypeLightningShader);

        drawLayer(redBuffer, poseStack, projection, camPos, snappedX, FLOOR_RED_Y, snappedZ);
        drawLayer(blueBuffer, poseStack, projection, camPos, snappedX, FLOOR_BLUE_Y, snappedZ);
        drawLayer(redBuffer, poseStack, projection, camPos, snappedX, CEILING_RED_Y, snappedZ);
        drawLayer(blueBuffer, poseStack, projection, camPos, snappedX, CEILING_BLUE_Y, snappedZ);

        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();

        RenderSystem.setShaderFogStart(prevStart);
        RenderSystem.setShaderFogEnd(prevEnd);
        RenderSystem.setShaderFogColor(prevColor[0], prevColor[1], prevColor[2], prevColor[3]);
        RenderSystem.setShaderFogShape(prevShape);
    }

    private static Matrix4f farProjection(Matrix4f vanilla) {
        float m00 = vanilla.m00();
        float m11 = vanilla.m11();
        float fovY = 2.0F * (float) Math.atan(1.0D / m11);
        float aspect = m11 / m00;
        return new Matrix4f().setPerspective(fovY, aspect, 0.1F, FAR_PLANE);
    }

    private static void drawLayer(VertexBuffer buffer, PoseStack poseStack, Matrix4f projection, Vec3 camPos, double snappedX, float y, double snappedZ) {
        poseStack.pushPose();
        poseStack.translate(snappedX - camPos.x, y - camPos.y, snappedZ - camPos.z);

        buffer.bind();
        buffer.drawWithShader(poseStack.last().pose(), projection, RenderSystem.getShader());
        VertexBuffer.unbind();

        poseStack.popPose();
    }

    private static VertexBuffer buildGrid(Style style, int spacing, int offset) {
        BufferBuilder bb = Tesselator.getInstance().getBuilder();
        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        for (int line = -HALF + offset; line <= HALF; line += spacing) {
            int t = -HALF;
            while (t < HALF) {
                int step = stepFor(line, t);
                addSegment(bb, true, line, t, t + step, style);   // runs along Z
                addSegment(bb, false, line, t, t + step, style);  // runs along X
                t += step;
            }
        }

        VertexBuffer buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        buffer.bind();
        buffer.upload(bb.end());
        VertexBuffer.unbind();
        return buffer;
    }

    private static int stepFor(int line, int t) {
        int d = Math.max(Math.abs(line), Math.abs(t));
        if (d < 128) {return 8;}
        if (d < 512) {return 32;}
        return 128;
    }

    private static float widen(float fixed, float along) {
        float dist = (float) Math.sqrt(fixed * fixed + along * along);
        return 1.0F + dist * WIDEN;
    }

    private static void addSegment(BufferBuilder bb, boolean alongZ, float fixed, float t1, float t2, Style s) {
        float[] off = {-s.glowHalf, -s.coreHalf, 0.0F, s.coreHalf, s.glowHalf};
        float[] a = {0.0F, s.shoulderAlpha, s.coreAlpha, s.shoulderAlpha, 0.0F};
        float k1 = widen(fixed, t1);
        float k2 = widen(fixed, t2);

        for (int i = 0; i < 4; i++) {
            vert(bb, alongZ, fixed + off[i] * k1, t1, s, a[i]);
            vert(bb, alongZ, fixed + off[i + 1] * k1, t1, s, a[i + 1]);
            vert(bb, alongZ, fixed + off[i + 1] * k2, t2, s, a[i + 1]);
            vert(bb, alongZ, fixed + off[i] * k2, t2, s, a[i]);
        }
    }

    private static void vert(BufferBuilder bb, boolean alongZ, float perp, float along, Style s, float alpha) {
        if (alongZ) {
            bb.vertex(perp, 0.0D, along);
        } else {
            bb.vertex(along, 0.0D, perp);
        }
        bb.color(s.r, s.g, s.b, alpha).endVertex();
    }
}
