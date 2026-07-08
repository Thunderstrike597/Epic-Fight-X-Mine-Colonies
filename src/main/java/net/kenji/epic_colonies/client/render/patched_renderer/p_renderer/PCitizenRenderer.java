package net.kenji.epic_colonies.client.render.patched_renderer.p_renderer;

import com.minecolonies.api.client.render.modeltype.CitizenModel;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.ICitizenDataView;
import com.minecolonies.api.colony.jobs.IJob;
import com.minecolonies.api.colony.jobs.IJobView;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.client.render.CitizenArmorLayer;
import com.minecolonies.core.client.render.RenderBipedCitizen;
import com.mojang.blaze3d.vertex.PoseStack;
import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMeshes;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMesh;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import org.jline.utils.Log;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.patched.entity.PHumanoidRenderer;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;
import yesman.epicfight.client.renderer.patched.layer.PatchedItemInHandLayer;
import yesman.epicfight.client.renderer.patched.layer.WearableItemLayer;

import java.util.Map;

public class PCitizenRenderer extends PatchedLivingEntityRenderer<AbstractEntityCitizen, CitizenEntityPatch<AbstractEntityCitizen>, CitizenModel<AbstractEntityCitizen>, RenderBipedCitizen, EpicColoniesMesh> {
    public PCitizenRenderer(Meshes.MeshAccessor<EpicColoniesMesh> mesh, EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(context, entityType);
        this.addPatchedLayer(ItemInHandLayer.class, new PatchedItemInHandLayer<>());
        this.addPatchedLayer(CitizenArmorLayer.class, new WearableItemLayer<>(mesh, false, context.getModelManager()));    }

    @Override
    public AssetAccessor<EpicColoniesMesh> getMeshProvider(CitizenEntityPatch<AbstractEntityCitizen> entitypatch) {
        return getCitizenMesh(entitypatch.getOriginal(), !entitypatch.getOriginal().isFemale());
    }



    public AssetAccessor<EpicColoniesMesh> getCitizenMesh(AbstractEntityCitizen citizen, boolean isMale) {
        Map<JobEntry, Meshes.MeshAccessor<EpicColoniesMesh>> meshMap = isMale ? EpicColoniesMeshes.jobMeshMapMale : EpicColoniesMeshes.jobMeshMapFemale;
        Meshes.MeshAccessor<EpicColoniesMesh> citizenMesh = isMale ? EpicColoniesMeshes.CITIZEN_MALE : EpicColoniesMeshes.CITIZEN_FEMALE;
        Meshes.MeshAccessor<EpicColoniesMesh> defaultMesh = isMale ? EpicColoniesMeshes.DEFAULT_MALE : EpicColoniesMeshes.DEFAULT_FEMALE;
        Meshes.MeshAccessor<EpicColoniesMesh> childMesh = isMale ? EpicColoniesMeshes.CHILD_MALE : EpicColoniesMeshes.CHILD_FEMALE;


        JobEntry jobEntry = null;
        if (citizen.level().isClientSide) {
            ICitizenDataView view = citizen.getCitizenDataView();
            if (view != null) {
                if(view.isChild())
                    return childMesh;
                IJobView jobView = view.getJobView();
                if (jobView != null) jobEntry = jobView.getEntry();
            }
        } else {
            ICitizenData data = citizen.getCitizenData();
            if (data != null) {
                if(data.isChild())
                    return childMesh;
                IJob<?> job = data.getJob();
                if (job != null) jobEntry = job.getJobRegistryEntry();
            }
        }
        SkinnedMesh.SkinnedMeshPart citizenMeshPart = citizenMesh.get().hat;
        if(citizenMeshPart != null){
        if(!citizen.getItemBySlot(EquipmentSlot.HEAD).isEmpty())
            citizenMeshPart.setHidden(true);
        else citizenMeshPart.setHidden(false);
        }
        if (jobEntry == null) return citizenMesh;

        Meshes.MeshAccessor<EpicColoniesMesh> finalMesh = meshMap.getOrDefault(jobEntry, defaultMesh);


        SkinnedMesh.SkinnedMeshPart meshPart = finalMesh.get().hat;
        if(meshPart != null){
            if(!citizen.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                meshPart.setHidden(true);
            }
            else meshPart.setHidden(false);
        }
        return finalMesh;
    }

    @Override
    public void render(AbstractEntityCitizen entity, CitizenEntityPatch<AbstractEntityCitizen> entitypatch, RenderBipedCitizen renderer, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks) {
        super.render(entity, entitypatch, renderer, buffer, poseStack, packedLight, partialTicks);
    }

    @Override
    protected void renderLayer(LivingEntityRenderer<AbstractEntityCitizen, CitizenModel<AbstractEntityCitizen>> renderer, CitizenEntityPatch<AbstractEntityCitizen> entitypatch, AbstractEntityCitizen entity, OpenMatrix4f[] poses, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks) {
        super.renderLayer(renderer, entitypatch, entity, poses, buffer, poseStack, packedLight, partialTicks);

    }

    public AssetAccessor<EpicColoniesMesh> getDefaultMesh() {
        return EpicColoniesMeshes.CITIZEN_MALE;
    }
}
