package net.kenji.epic_colonies.compat.wom;


import com.mojang.datafixers.util.Pair;
import net.kenji.epic_colonies.compat.CombatBehaviourBase;
import net.kenji.epic_colonies.compat.CompatMobCombatBehaviours;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.gameasset.animations.weapons.AnimsAgony;
import reascer.wom.gameasset.animations.weapons.AnimsRuine;
import reascer.wom.world.capabilities.item.WOMWeaponCategories;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;

public class WomCombatBehaviours extends CombatBehaviourBase {

    static CombatBehaviors.Builder<HumanoidMobPatch<?>> agonyBehaviour;
    static CombatBehaviors.Builder<HumanoidMobPatch<?>> ruineBehaviour;
    static CombatBehaviors.Builder<HumanoidMobPatch<?>> tormentBehaviour;

    static CombatBehaviors.Builder<HumanoidMobPatch<?>> staffBehaviour;


    @SuppressWarnings("unchecked")
    public static void init(){
        buildMotions();
        CompatMobCombatBehaviours.HUMANOID_AGONY = register(
                WOMWeaponCategories.AGONY,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, agonyBehaviour, AnimsAgony.AGONY_IDLE, AnimsAgony.AGONY_WALK, AnimsAgony.AGONY_RUN)
        );
       CompatMobCombatBehaviours.HUMANOID_RUINE = register(
                WOMWeaponCategories.RUINE,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, ruineBehaviour, AnimsRuine.RUINE_IDLE, AnimsRuine.RUINE_WALK, AnimsRuine.RUINE_RUN)
        );
        CompatMobCombatBehaviours.HUMANOID_TORMENT = register(
                WOMWeaponCategories.RUINE,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, tormentBehaviour, WOMAnimations.TORMENT_IDLE, WOMAnimations.TORMENT_WALK, WOMAnimations.TORMENT_RUN)
        );
        CompatMobCombatBehaviours.HUMANOID_STAFF = register(
                WOMWeaponCategories.STAFF,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, staffBehaviour, WOMAnimations.STAFF_IDLE, WOMAnimations.STAFF_IDLE, WOMAnimations.STAFF_RUN)
        );
    }
    @SuppressWarnings("unchecked")
    private static void buildMotions(){
        agonyBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(AnimsAgony.AGONY_AUTO_1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, DynamicBehaviour.of(AnimsAgony.AGONY_AUTO_1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(AnimsAgony.AGONY_AUTO_2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, DynamicBehaviour.of(AnimsAgony.AGONY_AUTO_1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(AnimsAgony.AGONY_AUTO_2).distanceMinMax(1, 3).build(), DynamicBehaviour.of(AnimsAgony.AGONY_AUTO_3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );
        ruineBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(AnimsRuine.RUINE_AUTO_1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, DynamicBehaviour.of(AnimsRuine.RUINE_AUTO_1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(AnimsRuine.RUINE_AUTO_2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, DynamicBehaviour.of(AnimsRuine.RUINE_AUTO_1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(AnimsRuine.RUINE_AUTO_2).distanceMinMax(1, 3).build(), DynamicBehaviour.of(AnimsRuine.RUINE_AUTO_3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );
        tormentBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(WOMAnimations.TORMENT_AUTO_1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, DynamicBehaviour.of(WOMAnimations.TORMENT_AUTO_1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(WOMAnimations.TORMENT_AUTO_2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, DynamicBehaviour.of(WOMAnimations.TORMENT_AUTO_1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(WOMAnimations.TORMENT_AUTO_2).distanceMinMax(1, 3).build(), DynamicBehaviour.of(WOMAnimations.TORMENT_AUTO_3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );
        staffBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(WOMAnimations.STAFF_AUTO_1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, DynamicBehaviour.of(WOMAnimations.STAFF_AUTO_1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(WOMAnimations.STAFF_AUTO_2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, DynamicBehaviour.of(WOMAnimations.STAFF_AUTO_1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(WOMAnimations.STAFF_AUTO_2).distanceMinMax(1, 3).build(), DynamicBehaviour.of(WOMAnimations.STAFF_AUTO_3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );
        
    }
}
