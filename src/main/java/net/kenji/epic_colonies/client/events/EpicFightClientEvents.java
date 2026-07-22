package net.kenji.epic_colonies.client.events;

import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.api.MobPatchFactory;
import net.kenji.epic_colonies.client.render.item_render.DualSwordsRender;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import yesman.epicfight.api.client.event.types.registry.RegisterPatchedRenderersEvent;


public class EpicFightClientEvents {

    public static void registerPatchedEntityRenderers(RegisterPatchedRenderersEvent.AddEntity event) {
        for (MobPatchFactory.MobPatchDefinitions def : MobPatchFactory.mobPatches) {
            event.addPatchedEntityRenderer(
                    def.entityType,
                    entityType -> def.rendererFactory.create(event.getContext(), entityType)
            );
        }
    }

    public static void registerItemRenderer(RegisterPatchedRenderersEvent.Item event) {
        event.addItemRenderer(EpicColonies.identifier("dual_weapon"), DualSwordsRender::new);
    }

}
