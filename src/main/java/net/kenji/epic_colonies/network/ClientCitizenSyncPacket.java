package net.kenji.epic_colonies.network;

import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.api.CitizenPatchData;
import net.kenji.epic_colonies.gameasset.EpicColoniesLivingMotions;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

import java.util.UUID;

public record ClientCitizenSyncPacket(int entityId, UUID uuid, CitizenPatchData data) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientCitizenSyncPacket> TYPE = new CustomPacketPayload.Type<>(EpicColonies.identifier("client_citizen_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientCitizenSyncPacket> STREAM_CODEC = StreamCodec.of(
            ClientCitizenSyncPacket::encode,
            ClientCitizenSyncPacket::decode
    );

    private static int fillNullMotion(LivingMotion motion){
        if(motion == null){
            return LivingMotions.NONE.universalOrdinal();
        }
        return motion.universalOrdinal();
    }
    
    private static LivingMotion getNullableMotion(int motionId){
        if(motionId == LivingMotions.NONE.universalOrdinal()){
            return null;
        }
        return LivingMotion.ENUM_MANAGER.get(motionId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(FriendlyByteBuf buf, ClientCitizenSyncPacket packet) {
        buf.writeInt(packet.entityId);
        buf.writeUUID(packet.uuid)    ;
        buf.writeInt(fillNullMotion(packet.data.currentOptionalMotion));
        buf.writeInt(fillNullMotion(packet.data.currentOptionalCompositeMotion));
        buf.writeInt(fillNullMotion(packet.data.prevOptionalCompositeMotion));
        buf.writeInt(fillNullMotion(packet.data.prevOptionalMotion));

        buf.writeBoolean(packet.data.isAsleep);
    }

    public static ClientCitizenSyncPacket decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        UUID entityUuid = buf.readUUID();
        int motionId = buf.readInt();
        int compositeMotionId = buf.readInt();
        int prevCompositeMotionId = buf.readInt();
        int prevMotionId = buf.readInt();
        boolean isAsleep = buf.readBoolean();
        LivingMotion motion = getNullableMotion(motionId);
        LivingMotion compositeMotion = getNullableMotion(compositeMotionId);
        LivingMotion prevCompositeMotion = getNullableMotion(prevCompositeMotionId);
        LivingMotion prevMotion = getNullableMotion(prevMotionId);

        CitizenPatchData data = new CitizenPatchData(entityUuid, motion, compositeMotion, prevCompositeMotion, prevMotion,isAsleep);

        return new ClientCitizenSyncPacket(entityId, entityUuid, data);
    }

    public static void handle(final ClientCitizenSyncPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            executeOnClient(packet);
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static void executeOnClient(ClientCitizenSyncPacket packet) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        Entity entity = player.level().getEntity(packet.entityId);
        if (entity == null) return;

        EpicFightCapabilities.getUnparameterizedEntityPatch(entity, CitizenEntityPatch.class).ifPresent(patch -> {
            patch.setCitizenPatchData(packet.data);
        });
    }
}