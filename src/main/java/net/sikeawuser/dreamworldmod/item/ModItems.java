package net.sikeawuser.dreamworldmod.item;

import net.minecraft.resources.ResourceLocation;
import net.sikeawuser.dreamworldmod.DreamMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DreamMod.MOD_ID);

    public static final RegistryObject<Item> KEY =
            ITEMS.register("key" , () -> new KeyItem(new Item.Properties()));
    private ModItems() {
    }
}
