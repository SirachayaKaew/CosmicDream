package net.sikeawuser.dreamworld.registry;

import net.minecraftforge.eventbus.api.IEventBus;
import net.sikeawuser.dreamworld.DreamMod;
import net.sikeawuser.dreamworld.block.InvisibleBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DMBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DreamMod.MOD_ID);

    public static final RegistryObject<Block> INVISIBLE_BLOCK = BLOCKS.register("invisible_block" , () -> new InvisibleBlock(BlockBehaviour.Properties.of().mapColor(MapColor.NONE).strength(-1.0F, 3600000.0F).noOcclusion().noLootTable().isValidSpawn((state,level,pos,type) -> false)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
