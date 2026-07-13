package net.kenji.epic_colonies.compat;

import com.mojang.datafixers.util.Pair;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.MobCombatBehaviors;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;

import java.util.ArrayList;
import java.util.List;

public class CompatMobCombatBehaviours {
    public static final List<WeaponMotionDetails> behaviourList = new ArrayList<>();


    /// ===MAIN MOTIONS=== ///
    public static WeaponMotionDetails HUMANOID_SWORD;
    public static WeaponMotionDetails HUMANOID_DAGGER;
    public static WeaponMotionDetails HUMANOID_LONGSWORD;
    public static WeaponMotionDetails HUMANOID_GREATSWORD;
    public static WeaponMotionDetails HUMANOID_TACHI;
    public static WeaponMotionDetails HUMANOID_SPEAR;
    public static WeaponMotionDetails HUMANOID_UCHIGATANA;

    /// ===WOM-COMPAT-MOTIONS=== ///
    public static WeaponMotionDetails HUMANOID_AGONY;
    public static WeaponMotionDetails HUMANOID_RUINE;
    public static WeaponMotionDetails HUMANOID_TORMENT;
    public static WeaponMotionDetails HUMANOID_STAFF;

    /// ===CDMOVESET-COMPAT-MOTIONS=== ///
    public static WeaponMotionDetails HUMANOID_S_SWORD;
    public static WeaponMotionDetails HUMANOID_S_DAGGER;
    public static WeaponMotionDetails HUMANOID_S_TACHI;
    public static WeaponMotionDetails HUMANOID_S_LONGSWORD;
    public static WeaponMotionDetails HUMANOID_S_SPEAR;
    public static WeaponMotionDetails HUMANOID_S_GREATSWORD;



    public record WeaponMotions(Style style, CombatBehaviors.Builder<HumanoidMobPatch<?>> behaviour , AnimationManager.AnimationAccessor<? extends StaticAnimation> idleMotion,
                                AnimationManager.AnimationAccessor<? extends StaticAnimation> walkMotion,
                                AnimationManager.AnimationAccessor<? extends StaticAnimation> runMotion) {

    }
    public record WeaponMotionDetails(WeaponCategory category, WeaponMotions[] motions){

    }


    public static WeaponMotions motion(Style style, CombatBehaviors.Builder<HumanoidMobPatch<?>> behaviour,
                                        AnimationManager.AnimationAccessor<? extends StaticAnimation> idle,
                                        AnimationManager.AnimationAccessor<? extends StaticAnimation> walk,
                                        AnimationManager.AnimationAccessor<? extends StaticAnimation> run) {
        return new WeaponMotions(style, behaviour,idle, walk, run);
    }

    public static WeaponMotionDetails register(
            WeaponCategory category,
            WeaponMotions... motions) {
        WeaponMotionDetails pair =
                new WeaponMotionDetails(category, motions);
        behaviourList.add(pair);
        return pair;
    }

    public static void initEpicFightWeaponMotions() {
        HUMANOID_SWORD = register(
                CapabilityItem.WeaponCategories.SWORD,
                motion(CapabilityItem.Styles.ONE_HAND, MobCombatBehaviors.SKELETON_SWORD, Animations.BIPED_IDLE, Animations.BIPED_WALK, Animations.BIPED_RUN),
                motion(CapabilityItem.Styles.TWO_HAND, MobCombatBehaviors.DROWNED_TRIDENT, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_RUN_DUAL)
        );
        HUMANOID_DAGGER = register(
                CapabilityItem.WeaponCategories.DAGGER,
                motion(CapabilityItem.Styles.ONE_HAND, MobCombatBehaviors.HUMANOID_ONEHAND_DAGGER, Animations.BIPED_IDLE, Animations.BIPED_WALK, Animations.BIPED_RUN),
                motion(CapabilityItem.Styles.TWO_HAND, MobCombatBehaviors.HUMANOID_TWOHAND_DAGGER, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_RUN_DUAL)
        );
        HUMANOID_LONGSWORD = register(
                CapabilityItem.WeaponCategories.LONGSWORD,
                motion(CapabilityItem.Styles.TWO_HAND, MobCombatBehaviors.HUMANOID_LONGSWORD, Animations.BIPED_HOLD_LONGSWORD, Animations.BIPED_WALK_LONGSWORD, Animations.BIPED_RUN_LONGSWORD)
        );

        HUMANOID_TACHI = register(
                CapabilityItem.WeaponCategories.TACHI,
                motion(CapabilityItem.Styles.TWO_HAND, MobCombatBehaviors.HUMANOID_TACHI, Animations.BIPED_HOLD_TACHI, Animations.BIPED_HOLD_TACHI, Animations.BIPED_RUN_SPEAR)
        );
        HUMANOID_SPEAR = register(
                CapabilityItem.WeaponCategories.SPEAR,

                motion(CapabilityItem.Styles.TWO_HAND, MobCombatBehaviors.HUMANOID_SPEAR_ONEHAND, Animations.BIPED_HOLD_TACHI, Animations.BIPED_HOLD_TACHI, Animations.BIPED_RUN_SPEAR)
        );
        HUMANOID_GREATSWORD = register(
                CapabilityItem.WeaponCategories.GREATSWORD,

                motion(CapabilityItem.Styles.TWO_HAND, MobCombatBehaviors.HUMANOID_GREATSWORD, Animations.BIPED_HOLD_GREATSWORD, Animations.BIPED_HOLD_GREATSWORD, Animations.BIPED_RUN_GREATSWORD)
        );
        HUMANOID_UCHIGATANA = register(
                CapabilityItem.WeaponCategories.UCHIGATANA,

                motion(CapabilityItem.Styles.TWO_HAND, MobCombatBehaviors.HUMANOID_KATANA, Animations.BIPED_HOLD_UCHIGATANA, Animations.BIPED_HOLD_UCHIGATANA, Animations.BIPED_RUN_UCHIGATANA)
        );
    }
}
