package net.kenji.epic_colonies.mixins;

import com.minecolonies.api.entity.ai.combat.CombatAIStates;
import com.minecolonies.api.entity.ai.statemachine.states.IState;
import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.ITickRateStateMachine;
import com.minecolonies.core.entity.ai.combat.AttackMoveAI;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;

@Mixin(value = AttackMoveAI.class, remap = false)
public abstract class MixinAttackMoveAi {
    @Shadow
    public abstract boolean canAttack();

    @Shadow
    protected long nextAttackTime;
    @Shadow
    private int pathAttempts;

    @Shadow
    protected abstract boolean isInDistanceForAttack(LivingEntity target);

    @Shadow
    protected abstract int getAttackDelay();

    @Shadow
    protected abstract void doAttack(LivingEntity target);

    @Unique Mob mobSelf;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(Mob owner, ITickRateStateMachine stateMachine, CallbackInfo ci){
        mobSelf = owner;
    }

    @Inject(method = "tryAttack", at = @At("HEAD"), cancellable = true)
    public void stopVanillaAttack(CallbackInfoReturnable<IState> cir){
        AttackMoveAI self = (AttackMoveAI) (Object)this;
        AccessorTargetAi selfMob = (AccessorTargetAi) self;
        if(selfMob.getUser().getMainHandItem().getItem() instanceof ProjectileWeaponItem) {
            return;
        }
        cir.cancel();
        if (selfMob.invokeCheckForTarget() && this.canAttack()) {
            if (this.nextAttackTime < selfMob.getUser().level().getGameTime() && this.isInDistanceForAttack(selfMob.getUser().getTarget())) {
                if (selfMob.getUser().getSensing().hasLineOfSight(selfMob.getUser().getTarget())) {
                    this.pathAttempts = 0;
                    EntityPatch<?> entityPatch = EpicFightCapabilities.ENTITY_PATCH_PROVIDER.getCapability(mobSelf);

                    selfMob.getUser().getLookControl().setLookAt(selfMob.getUser().getTarget());
                    if(entityPatch == null) {
                        this.doAttack(this.mobSelf.getTarget());
                        this.nextAttackTime = selfMob.getUser().level().getGameTime() + (long) this.getAttackDelay();
                    }
                }

                cir.setReturnValue(null);
            } else {
                cir.setReturnValue(null);
            }
        } else {
            cir.setReturnValue(CombatAIStates.NO_TARGET);
        }
    }
}
