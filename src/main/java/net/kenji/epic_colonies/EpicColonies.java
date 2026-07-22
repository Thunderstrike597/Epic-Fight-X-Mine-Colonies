package net.kenji.epic_colonies;

import com.mojang.logging.LogUtils;

import net.kenji.epic_colonies.client.events.EpicFightClientEvents;
import net.kenji.epic_colonies.compat.CompatMobCombatBehaviours;
import net.kenji.epic_colonies.events.ModEvents;
import net.kenji.epic_colonies.gameasset.EpicColoniesAnimations;
import net.kenji.epic_colonies.gameasset.EpicColoniesLivingMotions;
import net.kenji.epic_colonies.gameasset.EpicColoniesWeaponCapabilityPresets;
import net.kenji.epic_colonies.item.EpicColoniesItems;
import net.kenji.epic_colonies.network.EpicColoniesPacketHandler;
import net.kenji.epic_colonies.tab.EpicColoniesTab;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.client.event.EpicFightClientEventHooks;
import yesman.epicfight.api.event.EpicFightEventHooks;
import yesman.epicfight.api.event.types.registry.WeaponCapabilityPresetRegistryEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(EpicColonies.MODID)
public class EpicColonies {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "epic_colonies";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static @NotNull ResourceLocation identifier(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public EpicColonies(ModContainer container) {
        IEventBus modEventBus = ModLoadingContext.get().getActiveContainer().getEventBus();
        // Register the commonSetup method for modloading

        EpicColoniesItems.register(modEventBus);
        EpicColoniesTab.register(modEventBus);

        modEventBus.addListener(EpicColoniesAnimations::registerAnimations);

        modEventBus.addListener(ModEvents::commonSetup);
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        LivingMotions.ENUM_MANAGER.registerEnumCls(MODID, EpicColoniesLivingMotions.class);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            EpicFightClientEventHooks.Registry.ADD_PATCHED_ENTITY.registerEvent(EpicFightClientEvents::registerPatchedEntityRenderers);

            EpicFightEventHooks.Registry.WEAPON_CAPABILITY_PRESET.registerEvent(EpicColonies::registerWeaponType);
            EpicFightClientEventHooks.Registry.PATCHED_ITEM.registerEvent(EpicFightClientEvents::registerItemRenderer);
        }        EpicFightEventHooks.Registry.ENTITY_PATCH.registerEvent(ModEvents::registerPatchedEntities);


        container.registerConfig(ModConfig.Type.CLIENT, EpicColoniesConfigClient.SPEC, "EpicColonies-Client.toml");


    }
    public static void registerWeaponType(WeaponCapabilityPresetRegistryEvent event) {
        event.getTypeEntry().put(ResourceLocation.fromNamespaceAndPath(MODID, "dual_swords"), EpicColoniesWeaponCapabilityPresets.DUAL_SWORDS);
        event.getTypeEntry().put(ResourceLocation.fromNamespaceAndPath(MODID, "dual_daggers"), EpicColoniesWeaponCapabilityPresets.DUAL_DAGGERS);
    }
    public static void initWeaponMotions(){
        CompatMobCombatBehaviours.initEpicFightWeaponMotions();

        if(ModList.get().isLoaded("wom")){
            //WomCombatBehaviours.init();
        }
        if(ModList.get().isLoaded("cdmoveset")){
            //CdMovesetCombatBehaviours.init();
        }

    }

    public void commonSetup(FMLCommonSetupEvent event) {
    }
}
