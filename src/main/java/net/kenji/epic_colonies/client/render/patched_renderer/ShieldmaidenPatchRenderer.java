package net.kenji.epic_colonies.client.render.patched_renderer;

import net.kenji.epic_colonies.client.meshes.EpicColoniesMeshes;
import net.kenji.epic_colonies.client.render.patched_renderer.p_renderer.PBarbarianRenderer;
import net.kenji.epic_colonies.client.render.patched_renderer.p_renderer.PShieldmaidenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;

public class ShieldmaidenPatchRenderer extends PShieldmaidenRenderer {
    public ShieldmaidenPatchRenderer(EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(EpicColoniesMeshes.SHIELDMAIDEN, context, entityType);
    }
}
