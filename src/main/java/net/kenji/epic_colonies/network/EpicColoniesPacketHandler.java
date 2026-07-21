package net.kenji.epic_colonies.network;

import net.kenji.epic_colonies.EpicColonies;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class EpicColoniesPacketHandler {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(EpicColonies.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {

        INSTANCE.messageBuilder(ClientCitizenSyncPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientCitizenSyncPacket::decode)
                .encoder(ClientCitizenSyncPacket::encode)
                .consumerMainThread(ClientCitizenSyncPacket::handle)
                .add();
        INSTANCE.messageBuilder(ChangeLivingMotion.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ChangeLivingMotion::fromBytes)
                .encoder(ChangeLivingMotion::toBytes)
                .consumerMainThread(ChangeLivingMotion::handle)
                .add();
        INSTANCE.messageBuilder(ServerBowActionPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerBowActionPacket::decode)
                .encoder(ServerBowActionPacket::encode)
                .consumerMainThread(ServerBowActionPacket::handle)
                .add();
        INSTANCE.messageBuilder(ServerCitizenArmaturePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerCitizenArmaturePacket::decode)
                .encoder(ServerCitizenArmaturePacket::encode)
                .consumerMainThread(ServerCitizenArmaturePacket::handle)
                .add();
    }

    // Helper method to send packet to server
    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }

    // Helper method to send packet to specific player
    public static void sendToPlayer(Object packet, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    // Helper method to send packet to all players
    public static void sendToAll(Object packet) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), packet);
    }
}