package net.sikeawuser.dreamworldmod.teleport;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.sikeawuser.dreamworldmod.dimension.DreamworldSpawn;
import net.sikeawuser.dreamworldmod.dimension.ModDimensions;

public class TeleportManager {

    private static final String RETURN_DATA_KEY = "DreamWorldReturn";

    private TeleportManager() {
    }
    public static void saveReturnLoaction(ServerPlayer player) {
        CompoundTag playerData = player.getPersistentData();
        ReturnLocation returnLocation = new ReturnLocation(
                player.level().dimension(),
                player.getX(),
                player.getY(),
                player.getZ(),
                player.getYRot(),
                player.getXRot()
        );

        playerData.put(RETURN_DATA_KEY , returnLocation.toNBT());

    }

    public static boolean hasReturnLocation(ServerPlayer player) {
        return player.getPersistentData().contains(RETURN_DATA_KEY);
    }

    public static ReturnLocation getReturnLocation(ServerPlayer player) {
        CompoundTag playerData = player.getPersistentData();

        if (!playerData.contains(RETURN_DATA_KEY)){
            return null;
        }
        return ReturnLocation.formNBT(playerData.getCompound(RETURN_DATA_KEY),player);
    }

    public static void clearReturnLocation(ServerPlayer player) {
        player.getPersistentData().remove(RETURN_DATA_KEY);
    }

    public static boolean enterDreamWorld(ServerPlayer player) {
        if (player.level().isClientSide()){
            return false;
        }
        if (player.level().dimension().equals(ModDimensions.DREAMWORLD)) {
            return false;
        }
        ServerLevel dreamworld = player.getServer().getLevel(ModDimensions.DREAMWORLD);

        if (dreamworld == null) {
            return false;
        }
        saveReturnLoaction(player);
        createStartingPlatformIfNeed(dreamworld);
        BlockPos spawn = DreamworldSpawn.getSpawnPosition();
        player.teleportTo(dreamworld , spawn.getX() + 0.5 , spawn.getY() , spawn.getZ() + 0.5 , player.getYRot() , player.getXRot());
        return true;
    }

    public static boolean leaveDreamworld( ServerPlayer player ) {
        if (player.level().isClientSide()){
            return false;
        }
        if (!player.level().dimension().equals(ModDimensions.DREAMWORLD)) {
            return false;
        }
        ReturnLocation returnLocation = getReturnLocation(player);
        if (returnLocation == null) {
            return false;
        }

        ServerLevel destination = returnLocation.getServerLevel(player);

        if (destination == null) {
            return false;
        }

        player.teleportTo(destination , returnLocation.getX() , returnLocation.getY() , returnLocation.getZ() , returnLocation.getYaw() , returnLocation.getPitch());
        clearReturnLocation(player);
        return true;
    }

    private static void createStartingPlatformIfNeed(ServerLevel level){
        BlockPos center = new BlockPos(DreamworldSpawn.SPAWN_X , DreamworldSpawn.SPAWN_Y , DreamworldSpawn.SPAWN_Z);

        if (!level.isLoaded(center)) {
            return;
        }

        if (level.getBlockState(center).isAir()){
            DreamworldSpawn.creatingStartingPlatform(level);
        }
    }
}
