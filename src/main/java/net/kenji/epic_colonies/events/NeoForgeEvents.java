package net.kenji.epic_colonies.events;

import com.mojang.brigadier.CommandDispatcher;
import net.kenji.epic_colonies.EpicColonies;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;


@EventBusSubscriber(modid = EpicColonies.MODID)
public class NeoForgeEvents {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        NeoForgeEvents.registerCommands(event.getDispatcher());
    }
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {

    }
}
