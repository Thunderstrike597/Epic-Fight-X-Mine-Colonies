package net.kenji.epic_colonies.gameasset;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.registry.entries.EpicFightSkills;
import yesman.epicfight.registry.entries.EpicFightSounds;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.registry.entries.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCapabilityPresets;

import java.util.function.Function;

public class EpicColoniesWeaponCapabilityPresets {

    public static final Function<Item, CapabilityItem.Builder<?>> DUAL_SWORDS = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(EpicColoniesWeaponCategory.DUAL_SWORDS)
                .styleProvider((playerpatch) ->
                      CapabilityItem.Styles.TWO_HAND)
                .collider(ColliderPreset.SWORD)
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND,
                        new AnimationManager.AnimationAccessor[]{
                                Animations.SWORD_DUAL_AUTO1,
                                Animations.SWORD_DUAL_AUTO2,
                                Animations.SWORD_DUAL_AUTO3,
                                Animations.SWORD_DUAL_DASH,
                                Animations.SWORD_DUAL_AIR_SLASH
                })
                .newStyleCombo(CapabilityItem.Styles.MOUNT,
                        new AnimationManager.AnimationAccessor[]{
                                Animations.SWORD_MOUNT_ATTACK}
                )
                .innateSkill(CapabilityItem.Styles.TWO_HAND,
                        (itemstack) -> EpicFightSkills.DANCING_EDGE.get())
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_DUAL)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_DUAL_WEAPON);
        if (item instanceof TieredItem tieredItem) {
            builder.hitSound(tieredItem.getTier() == Tiers.WOOD ? (SoundEvent) EpicFightSounds.BLUNT_HIT.get() : (SoundEvent)EpicFightSounds.BLADE_HIT.get());
            builder.hitParticle(tieredItem.getTier() == Tiers.WOOD ? (HitParticleType) EpicFightParticles.HIT_BLUNT.get() : (HitParticleType)EpicFightParticles.HIT_BLADE.get());
        }

        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder<?>> DUAL_DAGGERS = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder().category(CapabilityItem.WeaponCategories.DAGGER)
                .styleProvider((playerpatch) -> CapabilityItem.Styles.TWO_HAND)
                .swingSound((SoundEvent)EpicFightSounds.WHOOSH_SMALL.get()).
                collider(ColliderPreset.DAGGER)
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND,
                        new AnimationManager.AnimationAccessor[]{
                                Animations.DAGGER_DUAL_AUTO1,
                                Animations.DAGGER_DUAL_AUTO2,
                                Animations.DAGGER_DUAL_AUTO3,
                                Animations.DAGGER_DUAL_AUTO4,
                                Animations.DAGGER_DUAL_DASH,
                                Animations.DAGGER_DUAL_AIR_SLASH})
                .newStyleCombo(CapabilityItem.Styles.MOUNT,
                        new AnimationManager.AnimationAccessor[]{Animations.SWORD_MOUNT_ATTACK})
                .innateSkill(CapabilityItem.Styles.ONE_HAND,
                        (itemstack) -> EpicFightSkills.EVISCERATE.get())
                .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemstack) ->
                        EpicFightSkills.BLADE_RUSH.get())
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_DUAL)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_DUAL_WEAPON);

        if (item instanceof TieredItem tieredItem) {
            builder.hitSound(tieredItem.getTier() == Tiers.WOOD ? (SoundEvent)EpicFightSounds.BLUNT_HIT.get() : (SoundEvent)EpicFightSounds.BLADE_HIT.get());
            builder.hitParticle(tieredItem.getTier() == Tiers.WOOD ? (HitParticleType)EpicFightParticles.HIT_BLUNT.get() : (HitParticleType)EpicFightParticles.HIT_BLADE.get());
        }

        return builder;
    };
}