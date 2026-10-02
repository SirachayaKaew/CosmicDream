package net.sikeawuser.dreamworldmod.registry;

import net.minecraftforge.eventbus.api.IEventBus;
import net.sikeawuser.dreamworldmod.DreamMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.sikeawuser.dreamworldmod.item.KeyItem;

public class DMItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DreamMod.MOD_ID);
    public static final RegistryObject<Item> KEY = ITEMS.register("key" , () -> new KeyItem(new Item.Properties().stacksTo(1)));

    private DMItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
