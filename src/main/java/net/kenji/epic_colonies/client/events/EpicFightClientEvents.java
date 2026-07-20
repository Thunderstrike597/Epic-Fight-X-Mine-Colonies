package net.kenji.epic_colonies.client.events;

import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.api.MobPatchFactory;
import net.kenji.epic_colonies.client.render.item_render.DualSwordsRender;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;

@Mod.EventBusSubscriber(modid = EpicColonies.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EpicFightClientEvents {

    public static void registerPatchedEntityRenderers(PatchedRenderersEvent.Add event) {
        for (MobPatchFactory.MobPatchDefinitions def : MobPatchFactory.mobPatches) {
            event.addPatchedEntityRenderer(
                    def.entityType,
                    entityType -> def.rendererFactory.create(event.getContext(), entityType)
            );
        }
    }
    @SubscribeEvent
    public static void registerRenderer(PatchedRenderersEvent.RegisterItemRenderer event) {
        event.addItemRenderer(EpicColonies.identifier("dual_weapon"), DualSwordsRender::new);

    }

}
