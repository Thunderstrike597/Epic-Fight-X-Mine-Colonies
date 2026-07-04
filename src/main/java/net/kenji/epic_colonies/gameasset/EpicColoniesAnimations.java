package net.kenji.epic_colonies.gameasset;

import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Armatures;

public class EpicColoniesAnimations {

    public static void registerAnimations(AnimationManager.AnimationRegistryEvent event) {
        event.newBuilder(EpicColonies.MODID, EpicColoniesAnimations::build);    }

    public static AnimationManager.AnimationAccessor<StaticAnimation> CITIZEN_BLINK;
    public static AnimationManager.AnimationAccessor<StaticAnimation> CITIZEN_EYES_MOVE;

    public static AnimationManager.AnimationAccessor<StaticAnimation> CITIZEN_WALK;

    public static AnimationManager.AnimationAccessor<StaticAnimation> CITIZEN_JOG;

    private static void build(AnimationManager.AnimationBuilder builder){
        CITIZEN_BLINK = builder.nextAccessor("citizen/living/citizen_blink", (accessor -> new StaticAnimation(0.2F,true, accessor, EpicColoniesArmatures.CITIZEN_REGULAR)));
        CITIZEN_EYES_MOVE = builder.nextAccessor("citizen/living/citizen_eyes_move", (accessor -> new StaticAnimation(0.2F,true, accessor, EpicColoniesArmatures.CITIZEN_REGULAR)));
        CITIZEN_WALK = builder.nextAccessor("citizen/living/citizen_walk", (accessor -> new StaticAnimation(0.2F,true, accessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> {
            if (entitypatch instanceof CitizenEntityPatch<?> patchInterface) {
                return patchInterface.getAnimForwardSpeed(0.6F, 1.25F);
            }
            return speed;
        })));
        CITIZEN_JOG = builder.nextAccessor("citizen/living/citizen_jog", (accessor -> new StaticAnimation(0.2F,true, accessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> {
            if (entitypatch instanceof CitizenEntityPatch<?> patchInterface) {
                return patchInterface.getAnimForwardSpeed(1F, 2.25F);
            }
            return speed;
        })));

    }

}
