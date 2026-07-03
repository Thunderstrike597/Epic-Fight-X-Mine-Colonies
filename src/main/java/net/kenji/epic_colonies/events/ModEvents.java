package net.kenji.epic_colonies.events;

import com.minecolonies.api.entity.ModEntities;
import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.api.MobPatchFactory;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMeshes;
import net.kenji.epic_colonies.gameasset.EpicColoniesArmatures;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import yesman.epicfight.api.forgeevent.EntityPatchRegistryEvent;
import yesman.epicfight.gameasset.Armatures;

@Mod.EventBusSubscriber(modid = EpicColonies.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    public static void registerPatchedEntities(EntityPatchRegistryEvent event) {
        for (MobPatchFactory.MobPatchDefinitions def : MobPatchFactory.mobPatches) {
            event.getTypeEntry().put(def.entityType, def.mobPatch);
        }
    }

    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModEvents::registerEntityTypeArmatures);
    }



    private static void registerEntityTypeArmatures() {
        for (MobPatchFactory.MobPatchDefinitions def : MobPatchFactory.mobPatches) {
            Armatures.registerEntityTypeArmature(def.entityType, def.armature);
        }
    }

    @SubscribeEvent
    public static void loadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(EpicColoniesMeshes::buildJobMeshMaps);
    }
    @SubscribeEvent
    public static void existingEntityAttributes(EntityAttributeModificationEvent event){

        event.add(ModEntities.CITIZEN, Attributes.ATTACK_KNOCKBACK, 1);
        event.add(ModEntities.NORSEMEN_CHIEF, Attributes.ATTACK_KNOCKBACK, 1);

    }

}