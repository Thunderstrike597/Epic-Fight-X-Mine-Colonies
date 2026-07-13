package net.kenji.epic_colonies.compat.wom;


import com.mojang.datafixers.util.Pair;
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

public class WomCombatBehaviours {

    static CombatBehaviors.Builder<HumanoidMobPatch<?>> agonyBehaviour;
    static CombatBehaviors.Builder<HumanoidMobPatch<?>> ruineBehaviour;
    static CombatBehaviors.Builder<HumanoidMobPatch<?>> tormentBehaviour;

    static CombatBehaviors.Builder<HumanoidMobPatch<?>> staffBehaviour;


    @SuppressWarnings("unchecked")
    public static void init(){
        buildMotions();
        CompatMobCombatBehaviours.HUMANOID_AGONY = CompatMobCombatBehaviours.register(
                WOMWeaponCategories.AGONY,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, agonyBehaviour, AnimsAgony.AGONY_IDLE, AnimsAgony.AGONY_WALK, AnimsAgony.AGONY_RUN)
        );
       CompatMobCombatBehaviours.HUMANOID_RUINE = CompatMobCombatBehaviours.register(
                WOMWeaponCategories.RUINE,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, ruineBehaviour, AnimsRuine.RUINE_IDLE, AnimsRuine.RUINE_WALK, AnimsRuine.RUINE_RUN)
        );
        CompatMobCombatBehaviours.HUMANOID_TORMENT = CompatMobCombatBehaviours.register(
                WOMWeaponCategories.RUINE,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, tormentBehaviour, WOMAnimations.TORMENT_IDLE, WOMAnimations.TORMENT_WALK, WOMAnimations.TORMENT_RUN)
        );
        CompatMobCombatBehaviours.HUMANOID_STAFF = CompatMobCombatBehaviours.register(
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

    @SuppressWarnings("unchecked")
    private static CombatBehaviors.BehaviorSeries.Builder createBehaviourSeries(float weight, DynamicBehaviour... dynamicBehaviours){
        CombatBehaviors.BehaviorSeries.Builder behaviorSeries = CombatBehaviors.BehaviorSeries.builder().weight(weight).canBeInterrupted(false);
        for(DynamicBehaviour dynamicBehaviour : dynamicBehaviours){
            CombatBehaviors.Behavior.Builder behaviour = CombatBehaviors.Behavior.builder().withinEyeHeight().animationBehavior(dynamicBehaviour.animationAccessor).withinDistance(dynamicBehaviour.distance.getFirst(), dynamicBehaviour.distance.getSecond());
            if(dynamicBehaviour.randomChance != -1){
                behaviour.randomChance(dynamicBehaviour.randomChance);
            }
            behaviorSeries.nextBehavior(behaviour);
        }
        return behaviorSeries;
    }


    public static class DynamicBehaviour{
        AnimationManager.AnimationAccessor<? extends StaticAnimation> animationAccessor;
        public final Pair<Float, Float> distance;
        public final float randomChance;

        public DynamicBehaviour(Builder builder){
            this.distance = builder.distance;
            this.randomChance = builder.randomChance;
            this.animationAccessor = builder.animation;
        }

        public static Builder of(AnimationManager.AnimationAccessor<? extends StaticAnimation> animations) {
            return new Builder(animations);
        }
        public static class Builder{
            public AnimationManager.AnimationAccessor<? extends StaticAnimation> animation = null;
            public Pair<Float, Float> distance = new Pair<>(1F, 2.75F);
            public float randomChance = -1;



            private Builder(AnimationManager.AnimationAccessor<? extends StaticAnimation> animation) {
                this.animation = animation;
            }

            public Builder distanceMinMax(float min, float max){
                distance = new Pair<>(min, max);
                return this;
            }
            public Builder randomChance(float chance){
                randomChance = chance;
                return this;
            }

            public DynamicBehaviour build(){
                return new DynamicBehaviour(this);
            }
        }
    }
}
