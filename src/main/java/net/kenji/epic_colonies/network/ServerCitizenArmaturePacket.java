package net.kenji.epic_colonies.network;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import net.kenji.epic_colonies.api.CitizenArmatureTypes;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

import java.util.UUID;
import java.util.function.Supplier;

public record ServerCitizenArmaturePacket(UUID entityUuid, CitizenArmatureTypes armatureType) {

    public static void encode(ServerCitizenArmaturePacket packet, FriendlyByteBuf buf) {
        buf.writeUUID(packet.entityUuid);
        buf.writeEnum(packet.armatureType);
    }

    public static ServerCitizenArmaturePacket decode(FriendlyByteBuf buf) {
        UUID entityUuid = buf.readUUID();
        CitizenArmatureTypes type = buf.readEnum(CitizenArmatureTypes.class);
        return new ServerCitizenArmaturePacket(entityUuid, type); // Fixed - matches constructor
    }

    public static void handle(ServerCitizenArmaturePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();

            if(player == null)return;

            if (player.level() instanceof ServerLevel serverLevel) {
                Entity entity = serverLevel.getEntity(packet.entityUuid);
                if (entity == null) return;
                if(entity instanceof AbstractEntityCitizen citizen){
                    CitizenEntityPatch<?> citizenEntityPatch = EpicFightCapabilities.getEntityPatch(citizen, CitizenEntityPatch.class);
                    if(citizenEntityPatch == null) return;
                    citizenEntityPatch.setCurrentCitizenArmatureFromArmatureType(packet.armatureType);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}