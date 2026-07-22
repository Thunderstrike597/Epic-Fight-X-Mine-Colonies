package net.kenji.epic_colonies.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.kenji.epic_colonies.EpicColonies;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = EpicColonies.MODID)
public class EpicColoniesCommand {
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal(EpicColonies.MODID)
                        .requires(source -> source.hasPermission(2)) // optional
                        .then(CommandCitizenModify.register(event.getDispatcher()))
        );
    }
}
