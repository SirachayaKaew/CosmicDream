package net.sikeawuser.dreamworldmod.teleport;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
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
    }
}
