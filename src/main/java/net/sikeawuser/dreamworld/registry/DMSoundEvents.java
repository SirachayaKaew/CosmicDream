package net.sikeawuser.dreamworld.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sikeawuser.dreamworld.DreamMod;

public class DMSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DreamMod.MOD_ID);

    public static final RegistryObject<SoundEvent> DIMENSION_THEME1 = SOUND_EVENTS.register("dreamworld1", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(DreamMod.MOD_ID, "dreamworld1")));

    public static final RegistryObject<SoundEvent> DIMENSION_THEME2 = SOUND_EVENTS.register("dreamworld2", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(DreamMod.MOD_ID, "dreamworld2")));

    public static final RegistryObject<SoundEvent> DIMENSION_THEME_DISK = SOUND_EVENTS.register("dreamworld_disk", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(DreamMod.MOD_ID, "dreamworld_disk")));
}
