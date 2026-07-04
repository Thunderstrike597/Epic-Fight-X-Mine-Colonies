package net.kenji.epic_colonies.client.render.patched_renderer;

import net.kenji.epic_colonies.client.meshes.EpicColoniesMeshes;
import net.kenji.epic_colonies.client.render.patched_renderer.p_renderer.PChiefPirateRenderer;
import net.kenji.epic_colonies.client.render.patched_renderer.p_renderer.PPirateRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;

public class PiratePatchRenderer extends PPirateRenderer {
    public PiratePatchRenderer(EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(EpicColoniesMeshes.HUMANOID_RAIDER, context, entityType);
    }
}
