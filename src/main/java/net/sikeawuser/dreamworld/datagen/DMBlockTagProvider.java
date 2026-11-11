package net.sikeawuser.dreamworld.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.sikeawuser.dreamworld.DreamMod;
import net.sikeawuser.dreamworld.registry.DMBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DMBlockTagProvider extends BlockTagsProvider {

    public DMBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, DreamMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {
//        tag(BlockTags.MINEABLE_WITH_AXE)
        tag(BlockTags.DRAGON_IMMUNE).add(DMBlocks.INVISIBLE_BLOCK.get());
        tag(BlockTags.WITHER_IMMUNE).add(DMBlocks.INVISIBLE_BLOCK.get());

    }
}
