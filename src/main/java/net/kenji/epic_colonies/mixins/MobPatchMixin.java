package net.kenji.epic_colonies.mixins;

import net.kenji.epic_colonies.gameasset.EpicColoniesLivingMotions;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;

@Mixin(value = MobPatch.class, remap = false)
public class MobPatchMixin {
    @Inject(method = "commonMobUpdateMotion", at = @At("HEAD"), cancellable = true)
    public void onUpdateMotion(boolean considerInaction, CallbackInfo ci){
        MobPatch<?> self = (MobPatch<?>) (Object)this;
        if (self instanceof CitizenEntityPatch<?> patch) {
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
                    if(patch.shouldJogWithAnim()) {
                        patch.currentLivingMotion = EpicColoniesLivingMotions.JOG;
                        if(patch.shouldRun()){
                            patch.currentLivingMotion = LivingMotions.CHASE;
                        }
                    }
                    else patch.currentLivingMotion = LivingMotions.WALK;
                }
                else{
                    patch.currentLivingMotion = LivingMotions.IDLE;
                }
            }
            patch.currentCompositeMotion = patch.currentLivingMotion;
        }
    }




}
