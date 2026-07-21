package net.kenji.epic_colonies.mixins;

import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.ITickRateStateMachine;
import com.minecolonies.api.entity.citizen.Skill;
import com.minecolonies.api.entity.citizen.VisibleCitizenStatus;
import com.minecolonies.api.entity.other.AbstractFastMinecoloniesEntity;
import com.minecolonies.api.research.util.ResearchConstants;
import com.minecolonies.api.util.SoundUtils;
import com.minecolonies.core.colony.buildings.AbstractBuildingGuards;
import com.minecolonies.core.entity.ai.combat.CombatUtils;
import com.minecolonies.core.entity.ai.workers.guard.AbstractEntityAIGuard;
import com.minecolonies.core.entity.ai.workers.guard.RangeCombatAI;
import com.minecolonies.core.entity.citizen.EntityCitizen;
import com.minecolonies.core.entity.pathfinding.navigation.EntityNavigationUtils;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

@Mixin(value = RangeCombatAI.class, remap = false)
public abstract class MixinRangerCombatAi {

    @Shadow
    protected abstract double calculateDamage(AbstractArrow arrow);

    @Shadow
    @Final
    private static VisibleCitizenStatus ARCHER_COMBAT;

    @Shadow
    protected abstract double getAttackDistance();

    @Shadow
    protected abstract double getCombatMovementSpeed();

    @Unique Mob mobSelf;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(EntityCitizen owner, ITickRateStateMachine stateMachine, AbstractEntityAIGuard parentAI, CallbackInfo ci){
        mobSelf = owner;
    }

    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void tryStopShoot(LivingEntity target, CallbackInfo ci) {
        if (mobSelf == null) return;
        ci.cancel();
        if (((EntityCitizen) this.mobSelf).distanceToSqr(target) < (double) 7.0F) {
            if (((EntityCitizen) this.mobSelf).getRandom().nextInt(3) == 0 && !((AbstractBuildingGuards) ((EntityCitizen) this.mobSelf).getCitizenData().getWorkBuilding()).getTask().equals("com.minecolonies.core.guard.setting.guard")) {
                EntityNavigationUtils.walkAwayFrom((AbstractFastMinecoloniesEntity) this.mobSelf, target.blockPosition(), (int) (this.getAttackDistance() / (double) 2.0F), this.getCombatMovementSpeed());
            }
        } else {
            ((EntityCitizen) this.mobSelf).getNavigation().stop();
        }

        ((EntityCitizen) this.mobSelf).getCitizenData().setVisibleStatus(ARCHER_COMBAT);
        ((EntityCitizen) this.mobSelf).swing(InteractionHand.MAIN_HAND);
        ((EntityCitizen) this.mobSelf).stopUsingItem();
        int amountOfArrows = 1;
        if (((EntityCitizen) this.mobSelf).getCitizenColonyHandler().getColonyOrRegister().getResearchManager().getResearchEffects().getEffectStrength(ResearchConstants.DOUBLE_ARROWS) > (double) 0.0F && ((EntityCitizen) this.mobSelf).getRandom().nextDouble() < ((EntityCitizen) this.mobSelf).getCitizenColonyHandler().getColonyOrRegister().getResearchManager().getResearchEffects().getEffectStrength(ResearchConstants.DOUBLE_ARROWS)) {
            ++amountOfArrows;
        }

        Registry<Enchantment> enchantmentRegistry = mobSelf.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> flame = enchantmentRegistry.getHolderOrThrow(Enchantments.FLAME);
        Holder<Enchantment> punch = enchantmentRegistry.getHolderOrThrow(Enchantments.PUNCH);

        for (int i = 0; i < amountOfArrows; ++i) {
            AbstractArrow arrow = CombatUtils.createArrowForShooter(this.mobSelf);
            if (((EntityCitizen) this.mobSelf).getCitizenColonyHandler().getColonyOrRegister().getResearchManager().getResearchEffects().getEffectStrength(ResearchConstants.ARROW_PIERCE) > (double) 0.0F) {
                ((AccessorAbstractArrow) arrow).invokeSetPierceLevel((byte) 2);
            }

            ItemStack bow = ((EntityCitizen) this.mobSelf).getItemInHand(InteractionHand.MAIN_HAND);
            if (EnchantmentHelper.getItemEnchantmentLevel(flame, bow) > 0) {
                arrow.igniteForSeconds(5); // Standard 1.21.1 entity ignition method
            }

            int k = EnchantmentHelper.getItemEnchantmentLevel(punch, bow);
            if (k > 0) {
                ((AccessorAbstractArrow) arrow).setKnockback(k); // Write to the private knockback field directly!
            }

            double damage = this.calculateDamage(arrow);
            arrow.setBaseDamage(damage);
            float chance = 15.0F / (float) (((EntityCitizen) this.mobSelf).getCitizenData().getCitizenSkillHandler().getLevel(Skill.Adaptability) + 1);
            
            EpicFightCapabilities.getUnparameterizedEntityPatch(mobSelf, CitizenEntityPatch.class).ifPresent((cap) -> {
                if(cap.wasUsingBow && cap.bowUseCounter >= 32) {
                    CombatUtils.shootArrow(arrow, target, chance);
                    ((EntityCitizen) this.mobSelf).playSound(SoundEvents.SKELETON_SHOOT, 1.0F, (float) SoundUtils.getRandomPitch(((EntityCitizen) this.mobSelf).getRandom()));
                    cap.setWasUsingBow(false);
                    cap.bowUseCounter = 0;
                }
            });
        }
    }
}