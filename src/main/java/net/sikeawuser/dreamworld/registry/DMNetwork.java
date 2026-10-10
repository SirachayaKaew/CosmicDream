package net.sikeawuser.dreamworld.registry;

import net.sikeawuser.dreamworld.DreamMod;
import net.sikeawuser.dreamworld.network.StartTeleportPacket;

import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.server.level.ServerPlayer;

public class DMNetwork {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL1 = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(DreamMod.MOD_ID,"main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals
    );

    private static int packetID = 0;

    private DMNetwork() {}

    public static void register() {
        CHANNEL1.registerMessage( packetID++, StartTeleportPacket.class, StartTeleportPacket::encode, StartTeleportPacket::new,
                (packet,context) -> {
                    context.get().enqueueWork(() ->{

                            });
                    context.get().setPacketHandled(true);
                }
        );
    }

    public static void sendToPlayer(StartTeleportPacket packet,ServerPlayer player) {
        CHANNEL1.send(
                PacketDistributor.PLAYER.with(() -> player),packet
        );
    }
}
