package net.kenji.epic_colonies.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.renderer.patched.layer.WearableItemLayer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@Mixin(value = WearableItemLayer.class, remap = false)
public interface AccessorWearableItemLayer<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends HumanoidModel<E>, AM extends HumanoidMesh>{

    @Invoker("getArmorModel")
    SkinnedMesh invokeGetArmorModel(HumanoidArmorLayer<E, M, M> originalRenderer, M originalModel, Model forgeHooksArmorModel, E entityliving, ArmorItem armorItem, ItemStack itemstack, EquipmentSlot slot);
    @Invoker("renderArmor")
    void invokeRenderArmor(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, SkinnedMesh model, Armature armature, float r, float g, float b, ResourceLocation armorTexture, OpenMatrix4f[] poses);
    @Invoker("renderGlint")
    void invokeRenderGlint(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, SkinnedMesh model, Armature armature, OpenMatrix4f[] poses);
    @Invoker("renderTrim")
    void invokeRenderTrim(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, SkinnedMesh model, Armature armature, ArmorMaterial armorMaterial, ArmorTrim armorTrim, EquipmentSlot slot, OpenMatrix4f[] pose);
    @Invoker("getArmorTexture")
    ResourceLocation invokeGetArmorTexture(ItemStack itemstack, LivingEntity entity, SkinnedMesh armorMesh, EquipmentSlot slot, String type, M originalModel);
    @Accessor("firstPersonModel")
    boolean getIsFirstPerson();
}