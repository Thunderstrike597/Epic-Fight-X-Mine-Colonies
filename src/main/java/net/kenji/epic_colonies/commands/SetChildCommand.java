package net.kenji.epic_colonies.commands;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.RotationArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.SignableCommand;
import net.minecraft.server.commands.TeleportCommand;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityTeleportEvent;

import java.util.Collection;
import java.util.Collections;

public class SetChildCommand {


    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("debug_set_child")
                .then(
                        Commands.argument("targets", EntityArgument.entities())
                                .suggests((ctx, builder) -> {

                                    return builder.buildFuture();
                                }).then(Commands.argument("targets", EntityArgument.entities())
                                        .suggests((ctx, builder) -> {

                                            return builder.buildFuture();
                                        }))
                                .then(Commands.argument("child", BoolArgumentType.bool())
                                        .suggests((ctx, builder) -> {
                                            builder.suggest("true");
                                            builder.suggest("false");

                                            return builder.buildFuture();
                                        }))
                                .executes(ctx -> {

                                    boolean isChild = BoolArgumentType.getBool(ctx, "child");
                                    Collection<? extends Entity> entity = EntityArgument.getEntities(ctx, "targets");
                                    executeSetChild(ctx, entity, isChild);
                                    return 1;
                                })
                );
    }

    private static void executeSetChild(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, boolean isChild) {
        int count = 0;
        for (Entity entity : entities) {
            if (!(entity instanceof AbstractEntityCitizen citizen)) return;

            citizen.setIsChild(isChild);
            count++;
        }


        if (count > 0) {

            int finalCount = count;
            ctx.getSource().sendSuccess(
                    () -> Component.literal("Success setting " + finalCount + " Citizens to: " + (isChild ? "Child" : "Adult")),
                    false
            );
        } else {
            ctx.getSource().sendFailure(Component.literal("0 Citizen Set to: " + (isChild ? "Child" : "Adult")));
        }
    }
}

