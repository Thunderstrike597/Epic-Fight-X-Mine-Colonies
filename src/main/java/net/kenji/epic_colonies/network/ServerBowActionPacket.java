package net.kenji.epic_colonies.network;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.entity.mobs.AbstractEntityMinecoloniesMonster;
import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import net.kenji.epic_colonies.gameasset.patch.MinecoloniesMonsterPatch;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

import java.util.UUID;

public record ServerBowActionPacket(UUID entityUuid, boolean wasUsingBow) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerBowActionPacket> TYPE = new CustomPacketPayload.Type<>(EpicColonies.identifier("server_bow_action"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerBowActionPacket> STREAM_CODEC = StreamCodec.of(
            ServerBowActionPacket::encode,
            ServerBowActionPacket::decode
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(FriendlyByteBuf buf, ServerBowActionPacket packet) {
        buf.writeUUID(packet.entityUuid);
        buf.writeBoolean(packet.wasUsingBow);
    }

    public static ServerBowActionPacket decode(FriendlyByteBuf buf) {
        UUID entityUuid = buf.readUUID();
        boolean wasUsingBow = buf.readBoolean();
        return new ServerBowActionPacket(entityUuid, wasUsingBow);
    }

    public static void handle(final ServerBowActionPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player == null) return;

            if (player.level() instanceof ServerLevel serverLevel) {
                Entity entity = serverLevel.getEntity(packet.entityUuid);
                if (entity == null) return;
                
                if (entity instanceof AbstractEntityCitizen citizen){
                    CitizenEntityPatch<?> citizenEntityPatch = EpicFightCapabilities.getEntityPatch(citizen, CitizenEntityPatch.class);
                    if (citizenEntityPatch != null){
                        citizenEntityPatch.setWasUsingBow(packet.wasUsingBow);
                    }
                }
                if (entity instanceof AbstractEntityMinecoloniesMonster monster){
                    MinecoloniesMonsterPatch<?> monsterPatch = EpicFightCapabilities.getEntityPatch(monster, MinecoloniesMonsterPatch.class);
                    if (monsterPatch != null){
                        monsterPatch.setWasUsingBow(packet.wasUsingBow);
                    }
                }
            }
        });
    }
}