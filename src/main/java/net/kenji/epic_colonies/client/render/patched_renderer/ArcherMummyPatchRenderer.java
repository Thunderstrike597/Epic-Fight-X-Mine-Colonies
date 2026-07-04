package net.kenji.epic_colonies.client.render.patched_renderer;

import net.kenji.epic_colonies.client.meshes.EpicColoniesMeshes;
import net.kenji.epic_colonies.client.render.patched_renderer.p_renderer.PArcherMummyRenderer;
import net.kenji.epic_colonies.client.render.patched_renderer.p_renderer.PMummyRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;

public class ArcherMummyPatchRenderer extends PArcherMummyRenderer {
    public ArcherMummyPatchRenderer(EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(EpicColoniesMeshes.MUMMY, context, entityType);
    }
}
