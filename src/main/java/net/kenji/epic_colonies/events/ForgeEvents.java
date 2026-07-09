package net.kenji.epic_colonies.events;

import com.mojang.brigadier.CommandDispatcher;
import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.commands.SetChildCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
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
        dispatcher.register(
                Commands.literal("epic_colonies")
                        .requires(source -> source.hasPermission(2)) // optional
                        .then(SetChildCommand.register())
        );
    }
}
