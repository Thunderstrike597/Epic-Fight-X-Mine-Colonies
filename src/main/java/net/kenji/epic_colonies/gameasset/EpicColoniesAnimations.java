package net.kenji.epic_colonies.gameasset;

import net.kenji.epic_colonies.EpicColonies;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.ColliderPreset;

public class EpicColoniesAnimations {

    public static void registerAnimations(AnimationManager.AnimationRegistryEvent event) {
        event.newBuilder(EpicColonies.MODID, EpicColoniesAnimations::build);    }

    public static AnimationManager.AnimationAccessor<StaticAnimation> CITIZEN_BLINK;
    public static AnimationManager.AnimationAccessor<StaticAnimation> CITIZEN_EYES_MOVE;

    private static void build(AnimationManager.AnimationBuilder builder){
        CITIZEN_BLINK = builder.nextAccessor("citizen/living/citizen_blink", (accessor -> new StaticAnimation(0.2F,true, accessor, EpicColoniesArmatures.CITIZEN_MALE)));
        CITIZEN_EYES_MOVE = builder.nextAccessor("citizen/living/citizen_eyes_move", (accessor -> new StaticAnimation(0.2F,true, accessor, EpicColoniesArmatures.CITIZEN_MALE)));

    }

}
