package net.kenji.epic_colonies.client.render.patched_renderer;

import net.kenji.epic_colonies.client.meshes.EpicColoniesMeshes;
import net.kenji.epic_colonies.client.render.patched_renderer.p_renderer.PAmazonRenderer;
import net.kenji.epic_colonies.client.render.patched_renderer.p_renderer.PMummyRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;

public class MummyPatchRenderer extends PMummyRenderer {
    public MummyPatchRenderer(EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(EpicColoniesMeshes.MUMMY, context, entityType);
    }
}
