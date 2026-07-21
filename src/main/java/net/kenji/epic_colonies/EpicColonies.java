package net.kenji.epic_colonies;

import com.mojang.logging.LogUtils;

import net.kenji.epic_colonies.client.events.EpicFightClientEvents;
import net.kenji.epic_colonies.compat.CompatMobCombatBehaviours;
import net.kenji.epic_colonies.compat.cd_moveset.CdMovesetCombatBehaviours;
import net.kenji.epic_colonies.compat.wom.WomCombatBehaviours;
import net.kenji.epic_colonies.events.ModEvents;
import net.kenji.epic_colonies.gameasset.EpicColoniesAnimations;
import net.kenji.epic_colonies.gameasset.EpicColoniesLivingMotions;
import net.kenji.epic_colonies.gameasset.EpicColoniesWeaponCapabilityPresets;
import net.kenji.epic_colonies.item.EpicColoniesItems;
import net.kenji.epic_colonies.tab.EpicColoniesTab;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.event.EpicFightEventHooks;
import yesman.epicfight.api.client.event.EpicFightClientEventHooks;
import yesman.epicfight.api.event.types.registry.WeaponCapabilityPresetRegistryEvent;

@Mod(EpicColonies.MODID)
public class EpicColonies {

    public static final String MODID = "epic_colonies";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static @NotNull ResourceLocation identifier(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public EpicColonies(IEventBus modEventBus) {
        EpicColoniesItems.register(modEventBus);
        EpicColoniesTab.register(modEventBus);

        // Register natively using Epic Fight Mod's custom event hooks
        EpicFightEventHooks.Registry.ENTITY_PATCH.registerEvent(ModEvents::registerPatchedEntities);
        EpicFightEventHooks.Registry.WEAPON_CAPABILITY_PRESET.registerEvent(EpicColonies::registerWeaponType);

        modEventBus.addListener(EpicColoniesAnimations::registerAnimations);
        modEventBus.addListener(ModEvents::commonSetup);
        modEventBus.addListener(this::commonSetup);

        LivingMotions.ENUM_MANAGER.registerEnumCls(MODID, EpicColoniesLivingMotions.class);
        
        
        if (FMLEnvironment.dist == Dist.CLIENT) {
            EpicFightClientEventHooks.Registry.ADD_PATCHED_ENTITY.registerEvent(EpicFightClientEvents::registerPatchedEntityRenderers);
            EpicFightClientEventHooks.Registry.PATCHED_ITEM.registerEvent(EpicFightClientEvents::registerRenderer);
        }

        // NeoForge standard way to register configuration specs directly on the active mod container
        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.CLIENT, EpicColoniesConfigClient.SPEC, "EpicColonies-Client.toml");
    }

    public static void registerWeaponType(WeaponCapabilityPresetRegistryEvent event) {
        event.getTypeEntry().put(ResourceLocation.fromNamespaceAndPath(MODID, "dual_swords"), EpicColoniesWeaponCapabilityPresets.DUAL_SWORDS);
        event.getTypeEntry().put(ResourceLocation.fromNamespaceAndPath(MODID, "dual_daggers"), EpicColoniesWeaponCapabilityPresets.DUAL_DAGGERS);
    }

    public static void initWeaponMotions(){
        CompatMobCombatBehaviours.initEpicFightWeaponMotions();

        if(ModList.get().isLoaded("wom")){
            WomCombatBehaviours.init();
        }
        if(ModList.get().isLoaded("cdmoveset")){
            CdMovesetCombatBehaviours.init();
        }
    }

    public void commonSetup(FMLCommonSetupEvent event) {
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}