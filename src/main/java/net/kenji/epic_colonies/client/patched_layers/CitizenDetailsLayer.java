package net.kenji.epic_colonies.client.patched_layers;

import com.minecolonies.api.client.render.modeltype.IModelType;
import com.minecolonies.api.client.render.modeltype.ModModelTypes;
import com.minecolonies.api.colony.ICitizenDataView;
import com.minecolonies.api.colony.jobs.IJobView;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMesh;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMeshes;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.patched.layer.ModelRenderLayer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.HashMap;
import java.util.Map;

public class CitizenDetailsLayer<E extends AbstractEntityCitizen, T extends LivingEntityPatch<E>,
        M extends HumanoidModel<E>, AM extends EpicColoniesMesh>
        extends ModelRenderLayer<E, T, M, HumanoidArmorLayer<E, M, M>, AM> {

    private final  Map<Pair<Boolean, ResourceLocation>, Meshes.MeshAccessor<EpicColoniesMesh>> meshMap;

    private EpicColoniesMesh currentMesh;

    public CitizenDetailsLayer(AssetAccessor<AM> defaultMesh, Map<Pair<Boolean, ResourceLocation>, Meshes.MeshAccessor<EpicColoniesMesh>> meshMap) {
        super(defaultMesh);
        this.meshMap = meshMap;
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
        Meshes.MeshAccessor<EpicColoniesMesh> childMesh = !female ? EpicColoniesMeshes.CHILD_MALE : EpicColoniesMeshes.CHILD_FEMALE;

        if (view == null) return defaultMesh;

        IJobView jobView = view.getJobView();
        if (jobView == null || jobView.getEntry() == null) {
            if(view.isChild()){
                return childMesh;
            }
        }

        return meshMap.getOrDefault(new Pair<>(female, citizen.getModelType()), defaultMesh);
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

}
