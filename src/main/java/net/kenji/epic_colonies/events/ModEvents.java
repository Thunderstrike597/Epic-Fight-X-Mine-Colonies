package net.kenji.epic_colonies.events;

import com.minecolonies.api.entity.ModEntities;
import com.mojang.brigadier.CommandDispatcher;
import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.api.MobPatchFactory;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMeshes;
import net.kenji.epic_colonies.gameasset.EpicColoniesArmatures;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import yesman.epicfight.api.event.types.registry.EntityPatchRegistryEvent;
import yesman.epicfight.gameasset.Armatures;

@EventBusSubscriber(modid = EpicColonies.MODID)
public class ModEvents {

    public static void registerPatchedEntities(EntityPatchRegistryEvent event) {
        MobPatchFactory.registerPatchedEntities(event);
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
        event.enqueueWork(EpicColonies::initWeaponMotions);

    }
    @SubscribeEvent
    public static void existingEntityAttributes(EntityAttributeModificationEvent event){

        for(MobPatchFactory.MobPatchDefinitions patchDefinitions : MobPatchFactory.mobPatches) {
            event.add(patchDefinitions.entityType, Attributes.ATTACK_KNOCKBACK, 1);
        }

    }

}