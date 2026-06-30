package net.kenji.epic_colonies.client.render.patched_renderer;

import net.kenji.epic_colonies.client.EpicColoniesMeshes;
import net.kenji.epic_colonies.client.render.patched_renderer.p_renderer.PCitizenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.EntityType;
import yesman.epicfight.client.renderer.patched.layer.PatchedItemInHandLayer;
import yesman.epicfight.client.renderer.patched.layer.WearableItemLayer;

public class CitizenPatchRenderer extends PCitizenRenderer {
    public CitizenPatchRenderer(EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(EpicColoniesMeshes.CITIZEN_MALE, context, entityType);
    }
}
