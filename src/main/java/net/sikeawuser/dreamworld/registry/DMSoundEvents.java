package net.sikeawuser.dreamworld.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sikeawuser.dreamworld.DreamMod;
import net.minecraftforge.common.util.ForgeSoundType;

public class DMSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DreamMod.MOD_ID);

    public static final RegistryObject<SoundEvent> DIMENSION_THEME1 = registerSoundEvents("dream1");
    public static final RegistryObject<SoundEvent> DIMENSION_THEME_DISK = registerSoundEvents("cosmic");


    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(DreamMod.MOD_ID, name)));
    }
    public static void register(IEventBus eventBus) { SOUND_EVENTS.register(eventBus);}
}
