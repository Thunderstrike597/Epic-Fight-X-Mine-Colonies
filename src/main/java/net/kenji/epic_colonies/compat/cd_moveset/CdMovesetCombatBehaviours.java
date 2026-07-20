package net.kenji.epic_colonies.compat.cd_moveset;


import net.corruptdog.cdm.gameasset.CorruptAnimations;
import net.kenji.epic_colonies.compat.CombatBehaviourBase;
import net.kenji.epic_colonies.compat.CompatMobCombatBehaviours;
import net.kenji.epic_colonies.gameasset.EpicColoniesWeaponCategory;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.MobCombatBehaviors;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;

public class CdMovesetCombatBehaviours extends CombatBehaviourBase {

    static CombatBehaviors.Builder<HumanoidMobPatch<?>> swordSBehaviour;
    static CombatBehaviors.Builder<HumanoidMobPatch<?>> daggerDualSBehaviour;

    static CombatBehaviors.Builder<HumanoidMobPatch<?>> tachiSBehaviour;
    static CombatBehaviors.Builder<HumanoidMobPatch<?>> longswordSBehaviour;
    static CombatBehaviors.Builder<HumanoidMobPatch<?>> greatswordSBehaviour;

    static CombatBehaviors.Builder<HumanoidMobPatch<?>> spearOneHandSBehaviour;
    static CombatBehaviors.Builder<HumanoidMobPatch<?>> spearTwoHandSBehaviour;
    
    static CombatBehaviors.Builder<HumanoidMobPatch<?>> katanaSBehaviour;

