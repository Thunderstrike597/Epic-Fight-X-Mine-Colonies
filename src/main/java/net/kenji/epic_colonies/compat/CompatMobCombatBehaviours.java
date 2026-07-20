package net.kenji.epic_colonies.compat;

import net.kenji.epic_colonies.gameasset.EpicColoniesWeaponCategory;
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

public class CompatMobCombatBehaviours extends CombatBehaviourBase{
    public static CombatBehaviors.Builder<HumanoidMobPatch<?>> swordOneHandBehaviour;
    public static CombatBehaviors.Builder<HumanoidMobPatch<?>> swordDualBehaviour;

    public static CombatBehaviors.Builder<HumanoidMobPatch<?>> daggerBehaviour;
    public static CombatBehaviors.Builder<HumanoidMobPatch<?>> daggerDualBehaviour;

    public static CombatBehaviors.Builder<HumanoidMobPatch<?>> tachiBehaviour;
    public static CombatBehaviors.Builder<HumanoidMobPatch<?>> longswordOneHandBehaviour;
    public static CombatBehaviors.Builder<HumanoidMobPatch<?>> longswordTwoHandBehaviour;

    public static CombatBehaviors.Builder<HumanoidMobPatch<?>> greatswordBehaviour;

    public static CombatBehaviors.Builder<HumanoidMobPatch<?>> spearOneHandBehaviour;
    public static CombatBehaviors.Builder<HumanoidMobPatch<?>> spearTwoHandBehaviour;

    public static CombatBehaviors.Builder<HumanoidMobPatch<?>> katanaBehaviour;


    /// ===MAIN MOTIONS=== ///
    public static WeaponMotionDetails HUMANOID_SWORD;
    public static WeaponMotionDetails HUMANOID_SWORD_DUAL;

    public static WeaponMotionDetails HUMANOID_DAGGER;
    public static WeaponMotionDetails HUMANOID_DAGGER_DUAL;

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
    public static WeaponMotionDetails HUMANOID_S_SWORD_DUAL;
    public static WeaponMotionDetails HUMANOID_S_DAGGER;
    public static WeaponMotionDetails HUMANOID_S_DAGGER_DUAL;
    public static WeaponMotionDetails HUMANOID_S_TACHI;
    public static WeaponMotionDetails HUMANOID_S_LONGSWORD;
    public static WeaponMotionDetails HUMANOID_S_SPEAR;
    public static WeaponMotionDetails HUMANOID_S_GREATSWORD;



