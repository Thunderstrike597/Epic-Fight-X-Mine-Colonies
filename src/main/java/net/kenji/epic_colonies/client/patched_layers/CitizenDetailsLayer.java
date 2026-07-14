package net.kenji.epic_colonies.client.patched_layers;

import com.minecolonies.api.colony.ICitizenDataView;
import com.minecolonies.api.colony.jobs.IJobView;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.colony.CitizenDataView;
import com.mojang.blaze3d.vertex.PoseStack;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMesh;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMeshes;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import org.jetbrains.annotations.Nullable;
import org.jline.utils.Log;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.renderer.patched.layer.ModelRenderLayer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.Map;

public class CitizenDetailsLayer<E extends AbstractEntityCitizen, T extends LivingEntityPatch<E>,
        M extends HumanoidModel<E>, AM extends EpicColoniesMesh>
        extends ModelRenderLayer<E, T, M, HumanoidArmorLayer<E, M, M>, AM> {

    private final Map<JobEntry, Meshes.MeshAccessor<EpicColoniesMesh>> jobMeshesMale;
    private final Map<JobEntry, Meshes.MeshAccessor<EpicColoniesMesh>> jobMeshesFemale;

    private EpicColoniesMesh currentMesh;

    public CitizenDetailsLayer(AssetAccessor<AM> defaultMesh, Map<JobEntry, Meshes.MeshAccessor<EpicColoniesMesh>> jobMeshesMale, Map<JobEntry, Meshes.MeshAccessor<EpicColoniesMesh>> jobMeshesFemale, ModelManager modelManager) {
        super(defaultMesh);
        this.jobMeshesMale = jobMeshesMale;
        this.jobMeshesFemale = jobMeshesFemale;
    }

    @Override
    protected void initMesh() {
        // no-op — base ModelRenderLayer#initMesh() only knows the single
        // fallback accessor passed to super(). We resolve + initialize the
        // correct per-job mesh ourselves in renderLayer() below.
    }

    @Override
    public void renderLayer(E entityliving, T entitypatch, RenderLayer<E, M> vanillaLayer, PoseStack poseStack, MultiBufferSource buffer, int packedLight, OpenMatrix4f[] poses, float bob, float yRot, float xRot, float partialTicks) {
        AssetAccessor<EpicColoniesMesh> accessor = this.resolveMeshAccessor(entityliving);

        if (accessor == null || accessor.isEmpty()) {
            return;
        }

        this.currentMesh = accessor.get();
        this.currentMesh.initialize(); // cheap state reset on the cached mesh, no rebuild

        this.renderLayer(entitypatch, entityliving, this.castLayer(vanillaLayer), poseStack, buffer, packedLight, poses, bob, yRot, xRot, partialTicks);
    }

    private Meshes.MeshAccessor<EpicColoniesMesh> resolveMeshAccessor(E citizen) {
        ICitizenDataView view = citizen.getCitizenDataView();
        boolean female = citizen.isFemale();
        Meshes.MeshAccessor<EpicColoniesMesh> defaultMesh = female ? EpicColoniesMeshes.DEFAULT_FEMALE : EpicColoniesMeshes.DEFAULT_MALE;

        if (view == null) return defaultMesh;

        IJobView jobView = view.getJobView();
        if (jobView == null) return defaultMesh;

        JobEntry job = jobView.getEntry();
        Map<JobEntry, Meshes.MeshAccessor<EpicColoniesMesh>> jobMeshes = female ? this.jobMeshesFemale : this.jobMeshesMale;

        return jobMeshes.getOrDefault(job, defaultMesh);
    }

    @Override
    protected void renderLayer(T entitypatch, E entityliving, @Nullable HumanoidArmorLayer<E, M, M> vanillaLayer, PoseStack poseStack, MultiBufferSource buffer, int packedLight, OpenMatrix4f[] poses, float bob, float yRot, float xRot, float partialTicks) {
        if (this.currentMesh == null || entityliving.isInvisible()) {
            return;
        }

        EpicColoniesMesh mesh = this.currentMesh;
        if(mesh.hat != null)
            mesh.hat.setHidden(CitizenWearableItemLayer.shouldHidePart(entityliving, EquipmentSlot.HEAD));

        if(mesh.head != null)
            mesh.head.setHidden(true);

        if (mesh.breast != null) {
            mesh.breast.setHidden(CitizenWearableItemLayer.shouldHidePart(entityliving, EquipmentSlot.CHEST));
        }
        if (mesh.jacket != null) {
            mesh.jacket.setHidden(CitizenWearableItemLayer.shouldHidePart(entityliving, EquipmentSlot.CHEST));
        }
        if (mesh.leftSleeve != null) {
            mesh.leftSleeve.setHidden(CitizenWearableItemLayer.shouldHidePart(entityliving, EquipmentSlot.CHEST));
        }
        if (mesh.rightSleeve != null) {
            mesh.rightSleeve.setHidden(CitizenWearableItemLayer.shouldHidePart(entityliving, EquipmentSlot.CHEST));
        }
        if (mesh.pants != null) {
            mesh.pants.setHidden(CitizenWearableItemLayer.shouldHidePart(entityliving, EquipmentSlot.LEGS));
        }
        if (mesh.leftPants != null) {
            mesh.leftPants.setHidden(CitizenWearableItemLayer.shouldHidePart(entityliving, EquipmentSlot.LEGS));
        }
        if (mesh.rightPants != null) {
            mesh.rightPants.setHidden(CitizenWearableItemLayer.shouldHidePart(entityliving, EquipmentSlot.LEGS));
        }
        if(mesh.featuresOn != null){
            mesh.featuresOn.setHidden(true);
        }
        if(mesh.torso != null)
            mesh.torso.setHidden(true);
        if(mesh.rightLeg != null)
            mesh.rightLeg.setHidden(true);
        if(mesh.leftLeg != null)
            mesh.leftLeg.setHidden(true);

        ResourceLocation tex = entityliving.getTexture();

        mesh.draw(poseStack, buffer, RenderType.entityCutoutNoCull(tex),
                packedLight, 1.0F, 1.0F, 1.0F, 1.0F, LivingEntityRenderer.getOverlayCoords(entityliving, 0.0F),
                entitypatch.getArmature(), poses);

        this.currentMesh = null;
    }

    private ResourceLocation getAccessoryTexture(EpicColoniesMesh mesh) {
        if (mesh.getRenderProperties() != null && mesh.getRenderProperties().customTexturePath() != null) {
            return mesh.getRenderProperties().customTexturePath();
        }
        throw new IllegalStateException("No texture resolution configured for accessory mesh " + mesh);
    }
}