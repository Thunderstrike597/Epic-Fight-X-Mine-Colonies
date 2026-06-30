package net.kenji.epic_colonies.client.render.patched_renderer.p_renderer;

import com.minecolonies.api.client.render.modeltype.CitizenModel;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.client.render.RenderBipedCitizen;
import net.kenji.epic_colonies.client.EpicColoniesMeshes;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMesh;
import net.kenji.epic_colonies.patch.CitizenEntityPatch;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.EntityType;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.client.renderer.patched.entity.PHumanoidRenderer;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;
import yesman.epicfight.client.renderer.patched.layer.PatchedItemInHandLayer;
import yesman.epicfight.client.renderer.patched.layer.WearableItemLayer;

public class PCitizenRenderer extends PatchedLivingEntityRenderer<AbstractEntityCitizen, CitizenEntityPatch<AbstractEntityCitizen>, CitizenModel<AbstractEntityCitizen>, RenderBipedCitizen, EpicColoniesMesh> {
    public PCitizenRenderer(Meshes.MeshAccessor<EpicColoniesMesh> mesh, EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(context, entityType);
        this.addPatchedLayer(ItemInHandLayer.class, new PatchedItemInHandLayer());
        this.addPatchedLayer(HumanoidArmorLayer.class, new WearableItemLayer(getDefaultMesh(), false, context.getModelManager()));
    }

    @Override
    public AssetAccessor<EpicColoniesMesh> getMeshProvider(CitizenEntityPatch<AbstractEntityCitizen> entitypatch) {
        return !entitypatch.getOriginal().isFemale() ? EpicColoniesMeshes.CITIZEN_MALE : EpicColoniesMeshes.CITIZEN_FEMALE;
    }

    public AssetAccessor<EpicColoniesMesh> getDefaultMesh() {
        return EpicColoniesMeshes.CITIZEN_MALE;
    }
}
