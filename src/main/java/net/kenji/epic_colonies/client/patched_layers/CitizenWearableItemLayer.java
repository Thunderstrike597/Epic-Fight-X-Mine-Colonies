package net.kenji.epic_colonies.client.patched_layers;

import com.minecolonies.api.colony.ICitizenDataView;
import com.minecolonies.api.colony.jobs.IJobView;
import com.minecolonies.api.colony.jobs.ModJobs;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.*;

import net.kenji.epic_colonies.mixins.AccessorHumanoidArmorLayer;
import net.kenji.epic_colonies.mixins.AccessorWearableItemLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.registries.ForgeRegistries;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.asset.JsonAssetLoader;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.client.model.transformer.HumanoidModelBaker;
import yesman.epicfight.api.exception.AssetLoadingException;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.renderer.patched.layer.WearableItemLayer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class CitizenWearableItemLayer<E extends AbstractEntityCitizen, T extends LivingEntityPatch<E>,
        M extends HumanoidModel<E>, AM extends HumanoidMesh>
        extends WearableItemLayer<E, T, M, AM> {
    private static final Map<ResourceLocation, SkinnedMesh> ARMOR_MODELS = new HashMap();
    private static final Map<String, ResourceLocation> EPICFIGHT_OVERRIDING_TEXTURES = new HashMap();
    private final boolean firstPersonModel;
    private final TextureAtlas armorTrimAtlas;

    private static final List<JobEntry> validRenderEntries = new ArrayList<>();
    private static final Map<UUID, Boolean> shouldRenderArmorMap = new HashMap<>();
    public static void clearModels() {
        ARMOR_MODELS.values().forEach(SkinnedMesh::destroy);
        ARMOR_MODELS.clear();
        EPICFIGHT_OVERRIDING_TEXTURES.clear();
    }

    public static void putModel(ResourceLocation rl, SkinnedMesh skinnedMesh) {
        ARMOR_MODELS.computeIfPresent(rl, (key, mesh) -> {
            if (mesh != skinnedMesh) {
                mesh.destroy();
            }

            return mesh;
        });
        ARMOR_MODELS.put(rl, skinnedMesh);
    }

    public static boolean shouldHidePart(AbstractEntityCitizen citizen, EquipmentSlot slot){

        return !citizen.getItemBySlot(slot).isEmpty() && shouldRenderArmorMap.getOrDefault(citizen.getUUID(), true);
    }

    public static SkinnedMesh getCachedModel(Item item) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
        return (SkinnedMesh)ARMOR_MODELS.get(key);
    }

    public CitizenWearableItemLayer(AssetAccessor<AM> meshProvider, boolean firstPersonModel, ModelManager modelManager) {
        super(meshProvider, false, modelManager); // always false — we handle full body
        this.firstPersonModel = firstPersonModel;
        this.armorTrimAtlas = modelManager.getAtlas(Sheets.ARMOR_TRIMS_SHEET);
        validRenderEntries.add(ModJobs.knight.get());
        validRenderEntries.add(ModJobs.archer.get());
    }

    public void renderLayer(T entitypatch, E entityliving, HumanoidArmorLayer<E, M, M> vanillaLayer, PoseStack poseStack, MultiBufferSource buf, int packedLight, OpenMatrix4f[] poses, float bob, float yRot, float xRot, float partialTicks) {
        ICitizenDataView view = entityliving.getCitizenDataView();
        JobEntry jobEntry = null;
        if (view != null) {
            IJobView jobView = view.getJobView();
            if (jobView == null)
                return;
            jobEntry = jobView.getEntry();
        }

        if(jobEntry == null)
            return;
        if(!validRenderEntries.contains(jobEntry)){
            shouldRenderArmorMap.put(entityliving.getUUID(), false);
            return;
        }
        shouldRenderArmorMap.put(entityliving.getUUID(), true);

        for(EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == Type.ARMOR) {
                boolean firstPersonChest = false;
                if (entitypatch.isFirstPerson() && this.firstPersonModel) {
                    if (slot != EquipmentSlot.CHEST) {
                        continue;
                    }

                    firstPersonChest = true;
                }
               // if (slot == EquipmentSlot.HEAD) return;

                if (slot != EquipmentSlot.HEAD || !this.firstPersonModel) {
                    ItemStack itemstack = entityliving.getItemBySlot(slot);
                    Item item = itemstack.getItem();
                    if (item instanceof ArmorItem) {
                        ArmorItem armorItem = (ArmorItem)item;
                        if (slot != armorItem.getEquipmentSlot()) {
                            return;
                        }

                        poseStack.pushPose();
                        float head = 0.0F;
                        if (slot == EquipmentSlot.HEAD) {
                            poseStack.translate((double)0.0F, (double)head * 0.055, (double)0.0F);
                        }

                        HumanoidModel<?> defaultModel = ((AccessorHumanoidArmorLayer)vanillaLayer).invokeGetArmorModel(slot);
                        Model armorModel = ForgeHooksClient.getArmorModel(entityliving, itemstack, slot, defaultModel);
                        SkinnedMesh armorMesh = this.getArmorModel(vanillaLayer, defaultModel, armorModel, entityliving, armorItem, itemstack, slot);
                        if (armorMesh == null) {
                            poseStack.popPose();
                            return;
                        }

                        if (armorModel instanceof HumanoidModel) {
                            HumanoidModel humanoidModel = (HumanoidModel)armorModel;
                            boolean shouldSit = entityliving.isPassenger() && entityliving.getVehicle() != null && entityliving.getVehicle().shouldRiderSit();
                            float f8 = 0.0F;
                            float f5 = 0.0F;
                            if (!shouldSit && entityliving.isAlive()) {
                                f8 = entityliving.walkAnimation.speed(partialTicks);
                                f5 = entityliving.walkAnimation.position(partialTicks);
                                if (entityliving.isBaby()) {
                                    f5 *= 3.0F;
                                }

                                if (f8 > 1.0F) {
                                    f8 = 1.0F;
                                }
                            }

                            try {
                                humanoidModel.setupAnim(entityliving, f8, f5, bob, yRot, xRot);
                            } catch (ClassCastException var29) {
                            }

                            humanoidModel.head.loadPose(humanoidModel.head.getInitialPose());
                            humanoidModel.hat.loadPose(humanoidModel.hat.getInitialPose());
                            humanoidModel.body.loadPose(humanoidModel.body.getInitialPose());
                            humanoidModel.leftArm.loadPose(humanoidModel.leftArm.getInitialPose());
                            humanoidModel.rightArm.loadPose(humanoidModel.rightArm.getInitialPose());
                            humanoidModel.leftLeg.loadPose(humanoidModel.leftLeg.getInitialPose());
                            humanoidModel.rightLeg.loadPose(humanoidModel.rightLeg.getInitialPose());
                        }

                        armorMesh.initialize();
                        if (firstPersonChest) {
                            armorMesh.getAllParts().forEach((part) -> part.setHidden(true));
                            if (armorMesh.hasPart("leftArm")) {
                                armorMesh.getPart("leftArm").setHidden(false);
                            }

                            if (armorMesh.hasPart("rightArm")) {
                                armorMesh.getPart("rightArm").setHidden(false);
                            }
                        }

                        if (armorItem instanceof DyeableLeatherItem) {
                            DyeableLeatherItem dyeableItem = (DyeableLeatherItem)armorItem;
                            int i = dyeableItem.getColor(itemstack);
                            float r = (float)(i >> 16 & 255) / 255.0F;
                            float g = (float)(i >> 8 & 255) / 255.0F;
                            float b = (float)(i & 255) / 255.0F;
                            ((AccessorWearableItemLayer)this).invokeRenderArmor(poseStack, buf, packedLight, armorMesh, entitypatch.getArmature(), r, g, b, ((AccessorWearableItemLayer)this).invokeGetArmorTexture(itemstack, entityliving, armorMesh, slot, (String)null, defaultModel), poses);
                            ((AccessorWearableItemLayer)this).invokeRenderArmor(poseStack, buf, packedLight, armorMesh, entitypatch.getArmature(), 1.0F, 1.0F, 1.0F, ((AccessorWearableItemLayer)this).invokeGetArmorTexture(itemstack, entityliving, armorMesh, slot, "overlay", defaultModel), poses);
                        } else {
                            ((AccessorWearableItemLayer)this).invokeRenderArmor(poseStack, buf, packedLight, armorMesh, entitypatch.getArmature(), 1.0F, 1.0F, 1.0F, ((AccessorWearableItemLayer)this).invokeGetArmorTexture(itemstack, entityliving, armorMesh, slot, (String)null, defaultModel), poses);
                        }

                        ArmorTrim.getTrim(entityliving.level().registryAccess(), itemstack).ifPresent((armorTrim) -> ((AccessorWearableItemLayer)this).invokeRenderTrim(poseStack, buf, packedLight, armorMesh, entitypatch.getArmature(), armorItem.getMaterial(), armorTrim, slot, poses));
                        if (itemstack.hasFoil()) {
                            ((AccessorWearableItemLayer)this).invokeRenderGlint(poseStack, buf, packedLight, armorMesh, entitypatch.getArmature(), poses);
                        }

                        poseStack.popPose();
                    }
                }
            }
        }

    }

    private SkinnedMesh getArmorModel(HumanoidArmorLayer<E, M, M> originalRenderer, HumanoidModel originalModel, Model forgeHooksArmorModel, E entityliving, ArmorItem armorItem, ItemStack itemstack, EquipmentSlot slot) {
        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(armorItem);
        if (ARMOR_MODELS.containsKey(registryName) && !ClientEngine.getInstance().renderEngine.shouldRenderVanillaModel()) {
            return (SkinnedMesh)ARMOR_MODELS.get(registryName);
        } else {
            ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
            ResourceLocation rl = ResourceLocation.fromNamespaceAndPath(ForgeRegistries.ITEMS.getKey(armorItem).getNamespace(), "animmodels/armor/" + ForgeRegistries.ITEMS.getKey(armorItem).getPath() + ".json");
            SkinnedMesh skinnedMesh = null;
            if (resourceManager.getResource(rl).isPresent()) {
                try {
                    JsonAssetLoader modelLoader = new JsonAssetLoader(resourceManager, rl);
                    skinnedMesh = modelLoader.loadSkinnedMesh(SkinnedMesh::new);
                } catch (AssetLoadingException e) {
                    e.printStackTrace();
                    skinnedMesh = null;
                }
            } else {
                Iterable<ItemStack> armorItems = entityliving.getArmorSlots();
                ItemStack head = entityliving.getItemBySlot(EquipmentSlot.HEAD);
                ItemStack chest = entityliving.getItemBySlot(EquipmentSlot.CHEST);
                ItemStack legs = entityliving.getItemBySlot(EquipmentSlot.LEGS);
                ItemStack feet = entityliving.getItemBySlot(EquipmentSlot.FEET);
                if (armorItems instanceof List) {
                    List<ItemStack> armorItemList = (List)armorItems;
                    armorItemList.set(0, ItemStack.EMPTY);
                    armorItemList.set(1, ItemStack.EMPTY);
                    armorItemList.set(2, ItemStack.EMPTY);
                    armorItemList.set(3, ItemStack.EMPTY);
                    armorItemList.set(slot.getIndex(), itemstack);
                }

                PoseStack ps = new PoseStack();
                ps.translate(0.0F, 0.0F, 10000.0F);
                if (forgeHooksArmorModel instanceof HumanoidModel) {
                    HumanoidModel<?> humanoidModel = (HumanoidModel)forgeHooksArmorModel;
                    switch (slot) {
                        case FEET:
                            humanoidModel.rightLeg.visible = true;
                            humanoidModel.leftLeg.visible = true;
                            break;
                        case LEGS:
                            humanoidModel.body.visible = true;
                            humanoidModel.rightLeg.visible = true;
                            humanoidModel.leftLeg.visible = true;
                            break;
                        case CHEST:
                            humanoidModel.body.visible = true;
                            humanoidModel.rightArm.visible = true;
                            humanoidModel.leftArm.visible = true;
                            break;
                        case HEAD:
                            humanoidModel.head.visible = true;
                            humanoidModel.hat.visible = true;
                    }
                }

                originalRenderer.render(ps, Minecraft.getInstance().renderBuffers().bufferSource(), 0, entityliving, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
                if (armorItems instanceof List) {
                    List<ItemStack> armorItemList = (List)armorItems;
                    armorItemList.set(0, feet);
                    armorItemList.set(1, legs);
                    armorItemList.set(2, chest);
                    armorItemList.set(3, head);
                }

                skinnedMesh = HumanoidModelBaker.bakeArmor(entityliving, itemstack, armorItem, slot, originalModel, forgeHooksArmorModel, (HumanoidModel)originalRenderer.getParentModel(), (HumanoidMesh)this.mesh.get());
            }

            putModel(registryName, skinnedMesh);
            return skinnedMesh;
        }
    }


}
