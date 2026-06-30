package net.kenji.epic_colonies.events;

import com.minecolonies.api.entity.ModEntities;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.patch.CitizenEntityPatch;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jline.utils.Log;
import yesman.epicfight.api.forgeevent.EntityPatchRegistryEvent;
import yesman.epicfight.gameasset.Armatures;

@Mod.EventBusSubscriber(modid = EpicColonies.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void registerPatchedEntities(EntityPatchRegistryEvent event) {
        event.getTypeEntry().put(ModEntities.CITIZEN, entity -> CitizenEntityPatch::new);
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModEvents::registerEntityTypeArmatures);
    }

    private static void registerEntityTypeArmatures() {
        Armatures.registerEntityTypeArmature(ModEntities.CITIZEN, Armatures.BIPED);
    }
}