package net.sikeawuser.dreamworld.datagen;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import net.sikeawuser.dreamworld.registry.DMBlocks;

import java.util.Set;

public class DMBlockLoot extends BlockLootSubProvider {
    protected DMBlockLoot() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
//        dropSelf();
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return DMBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block != DMBlocks.INVISIBLE_BLOCK.get()).toList();
    }
}
