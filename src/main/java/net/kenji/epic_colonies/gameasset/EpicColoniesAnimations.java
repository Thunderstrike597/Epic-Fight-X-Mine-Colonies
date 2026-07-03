package net.kenji.epic_colonies.gameasset;

import net.kenji.epic_colonies.EpicColonies;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;

public class EpicColoniesAnimations {

    public static void registerAnimations(AnimationManager.AnimationRegistryEvent event) {
        event.newBuilder(EpicColonies.MODID, EpicColoniesAnimations::build);    }

    public static AnimationManager.AnimationAccessor<StaticAnimation> CITIZEN_BLINK;
    public static AnimationManager.AnimationAccessor<StaticAnimation> CITIZEN_EYES_MOVE;

    private static void build(AnimationManager.AnimationBuilder builder){
        CITIZEN_BLINK = builder.nextAccessor("citizen/living/citizen_blink", (accessor -> new StaticAnimation(0.2F,true, accessor, EpicColoniesArmatures.CITIZEN_REGULAR)));
        CITIZEN_EYES_MOVE = builder.nextAccessor("citizen/living/citizen_eyes_move", (accessor -> new StaticAnimation(0.2F,true, accessor, EpicColoniesArmatures.CITIZEN_REGULAR)));

    }

}
