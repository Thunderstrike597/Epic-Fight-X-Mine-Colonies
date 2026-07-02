package net.kenji.epic_colonies.gameasset.patch;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.client.model.MaleCitizenModel;
import com.minecolonies.core.entity.citizen.EntityCitizen;
import net.kenji.epic_colonies.gameasset.EpicColoniesAnimations;
import net.kenji.epic_colonies.gameasset.EpicColoniesArmatures;
import net.kenji.epic_colonies.gameasset.armatures.CitizenArmature;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.jline.utils.Log;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.MobCombatBehaviors;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.world.capabilities.entitypatch.Factions;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.entity.ai.goal.AnimatedAttackGoal;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
import yesman.epicfight.world.entity.ai.goal.TargetChasingGoal;

public class CitizenEntityPatch<E extends AbstractEntityCitizen> extends HumanoidMobPatch<AbstractEntityCitizen> {

    public CitizenEntityPatch() {
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
        return !this.getOriginal().isFemale() ? EpicColoniesArmatures.CITIZEN_MALE.get() : EpicColoniesArmatures.CITIZEN_FEMALE.get();
    }


    @Override
    protected CombatBehaviors.Builder<HumanoidMobPatch<?>> getHoldingItemWeaponMotionBuilder() {
        if(this.getOriginal().getMainHandItem().getItem() instanceof SwordItem swordItem){
            return MobCombatBehaviors.SKELETON_SWORD;
        }
        return super.getHoldingItemWeaponMotionBuilder();
    }

    @Override
    protected void clientTick(LivingEvent.LivingTickEvent event) {
        super.clientTick(event);
        AnimationPlayer highestAnimPlayer = this.getClientAnimator().getCompositeLayer(Layer.Priority.HIGHEST).animationPlayer;
        AnimationPlayer middleAnimPlayer = this.getClientAnimator().getCompositeLayer(Layer.Priority.MIDDLE).animationPlayer;

        if(highestAnimPlayer != null){
            if(highestAnimPlayer.getAnimation() == null || highestAnimPlayer.getAnimation().get() != EpicColoniesAnimations.CITIZEN_BLINK.get()){
                animator.playAnimationInstantly(EpicColoniesAnimations.CITIZEN_BLINK);
                Log.info("Logging Anim Play! | Anim:" + highestAnimPlayer.getAnimation().get());
            }
            if(middleAnimPlayer.getAnimation() == null || middleAnimPlayer.getAnimation().get() != EpicColoniesAnimations.CITIZEN_EYES_MOVE.get()){
                animator.playAnimationInstantly(EpicColoniesAnimations.CITIZEN_EYES_MOVE);
                Log.info("Logging Anim Play! | Anim:" + middleAnimPlayer.getAnimation().get());
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



    @Override
    public void updateMotion(boolean b) {
        super.commonMobUpdateMotion(b);
    }
}