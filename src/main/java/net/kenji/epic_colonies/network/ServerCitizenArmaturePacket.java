package net.kenji.epic_colonies.network;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.api.CitizenArmatureTypes;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import java.util.UUID;

public record ServerCitizenArmaturePacket(UUID entityUuid, CitizenArmatureTypes armatureType) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerCitizenArmaturePacket> TYPE = new CustomPacketPayload.Type<>(EpicColonies.identifier("server_citizen_armature"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerCitizenArmaturePacket> STREAM_CODEC = StreamCodec.of(
            ServerCitizenArmaturePacket::encode,
            ServerCitizenArmaturePacket::decode
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(FriendlyByteBuf buf, ServerCitizenArmaturePacket packet) {
        buf.writeUUID(packet.entityUuid);
        buf.writeEnum(packet.armatureType);
    }

    public static ServerCitizenArmaturePacket decode(FriendlyByteBuf buf) {
        UUID entityUuid = buf.readUUID();
        CitizenArmatureTypes type = buf.readEnum(CitizenArmatureTypes.class);
        return new ServerCitizenArmaturePacket(entityUuid, type);
    }

    public static void handle(final ServerCitizenArmaturePacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player == null) return;

            if (player.level() instanceof ServerLevel serverLevel) {
                Entity entity = serverLevel.getEntity(packet.entityUuid);
                if (entity == null) return;
                if (entity instanceof AbstractEntityCitizen citizen){
                    CitizenEntityPatch<?> citizenEntityPatch = EpicFightCapabilities.getEntityPatch(citizen, CitizenEntityPatch.class);
                    if (citizenEntityPatch != null) {
                        citizenEntityPatch.setCurrentCitizenArmatureFromArmatureType(packet.armatureType);
                    }
                }
            }
        });
    }
}