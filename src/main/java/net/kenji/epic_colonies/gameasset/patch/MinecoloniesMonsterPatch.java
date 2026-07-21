package net.kenji.epic_colonies.gameasset.patch;

import com.google.common.collect.ImmutableMap;
import com.minecolonies.api.entity.mobs.AbstractEntityMinecoloniesMonster;
import com.mojang.datafixers.util.Pair;
import net.kenji.epic_colonies.compat.CombatBehaviourBase;
import net.kenji.epic_colonies.compat.CompatMobCombatBehaviours;
import net.kenji.epic_colonies.gameasset.EpicColoniesAnimations;
import net.kenji.epic_colonies.gameasset.EpicColoniesArmatures;
import net.kenji.epic_colonies.gameasset.EpicColoniesLivingMotions;
import net.kenji.epic_colonies.gameasset.armatures.CitizenArmature;
import net.kenji.epic_colonies.network.EpicColoniesPacketHandler;
import net.kenji.epic_colonies.network.ServerBowActionPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.sounds.SoundEvent;
import yesman.epicfight.api.animation.*;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.registry.entries.EpicFightSounds;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.world.capabilities.entitypatch.Factions;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MinecoloniesMonsterPatch<E extends AbstractEntityMinecoloniesMonster> extends HumanoidMobPatch<AbstractEntityMinecoloniesMonster> {

    public MinecoloniesMonsterPatch(AbstractEntityMinecoloniesMonster entity) {
        super(entity, Factions.VILLAGER);
    }

    public static int MAX_BLINK_COUNTER = 20 * 20;
    public int blinkCounter = 0;
    public boolean wasUsingBow = false;
    public int bowUseCounter = 0;
    @Override
    public boolean overrideRender() {
        return true;
    }
    public void setWasUsingBow(boolean value){
        if(this.getCurrentLivingMotion() == EpicColoniesLivingMotions.JOG) return;
        this.wasUsingBow = value;

        if(this.getOriginal().level().isClientSide()) {
            EpicColoniesPacketHandler.sendToServer(new ServerBowActionPacket(this.getOriginal().getUUID(), this.wasUsingBow));
        }
    }
    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
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
    protected void setWeaponMotions() {
        super.setWeaponMotions();

        Map<WeaponCategory, Map<Style, Set<Pair<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>>>>> livingByCategory = new HashMap<>();
        Map<WeaponCategory, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> attackByCategory = new HashMap<>();

        for (CombatBehaviourBase.WeaponMotionDetails details : CombatBehaviourBase.behaviourList) {
            for (CombatBehaviourBase.WeaponMotions motions : details.motions()) {
                Set<Pair<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>>> livingMotionSet = Set.of(
                        Pair.of(LivingMotions.IDLE, motions.idleMotion()),
                        Pair.of(LivingMotions.WALK, motions.walkMotion()),
                        Pair.of(EpicColoniesLivingMotions.JOG, motions.jogMotion()),
                        Pair.of(LivingMotions.CHASE, motions.runMotion())
                );

                livingByCategory
                        .computeIfAbsent(details.category(), c -> new HashMap<>())
                        .put(motions.style(), livingMotionSet); // later registration for the same (category, style) wins

                attackByCategory
                        .computeIfAbsent(details.category(), c -> new HashMap<>())
                        .put(motions.style(), motions.behaviour());
            }
        }

        livingByCategory.forEach((category, byStyle) -> this.weaponLivingMotions.put(category, ImmutableMap.copyOf(byStyle)));
        attackByCategory.forEach((category, byStyle) -> this.weaponAttackMotions.put(category, ImmutableMap.copyOf(byStyle)));
    }

    @Override
    public void updateMotion(boolean considerInaction) {
        if(this.getOriginal().getMainHandItem().getItem() instanceof ProjectileWeaponItem)
            super.commonAggressiveRangedMobUpdateMotion(considerInaction);
        else super.commonMobUpdateMotion(considerInaction);
    }

    @Override
    public void preTickServer() {
        super.preTickServer();
        if(wasUsingBow && this.getCurrentLivingMotion() != EpicColoniesLivingMotions.JOG){
            bowUseCounter++;
        }
        else{
            bowUseCounter = 0;
        }
    }

    @Override
    public void preTickClient() {
        super.preTickClient();
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

    @Override
    public void initAnimator(Animator animator) {
        // All available living motions are listed in this enum: https://github.com/Epic-Fight/epicfight/blob/1.21.1/src/main/java/yesman/epicfight/api/animation/LivingMotions.java#L4-L6
        animator.addLivingAnimation(LivingMotions.IDLE, Animations.BIPED_IDLE);
        animator.addLivingAnimation(LivingMotions.WALK, Animations.BIPED_WALK);
        animator.addLivingAnimation(LivingMotions.RUN, Animations.BIPED_RUN);
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