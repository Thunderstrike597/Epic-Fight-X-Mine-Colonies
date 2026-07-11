package net.kenji.epic_colonies.client.render.patched_renderer;

import net.kenji.epic_colonies.client.meshes.EpicColoniesMeshes;
import net.kenji.epic_colonies.client.render.patched_renderer.p_renderer.PBarbarianRenderer;
import net.kenji.epic_colonies.client.render.patched_renderer.p_renderer.PMercenaryRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;

public class MercenaryPatchRenderer extends PMercenaryRenderer {
    public MercenaryPatchRenderer(EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(EpicColoniesMeshes.MERCENARY, context, entityType);
    }
}
