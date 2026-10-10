package net.sikeawuser.dreamworld.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sikeawuser.dreamworld.DreamMod;
import net.sikeawuser.dreamworld.client.render.CosmicSkyEffects;

@Mod.EventBusSubscriber(modid = DreamMod.MOD_ID , bus = Mod.EventBusSubscriber.Bus.MOD ,value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onRegisterDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
        event.register(new ResourceLocation(DreamMod.MOD_ID, "cosmic_sky"),new CosmicSkyEffects());
    }
}