    public static void initEpicFightWeaponMotions() {
        buildMotions();

        HUMANOID_SWORD = register(
                CapabilityItem.WeaponCategories.SWORD,
                motion(CapabilityItem.Styles.ONE_HAND, swordOneHandBehaviour, Animations.BIPED_HOLD_LONGSWORD, Animations.BIPED_WALK, Animations.BIPED_RUN),
                motion(CapabilityItem.Styles.TWO_HAND, swordDualBehaviour, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_RUN_DUAL)
        );
        HUMANOID_SWORD_DUAL = register(
                EpicColoniesWeaponCategory.DUAL_SWORDS,
                motion(CapabilityItem.Styles.TWO_HAND, swordDualBehaviour, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_RUN_DUAL)
        );
        HUMANOID_DAGGER = register(
                CapabilityItem.WeaponCategories.DAGGER,
                motion(CapabilityItem.Styles.ONE_HAND, daggerBehaviour, Animations.BIPED_IDLE, Animations.BIPED_WALK, Animations.BIPED_HOLD_SPEAR, Animations.BIPED_RUN),
                motion(CapabilityItem.Styles.TWO_HAND, daggerDualBehaviour, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_RUN_DUAL)
        );
        HUMANOID_DAGGER_DUAL = register(
                EpicColoniesWeaponCategory.DUAL_DAGGER,
                motion(CapabilityItem.Styles.TWO_HAND, daggerDualBehaviour, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_RUN_DUAL)
        );
        HUMANOID_LONGSWORD = register(
                CapabilityItem.WeaponCategories.LONGSWORD,
                motion(CapabilityItem.Styles.ONE_HAND, longswordOneHandBehaviour, Animations.BIPED_HOLD_LONGSWORD, Animations.BIPED_WALK_LONGSWORD, Animations.BIPED_HOLD_SPEAR, Animations.BIPED_RUN_LONGSWORD),
                motion(CapabilityItem.Styles.TWO_HAND, longswordTwoHandBehaviour, Animations.BIPED_HOLD_LONGSWORD, Animations.BIPED_WALK_LONGSWORD, Animations.BIPED_HOLD_SPEAR, Animations.BIPED_RUN_LONGSWORD)
        );

        HUMANOID_TACHI = register(
                CapabilityItem.WeaponCategories.TACHI,
                motion(CapabilityItem.Styles.TWO_HAND, tachiBehaviour, Animations.BIPED_HOLD_TACHI, Animations.BIPED_HOLD_TACHI, Animations.BIPED_RUN_SPEAR)
        );
        HUMANOID_SPEAR = register(
                CapabilityItem.WeaponCategories.SPEAR,
                motion(CapabilityItem.Styles.ONE_HAND, spearOneHandBehaviour, Animations.BIPED_HOLD_TACHI, Animations.BIPED_HOLD_TACHI, Animations.BIPED_RUN_SPEAR),
                motion(CapabilityItem.Styles.TWO_HAND, spearTwoHandBehaviour, Animations.BIPED_HOLD_TACHI, Animations.BIPED_HOLD_TACHI, Animations.BIPED_RUN_SPEAR)
        );
        HUMANOID_GREATSWORD = register(
                CapabilityItem.WeaponCategories.GREATSWORD,
                motion(CapabilityItem.Styles.TWO_HAND, greatswordBehaviour, Animations.BIPED_HOLD_GREATSWORD, Animations.BIPED_HOLD_GREATSWORD, Animations.BIPED_RUN_GREATSWORD)
        );
        HUMANOID_UCHIGATANA = register(
                CapabilityItem.WeaponCategories.UCHIGATANA,
                motion(CapabilityItem.Styles.TWO_HAND, katanaBehaviour, Animations.BIPED_HOLD_UCHIGATANA, Animations.BIPED_HOLD_UCHIGATANA, Animations.BIPED_RUN_UCHIGATANA)
        );
    }
    @SuppressWarnings("unchecked")
    private static void buildMotions(){
        swordOneHandBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.SWORD_AUTO1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.SWORD_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.SWORD_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, CombatBehaviourBase.DynamicBehaviour.of(Animations.SWORD_AUTO1).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.SWORD_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, CombatBehaviourBase.DynamicBehaviour.of(Animations.SWORD_AUTO1).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.SWORD_AUTO2).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.SWORD_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );
        swordDualBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.SWORD_DUAL_AUTO1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.SWORD_DUAL_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(25, CombatBehaviourBase.DynamicBehaviour.of(Animations.SWORD_DUAL_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, CombatBehaviourBase.DynamicBehaviour.of(Animations.SWORD_DUAL_AUTO1).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.SWORD_DUAL_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, CombatBehaviourBase.DynamicBehaviour.of(Animations.SWORD_DUAL_AUTO1).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.SWORD_DUAL_AUTO2).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.SWORD_DUAL_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );
        daggerBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_AUTO1).distanceMinMax(1, 2).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_AUTO2).distanceMinMax(1, 2).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_AUTO3).distanceMinMax(1, 2).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_AUTO1).distanceMinMax(1, 2).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_AUTO1).distanceMinMax(1, 2).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_AUTO2).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );
        daggerDualBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_DUAL_AUTO1).distanceMinMax(1, 2).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_DUAL_AUTO2).distanceMinMax(1, 2).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_DUAL_AUTO3).distanceMinMax(1, 2).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_DUAL_AUTO1).distanceMinMax(1, 2).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_DUAL_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_DUAL_AUTO1).distanceMinMax(1, 2).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_DUAL_AUTO2).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_DUAL_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(8, CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_DUAL_AUTO1).distanceMinMax(1, 2).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_DUAL_AUTO2).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_DUAL_AUTO3).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.DAGGER_DUAL_AUTO4).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );
        tachiBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.TACHI_AUTO1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.TACHI_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.TACHI_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, CombatBehaviourBase.DynamicBehaviour.of(Animations.TACHI_AUTO1).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.TACHI_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, CombatBehaviourBase.DynamicBehaviour.of(Animations.TACHI_AUTO1).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.TACHI_AUTO2).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.TACHI_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );

        longswordOneHandBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.LONGSWORD_AUTO1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.LONGSWORD_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.LONGSWORD_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, CombatBehaviourBase.DynamicBehaviour.of(Animations.LONGSWORD_AUTO1).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.LONGSWORD_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, CombatBehaviourBase.DynamicBehaviour.of(Animations.LONGSWORD_AUTO1).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.LONGSWORD_AUTO2).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.LONGSWORD_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );
        longswordTwoHandBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.LONGSWORD_LIECHTENAUER_AUTO1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.LONGSWORD_LIECHTENAUER_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.LONGSWORD_LIECHTENAUER_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, CombatBehaviourBase.DynamicBehaviour.of(Animations.LONGSWORD_LIECHTENAUER_AUTO1).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.LONGSWORD_LIECHTENAUER_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, CombatBehaviourBase.DynamicBehaviour.of(Animations.LONGSWORD_LIECHTENAUER_AUTO1).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.LONGSWORD_LIECHTENAUER_AUTO2).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.LONGSWORD_LIECHTENAUER_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );

        spearOneHandBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.SPEAR_ONEHAND_AUTO).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );
        spearTwoHandBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.SPEAR_TWOHAND_AUTO1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.SPEAR_TWOHAND_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, CombatBehaviourBase.DynamicBehaviour.of(Animations.SPEAR_TWOHAND_AUTO1).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.SPEAR_TWOHAND_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );
        greatswordBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.GREATSWORD_AUTO1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.GREATSWORD_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, CombatBehaviourBase.DynamicBehaviour.of(Animations.GREATSWORD_AUTO1).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.GREATSWORD_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );
        katanaBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.UCHIGATANA_AUTO1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.UCHIGATANA_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(Animations.UCHIGATANA_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, CombatBehaviourBase.DynamicBehaviour.of(Animations.UCHIGATANA_AUTO1).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.UCHIGATANA_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, CombatBehaviourBase.DynamicBehaviour.of(Animations.UCHIGATANA_AUTO1).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.UCHIGATANA_AUTO2).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(Animations.UCHIGATANA_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );
    }
}
