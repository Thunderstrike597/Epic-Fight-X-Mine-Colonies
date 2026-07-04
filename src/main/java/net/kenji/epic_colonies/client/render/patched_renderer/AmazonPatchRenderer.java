package net.kenji.epic_colonies.client.render.patched_renderer;

import net.kenji.epic_colonies.client.meshes.EpicColoniesMeshes;
import net.kenji.epic_colonies.client.render.patched_renderer.p_renderer.PAmazonChiefRenderer;
import net.kenji.epic_colonies.client.render.patched_renderer.p_renderer.PAmazonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;

public class AmazonPatchRenderer extends PAmazonRenderer {
    public AmazonPatchRenderer(EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(EpicColoniesMeshes.AMAZON, context, entityType);
    }
}
