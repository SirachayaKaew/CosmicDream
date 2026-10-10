package net.sikeawuser.dreamworld.teleport;

import net.sikeawuser.dreamworld.DreamMod;
import net.sikeawuser.dreamworld.dimension.DreamworldSpawn;
import net.sikeawuser.dreamworld.registry.DMDimensions;

import net.sikeawuser.dreamworld.teleport.ReturnLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.core.jmx.Server;


public class TeleportManager {

    private static final String TELEPORT_DATA = "Teleport";

    private static final String OVERWORLD_LOCATION = "Overworld";
    private static final String DIMEN_LOCATION = "Dimension";

    private TeleportManager() {
    }
    public static void saveCurrentPos(ServerPlayer player) {
        CompoundTag playerData = player.getPersistentData();
        CompoundTag TeleportData;

        if (playerData.contains(TELEPORT_DATA)) {
            TeleportData = playerData.getCompound(TELEPORT_DATA);
        } else {
            TeleportData = new CompoundTag();
        }

        ReturnLocation Location = new ReturnLocation(
                player.level().dimension(),
                player.getX(),
                player.getY(),
                player.getZ(),
                player.getYRot(),
                player.getXRot()
        );

        if (player.level().dimension() == Level.OVERWORLD) {
            TeleportData.put(OVERWORLD_LOCATION , Location.save());
        } else if (player.level().dimension().location().equals(DMDimensions.DREAMWORLD.location())) {
            TeleportData.put(DIMEN_LOCATION , Location.save());
        }
        playerData.put(TELEPORT_DATA, TeleportData);

    }

    public static ReturnLocation getOverworldPos (ServerPlayer player) {
        CompoundTag playerData = player.getPersistentData();

        if (!playerData.contains(TELEPORT_DATA)) { return null; }

        CompoundTag teleportData = playerData.getCompound(TELEPORT_DATA);

        if (!teleportData.contains(OVERWORLD_LOCATION)) { return null; }

        return ReturnLocation.load(teleportData.getCompound(OVERWORLD_LOCATION),player);
    }

    public static ReturnLocation getDimensionPos( ServerPlayer player ){
        CompoundTag playerData = player.getPersistentData();

        if (!playerData.contains(TELEPORT_DATA)) { return null; }

        CompoundTag teleportData = playerData.getCompound(TELEPORT_DATA);

        if (!teleportData.contains(DIMEN_LOCATION)) { return null;}

        return ReturnLocation.load(teleportData.getCompound(DIMEN_LOCATION),player);
    }


    public static boolean enterDreamWorld(ServerPlayer player) {

        if (player.level().dimension() == Level.OVERWORLD ) {saveCurrentPos(player);}

        ServerLevel dreamworld = player.getServer().getLevel(DMDimensions.DREAMWORLD);

        if (dreamworld == null) { return false; }
        ReturnLocation savedDreamworld = getDimensionPos(player);
        if (savedDreamworld != null){
            return teleportPlayer(player,dreamworld,savedDreamworld);
        } else {
        BlockPos spawn = DreamworldSpawn.getSpawnPosition();
        player.teleportTo(dreamworld , spawn.getX() + 0.5 , spawn.getY() , spawn.getZ() + 0.5 , player.getYRot() , player.getXRot());
        }
        return true;
    }

    public static boolean leaveDreamworld( ServerPlayer player ) {
        if (!player.level().dimension().equals(DMDimensions.DREAMWORLD)) { return false;}
        saveCurrentPos(player);
        ReturnLocation overworldPos = getOverworldPos(player);
        if (overworldPos == null) {
            return false;
        }

        ServerLevel overworld = player.server.getLevel(Level.OVERWORLD);

        if (overworld == null) {
            return false;
        }

        if (overworldPos != null) {
            return teleportPlayer(player,overworld,overworldPos);
        } else {
            BlockPos spawn = overworld.getSharedSpawnPos();
            player.teleportTo(overworld , spawn.getX() + 0.5 , spawn.getY() , spawn.getZ() + 0.5, player.getYRot() , player.getXRot());
        }
        return true;
    }

    private static boolean teleportPlayer(ServerPlayer player ,ServerLevel destination ,ReturnLocation location){
        if (destination == null || location == null) { return false;}
        player.teleportTo(destination , location.getX() , location.getY() , location.getZ() , location.getYaw() ,location.getPitch());
        return true;
    }

}
