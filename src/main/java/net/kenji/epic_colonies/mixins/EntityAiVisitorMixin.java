package net.kenji.epic_colonies.mixins;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.DamageSourceKeys;
import com.minecolonies.core.client.render.RenderBipedCitizen;
import com.minecolonies.core.entity.ai.visitor.EntityAIVisitor;
import com.minecolonies.core.entity.pathfinding.navigation.EntityNavigationUtils;
import com.minecolonies.core.entity.visitor.VisitorCitizen;
import net.kenji.epic_colonies.api.data.CitizenMeshCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityAIVisitor.class, remap = false)
public class EntityAiVisitorMixin {
    @Shadow
    private Entity target;

    @Shadow
    private int actionTimeoutCounter;

    @Shadow
    @Final
    private VisitorCitizen citizen;

    @Inject(method = "doFight", at = @At("HEAD"), cancellable = true)
    public void onGetTextureLocation(CallbackInfoReturnable<Boolean> cir){
        EntityAIVisitor self = (EntityAIVisitor) (Object)this;
        cir.cancel();
        if (this.target != null && this.target.isAlive() && (this.actionTimeoutCounter -= 20) > 0) {
            cir.setReturnValue(false);
        } else {
            this.target = null;
            this.citizen.setLastHurtByMob((LivingEntity)null);
            this.citizen.setTarget((LivingEntity)null);
            cir.setReturnValue(true);
        }
    }
}
