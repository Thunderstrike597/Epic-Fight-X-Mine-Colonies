package net.kenji.epic_colonies.api;

import net.kenji.epic_colonies.gameasset.EpicColoniesAnimations;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;

public enum FacialEmotionExpressions {
    HAPPY(7.25F, EpicColoniesAnimations.CITIZEN_BLINK_HAPPY),
    NEUTRAL(4.0, EpicColoniesAnimations.CITIZEN_BLINK),
    SAD(2.25F, EpicColoniesAnimations.CITIZEN_BLINK_SAD),
    UPSET(0.0F, EpicColoniesAnimations.CITIZEN_BLINK_ANGRY);

    final double happiness;
    final AnimationManager.AnimationAccessor<? extends StaticAnimation> expressionAnimation;

    FacialEmotionExpressions(double happiness, AnimationManager.AnimationAccessor<? extends StaticAnimation> animation){
        this.happiness = happiness;
        expressionAnimation = animation;
    }

    public static AnimationManager.AnimationAccessor<? extends StaticAnimation> getAnimationFromHappiness(double happiness){
        for (int i = 0; i < FacialEmotionExpressions.values().length; i++) {
            if(happiness >= FacialEmotionExpressions.values()[i].happiness){
                return FacialEmotionExpressions.values()[i].expressionAnimation;
            }
        }
        return NEUTRAL.expressionAnimation;
    }
}
