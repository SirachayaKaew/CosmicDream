package net.sikeawuser.dreamworld.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.sikeawuser.dreamworld.DreamMod;
import net.sikeawuser.dreamworld.registry.DMSoundEvents;

public class DMSoundProvider extends SoundDefinitionsProvider {

    protected DMSoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, DreamMod.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
//        add(DMSoundEvents.DIMENSION_THEME1, definition().with(sound(new ResourceLocation(DreamMod.MOD_ID,"dream1"))));
        add(DMSoundEvents.DIMENSION_THEME_DISK,definition().with(sound(new ResourceLocation(DreamMod.MOD_ID, "cosmic")).stream()));

    }
}
