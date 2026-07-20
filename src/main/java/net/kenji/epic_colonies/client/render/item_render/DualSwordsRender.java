package net.kenji.epic_colonies.client.render.item_render;

import com.google.gson.JsonElement;
import com.mojang.blaze3d.vertex.PoseStack;
import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.item.EpicColoniesItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.patched.item.RenderItemBase;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = EpicColonies.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class DualSwordsRender extends RenderItemBase {
    private final ItemStack swordStack;

    public DualSwordsRender(JsonElement jsonElement) {
        super(jsonElement);
        if (jsonElement.getAsJsonObject().has("weapon")) {
            this.swordStack = new ItemStack((ItemLike) Objects.requireNonNull((Item) ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(jsonElement.getAsJsonObject().get("weapon").getAsString()))));
        }else {
            this.swordStack = new ItemStack((ItemLike) EpicColoniesItems.IRON_DUAL_SWORDS.get());
        }
    }

    @Override
    public void renderItemInHand(ItemStack stack, LivingEntityPatch<?> entitypatch, InteractionHand hand, OpenMatrix4f[] poses, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks) {
        super.renderItemInHand(swordStack, entitypatch, InteractionHand.MAIN_HAND, poses, buffer, poseStack, packedLight, partialTicks);
        super.renderItemInHand(swordStack, entitypatch, InteractionHand.OFF_HAND, poses, buffer, poseStack, packedLight, partialTicks);
    }
}

