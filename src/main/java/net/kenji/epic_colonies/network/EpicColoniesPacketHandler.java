package net.kenji.epic_colonies.network;

import net.kenji.epic_colonies.EpicColonies;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.PacketDistributor;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

@EventBusSubscriber(modid = EpicColonies.MODID, bus = EventBusSubscriber.Bus.MOD)
public class EpicColoniesPacketHandler {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        // Server to Client
        registrar.playToClient(
                ClientCitizenSyncPacket.TYPE,
                ClientCitizenSyncPacket.STREAM_CODEC,
                ClientCitizenSyncPacket::handle
        );
        registrar.playToClient(
                ChangeLivingMotion.TYPE,
                ChangeLivingMotion.STREAM_CODEC,
                ChangeLivingMotion::handle
        );

        // Client to Server

        registrar.playToServer(

                ServerBowActionPacket.TYPE,

                ServerBowActionPacket.STREAM_CODEC,

                ServerBowActionPacket::handle

        );

        registrar.playToServer(

                ServerCitizenArmaturePacket.TYPE,

                ServerCitizenArmaturePacket.STREAM_CODEC,

                ServerCitizenArmaturePacket::handle

        );
    }

    public static void sendToServer(CustomPacketPayload packet) {
        PacketDistributor.sendToServer(packet);
    }

    public static void sendToPlayer(CustomPacketPayload packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    public static void sendToAll(CustomPacketPayload packet) {
        PacketDistributor.sendToAllPlayers(packet);
    }
}