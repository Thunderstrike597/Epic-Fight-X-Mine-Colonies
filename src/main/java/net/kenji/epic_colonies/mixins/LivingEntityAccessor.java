package net.kenji.epic_colonies.mixins;

import net.kenji.epic_colonies.gameasset.EpicColoniesLivingMotions;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import net.minecraft.world.entity.LivingEntity;
import org.jline.utils.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;

@Mixin(value = LivingEntity.class)
public interface LivingEntityAccessor {
    @Accessor("jumping")
    boolean isJumping();

}
