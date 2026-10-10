package net.sikeawuser.dreamworld.client.render;

import net.sikeawuser.dreamworld.DreamMod;
import net.sikeawuser.dreamworld.registry.DMDimensions;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraftforge.eventbus.api.EventPriority;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = DreamMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CosmicSkyRender {

    private static final float R = 90.0F;
    private static final float SKY_BRIGHTNESS = 0.15F;
    private static final boolean MOON_ENABLED = true;
    private static final float MOON_YAW = 20.0F;
    private static final float MOON_PITCH = 12.0F;
    private static final float MOON_SIZE = 5.0F;

    private static final int STAR_COUNT = 2000;
    private static final int FLARE_COUNT = 72;
    private static final int NEBULA_COUNT = 15;

    private static final float DRIFT_SPEED = 0.004F;

    private static VertexBuffer fixedBuffer;
    private static VertexBuffer driftBuffer;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRenderSky(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || !mc.level.dimension().equals(DMDimensions.DREAMWORLD)) {
            return;
        }

        if (fixedBuffer == null) {
            fixedBuffer = buildFixed();
            driftBuffer = buildDrift();
        }

        float time = mc.level.getGameTime() + event.getPartialTick();
        PoseStack poseStack = event.getPoseStack();

        RenderSystem.enableBlend();

        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        fixedBuffer.bind();
        fixedBuffer.drawWithShader(poseStack.last().pose(), event.getProjectionMatrix(), RenderSystem.getShader());
        VertexBuffer.unbind();

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(time * DRIFT_SPEED));
        poseStack.mulPose(Axis.XP.rotationDegrees(15.0F));
        driftBuffer.bind();
        driftBuffer.drawWithShader(poseStack.last().pose(), event.getProjectionMatrix(), RenderSystem.getShader());
        VertexBuffer.unbind();
        poseStack.popPose();

        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    private static VertexBuffer buildFixed() {
        BufferBuilder b = Tesselator.getInstance().getBuilder();
        b.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);

        addDome(b);

        if (MOON_ENABLED) {
            double[] m = dir(MOON_YAW, MOON_PITCH);
            addDisc(b, m, 38.0F, 40, 0.04F, 0.09F, 0.40F, 0, 0, 0);
            addDisc(b, m, 14.0F, 40, 0.22F, 0.36F, 0.95F, 0, 0, 0);
            addDisc(b, m, MOON_SIZE * 1.6F, 32, 0.65F, 0.75F, 1.00F, 0, 0, 0);
            addDisc(b, m, MOON_SIZE, 32, 1.00F, 1.00F, 1.00F, 0.80F, 0.86F, 1.00F);
        }

        return upload(b);
    }

    private static VertexBuffer buildDrift() {
        BufferBuilder b = Tesselator.getInstance().getBuilder();
        b.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
        RandomSource rnd = RandomSource.create(10842L);
        float[][] palette = {
                {0.05F, 0.12F, 0.50F},
                {0.00F, 0.28F, 0.34F},
                {0.26F, 0.05F, 0.38F},
                {0.10F, 0.10F, 0.45F}
        };
        for (int n = 0; n < NEBULA_COUNT; n++) {
            float yaw = rnd.nextFloat() * 360.0F;
            float pitch = (rnd.nextFloat() - 0.5F) * 140.0F;
            float[] c = palette[rnd.nextInt(palette.length)];
            for (int k = 0; k < 6; k++) {
                double[] d = dir(yaw + (rnd.nextFloat() - 0.5F) * 28.0F,
                        pitch + (rnd.nextFloat() - 0.5F) * 20.0F);
                float size = 7.0F + rnd.nextFloat() * 10.0F;
                float s = 0.35F + rnd.nextFloat() * 0.35F;
                addDisc(b, d, size, 20, c[0] * s, c[1] * s, c[2] * s, 0, 0, 0);
            }
        }

        for (int i = 0; i < STAR_COUNT; i++) {
            double[] d = dir(rnd.nextFloat() * 360.0F, (float) Math.toDegrees(Math.asin(rnd.nextFloat() * 2.0F - 1.0F)));
            float size = 0.12F + rnd.nextFloat() * 0.25F;
            float v = 0.35F + rnd.nextFloat() * 0.65F;
            float[] tint = starTint(rnd);
            addDisc(b, d, size, 4, tint[0] * v, tint[1] * v, tint[2] * v, tint[0] * v, tint[1] * v, tint[2] * v);
        }

        for (int i = 0; i < FLARE_COUNT; i++) {
            double[] d = dir(rnd.nextFloat() * 360.0F, (float) Math.toDegrees(Math.asin(rnd.nextFloat() * 2.0F - 1.0F)));
            float len = 0.8F + rnd.nextFloat() * 1.8F;
            float[] tint = starTint(rnd);
            addFlare(b, d, len, tint[0], tint[1], tint[2]);
        }

        return upload(b);
    }

    private static VertexBuffer upload(BufferBuilder b) {
        VertexBuffer buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        buffer.bind();
        buffer.upload(b.end());
        VertexBuffer.unbind();
        return buffer;
    }

    private static void addDome(BufferBuilder b) {
        final int stacks = 16;
        final int slices = 32;
        for (int i = 0; i < stacks; i++) {
            float p0 = -90.0F + 180.0F * i / stacks;
            float p1 = -90.0F + 180.0F * (i + 1) / stacks;
            for (int j = 0; j < slices; j++) {
                float y0 = 360.0F * j / slices;
                float y1 = 360.0F * (j + 1) / slices;
                double[] a = scale(dir(y0, p0), R);
                double[] bb = scale(dir(y1, p0), R);
                double[] c = scale(dir(y1, p1), R);
                double[] d = scale(dir(y0, p1), R);
                float[] c0 = domeColor(p0);
                float[] c1 = domeColor(p1);
                v(b, a, c0);
                v(b, bb, c0);
                v(b, c, c1);
                v(b, a, c0);
                v(b, c, c1);
                v(b, d, c1);
            }
        }
    }

    private static float[] domeColor(float pitchDeg) {
        float t = (float) Math.pow(Math.abs(pitchDeg) / 90.0F, 0.7D);
        float r = lerp(0.03F, 0.00F, t);
        float g = lerp(0.02F, 0.00F, t);
        float bl = lerp(0.10F, 0.04F, t);
        return new float[]{r, g, bl};
    }

    private static void addDisc(BufferBuilder b, double[] dir, float angRadiusDeg, int segs, float cr, float cg, float cb, float er, float eg, float eb) {
        double[][] basis = basis(dir);
        double[] right = basis[0];
        double[] up = basis[1];
        double[] centre = scale(dir, R);
        double rad = R * Math.tan(Math.toRadians(angRadiusDeg));

        for (int i = 0; i < segs; i++) {
            double a0 = 2.0D * Math.PI * i / segs;
            double a1 = 2.0D * Math.PI * (i + 1) / segs;
            double[] p0 = ring(centre, right, up, a0, rad);
            double[] p1 = ring(centre, right, up, a1, rad);
            v(b, centre, new float[]{cr, cg, cb});
            v(b, p0, new float[]{er, eg, eb});
            v(b, p1, new float[]{er, eg, eb});
        }
    }

    private static void addFlare(BufferBuilder b, double[] dir, float lenDeg, float r, float g, float bl) {
        double[][] basis = basis(dir);
        double[] right = basis[0];
        double[] up = basis[1];
        double[] centre = scale(dir, R);
        double len = R * Math.tan(Math.toRadians(lenDeg));
        double w = len * 0.07D;

        double[][] arms = {right, up, scale(right, -1.0D), scale(up, -1.0D)};
        for (int i = 0; i < 4; i++) {
            double[] arm = arms[i];
            double[] side = arms[(i + 1) % 4];
            double[] tip = add(centre, scale(arm, len));
            double[] b1 = add(centre, scale(side, w));
            double[] b2 = add(centre, scale(side, -w));
            float[] bright = {r, g, bl};
            float[] dark = {0, 0, 0};
            v(b, b1, bright);
            v(b, tip, dark);
            v(b, b2, bright);
        }

        addDisc(b, dir, lenDeg * 0.18F, 8, 1.0F, 1.0F, 1.0F, r * 0.6F, g * 0.6F, bl * 0.6F);
    }

    private static float[] starTint(RandomSource rnd) {
        return switch (rnd.nextInt(4)) {
            case 0 -> new float[]{1.00F, 1.00F, 1.00F};
            case 1 -> new float[]{0.70F, 0.80F, 1.00F};
            case 2 -> new float[]{0.85F, 0.70F, 1.00F};
            default -> new float[]{0.65F, 1.00F, 0.95F};
        };
    }

    private static void v(BufferBuilder b, double[] p, float[] c) {
        b.vertex(p[0], p[1], p[2]).color(c[0] * SKY_BRIGHTNESS, c[1] * SKY_BRIGHTNESS, c[2] * SKY_BRIGHTNESS, 1.0F).endVertex();
    }

    private static double[] dir(float yawDeg, float pitchDeg) {
        double y = Math.toRadians(yawDeg);
        double p = Math.toRadians(pitchDeg);
        return new double[]{Math.cos(p) * Math.sin(y), Math.sin(p), Math.cos(p) * Math.cos(y)};
    }

    private static double[][] basis(double[] d) {
        double[] ref = Math.abs(d[1]) > 0.99D ? new double[]{1, 0, 0} : new double[]{0, 1, 0};
        double[] right = normalize(cross(ref, d));
        double[] up = cross(d, right);
        return new double[][]{right, up};
    }

    private static double[] ring(double[] centre, double[] right, double[] up, double angle, double rad) {
        double c = Math.cos(angle) * rad;
        double s = Math.sin(angle) * rad;
        return new double[]{ centre[0] + right[0] * c + up[0] * s, centre[1] + right[1] * c + up[1] * s, centre[2] + right[2] * c + up[2] * s };
    }

    private static double[] cross(double[] a, double[] b) { return new double[]{a[1] * b[2] - a[2] * b[1], a[2] * b[0] - a[0] * b[2], a[0] * b[1] - a[1] * b[0]}; }

    private static double[] normalize(double[] a) {
        double l = Math.sqrt(a[0] * a[0] + a[1] * a[1] + a[2] * a[2]);
        return new double[]{a[0] / l, a[1] / l, a[2] / l};
    }

    private static double[] scale(double[] a, double k) {
        return new double[]{ a[0] * k, a[1] * k, a[2] * k};
    }

    private static double[] add(double[] a, double[] b) {
        return new double[]{ a[0] + b[0], a[1] + b[1], a[2] + b[2]};
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
}
