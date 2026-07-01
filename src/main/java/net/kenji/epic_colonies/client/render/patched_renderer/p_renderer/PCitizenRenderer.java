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
import com.minecolonies.core.colony.CitizenDataView;
import net.kenji.epic_colonies.client.EpicColoniesMeshes;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMesh;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.EntityType;
import org.jline.utils.Log;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.Meshes;
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
        Meshes.MeshAccessor<EpicColoniesMesh> defaultMesh = isMale ? EpicColoniesMeshes.CITIZEN_MALE : EpicColoniesMeshes.CITIZEN_FEMALE;

        JobEntry jobEntry = null;

        if (citizen.level().isClientSide) {
            ICitizenDataView view = citizen.getCitizenDataView();
            if (view != null) {
                IJobView jobView = view.getJobView();
                if (jobView != null) jobEntry = jobView.getEntry();
            }
        } else {
            ICitizenData data = citizen.getCitizenData();
            if (data != null) {
                IJob<?> job = data.getJob();
                if (job != null) jobEntry = job.getJobRegistryEntry();
            }
        }

        if (jobEntry == null) return defaultMesh;
        return meshMap.getOrDefault(jobEntry, defaultMesh);
    }

    public AssetAccessor<EpicColoniesMesh> getDefaultMesh() {
        return EpicColoniesMeshes.CITIZEN_MALE;
    }
}
