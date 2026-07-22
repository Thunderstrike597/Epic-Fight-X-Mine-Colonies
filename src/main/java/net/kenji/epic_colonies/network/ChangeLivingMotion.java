package net.kenji.epic_colonies.network;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.function.Supplier;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;

import org.jline.utils.Log;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.utils.ByteBufCodecsExtends;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.network.ManagedCustomPacketPayload;

public record ChangeLivingMotion(List<LivingMotion> livingMotions, List<AssetAccessor<? extends StaticAnimation>> animations, int entityId, boolean setChangesAsDefault) implements ManagedCustomPacketPayload {
    public static final StreamCodec<ByteBuf, ChangeLivingMotion> STREAM_CODEC;

    public ChangeLivingMotion(int entityId) {
        this(entityId, false);
    }

    public ChangeLivingMotion(int entityId, boolean setChangesAsDefault) {
        this(new ArrayList(), new ArrayList(), entityId, setChangesAsDefault);
    }

    public ChangeLivingMotion putPair(LivingMotion motion, AssetAccessor<? extends StaticAnimation> animation) {
        if (animation != null) {
            this.livingMotions.add(motion);
            this.animations.add(animation);
        }

        return this;
    }

    public void putEntries(Set<Map.Entry<LivingMotion, AssetAccessor<? extends StaticAnimation>>> motionSet) {
        motionSet.forEach((entry) -> {
            if (entry.getValue() != null) {
                this.livingMotions.add((LivingMotion)entry.getKey());
                this.animations.add((AssetAccessor)entry.getValue());
            }

        });
    }

    static {
        STREAM_CODEC = StreamCodec.composite(ByteBufCodecsExtends.listOf(ByteBufCodecsExtends.extendableEnumCodec(LivingMotion.ENUM_MANAGER)), ChangeLivingMotion::livingMotions, ByteBufCodecsExtends.listOf(ByteBufCodecsExtends.ANIMATION), ChangeLivingMotion::animations, ByteBufCodecs.INT, ChangeLivingMotion::entityId, ByteBufCodecs.BOOL, ChangeLivingMotion::setChangesAsDefault, ChangeLivingMotion::new);
    }
}