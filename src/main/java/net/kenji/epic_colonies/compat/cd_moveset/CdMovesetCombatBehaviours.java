package net.kenji.epic_colonies.compat.cd_moveset;


import com.mojang.datafixers.util.Pair;
import net.corruptdog.cdm.gameasset.CorruptAnimations;
import net.corruptdog.cdm.world.CorruptWeaponCategories;
import net.kenji.epic_colonies.compat.CompatMobCombatBehaviours;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.MobCombatBehaviors;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;

public class CdMovesetCombatBehaviours {

    static CombatBehaviors.Builder<HumanoidMobPatch<?>> swordSBehaviour;
    static CombatBehaviors.Builder<HumanoidMobPatch<?>> daggerDualSBehaviour;

    static CombatBehaviors.Builder<HumanoidMobPatch<?>> tachiSBehaviour;
    static CombatBehaviors.Builder<HumanoidMobPatch<?>> longswordSBehaviour;
    static CombatBehaviors.Builder<HumanoidMobPatch<?>> greatswordSBehaviour;

    static CombatBehaviors.Builder<HumanoidMobPatch<?>> spearOneHandSBehaviour;
    static CombatBehaviors.Builder<HumanoidMobPatch<?>> spearTwoHandSBehaviour;

    @SuppressWarnings("unchecked")
    public static void init(){
     buildMotions();

        CompatMobCombatBehaviours.HUMANOID_S_SWORD = CompatMobCombatBehaviours.register(
                CapabilityItem.WeaponCategories.SWORD,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.ONE_HAND, swordSBehaviour, Animations.BIPED_HOLD_LONGSWORD, Animations.BIPED_WALK, Animations.BIPED_RUN),
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, MobCombatBehaviors.HUMANOID_DUAL_SWORD, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_RUN_DUAL)
        );

        CompatMobCombatBehaviours.HUMANOID_S_DAGGER = CompatMobCombatBehaviours.register(
                CapabilityItem.WeaponCategories.DAGGER,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.ONE_HAND, MobCombatBehaviors.HUMANOID_ONEHAND_DAGGER, Animations.BIPED_IDLE, Animations.BIPED_WALK, Animations.BIPED_HOLD_SPEAR, Animations.BIPED_RUN),
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, daggerDualSBehaviour, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_RUN_DUAL)
                );
        CompatMobCombatBehaviours.HUMANOID_S_TACHI = CompatMobCombatBehaviours.register(
                CapabilityItem.WeaponCategories.TACHI,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, tachiSBehaviour, Animations.BIPED_HOLD_TACHI, Animations.BIPED_HOLD_TACHI, Animations.BIPED_RUN_SPEAR)
        );
        CompatMobCombatBehaviours.HUMANOID_S_LONGSWORD = CompatMobCombatBehaviours.register(
                CapabilityItem.WeaponCategories.LONGSWORD,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, longswordSBehaviour, Animations.BIPED_HOLD_LONGSWORD, Animations.BIPED_WALK_LONGSWORD, Animations.BIPED_HOLD_SPEAR, Animations.BIPED_RUN_LONGSWORD)
        );
        CompatMobCombatBehaviours.HUMANOID_S_SPEAR = CompatMobCombatBehaviours.register(
                CapabilityItem.WeaponCategories.SPEAR,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.ONE_HAND, spearOneHandSBehaviour, Animations.BIPED_HOLD_SPEAR, Animations.BIPED_HOLD_SPEAR, Animations.BIPED_RUN_SPEAR),
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, spearTwoHandSBehaviour, Animations.BIPED_HOLD_SPEAR, Animations.BIPED_HOLD_SPEAR, Animations.BIPED_RUN_SPEAR)

        );
        CompatMobCombatBehaviours.HUMANOID_S_GREATSWORD = CompatMobCombatBehaviours.register(
                CapabilityItem.WeaponCategories.GREATSWORD,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, greatswordSBehaviour, Animations.BIPED_HOLD_GREATSWORD, Animations.BIPED_WALK_GREATSWORD, Animations.BIPED_RUN_GREATSWORD)
        );
    }
    @SuppressWarnings("unchecked")
    private static void buildMotions(){
        swordSBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.SWORD_ONEHAND_AUTO1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, DynamicBehaviour.of(CorruptAnimations.SWORD_ONEHAND_AUTO1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.SWORD_ONEHAND_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, DynamicBehaviour.of(CorruptAnimations.TACHI_TWOHAND_AUTO_1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.SWORD_ONEHAND_AUTO2).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.SWORD_ONEHAND_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );
        daggerDualSBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.DAGGER_DUAL_AUTO1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, DynamicBehaviour.of(CorruptAnimations.DAGGER_DUAL_AUTO1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.DAGGER_DUAL_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, DynamicBehaviour.of(CorruptAnimations.DAGGER_DUAL_AUTO1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.DAGGER_DUAL_AUTO2).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.DAGGER_DUAL_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );
        tachiSBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.TACHI_TWOHAND_AUTO_1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, DynamicBehaviour.of(CorruptAnimations.TACHI_TWOHAND_AUTO_1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.TACHI_TWOHAND_AUTO_2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, DynamicBehaviour.of(CorruptAnimations.TACHI_TWOHAND_AUTO_1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.TACHI_TWOHAND_AUTO_2).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.TACHI_TWOHAND_AUTO_3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );

        longswordSBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.LONGSWORD_OLD_AUTO1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, DynamicBehaviour.of(CorruptAnimations.LONGSWORD_OLD_AUTO1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.LONGSWORD_OLD_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, DynamicBehaviour.of(CorruptAnimations.LONGSWORD_OLD_AUTO1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.LONGSWORD_OLD_AUTO2).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.LONGSWORD_OLD_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );

        spearOneHandSBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.SSPEAR_ONEHAND_AUTO).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );
        spearTwoHandSBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.SSPEAR_TWOHAND_AUTO1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, DynamicBehaviour.of(CorruptAnimations.SSPEAR_TWOHAND_AUTO1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.SSPEAR_TWOHAND_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );
        greatswordSBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.GREATSWORD_OLD_AUTO1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, DynamicBehaviour.of(CorruptAnimations.GREATSWORD_OLD_AUTO1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.GREATSWORD_OLD_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, DynamicBehaviour.of(CorruptAnimations.GREATSWORD_OLD_AUTO1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.GREATSWORD_OLD_AUTO2).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.GREATSWORD_OLD_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
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
