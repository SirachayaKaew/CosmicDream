package net.sikeawuser.dreamworldmod.registry;

import net.sikeawuser.dreamworldmod.DreamMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class DMDimensions {
    public static final ResourceKey<Level> DREAMWORLD = ResourceKey.create(Registries.DIMENSION,
            new ResourceLocation(DreamMod.MOD_ID, "dreamworld"));

    private DMDimensions() {

    }
}
