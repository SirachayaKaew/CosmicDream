package net.sikeawuser.dreamworld.client.render;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;

public class CosmicSkyEffects extends DimensionSpecialEffects {
    public CosmicSkyEffects() { super(Float.NaN , false , SkyType.NONE , false , false ); }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 color ,float brightness) {

        return color;
    }

    @Override
    public boolean isFoggyAt(int x, int y) {
        return false;
    }
}
