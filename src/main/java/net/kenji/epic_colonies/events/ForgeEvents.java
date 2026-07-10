package net.kenji.epic_colonies.events;

import com.mojang.brigadier.CommandDispatcher;
import net.kenji.epic_colonies.EpicColonies;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EpicColonies.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)

public class ForgeEvents {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        ForgeEvents.registerCommands(event.getDispatcher());
    }
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {

    }
}
