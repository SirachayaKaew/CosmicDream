package net.sikeawuser.dreamworldmod.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;

public final class DreamworldSpawn {

    public static int SPAWN_X = 0;
    public static int SPAWN_Y = 80;
    public static int SPAWN_Z = 0;

    private DreamworldSpawn() {
    }

    public static BlockPos getSpawnPosition() {
        return new BlockPos( SPAWN_X , SPAWN_Y + 1 , SPAWN_Z);
    }

    public static void creatingStartingPlatform(ServerLevel level) {
        int radius = 5;

        int platformY = SPAWN_Y;

        for (int x = -radius ; x <= radius ; x++) {
            for (int z = -radius ; z <= radius ; z++) {

                BlockPos pos = new BlockPos( SPAWN_X +x , SPAWN_Y , SPAWN_Z + z);

                level.setBlock( pos, Blocks.END_STONE.defaultBlockState() , 3);
            }
        }
    }

}
