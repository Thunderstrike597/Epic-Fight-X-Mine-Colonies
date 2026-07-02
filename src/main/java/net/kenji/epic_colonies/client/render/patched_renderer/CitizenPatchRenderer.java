package net.kenji.epic_colonies.client.render.patched_renderer;

import net.kenji.epic_colonies.client.meshes.EpicColoniesMeshes;
import net.kenji.epic_colonies.client.render.patched_renderer.p_renderer.PCitizenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;

public class CitizenPatchRenderer extends PCitizenRenderer {
    public CitizenPatchRenderer(EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(EpicColoniesMeshes.CITIZEN_MALE, context, entityType);
    }
}
