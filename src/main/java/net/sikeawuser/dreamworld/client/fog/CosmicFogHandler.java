package net.sikeawuser.dreamworld.client.fog;


import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sikeawuser.dreamworld.DreamMod;
import net.sikeawuser.dreamworld.registry.DMDimensions;

@Mod.EventBusSubscriber(modid = DreamMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE , value = Dist.CLIENT)
public class CosmicFogHandler {

    private static final float FOG_RED = 0.015F;
    private static final float FOG_GREEN = 0.0015F;
    private static final float FOG_BLUE = 0.085F;
    private static final float FOG_START = 8.0F;
    private static final float FOG_END = 80.0F;

    @SubscribeEvent
    public static void onComputeFogColor(ViewportEvent.ComputeFogColor event) {
        if (!isInDreamworld()) { return; }

        event.setRed(FOG_RED);
        event.setGreen(FOG_GREEN);
        event.setBlue(FOG_BLUE);
    }

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        if (!isInDreamworld()) {return;}
        event.setNearPlaneDistance(FOG_START);
        event.setFarPlaneDistance(FOG_END);
        event.setCanceled(true);
    }
    private static boolean isInDreamworld() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.level != null && minecraft.level.dimension().equals(DMDimensions.DREAMWORLD);
    }
}
