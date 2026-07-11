package net.kenji.epic_colonies.gameasset.patch;

import net.kenji.epic_colonies.gameasset.EpicColoniesAnimations;
import net.kenji.epic_colonies.gameasset.EpicColoniesArmatures;
import net.kenji.epic_colonies.gameasset.armatures.CitizenArmature;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.MobCombatBehaviors;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.Factions;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
import yesman.epicfight.world.item.*;

public class MercenaryPatch<E extends PathfinderMob> extends HumanoidMobPatch<PathfinderMob> {

    public MercenaryPatch() {
        super(Factions.VILLAGER);
    }

    public static int MAX_BLINK_COUNTER = 20 * 20;
    public int blinkCounter = 0;
    @Override
    public boolean overrideRender() {
        return true;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        animator.playAnimation(EpicColoniesAnimations.CITIZEN_BLINK, 0F);
        animator.playAnimation(EpicColoniesAnimations.CITIZEN_EYES_MOVE, 0F);

    }
    @Override
    public Joint getParentJointOfHand(InteractionHand hand) {
        if(this.getArmature() instanceof CitizenArmature citizenArmature){
            return hand == InteractionHand.MAIN_HAND ? citizenArmature.rightToolJoint() : citizenArmature.leftToolJoint();
        }
        return (Joint)this.parentJointOfHands.getOrDefault(hand, this.armature.rootJoint);
    }
    @Override
    public HumanoidArmature getArmature() {
        return EpicColoniesArmatures.CITIZEN_REGULAR.get();
    }

    @Override
    protected CombatBehaviors.Builder<HumanoidMobPatch<?>> getHoldingItemWeaponMotionBuilder() {
        CapabilityItem mainHandCap = EpicFightCapabilities.getItemStackCapability(this.getOriginal().getMainHandItem());
        if (mainHandCap == null) return MobCombatBehaviors.HUMANOID_FIST;
        WeaponCategory category = mainHandCap.getWeaponCategory();
        if (category == CapabilityItem.WeaponCategories.SWORD) {
            return MobCombatBehaviors.SKELETON_SWORD;
        }
        if (this.getOriginal().getMainHandItem().getItem() instanceof SpearItem || category == CapabilityItem.WeaponCategories.SPEAR) {
            if (this.getOriginal().getOffhandItem().isEmpty())
                return MobCombatBehaviors.HUMANOID_SPEAR_TWOHAND;
            return MobCombatBehaviors.HUMANOID_SPEAR_ONEHAND;
        }
        if (this.getOriginal().getMainHandItem().getItem() instanceof DaggerItem || category == CapabilityItem.WeaponCategories.DAGGER) {
            if (this.getOriginal().getOffhandItem().getItem() instanceof DaggerItem || category == CapabilityItem.WeaponCategories.DAGGER)
                return MobCombatBehaviors.HUMANOID_TWOHAND_DAGGER;
            return MobCombatBehaviors.HUMANOID_ONEHAND_DAGGER;
        }
        if (this.getOriginal().getMainHandItem().getItem() instanceof LongswordItem || category == CapabilityItem.WeaponCategories.LONGSWORD) {
            return MobCombatBehaviors.HUMANOID_LONGSWORD;
        }
        if (this.getOriginal().getMainHandItem().getItem() instanceof TachiItem || category == CapabilityItem.WeaponCategories.TACHI) {
            return MobCombatBehaviors.HUMANOID_TACHI;
        }
        if (this.getOriginal().getMainHandItem().getItem() instanceof GreatswordItem || category == CapabilityItem.WeaponCategories.GREATSWORD) {
            return MobCombatBehaviors.HUMANOID_GREATSWORD;
        }

        return super.getHoldingItemWeaponMotionBuilder();
    }

    @Override
    public void setAIAsInfantry(boolean holdingRangedWeapon) {
        if(!holdingRangedWeapon){
            super.setAIAsInfantry(false);
        }
    }

    @Override
    public void updateMotion(boolean considerInaction) {
        if(this.getOriginal().getMainHandItem().getItem() instanceof ProjectileWeaponItem)
            super.commonAggressiveRangedMobUpdateMotion(considerInaction);
        else super.commonMobUpdateMotion(considerInaction);
    }

    @Override
    protected void clientTick(LivingEvent.LivingTickEvent event) {
        super.clientTick(event);
        AnimationPlayer highestAnimPlayer = this.getClientAnimator().getCompositeLayer(Layer.Priority.HIGHEST).animationPlayer;
        AnimationPlayer middleAnimPlayer = this.getClientAnimator().getCompositeLayer(Layer.Priority.MIDDLE).animationPlayer;

        if(highestAnimPlayer != null){
            if(highestAnimPlayer.getAnimation() == null || highestAnimPlayer.getAnimation().get() != EpicColoniesAnimations.CITIZEN_BLINK.get()){
                animator.playAnimationInstantly(EpicColoniesAnimations.CITIZEN_BLINK);
            }
            if(middleAnimPlayer.getAnimation() == null || middleAnimPlayer.getAnimation().get() != EpicColoniesAnimations.CITIZEN_EYES_MOVE.get()){
                animator.playAnimationInstantly(EpicColoniesAnimations.CITIZEN_EYES_MOVE);
            }
        }
    }
    public boolean shouldJRunWithAnim() {
        return (getCurrentForwardSpeed() > 0.08);
    }

    public double getCurrentForwardSpeed() {
        Vec3 movement = this.getOriginal().getDeltaMovement();
        Vec3 forward = this.getOriginal().getForward();
        return movement.dot(forward);
    }

    @Override
    public void initAnimator(Animator animator) {
        // All available living motions are listed in this enum: https://github.com/Epic-Fight/epicfight/blob/1.21.1/src/main/java/yesman/epicfight/api/animation/LivingMotions.java#L4-L6
        animator.addLivingAnimation(LivingMotions.IDLE, Animations.BIPED_IDLE);
        animator.addLivingAnimation(LivingMotions.WALK, Animations.BIPED_WALK);
        animator.addLivingAnimation(LivingMotions.RUN, Animations.BIPED_RUN);
        animator.addLivingAnimation(LivingMotions.CHASE, Animations.BIPED_RUN);

        animator.addLivingAnimation(LivingMotions.FALL, Animations.BIPED_FALL);
        animator.addLivingAnimation(LivingMotions.SIT, Animations.BIPED_SIT);
        animator.addLivingAnimation(LivingMotions.DEATH, Animations.BIPED_DEATH);
        animator.addLivingAnimation(LivingMotions.JUMP, Animations.BIPED_JUMP);
        animator.addLivingAnimation(LivingMotions.SLEEP, Animations.BIPED_SLEEPING);
        animator.addLivingAnimation(LivingMotions.AIM, Animations.BIPED_BOW_AIM);
        animator.addLivingAnimation(LivingMotions.SHOT, Animations.BIPED_BOW_SHOT);
        animator.addLivingAnimation(LivingMotions.DRINK, Animations.BIPED_DRINK);
        animator.addLivingAnimation(LivingMotions.EAT, Animations.BIPED_EAT);
    }
}