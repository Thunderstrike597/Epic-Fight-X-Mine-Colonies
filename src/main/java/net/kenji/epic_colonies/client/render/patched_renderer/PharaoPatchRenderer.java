package net.kenji.epic_colonies.client.render.patched_renderer;

import net.kenji.epic_colonies.client.meshes.EpicColoniesMeshes;
import net.kenji.epic_colonies.client.render.patched_renderer.p_renderer.PMummyRenderer;
import net.kenji.epic_colonies.client.render.patched_renderer.p_renderer.PPharaoRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;

public class PharaoPatchRenderer extends PPharaoRenderer {
    public PharaoPatchRenderer(EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(EpicColoniesMeshes.PHARAO, context, entityType);
    }
}
