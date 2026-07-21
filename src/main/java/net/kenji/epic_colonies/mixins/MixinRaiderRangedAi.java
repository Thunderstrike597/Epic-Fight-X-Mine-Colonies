package net.kenji.epic_colonies.mixins;

import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.ITickRateStateMachine;
import com.minecolonies.api.entity.citizen.Skill;
import com.minecolonies.api.entity.citizen.VisibleCitizenStatus;
import com.minecolonies.api.entity.mobs.AbstractEntityMinecoloniesMonster;
import com.minecolonies.api.entity.mobs.ICustomAttackSound;
import com.minecolonies.api.entity.mobs.IRangedMobEntity;
import com.minecolonies.api.entity.mobs.RaiderMobUtils;
import com.minecolonies.api.entity.other.AbstractFastMinecoloniesEntity;
import com.minecolonies.api.research.util.ResearchConstants;
import com.minecolonies.api.util.SoundUtils;
import com.minecolonies.core.colony.buildings.AbstractBuildingGuards;
import com.minecolonies.core.entity.ai.combat.CombatUtils;
import com.minecolonies.core.entity.ai.workers.guard.AbstractEntityAIGuard;
import com.minecolonies.core.entity.ai.workers.guard.RangerCombatAI;
import com.minecolonies.core.entity.citizen.EntityCitizen;
import com.minecolonies.core.entity.mobs.aitasks.RaiderRangedAI;
import com.minecolonies.core.entity.other.CustomArrowEntity;
import com.minecolonies.core.entity.pathfinding.navigation.EntityNavigationUtils;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import net.kenji.epic_colonies.gameasset.patch.MinecoloniesMonsterPatch;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jline.utils.Log;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

@Mixin(value = RaiderRangedAI.class, remap = false)
public abstract class MixinRaiderRangedAi {


    @Shadow
    protected abstract double getAttackDistance();

    @Shadow
    private int flightCounter;

    @Shadow
    protected abstract double getRandomPitch();

    @Unique
    Mob mobSelf;

    @Inject(method = "<init>", at = @At("RETURN"), cancellable = true)
    public void onInit(AbstractEntityMinecoloniesMonster owner, ITickRateStateMachine stateMachine, CallbackInfo ci) {
        mobSelf = owner;
    }

    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void tryStopShoot(LivingEntity target, CallbackInfo ci) {
        ((AbstractEntityMinecoloniesMonster) this.mobSelf).getNavigation().stop();
        AbstractArrow arrowEntity = CombatUtils.createArrowForShooter(this.mobSelf);
        if (((IRangedMobEntity) ((AbstractEntityMinecoloniesMonster) this.mobSelf)).penetrateFluids() && arrowEntity instanceof CustomArrowEntity customArrowEntity) {
            customArrowEntity.setWaterInertia(0.99F);
        }

        arrowEntity.setBaseDamage(((AbstractEntityMinecoloniesMonster) this.mobSelf).getAttribute((Attribute) RaiderMobUtils.MOB_ATTACK_DAMAGE.get()).getValue());
        if (this.flightCounter > 5 && arrowEntity instanceof CustomArrowEntity) {
            ((CustomArrowEntity) arrowEntity).setPlayerArmorPierce();
            arrowEntity.setSecondsOnFire(10);
            arrowEntity.setBaseDamage((double) 10.0F);
        }

        if (((AbstractEntityMinecoloniesMonster) this.mobSelf).getDifficulty() > (double) 3.0F) {
            arrowEntity.setPierceLevel((byte) 2);
        }
        mobSelf.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent((cap) -> {
            if (cap instanceof MinecoloniesMonsterPatch<?> entityPatch) {
                if (entityPatch.isWasUsingBow()) {
                    CombatUtils.shootArrow(arrowEntity, target, 10.0F);
                    ((AbstractEntityMinecoloniesMonster) this.mobSelf).swing(InteractionHand.MAIN_HAND);
                    ((AbstractEntityMinecoloniesMonster) this.mobSelf).stopUsingItem();
                    SoundEvent attackSound = SoundEvents.SKELETON_SHOOT;
                    if (arrowEntity instanceof ICustomAttackSound) {
                        attackSound = ((ICustomAttackSound) arrowEntity).getAttackSound();
                    }

                    ((AbstractEntityMinecoloniesMonster) this.mobSelf).playSound(attackSound, 1.0F, (float) this.getRandomPitch());
                    entityPatch.setWasUsingBow(false);
                }

            }
        });
    }
}
