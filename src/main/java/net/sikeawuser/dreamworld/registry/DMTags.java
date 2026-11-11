package net.sikeawuser.dreamworld.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.sikeawuser.dreamworld.DreamMod;

public class DMTags {

    public static class Blocks {
        public static final TagKey<Block> DIMENSION_TRAVEL_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(DreamMod.MOD_ID, "dimension_travel_block"));
    }
    public static class Items {
        public static final TagKey<Item> TELEPORT_KEY = TagKey.create(Registries.ITEM, new ResourceLocation(DreamMod.MOD_ID , "teleport_key"));
    }
}
