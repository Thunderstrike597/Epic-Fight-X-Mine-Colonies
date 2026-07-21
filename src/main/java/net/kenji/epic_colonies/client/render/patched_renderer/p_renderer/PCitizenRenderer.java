package net.kenji.epic_colonies.client.render.patched_renderer.p_renderer;

import com.minecolonies.api.client.render.modeltype.CitizenModel;
import com.minecolonies.api.client.render.modeltype.ModModelTypes;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.ICitizenDataView;
import com.minecolonies.api.colony.jobs.IJob;
import com.minecolonies.api.colony.jobs.IJobView;
import com.minecolonies.api.colony.jobs.ModJobs;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.client.render.CitizenArmorLayer;
import com.minecolonies.core.client.render.RenderBipedCitizen;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.api.CitizenArmatureTypes;
import net.kenji.epic_colonies.api.data.CitizenMeshCache;
import net.kenji.epic_colonies.api.texture_detection.FaceOffsetDetector;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMeshes;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMesh;
import net.kenji.epic_colonies.client.patched_layers.CitizenDetailsLayer;
import net.kenji.epic_colonies.client.patched_layers.CitizenWearableItemLayer;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import net.kenji.epic_colonies.network.EpicColoniesPacketHandler;
import net.kenji.epic_colonies.network.ServerCitizenArmaturePacket;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import org.jline.utils.Log;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;
import yesman.epicfight.client.renderer.patched.layer.PatchedItemInHandLayer;

import java.util.Map;

public class PCitizenRenderer extends PatchedLivingEntityRenderer<AbstractEntityCitizen, CitizenEntityPatch<AbstractEntityCitizen>, CitizenModel<AbstractEntityCitizen>, RenderBipedCitizen, EpicColoniesMesh> {

    RenderBipedCitizen originalRenderer = null;

    public PCitizenRenderer(Meshes.MeshAccessor<EpicColoniesMesh> mesh, EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(context, entityType);
        this.addPatchedLayer(ItemInHandLayer.class, new PatchedItemInHandLayer<>());
        this.addCustomLayer(new CitizenDetailsLayer<>(getDefaultMesh(), EpicColoniesMeshes.meshMap));
        this.addPatchedLayer(CitizenArmorLayer.class, new CitizenWearableItemLayer<>(mesh, false, context.getModelManager()));
    }

    @Override
    public AssetAccessor<EpicColoniesMesh> getMeshProvider(CitizenEntityPatch<AbstractEntityCitizen> entitypatch) {
        try {
            AssetAccessor<EpicColoniesMesh> mesh = getCitizenMesh(entitypatch, entitypatch.getOriginal(), !entitypatch.getOriginal().isFemale());;

           return mesh;
        } catch (Throwable t) {
            EpicColonies.LOGGER.error("Mesh resolution failed for citizen {}", entitypatch.getOriginal().getUUID(), t);
            return EpicColoniesMeshes.CITIZEN_MALE; // safe fallback so at least *something* renders
        }
    }





