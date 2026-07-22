package net.kenji.epic_colonies.gameasset.patch;

import net.kenji.epic_colonies.gameasset.EpicColoniesAnimations;
import net.kenji.epic_colonies.gameasset.EpicColoniesArmatures;
import net.kenji.epic_colonies.gameasset.patch.base.AbstractExpressiveHumanoidPatch;
import net.minecraft.world.entity.PathfinderMob;
import yesman.epicfight.api.animation.*;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.world.capabilities.entitypatch.Factions;

public class MercenaryPatch<M extends PathfinderMob> extends AbstractExpressiveHumanoidPatch<M> {

    public MercenaryPatch(M entity) {
        super(entity);
    }
    @Override
    public boolean overrideRender() {
        return true;
    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
        animator.playAnimation(EpicColoniesAnimations.CITIZEN_BLINK, 0F);
        animator.playAnimation(EpicColoniesAnimations.CITIZEN_EYES_MOVE, 0F);

    }
    @Override
    public HumanoidArmature getArmature() {
        return EpicColoniesArmatures.CITIZEN_REGULAR.get();
    }

    @Override
    public void postTickClient() {
        super.postTickClient();
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
}