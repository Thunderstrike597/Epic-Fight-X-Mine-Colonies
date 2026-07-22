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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jline.utils.Log;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.UUID;
import java.util.function.Supplier;

public record ServerBowActionPacket(UUID entityUuid, boolean wasUsingBow) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ServerBowActionPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(EpicColonies.MODID, "server_bow_action_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerBowActionPacket> STREAM_CODEC =
            StreamCodec.of(ServerBowActionPacket::encode, ServerBowActionPacket::decode);


    public static void encode(FriendlyByteBuf buf, ServerBowActionPacket packet) {
        buf.writeUUID(packet.entityUuid);
        buf.writeBoolean(packet.wasUsingBow);
    }

    public static ServerBowActionPacket decode(FriendlyByteBuf buf) {
        UUID entityUuid = buf.readUUID();
        boolean wasUsingBow = buf.readBoolean();
        return new ServerBowActionPacket(entityUuid, wasUsingBow); // Fixed - matches constructor
    }

    public static void handle(ServerBowActionPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {

            Player player = ctx.player();

            if(!(player instanceof ServerPlayer serverPlayer))return;

            if (serverPlayer.level() instanceof ServerLevel serverLevel) {
                Entity entity = serverLevel.getEntity(packet.entityUuid);
                if (entity == null) return;
                if(entity instanceof AbstractEntityCitizen citizen){
                    CitizenEntityPatch<?> citizenEntityPatch = EpicFightCapabilities.getEntityPatch(citizen, CitizenEntityPatch.class);
                    if(citizenEntityPatch != null){
                        citizenEntityPatch.setWasUsingBow(packet.wasUsingBow);
                    }
                }
                if(entity instanceof AbstractEntityMinecoloniesMonster monster){
                    MinecoloniesMonsterPatch<?> monsterPatch = EpicFightCapabilities.getEntityPatch(monster, MinecoloniesMonsterPatch.class);
                    if(monsterPatch != null){
                        monsterPatch.setWasUsingBow(packet.wasUsingBow);
                    }
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}