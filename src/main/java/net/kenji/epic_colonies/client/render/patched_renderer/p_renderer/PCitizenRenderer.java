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
import net.kenji.epic_colonies.api.data.CitizenMeshCache;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMeshes;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMesh;
import net.kenji.epic_colonies.client.patched_layers.CitizenWearableItemLayer;
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
        this.addPatchedLayer(CitizenArmorLayer.class, new CitizenWearableItemLayer<>(mesh, false, context.getModelManager()));
    }

    @Override
    public AssetAccessor<EpicColoniesMesh> getMeshProvider(CitizenEntityPatch<AbstractEntityCitizen> entitypatch) {
        try {
            AssetAccessor<EpicColoniesMesh> mesh = getCitizenMesh(entitypatch.getOriginal(), !entitypatch.getOriginal().isFemale());;
            return mesh;
        } catch (Throwable t) {
            EpicColonies.LOGGER.error("Mesh resolution failed for citizen {}", entitypatch.getOriginal().getUUID(), t);
            return EpicColoniesMeshes.CITIZEN_MALE; // safe fallback so at least *something* renders
        }
    }



    public AssetAccessor<EpicColoniesMesh> getCitizenMesh(AbstractEntityCitizen citizen, boolean isMale) {
        Map<JobEntry, Meshes.MeshAccessor<EpicColoniesMesh>> meshMap = isMale ? EpicColoniesMeshes.jobMeshMapMale : EpicColoniesMeshes.jobMeshMapFemale;
        Meshes.MeshAccessor<EpicColoniesMesh> citizenMesh = isMale ? EpicColoniesMeshes.CITIZEN_MALE : EpicColoniesMeshes.CITIZEN_FEMALE;
        Meshes.MeshAccessor<EpicColoniesMesh> defaultMesh = isMale ? EpicColoniesMeshes.DEFAULT_MALE : EpicColoniesMeshes.DEFAULT_FEMALE;
        Meshes.MeshAccessor<EpicColoniesMesh> childMesh = isMale ? EpicColoniesMeshes.CHILD_MALE : EpicColoniesMeshes.CHILD_FEMALE;

        Boolean isChild = null;
        JobEntry jobEntry = null;
        boolean dataAvailable = false;

        if (citizen.level().isClientSide) {
            ICitizenDataView view = citizen.getCitizenDataView();
            if (view != null) {
                dataAvailable = true;
                isChild = view.isChild();
                if (!isChild) {
                    IJobView jobView = view.getJobView();
                    if (jobView != null) jobEntry = jobView.getEntry();
                }else return childMesh;
            }
        } else {
            ICitizenData data = citizen.getCitizenData();
            if (data != null) {
                dataAvailable = true;
                isChild = data.isChild();
                IJob<?> job = data.getJob();
                if (job != null) jobEntry = job.getJobRegistryEntry();

            }
        }

        if (dataAvailable) {
            // real data arrived - update the disk-backed cache for next time
            if (citizen.level().isClientSide) {
                CitizenMeshCache.put(citizen.getUUID(), isChild, jobEntry);
            }
        } else if (citizen.level().isClientSide) {
            // no data yet - fall back to what we saved last session
            CitizenMeshCache.Entry cached = CitizenMeshCache.get(citizen.getUUID());
            if (cached != null) {
                isChild = cached.isChild();
                jobEntry = CitizenMeshCache.resolveJob(cached.jobId());
                dataAvailable = true;
            }
        }

        if (dataAvailable && isChild) {
            return childMesh;
        }

        return jobEntry == null ? citizenMesh : meshMap.getOrDefault(jobEntry, defaultMesh);
    }

    @Override
    public void render(AbstractEntityCitizen entity, CitizenEntityPatch<AbstractEntityCitizen> entitypatch, RenderBipedCitizen renderer, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks) {
        AssetAccessor<EpicColoniesMesh> mesh = this.getCitizenMesh(entity, !entity.isFemale());
        if(mesh.get().hat != null) {
            if (!entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {

                mesh.get().hat.setHidden(true);
                Log.info("Logging Hidden!");

            } else {
                mesh.get().hat.setHidden(false);
            }
        }
        super.render(entity, entitypatch, renderer, buffer, poseStack, packedLight, partialTicks);
    }
    @Override
    protected void prepareModel(EpicColoniesMesh mesh, AbstractEntityCitizen entity, CitizenEntityPatch<AbstractEntityCitizen> entitypatch, RenderBipedCitizen renderer) {
        super.prepareModel(mesh, entity, entitypatch, renderer); // runs mesh.initialize() (resets all parts to visible)

        if (mesh.hat != null) {
            mesh.hat.setHidden(!entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty());
        }
    }
    @Override
    protected void renderLayer(LivingEntityRenderer<AbstractEntityCitizen, CitizenModel<AbstractEntityCitizen>> renderer, CitizenEntityPatch<AbstractEntityCitizen> entitypatch, AbstractEntityCitizen entity, OpenMatrix4f[] poses, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks) {
        super.renderLayer(renderer, entitypatch, entity, poses, buffer, poseStack, packedLight, partialTicks);

    }

    public AssetAccessor<EpicColoniesMesh> getDefaultMesh() {
        return EpicColoniesMeshes.CITIZEN_MALE;
    }
}
