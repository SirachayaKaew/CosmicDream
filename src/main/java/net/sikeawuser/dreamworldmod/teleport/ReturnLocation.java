package net.sikeawuser.dreamworldmod.teleport;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class ReturnLocation {

    private final ResourceKey<Level> dimension;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public ReturnLocation( ResourceKey<Level> dimension , double x , double y , double z , float yaw , float ptich) {
        this.dimension = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = ptich;

    }

    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putString("Dimension" , dimension.location().toString());
        tag.putDouble("X",x);
        tag.putDouble("Y",y);
        tag.putDouble("Z",z);

        tag.putFloat("Yaw", yaw);
        tag.putFloat("Pitch", pitch);

        return tag;
    }

    public static ReturnLocation formNBT(CompoundTag tag, ServerPlayer player) {
        if (!tag.contains("Dimension")) {
            return null;
        }

        ResourceLocation dimensionLocation = new ResourceLocation(tag.getString("Dimension"));
        ResourceKey<Level> dimension = ResourceKey.create(
                Registries.DIMENSION, dimensionLocation
        );
        return  new ReturnLocation(dimension,
                tag.getDouble("X"),
                tag.getDouble("Y"),
                tag.getDouble("Z"),
                tag.getFloat("Yaw"),
                tag.getFloat("Pitch"));
    }

    public ServerLevel getServerLevel(ServerPlayer player) {
        return player.getServer().getLevel(dimension);
    }
}
