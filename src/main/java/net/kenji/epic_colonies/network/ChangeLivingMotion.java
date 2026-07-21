package net.kenji.epic_colonies.network;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import net.kenji.epic_colonies.EpicColonies;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class ChangeLivingMotion implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ChangeLivingMotion> TYPE = new CustomPacketPayload.Type<>(EpicColonies.identifier("change_living_motion"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ChangeLivingMotion> STREAM_CODEC = StreamCodec.of(
            ChangeLivingMotion::toBytes,
            ChangeLivingMotion::fromBytes
    );

    private final int entityId;
    private int count;
    private final boolean setChangesAsDefault;
    private List<LivingMotion> motionList;
    private List<AssetAccessor<? extends StaticAnimation>> animationList;

    public ChangeLivingMotion() {
        this(-1);
    }

    public ChangeLivingMotion(int entityId) {
        this(entityId, 0, false);
    }

    public ChangeLivingMotion(int entityId, boolean setChangesAsDefault) {
        this(entityId, 0, setChangesAsDefault);
    }

    private ChangeLivingMotion(int entityId, int count, boolean setChangesAsDefault) {
        this.motionList = Lists.newArrayList();
        this.animationList = Lists.newArrayList();
        this.entityId = entityId;
        this.count = count;
        this.setChangesAsDefault = setChangesAsDefault;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public ChangeLivingMotion putPair(LivingMotion motion, AssetAccessor<? extends StaticAnimation> animation) {
        if (animation != null) {
            this.motionList.add(motion);
            this.animationList.add(animation);
            ++this.count;
        }

        return this;
    }

    public void putEntries(Set<Map.Entry<LivingMotion, AssetAccessor<? extends StaticAnimation>>> motionSet) {
        motionSet.forEach((entry) -> {
            if (entry.getValue() != null) {
                this.motionList.add((LivingMotion)entry.getKey());
                this.animationList.add((AssetAccessor)entry.getValue());
                ++this.count;
            }

        });
    }

    public static ChangeLivingMotion fromBytes(FriendlyByteBuf buf) {
        ChangeLivingMotion msg = new ChangeLivingMotion(buf.readInt(), buf.readInt(), buf.readBoolean());
        List<LivingMotion> motionList = Lists.newArrayList();
        List<AssetAccessor<? extends StaticAnimation>> animationList = Lists.newArrayList();

        for(int i = 0; i < msg.count; ++i) {
            motionList.add((LivingMotion)LivingMotion.ENUM_MANAGER.getOrThrow(buf.readInt()));
        }

        for(int i = 0; i < msg.count; ++i) {
            try {
                animationList.add(AnimationManager.byId(buf.readInt()));
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                animationList.add(Animations.BIPED_IDLE);
            }
        }

        msg.motionList = motionList;
        msg.animationList = animationList;
        return msg;
    }

    public static void toBytes(FriendlyByteBuf buf, ChangeLivingMotion msg) {
        buf.writeInt(msg.entityId);
        buf.writeInt(msg.count);
        buf.writeBoolean(msg.setChangesAsDefault);

        for(LivingMotion motion : msg.motionList) {
            buf.writeInt(motion.universalOrdinal());
        }

        for(AssetAccessor<? extends StaticAnimation> anim : msg.animationList) {
            buf.writeInt(((StaticAnimation)anim.get()).getId());
        }
    }

    public static void handle(final ChangeLivingMotion msg, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            Entity entity = mc.player.level().getEntity(msg.entityId);
            if (entity != null) {
                LivingEntityPatch<?> livingEntityPatch = EpicFightCapabilities.getUnparameterizedEntityPatch(entity, LivingEntityPatch.class).orElse(null);
                if (livingEntityPatch != null) {
                    ClientAnimator animator = livingEntityPatch.getClientAnimator();
                    animator.resetLivingAnimations();
                    animator.offAllLayers();
                    animator.resetMotion(false);
                    animator.resetCompositeMotion();

                    for (int i = 0; i < msg.count; ++i) {
                        LivingMotion decoded = msg.motionList.get(i);
                        livingEntityPatch.getClientAnimator().addLivingAnimation(decoded, msg.animationList.get(i));
                    }

                    if (msg.setChangesAsDefault) {
                        animator.setCurrentMotionsAsDefault();
                    }
                }
            }
        });
    }
}