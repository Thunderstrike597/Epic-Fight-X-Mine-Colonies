package net.kenji.epic_colonies.network;

import net.kenji.epic_colonies.EpicColonies;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import yesman.epicfight.network.EpicFightClientBoundPayloadHandler;
import yesman.epicfight.network.EpicFightServerBoundPayloadHandler;
import yesman.epicfight.network.ManagedCustomPacketPayload;
import yesman.epicfight.network.client.*;
import yesman.epicfight.network.common.BiDirectionalAnimationVariable;
import yesman.epicfight.network.common.BiDirectionalSyncAnimationPositionPacket;
import yesman.epicfight.network.common.BiDirectionalSyncEmoteSlots;
import yesman.epicfight.network.server.*;

@EventBusSubscriber(modid = EpicColonies.MODID)
public class EpicColoniesPacketHandler {

    private static final String PROTOCOL_VERSION = "1";

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);

        registrar.playToClient(
                ClientCitizenSyncPacket.TYPE,
                ClientCitizenSyncPacket.STREAM_CODEC,
                ClientCitizenSyncPacket::handle
        );
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

    public static void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    public static void sendToPlayer(CustomPacketPayload payload, net.minecraft.server.level.ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    public static void sendToAll(CustomPacketPayload payload) {
        PacketDistributor.sendToAllPlayers(payload);
    }
}