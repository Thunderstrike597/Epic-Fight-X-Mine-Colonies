package net.kenji.epic_colonies.network;

import net.kenji.epic_colonies.gameasset.EpicColoniesLivingMotions;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.jline.utils.Log;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

import java.util.UUID;
import java.util.function.Supplier;

public record ClientCitizenSyncPacket(int entityId, UUID uuid, CitizenEntityPatch.CitizenPatchData data) {


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
    // Encode: Write data to buffer
    public static void encode(ClientCitizenSyncPacket packet, FriendlyByteBuf buf) {
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

        CitizenEntityPatch.CitizenPatchData data = new CitizenEntityPatch.CitizenPatchData(entityUuid, motion, compositeMotion, prevCompositeMotion, prevMotion,isAsleep);

        return new ClientCitizenSyncPacket(entityId, entityUuid, data);
    }

    // Handle: Process the packet on the receiving side
    public static void handle(ClientCitizenSyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                executeOnClient(packet);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void executeOnClient(ClientCitizenSyncPacket packet) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        Entity entity = player.level().getEntity(packet.entityId);
        if (entity == null) return;

        entity.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
            if (cap instanceof CitizenEntityPatch<?> patch) {

                patch.setCitizenPatchData(packet.data);
            }
        });
    }
}