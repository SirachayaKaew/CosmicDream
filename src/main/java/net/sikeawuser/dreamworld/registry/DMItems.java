package net.sikeawuser.dreamworld.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.sikeawuser.dreamworld.DreamMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.sikeawuser.dreamworld.item.KeyItem;

public class DMItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DreamMod.MOD_ID);
    public static final RegistryObject<Item> KEY = ITEMS.register("key" , () -> new KeyItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> ICON = ITEMS.register("icon", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INVISIBLE_BLOCK_ITEM = ITEMS.register("invisible_block", () -> new BlockItem(DMBlocks.INVISIBLE_BLOCK.get(), new Item.Properties()));
    private DMItems() {}

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
