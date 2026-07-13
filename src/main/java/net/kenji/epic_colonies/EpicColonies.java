package net.kenji.epic_colonies;

import com.mojang.logging.LogUtils;

import net.kenji.epic_colonies.client.events.EpicFightClientEvents;
import net.kenji.epic_colonies.compat.CompatMobCombatBehaviours;
import net.kenji.epic_colonies.compat.cd_moveset.CdMovesetCombatBehaviours;
import net.kenji.epic_colonies.compat.wom.WomCombatBehaviours;
import net.kenji.epic_colonies.events.ModEvents;
import net.kenji.epic_colonies.gameasset.EpicColoniesAnimations;
import net.kenji.epic_colonies.gameasset.EpicColoniesLivingMotions;
import net.kenji.epic_colonies.network.EpicColoniesPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import yesman.epicfight.api.animation.LivingMotions;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EpicColonies.MODID)
public class EpicColonies {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "epic_colonies";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();


    public EpicColonies() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the commonSetup method for modloading
        modEventBus.addListener(ModEvents::registerPatchedEntities);
        modEventBus.addListener(EpicColoniesAnimations::registerAnimations);

        modEventBus.addListener(ModEvents::commonSetup);
        modEventBus.addListener(this::commonSetup);
        // Register ourselves for server and other game events we are interested in
        LivingMotions.ENUM_MANAGER.registerEnumCls(MODID, EpicColoniesLivingMotions.class);
        MinecraftForge.EVENT_BUS.register(this);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(EpicFightClientEvents::registerPatchedEntityRenderers);        }

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, EpicColoniesConfigClient.SPEC, "EpicColonies-Client.toml");


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
        event.enqueueWork(EpicColoniesPacketHandler::register);
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
