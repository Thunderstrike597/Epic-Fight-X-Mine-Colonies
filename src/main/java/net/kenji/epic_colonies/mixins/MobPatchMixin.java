package net.kenji.epic_colonies.mixins;

import net.kenji.epic_colonies.gameasset.EpicColoniesLivingMotions;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import net.kenji.epic_colonies.gameasset.patch.MercenaryPatch;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;

@Mixin(value = MobPatch.class, remap = false)
public class MobPatchMixin {

    @Inject(method = "commonMobUpdateMotion", at = @At("HEAD"), cancellable = true)
    public void onUpdateCommonMotion(boolean considerInaction, CallbackInfo ci) {
        MobPatch<?> self = (MobPatch<?>) (Object) this;
        if (self instanceof CitizenEntityPatch<?> patch) {
            ci.cancel();
            if (patch.getOriginal().getHealth() <= 0.0F) {
                patch.currentLivingMotion = LivingMotions.DEATH;
            } else if (patch.getEntityState().inaction() && considerInaction) {
                patch.currentLivingMotion = LivingMotions.INACTION;
            } else {
                if (patch.getCitizenPatchData().currentOptionalMotion != null && patch.getCitizenPatchData().currentOptionalMotion != LivingMotions.NONE) {
                    patch.currentLivingMotion = patch.getCitizenPatchData().currentOptionalMotion;
                } else if (patch.getOriginal().getVehicle() != null)
                    patch.currentLivingMotion = LivingMotions.MOUNT;
                else if (patch.getOriginal().getDeltaMovement().y < -0.55F || patch.isAirborneState())
                    patch.currentLivingMotion = LivingMotions.FALL;
                else if (patch.getOriginal().walkAnimation.speed() > 0.01F) {
                    if (patch.shouldJogWithAnim()) {
                        patch.currentLivingMotion = EpicColoniesLivingMotions.JOG;
                        if (patch.shouldRun()) {
                            patch.currentLivingMotion = LivingMotions.CHASE;
                        }
                    } else patch.currentLivingMotion = LivingMotions.WALK;
                } else {
                    patch.currentLivingMotion = LivingMotions.IDLE;
                }
            }
            if (patch.getCitizenPatchData().currentOptionalCompositeMotion != null
                    && patch.getCitizenPatchData().currentOptionalCompositeMotion != LivingMotions.NONE) {
                patch.currentCompositeMotion = patch.getCitizenPatchData().currentOptionalCompositeMotion;
            } else {
                patch.currentCompositeMotion = patch.currentLivingMotion; // always a safe, harmless composite key
            }
        }
        if (self instanceof MercenaryPatch<?> patch) {
            ci.cancel();
            if (patch.getOriginal().getHealth() <= 0.0F) {
                patch.currentLivingMotion = LivingMotions.DEATH;
            } else if (patch.getEntityState().inaction() && considerInaction) {
                patch.currentLivingMotion = LivingMotions.INACTION;
            } else {
               if (patch.getOriginal().getVehicle() != null)
                    patch.currentLivingMotion = LivingMotions.MOUNT;
                else if (patch.getOriginal().getDeltaMovement().y < -0.55F || patch.isAirborneState())
                    patch.currentLivingMotion = LivingMotions.FALL;
                else if (patch.getOriginal().walkAnimation.speed() > 0.01F) {
                    if (patch.shouldJRunWithAnim()) {
                        patch.currentLivingMotion = LivingMotions.CHASE;
                    } else patch.currentLivingMotion = LivingMotions.WALK;
                } else {
                    patch.currentLivingMotion = LivingMotions.IDLE;
                }
            }
            patch.currentCompositeMotion = patch.currentLivingMotion; // always a safe, harmless composite key
        }
    }
    @Inject(method = "commonAggressiveRangedMobUpdateMotion", at = @At("HEAD"), cancellable = true)
    public void onUpdateRangedMotion(boolean considerInaction, CallbackInfo ci){
        MobPatch<?> self = (MobPatch<?>) (Object)this;
        UseAnim useAction = ((Mob)self.getOriginal()).getItemInHand(((Mob)self.getOriginal()).getUsedItemHand()).getUseAnimation();

        if (self instanceof CitizenEntityPatch<?> patch) {
            ci.cancel();
            if (patch.getOriginal().getHealth() <= 0.0F) {
                patch.currentLivingMotion = LivingMotions.DEATH;
            } else if (patch.getEntityState().inaction() && considerInaction) {
                patch.currentLivingMotion = LivingMotions.INACTION;
            } else {
                if(patch.getCitizenPatchData().currentOptionalMotion != null && patch.getCitizenPatchData().currentOptionalMotion != LivingMotions.NONE){
                    patch.currentLivingMotion = patch.getCitizenPatchData().currentOptionalMotion;
                }
                else if (patch.getOriginal().getVehicle() != null)
                    patch.currentLivingMotion = LivingMotions.MOUNT;
                else if (patch.getOriginal().getDeltaMovement().y < -0.55F || patch.isAirborneState())
                    patch.currentLivingMotion = LivingMotions.FALL;
                else if (patch.getOriginal().walkAnimation.speed() > 0.01F) {
                    if (patch.shouldJogWithAnim()) {
                        patch.currentLivingMotion = EpicColoniesLivingMotions.JOG;
                        if (patch.shouldRun()) {
                            patch.currentLivingMotion = LivingMotions.CHASE;
                        }
                    } else patch.currentLivingMotion = LivingMotions.WALK;
                }
                else {
                    patch.currentLivingMotion = LivingMotions.IDLE;
                }
            }
            if (patch.getCitizenPatchData().currentOptionalCompositeMotion != null
                    && patch.getCitizenPatchData().currentOptionalCompositeMotion != LivingMotions.NONE) {
                patch.currentCompositeMotion = patch.getCitizenPatchData().currentOptionalCompositeMotion;
            }
            else if (((StaticAnimation)self.getClientAnimator().getCompositeLayer(Layer.Priority.MIDDLE).animationPlayer.getRealAnimation().get()).isReboundAnimation()) {
                self.currentCompositeMotion = LivingMotions.SHOT;
            } else if (((Mob)self.getOriginal()).isUsingItem()) {
                if (useAction == UseAnim.CROSSBOW) {
                    self.currentCompositeMotion = LivingMotions.RELOAD;
                } else {
                    self.currentCompositeMotion = LivingMotions.AIM;
                    patch.setWasUsingBow(true);
                }
            } else if (CrossbowItem.isCharged(((Mob)self.getOriginal()).getMainHandItem())) {
                self.currentCompositeMotion = LivingMotions.AIM;
                patch.setWasUsingBow(true);
            } else {
                self.currentCompositeMotion = self.currentLivingMotion;
                patch.setWasUsingBow(false);
            }
        }
        if (self instanceof MercenaryPatch<?> patch) {
            ci.cancel();
            if (patch.getOriginal().getHealth() <= 0.0F) {
                patch.currentLivingMotion = LivingMotions.DEATH;
            } else if (patch.getEntityState().inaction() && considerInaction) {
                patch.currentLivingMotion = LivingMotions.INACTION;
            } else {
                if (patch.getOriginal().getVehicle() != null)
                    patch.currentLivingMotion = LivingMotions.MOUNT;
                else if (patch.getOriginal().getDeltaMovement().y < -0.55F || patch.isAirborneState())
                    patch.currentLivingMotion = LivingMotions.FALL;
                else if (patch.getOriginal().walkAnimation.speed() > 0.01F) {
                    if (patch.shouldJRunWithAnim()) {
                        patch.currentLivingMotion = LivingMotions.CHASE;
                    } else patch.currentLivingMotion = LivingMotions.WALK;
                } else {
                    patch.currentLivingMotion = LivingMotions.IDLE;
                }
            }
            if (((StaticAnimation)self.getClientAnimator().getCompositeLayer(Layer.Priority.MIDDLE).animationPlayer.getRealAnimation().get()).isReboundAnimation()) {
                self.currentCompositeMotion = LivingMotions.SHOT;
            } else if (((Mob)self.getOriginal()).isUsingItem()) {
                if (useAction == UseAnim.CROSSBOW) {
                    self.currentCompositeMotion = LivingMotions.RELOAD;
                } else {
                    self.currentCompositeMotion = LivingMotions.AIM;
                }
            } else if (CrossbowItem.isCharged(((Mob)self.getOriginal()).getMainHandItem())) {
                self.currentCompositeMotion = LivingMotions.AIM;
            } else {
                self.currentCompositeMotion = self.currentLivingMotion;
            }
        }
    }

}
