package net.kenji.epic_colonies.client.render.patched_renderer;

import net.kenji.epic_colonies.client.meshes.EpicColoniesMeshes;
import net.kenji.epic_colonies.client.render.patched_renderer.p_renderer.PChiefBarbarianRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;

public class BarbarianChiefPatchRenderer extends PChiefBarbarianRenderer {
    public BarbarianChiefPatchRenderer(EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(EpicColoniesMeshes.HUMANOID_RAIDER, context, entityType);
    }
}
