package net.sikeawuser.dreamworld.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.sikeawuser.dreamworld.DreamMod;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DMCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB , DreamMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> DREAM_TAB = CREATIVE_MODE_TABS.register("dream_tab",
            () -> CreativeModeTab.builder().icon( () ->new ItemStack(DMItems.ICON.get()))
                    .title((Component.translatable("creativetab.dreamworld.dream_tab")))
                    .displayItems((pParameters , pOutput) -> {
                        pOutput.accept(DMItems.KEY.get());
                    }).build());


}