    @SuppressWarnings("unchecked")
    public static void init(){
     buildMotions();

        CompatMobCombatBehaviours.HUMANOID_S_SWORD = register(
                CapabilityItem.WeaponCategories.SWORD,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.ONE_HAND, swordSBehaviour, Animations.BIPED_HOLD_LONGSWORD, Animations.BIPED_WALK, Animations.BIPED_RUN),
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, CompatMobCombatBehaviours.swordDualBehaviour, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_RUN_DUAL)
        );
        CompatMobCombatBehaviours.HUMANOID_S_DAGGER_DUAL = register(
                EpicColoniesWeaponCategory.DUAL_SWORDS,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, CompatMobCombatBehaviours.swordDualBehaviour, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_RUN_DUAL)
        );
        CompatMobCombatBehaviours.HUMANOID_S_DAGGER = register(
                CapabilityItem.WeaponCategories.DAGGER,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.ONE_HAND, CompatMobCombatBehaviours.daggerBehaviour, Animations.BIPED_IDLE, Animations.BIPED_WALK, Animations.BIPED_HOLD_SPEAR, Animations.BIPED_RUN),
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, daggerDualSBehaviour, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_RUN_DUAL)
                );
        CompatMobCombatBehaviours.HUMANOID_S_DAGGER_DUAL = register(
                EpicColoniesWeaponCategory.DUAL_DAGGER,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, daggerDualSBehaviour, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_HOLD_DUAL_WEAPON, Animations.BIPED_RUN_DUAL)
        );
        CompatMobCombatBehaviours.HUMANOID_S_TACHI = register(
                CapabilityItem.WeaponCategories.TACHI,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, tachiSBehaviour, Animations.BIPED_HOLD_TACHI, Animations.BIPED_HOLD_TACHI, Animations.BIPED_RUN_SPEAR)
        );
        CompatMobCombatBehaviours.HUMANOID_S_LONGSWORD = register(
                CapabilityItem.WeaponCategories.LONGSWORD,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, longswordSBehaviour, Animations.BIPED_HOLD_LONGSWORD, Animations.BIPED_WALK_LONGSWORD, Animations.BIPED_HOLD_SPEAR, Animations.BIPED_RUN_LONGSWORD)
        );
        CompatMobCombatBehaviours.HUMANOID_S_SPEAR = register(
                CapabilityItem.WeaponCategories.SPEAR,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.ONE_HAND, spearOneHandSBehaviour, Animations.BIPED_HOLD_SPEAR, Animations.BIPED_HOLD_SPEAR, Animations.BIPED_RUN_SPEAR),
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, spearTwoHandSBehaviour, Animations.BIPED_HOLD_SPEAR, Animations.BIPED_HOLD_SPEAR, Animations.BIPED_RUN_SPEAR)

        );
        CompatMobCombatBehaviours.HUMANOID_S_GREATSWORD = register(
                CapabilityItem.WeaponCategories.GREATSWORD,
                CompatMobCombatBehaviours.motion(CapabilityItem.Styles.TWO_HAND, greatswordSBehaviour, Animations.BIPED_HOLD_GREATSWORD, Animations.BIPED_WALK_GREATSWORD, Animations.BIPED_RUN_GREATSWORD)
        );
        CompatMobCombatBehaviours.HUMANOID_UCHIGATANA = register(
                CapabilityItem.WeaponCategories.UCHIGATANA,
                motion(CapabilityItem.Styles.TWO_HAND, katanaSBehaviour, CorruptAnimations.BIPED_HOLD_KATANA, CorruptAnimations.BIPED_HOLD_KATANA, CorruptAnimations.RUN_KATANA)
        );
    }
    @SuppressWarnings("unchecked")
    private static void buildMotions(){
        swordSBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.SWORD_ONEHAND_AUTO1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.SWORD_ONEHAND_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.SWORD_ONEHAND_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, DynamicBehaviour.of(CorruptAnimations.SWORD_ONEHAND_AUTO1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.SWORD_ONEHAND_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, DynamicBehaviour.of(CorruptAnimations.SWORD_ONEHAND_AUTO1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.SWORD_ONEHAND_AUTO2).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.SWORD_ONEHAND_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(8, DynamicBehaviour.of(CorruptAnimations.SWORD_ONEHAND_AUTO1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.SWORD_ONEHAND_AUTO2).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.SWORD_ONEHAND_AUTO3).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.SWORD_ONEHAND_AUTO4).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );
        daggerDualSBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.DAGGER_DUAL_AUTO1).distanceMinMax(1, 2).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.DAGGER_DUAL_AUTO2).distanceMinMax(1, 2).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.DAGGER_DUAL_AUTO3).distanceMinMax(1, 2).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, DynamicBehaviour.of(CorruptAnimations.DAGGER_DUAL_AUTO1).distanceMinMax(1, 2).build(), DynamicBehaviour.of(CorruptAnimations.DAGGER_DUAL_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, DynamicBehaviour.of(CorruptAnimations.DAGGER_DUAL_AUTO1).distanceMinMax(1, 2).build(), DynamicBehaviour.of(CorruptAnimations.DAGGER_DUAL_AUTO2).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.DAGGER_DUAL_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(8, DynamicBehaviour.of(CorruptAnimations.DAGGER_DUAL_AUTO1).distanceMinMax(1, 2).build(), DynamicBehaviour.of(CorruptAnimations.DAGGER_DUAL_AUTO2).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.DAGGER_DUAL_AUTO3).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.DAGGER_DUAL_AUTO4).distanceMinMax(1, 3).build())
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
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.TACHI_TWOHAND_AUTO_2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.TACHI_TWOHAND_AUTO_3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, DynamicBehaviour.of(CorruptAnimations.TACHI_TWOHAND_AUTO_1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.TACHI_TWOHAND_AUTO_2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, DynamicBehaviour.of(CorruptAnimations.TACHI_TWOHAND_AUTO_1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.TACHI_TWOHAND_AUTO_2).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.TACHI_TWOHAND_AUTO_3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(8, DynamicBehaviour.of(CorruptAnimations.TACHI_TWOHAND_AUTO_1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.TACHI_TWOHAND_AUTO_2).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.TACHI_TWOHAND_AUTO_3).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.TACHI_TWOHAND_AUTO_4).distanceMinMax(1, 3).build())
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
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.LONGSWORD_OLD_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.LONGSWORD_OLD_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, DynamicBehaviour.of(CorruptAnimations.LONGSWORD_OLD_AUTO1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.LONGSWORD_OLD_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, DynamicBehaviour.of(CorruptAnimations.LONGSWORD_OLD_AUTO1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.LONGSWORD_OLD_AUTO2).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.LONGSWORD_OLD_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(8, DynamicBehaviour.of(CorruptAnimations.LONGSWORD_OLD_AUTO1).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.LONGSWORD_OLD_AUTO2).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.LONGSWORD_OLD_AUTO3).distanceMinMax(1, 3).build(), DynamicBehaviour.of(CorruptAnimations.LONGSWORD_OLD_AUTO4).distanceMinMax(1, 3).build())
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
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.SSPEAR_TWOHAND_AUTO2).distanceMinMax(1, 3).build())
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
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.GREATSWORD_OLD_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, DynamicBehaviour.of(CorruptAnimations.GREATSWORD_OLD_AUTO3).distanceMinMax(1, 3).build())
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
        katanaSBehaviour = CombatBehaviors.builder().newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(CorruptAnimations.KATANA_AUTO1).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(CorruptAnimations.KATANA_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(30, CombatBehaviourBase.DynamicBehaviour.of(CorruptAnimations.KATANA_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(22, CombatBehaviourBase.DynamicBehaviour.of(CorruptAnimations.KATANA_AUTO1).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(CorruptAnimations.KATANA_AUTO2).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(14, CombatBehaviourBase.DynamicBehaviour.of(CorruptAnimations.KATANA_AUTO1).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(CorruptAnimations.KATANA_AUTO2).distanceMinMax(1, 3).build(), CombatBehaviourBase.DynamicBehaviour.of(CorruptAnimations.KATANA_AUTO3).distanceMinMax(1, 3).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_ROLL_BACKWARD).distanceMinMax(0, 1.5F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_LEFT).distanceMinMax(0, 1.8F).build())
        ).newBehaviorSeries(
                createBehaviourSeries(40, CombatBehaviourBase.DynamicBehaviour.of(Animations.BIPED_STEP_RIGHT).distanceMinMax(0, 1.8F).build())
        );
    }
   
}
