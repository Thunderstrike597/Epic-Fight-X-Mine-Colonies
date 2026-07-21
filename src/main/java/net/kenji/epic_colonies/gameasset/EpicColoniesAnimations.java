package net.kenji.epic_colonies.gameasset;

import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.EpicColoniesConfigClient;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.*;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.main.EpicFightMod;

public class EpicColoniesAnimations {

    public static void registerAnimations(AnimationManager.AnimationRegistryEvent event) {
        event.newBuilder(EpicColonies.MODID, EpicColoniesAnimations::build);    }

    public static AnimationManager.AnimationAccessor<StaticAnimation> CITIZEN_BLINK;
    public static AnimationManager.AnimationAccessor<StaticAnimation> CHILD_BLINK;

    public static AnimationManager.AnimationAccessor<StaticAnimation> CITIZEN_BLINK_ANGRY;
    public static AnimationManager.AnimationAccessor<StaticAnimation> CITIZEN_BLINK_HAPPY;
    public static AnimationManager.AnimationAccessor<StaticAnimation> CITIZEN_BLINK_SAD;

    public static AnimationManager.AnimationAccessor<StaticAnimation> CITIZEN_EYES_CLOSED;

    public static AnimationManager.AnimationAccessor<StaticAnimation> CITIZEN_EYES_MOVE;

    public static AnimationManager.AnimationAccessor<StaticAnimation> CITIZEN_WALK;
    public static AnimationManager.AnimationAccessor<StaticAnimation> CITIZEN_JOG;
    public static AnimationManager.AnimationAccessor<StaticAnimation> CITIZEN_EAT;
    public static AnimationManager.AnimationAccessor<StaticAnimation> CITIZEN_DIG;

    public static AnimationManager.AnimationAccessor<MovementAnimation> CITIZEN_CLIMB;


    private static void build(AnimationManager.AnimationBuilder builder){
        CITIZEN_BLINK = builder.nextAccessor("citizen/living/citizen_blink", (accessor -> new StaticAnimation(0.2F,true, accessor, EpicColoniesArmatures.CITIZEN_REGULAR)));
        CHILD_BLINK = builder.nextAccessor("citizen/living/child_blink", (accessor -> new StaticAnimation(0.2F,true, accessor, EpicColoniesArmatures.CITIZEN_REGULAR)));

        CITIZEN_BLINK_ANGRY = builder.nextAccessor("citizen/living/citizen_blink_angry", (accessor -> new StaticAnimation(0.2F,true, accessor, EpicColoniesArmatures.CITIZEN_REGULAR)));
        CITIZEN_BLINK_HAPPY = builder.nextAccessor("citizen/living/citizen_blink_happy", (accessor -> new StaticAnimation(0.2F,true, accessor, EpicColoniesArmatures.CITIZEN_REGULAR)));
        CITIZEN_BLINK_SAD = builder.nextAccessor("citizen/living/citizen_blink_sad", (accessor -> new StaticAnimation(0.2F,true, accessor, EpicColoniesArmatures.CITIZEN_REGULAR)));

        CITIZEN_EYES_CLOSED = builder.nextAccessor("citizen/living/citizen_eyes_closed", (accessor -> new StaticAnimation(0.2F,true, accessor, EpicColoniesArmatures.CITIZEN_REGULAR)));

        CITIZEN_EYES_MOVE = builder.nextAccessor("citizen/living/citizen_eyes_move", (accessor -> new StaticAnimation(0.2F,true, accessor, EpicColoniesArmatures.CITIZEN_REGULAR)));
        CITIZEN_WALK = builder.nextAccessor("citizen/living/citizen_walk", (accessor -> new StaticAnimation(0.2F,true, accessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> {
            if (entitypatch instanceof CitizenEntityPatch<?> patchInterface) {
                return patchInterface.getAnimForwardSpeed(0.6F, 1.25F);
            }
            return speed;
        })));
        CITIZEN_JOG = builder.nextAccessor("citizen/living/citizen_jog", (accessor -> new StaticAnimation(0.2F,true, accessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> {
            if (entitypatch instanceof CitizenEntityPatch<?> patchInterface) {
                return patchInterface.getAnimForwardSpeed((float) (double)EpicColoniesConfigClient.JOG_PLAYBACK_SPEED_MIN.get(), (float) (double)EpicColoniesConfigClient.JOG_PLAYBACK_SPEED_MAX.get());
            }
            return speed;
        })));
        CITIZEN_EAT = builder.nextAccessor("citizen/living/citizen_eat", (accessor -> new StaticAnimation(0.1F,true, accessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> {
            return 0.8F;
        })));

        CITIZEN_CLIMB = builder.nextAccessor("citizen/living/citizen_climb", (accessor) -> (MovementAnimation)(new MovementAnimation(0.16F, true, accessor, Armatures.BIPED)).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (AnimationProperty.PlaybackSpeedModifier)(self, entitypatch, speed, prevElapsedTime, elapsedTime) -> {
            if (self.isLinkAnimation()) {
                return 1.0F;
            } else {
                if(entitypatch instanceof CitizenEntityPatch<?> patch){
                    double netY = patch.getOriginal().position().y - patch.getLastY(); // e.g. N = 4
                    if (Math.abs(netY) < 0.03) {
                        return 0.0F; // net-stationary over the window — blocked/idle
                    }

                    return netY < 0.0 ? -1.0F : 1.0F;
                }
                double y = ((LivingEntity)entitypatch.getOriginal()).getY() - entitypatch.getYOld();

                return y < (double)0.0F ? -1.0F : 1.0F;

            }
        }).addProperty(AnimationProperty.StaticAnimationProperty.FIXED_HEAD_ROTATION, true).addProperty(AnimationProperty.StaticAnimationProperty.ON_ITEM_CHANGE_EVENT, AnimationEvent.SimpleEvent.create(Animations.ReusableSources.SET_TOOLS_BACK_WHEN_ITEM_CHANGED, AnimationEvent.Side.CLIENT)).addEvents(AnimationProperty.StaticAnimationProperty.ON_BEGIN_EVENTS, new AnimationEvent[]{AnimationEvent.SimpleEvent.create(Animations.ReusableSources.SET_TOOLS_BACK, AnimationEvent.Side.CLIENT), AnimationEvent.SimpleEvent.create(Animations.ReusableSources.UPDATE_Y_TO_NEARBY_LADDER, AnimationEvent.Side.CLIENT)}).addEvents(AnimationProperty.StaticAnimationProperty.TICK_EVENTS, new AnimationEvent[]{AnimationEvent.SimpleEvent.create(Animations.ReusableSources.UPDATE_Y_TO_NEARBY_LADDER, AnimationEvent.Side.CLIENT)}).addEvents(AnimationProperty.StaticAnimationProperty.ON_END_EVENTS, new AnimationEvent[]{AnimationEvent.SimpleEvent.create(Animations.ReusableSources.REVERT_TO_HANDS, AnimationEvent.Side.CLIENT)}).newTimePair(0.0F, 10000.0F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false).addStateRemoveOld(EntityState.CAN_SKILL_EXECUTION, false).addStateRemoveOld(EntityState.TURNING_LOCKED, true).addStateRemoveOld(EntityState.INACTION, true));

        CITIZEN_DIG = builder.nextAccessor("citizen/living/citizen_dig", (accessor -> new StaticAnimation(0.1F,true, accessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> {
            return 1.25F;
        })));
    }

}