    public AssetAccessor<EpicColoniesMesh> getCitizenMesh(CitizenEntityPatch<?> patch, AbstractEntityCitizen citizen, boolean isMale) {
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
                    if (jobView != null)
                        jobEntry = jobView.getEntry();
                }
            }
        } else {
            ICitizenData data = citizen.getCitizenData();
            if (data != null) {
                dataAvailable = true;
                isChild = data.isChild();
                IJob<?> job = data.getJob();
                if (job != null)
                    jobEntry = job.getJobRegistryEntry();

            }
        }

        if (dataAvailable) {
            if (citizen.level().isClientSide) {
                String textureStr = citizen.getTexture().toString();

                // Only cache the texture once it's actually resolved to something
                // real - MineColonies syncs this field independently/later than
                // isChild/job, so `view != null` alone doesn't guarantee it's ready.
                if (CitizenMeshCache.isValidTexture(textureStr)) {
                    CitizenMeshCache.put(citizen.getUUID(), isChild, jobEntry, textureStr);
                } else {
                    // still cache isChild/job now, keep whatever texture we had before
                    CitizenMeshCache.Entry existing = CitizenMeshCache.get(citizen.getUUID());
                    String keepTexture = existing != null ? existing.skinTextureId() : null;
                    CitizenMeshCache.put(citizen.getUUID(), isChild, jobEntry, keepTexture);
                }
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
        if(dataAvailable) {
            if (CitizenEntityPatch.getMeshFromTexture(citizen, isChild) != null) {
                return CitizenEntityPatch.getMeshFromTexture(citizen, isChild);
            }
            if (isChild) {
                return childMesh;
            }
            if(citizen.getModelType() != null) {
                if (citizen.getModelType() == ModModelTypes.ARISTOCRAT_ID) {
                    patch.setCurrentCitizenArmatureFromArmatureType(CitizenArmatureTypes.REGULAR);
                    return citizen.isFemale() ? EpicColoniesMeshes.ARISTOCRAT_FEMALE : EpicColoniesMeshes.ARISTOCRAT_MALE;
                } else if (citizen.getModelType() == ModModelTypes.NOBLE_ID) {
                    patch.setCurrentCitizenArmatureFromArmatureType(CitizenArmatureTypes.REGULAR);
                    return citizen.isFemale() ? EpicColoniesMeshes.NOBLE_FEMALE : EpicColoniesMeshes.NOBLE_MALE;
                } else {
                    ResourceLocation loc = originalRenderer == null ? citizen.getTexture() : originalRenderer.getTextureLocation(citizen);
                    int getFaceOffset = FaceOffsetDetector.getFaceOffset(loc);

                    if (getFaceOffset == 0 || !citizen.isFemale()) {
                        patch.setCurrentCitizenArmatureFromArmatureType(CitizenArmatureTypes.REGULAR);
                        return citizen.isFemale() ? EpicColoniesMeshes.DEFAULT_FEMALE : EpicColoniesMeshes.DEFAULT_MALE;
                    } else {
                        patch.setCurrentCitizenArmatureFromArmatureType(CitizenArmatureTypes.LOW_EYES);
                        return EpicColoniesMeshes.DEFAULT_FEMALE_LOWER_EYES;
                    }
                }
            }
        }
        patch.setCurrentCitizenArmatureFromArmatureType(CitizenArmatureTypes.REGULAR);
        return defaultMesh;
    }


    @Override
    protected void prepareModel(EpicColoniesMesh mesh, AbstractEntityCitizen entity, CitizenEntityPatch<AbstractEntityCitizen> entitypatch, RenderBipedCitizen renderer) {
        super.prepareModel(mesh, entity, entitypatch, renderer); // runs mesh.initialize() (resets all parts to visible)
        this.originalRenderer = renderer;

        if (mesh.hat != null) {
            mesh.hat.setHidden(CitizenWearableItemLayer.shouldHidePart(entity, EquipmentSlot.HEAD));
        }
        if (mesh.breast != null) {
            mesh.breast.setHidden(CitizenWearableItemLayer.shouldHidePart(entity, EquipmentSlot.CHEST));
        }
        if (mesh.jacket != null) {
            mesh.jacket.setHidden(CitizenWearableItemLayer.shouldHidePart(entity, EquipmentSlot.CHEST));
        }
        if (mesh.leftSleeve != null) {
            mesh.leftSleeve.setHidden(CitizenWearableItemLayer.shouldHidePart(entity, EquipmentSlot.CHEST));
        }
        if (mesh.rightSleeve != null) {
            mesh.rightSleeve.setHidden(CitizenWearableItemLayer.shouldHidePart(entity, EquipmentSlot.CHEST));
        }
        if (mesh.pants != null) {
            mesh.pants.setHidden(CitizenWearableItemLayer.shouldHidePart(entity, EquipmentSlot.LEGS));
        }
        if (mesh.leftPants != null) {
            mesh.leftPants.setHidden(CitizenWearableItemLayer.shouldHidePart(entity, EquipmentSlot.LEGS));
        }
        if (mesh.rightPants != null) {
            mesh.rightPants.setHidden(CitizenWearableItemLayer.shouldHidePart(entity, EquipmentSlot.LEGS));
        }
        if(mesh.featuresOn != null){
            mesh.featuresOn.setHidden(true);
        }
    }

    @Override
    protected void renderLayer(LivingEntityRenderer<AbstractEntityCitizen, CitizenModel<AbstractEntityCitizen>> renderer, CitizenEntityPatch<AbstractEntityCitizen> entitypatch, AbstractEntityCitizen entity, OpenMatrix4f[] poses, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks) {
        super.renderLayer(renderer, entitypatch, entity, poses, buffer, poseStack, packedLight, partialTicks);

    }

    public AssetAccessor<EpicColoniesMesh> getDefaultMesh() {
        return EpicColoniesMeshes.DEFAULT_MALE;
    }



}
