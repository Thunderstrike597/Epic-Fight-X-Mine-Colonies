package net.kenji.epic_colonies.mixins;

import com.minecolonies.api.colony.jobs.IJobView;
import com.minecolonies.api.colony.jobs.ModJobs;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.client.render.RenderBipedCitizen;
import net.kenji.epic_colonies.api.data.CitizenMeshCache;
import net.kenji.epic_colonies.api.texture_detection.FaceOffsetDetector;
import net.kenji.epic_colonies.gameasset.EpicColoniesLivingMotions;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;

@Mixin(value = RenderBipedCitizen.class, remap = false)
public class RenderBipedCitizenMixin {
    @Inject(method = "getTextureLocation(Lcom/minecolonies/api/entity/citizen/AbstractEntityCitizen;)Lnet/minecraft/resources/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    public void onGetTextureLocation(AbstractEntityCitizen entity, CallbackInfoReturnable<ResourceLocation> cir){
        RenderBipedCitizen self = (RenderBipedCitizen) (Object)this;
        if(entity.getCitizenDataView() == null) {
            CitizenMeshCache.Entry cached = CitizenMeshCache.get(entity.getUUID());
            if (cached != null) {
                ResourceLocation location = CitizenMeshCache.resolveTextLocation(cached.skinTextureId());

                if(location != null && cached.skinTextureId() != null && !cached.skinTextureId().isEmpty()) {
                    cir.setReturnValue(location);
                }
            }
        }
    }
}
