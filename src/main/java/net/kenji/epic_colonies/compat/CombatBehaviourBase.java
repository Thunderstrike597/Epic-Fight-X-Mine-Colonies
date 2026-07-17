package net.kenji.epic_colonies.compat;

import com.mojang.datafixers.util.Pair;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;

import java.util.ArrayList;
import java.util.List;

public abstract class CombatBehaviourBase {
    public static final List<CompatMobCombatBehaviours.WeaponMotionDetails> behaviourList = new ArrayList<>();


    public record WeaponMotions(Style style, CombatBehaviors.Builder<HumanoidMobPatch<?>> behaviour , AnimationManager.AnimationAccessor<? extends StaticAnimation> idleMotion,
                                AnimationManager.AnimationAccessor<? extends StaticAnimation> walkMotion,
                                AnimationManager.AnimationAccessor<? extends StaticAnimation> jogMotion,
                                AnimationManager.AnimationAccessor<? extends StaticAnimation> runMotion) {

    }
    public record WeaponMotionDetails(WeaponCategory category, WeaponMotions[] motions){

    }

    @SuppressWarnings("unchecked")
    protected static CombatBehaviors.BehaviorSeries.Builder createBehaviourSeries(float weight, DynamicBehaviour... dynamicBehaviours){
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
    public static CompatMobCombatBehaviours.WeaponMotions motion(Style style, CombatBehaviors.Builder<HumanoidMobPatch<?>> behaviour,
                                                                 AnimationManager.AnimationAccessor<? extends StaticAnimation> idle,
                                                                 AnimationManager.AnimationAccessor<? extends StaticAnimation> walk,
                                                                 AnimationManager.AnimationAccessor<? extends StaticAnimation> run) {
        return new CompatMobCombatBehaviours.WeaponMotions(style, behaviour, idle, walk, idle, run);
    }
    public static CompatMobCombatBehaviours.WeaponMotions motion(Style style, CombatBehaviors.Builder<HumanoidMobPatch<?>> behaviour,
                                                                 AnimationManager.AnimationAccessor<? extends StaticAnimation> idle,
                                                                 AnimationManager.AnimationAccessor<? extends StaticAnimation> walk,
                                                                 AnimationManager.AnimationAccessor<? extends StaticAnimation> jog,
                                                                 AnimationManager.AnimationAccessor<? extends StaticAnimation> run) {
        return new CompatMobCombatBehaviours.WeaponMotions(style, behaviour, idle, walk, jog, run);
    }

    public static CompatMobCombatBehaviours.WeaponMotionDetails register(
            WeaponCategory category,
            CompatMobCombatBehaviours.WeaponMotions... motions) {
        CompatMobCombatBehaviours.WeaponMotionDetails pair =
                new CompatMobCombatBehaviours.WeaponMotionDetails(category, motions);
        behaviourList.add(pair);
        return pair;
    }

}
