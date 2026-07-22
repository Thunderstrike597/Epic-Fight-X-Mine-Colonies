package net.kenji.epic_colonies.network;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.api.CitizenArmatureTypes;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

import java.util.UUID;

public record ServerCitizenArmaturePacket(UUID entityUuid, CitizenArmatureTypes armatureType) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ServerCitizenArmaturePacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(EpicColonies.MODID, "server_citizen_armature_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerCitizenArmaturePacket> STREAM_CODEC =
            StreamCodec.of(ServerCitizenArmaturePacket::encode, ServerCitizenArmaturePacket::decode);

    public static void encode(FriendlyByteBuf buf, ServerCitizenArmaturePacket packet) {
        buf.writeUUID(packet.entityUuid);
        buf.writeEnum(packet.armatureType);
    }

    public static ServerCitizenArmaturePacket decode(FriendlyByteBuf buf) {
        UUID entityUuid = buf.readUUID();
        CitizenArmatureTypes type = buf.readEnum(CitizenArmatureTypes.class);
        return new ServerCitizenArmaturePacket(entityUuid, type); // Fixed - matches constructor
    }

    public static void handle(ServerCitizenArmaturePacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {

            Player player = ctx.player();

            if(!(player instanceof ServerPlayer serverPlayer))return;


            if (serverPlayer.level() instanceof ServerLevel serverLevel) {
                Entity entity = serverLevel.getEntity(packet.entityUuid);
                if (entity == null) return;
                if(entity instanceof AbstractEntityCitizen citizen){
                    CitizenEntityPatch<?> citizenEntityPatch = EpicFightCapabilities.getEntityPatch(citizen, CitizenEntityPatch.class);
                    if(citizenEntityPatch == null) return;
                    citizenEntityPatch.setCurrentCitizenArmatureFromArmatureType(packet.armatureType);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}