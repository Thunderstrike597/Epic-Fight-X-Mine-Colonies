package net.kenji.epic_colonies;

import com.minecolonies.api.client.render.modeltype.CitizenModel;
import com.minecolonies.core.client.model.FemaleCitizenModel;
import com.minecolonies.core.client.model.MaleCitizenModel;
import com.mojang.logging.LogUtils;
import net.kenji.epic_colonies.client.events.EpicFightClientEvents;
import net.kenji.epic_colonies.events.ModEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EpicColonies.MODID)
public class EpicColonies {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "epic_colonies";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();


    public EpicColonies() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(ModEvents::registerPatchedEntities);
        modEventBus.addListener(ModEvents::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(EpicFightClientEvents::registerPatchedEntityRenderers);        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

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
