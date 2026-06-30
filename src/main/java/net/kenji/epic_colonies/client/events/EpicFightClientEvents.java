package net.kenji.epic_colonies.client.events;

import com.minecolonies.api.entity.ModEntities;
import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.client.render.patched_renderer.CitizenPatchRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jline.utils.Log;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;

@Mod.EventBusSubscriber(modid = EpicColonies.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EpicFightClientEvents {
    @SubscribeEvent
    public static void registerPatchedEntityRenderers(PatchedRenderersEvent.Add event) {

        event.addPatchedEntityRenderer(ModEntities.CITIZEN, entityType -> new CitizenPatchRenderer(
                        event.getContext(),
                        entityType
                )
        );
    }
}
