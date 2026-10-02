package net.sikeawuser.dreamworldmod.network;

import net.minecraft.network.FriendlyByteBuf;
public class StartTeleportPacket {

    private final int duration;

    public StartTeleportPacket(int duration) { this.duration = duration; }

    public StartTeleportPacket(FriendlyByteBuf friendlyByteBuf) { this.duration = friendlyByteBuf.readInt(); }

    public void encode(FriendlyByteBuf friendlyByteBuf) { friendlyByteBuf.writeInt(duration); }

    public int getDuration() {
        return duration;
    }

}
