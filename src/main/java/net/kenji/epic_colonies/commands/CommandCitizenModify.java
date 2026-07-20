package net.kenji.epic_colonies.commands;

import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.util.Log;
import com.minecolonies.core.MineColonies;
import com.minecolonies.core.commands.arguments.ColonyIdArgument;
import com.minecolonies.core.commands.commandTypes.IMCCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class CommandCitizenModify {

    public static LiteralCommandNode<CommandSourceStack> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return dispatcher.register(build());
    }

    private static LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("modifyAllFood")
                .then(Commands.argument("colonyID", ColonyIdArgument.id())
                        .then(Commands.literal("saturation")
                                .then(Commands.literal("=")
                                        .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0, 20.0))
                                                .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(List.of("0.0", "20"), builder))
                                                .executes(ctx -> adjust(ctx, "saturation",
                                                        citizen -> citizen.setSaturation(DoubleArgumentType.getDouble(ctx, "value")),
                                                        citizen -> String.valueOf(citizen.getSaturation())))))
                                .then(Commands.literal("+")
                                        .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0, 20.0))
                                                .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(List.of("1.0"), builder))
                                                .executes(ctx -> adjust(ctx, "saturation",
                                                        citizen -> citizen.increaseSaturation(DoubleArgumentType.getDouble(ctx, "value")),
                                                        citizen -> String.valueOf(citizen.getSaturation())))))
                                .then(Commands.literal("-")
                                        .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0, 20.0))
                                                .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(List.of("1.0"), builder))
                                                .executes(ctx -> adjust(ctx, "saturation",
                                                        citizen -> citizen.decreaseSaturation(DoubleArgumentType.getDouble(ctx, "value")),
                                                        citizen -> String.valueOf(citizen.getSaturation())))))
                        )
                );
    }

    private static int adjust(@NotNull CommandContext<CommandSourceStack> context, @NotNull String type,
                              @NotNull Consumer<ICitizenData> action, @NotNull Function<ICitizenData, String> valueProvider) {
        return execute(context, citizen -> {
            action.accept(citizen);
            String value = valueProvider.apply(citizen);
            context.getSource().sendSuccess(() ->
                    Component.translatable("com.minecolonies.command.citizenmodify.success", type, citizen.getName(), value), true);
            return 0;
        });
    }
    static boolean checkPreCondition(CommandContext<CommandSourceStack> context) {
        if (((CommandSourceStack)context.getSource()).hasPermission(4)) {
            return true;
        } else {
            Entity sender = ((CommandSourceStack)context.getSource()).getEntity();
            if (!(sender instanceof Player)) {
                return false;
            } else {
                IColony colony;
                try {
                    colony = ColonyIdArgument.getColony(context, "colonyID");
                } catch (CommandRuntimeException var5) {
                    return false;
                }

                return IMCCommand.isPlayerOped((Player)sender) || colony.getPermissions().getRank((Player)sender).isColonyManager();
            }
        }
    }
    private static int execute(@NotNull CommandContext<CommandSourceStack> context, @NotNull ToIntFunction<ICitizenData> action) {
        try {
            CommandSourceStack source = context.getSource();
            if (!checkPreCondition(context))
                return 0;
            if (!source.hasPermission(4) && !MineColonies.getConfig().getServer().canPlayerUseModifyCitizensCommand.get()) {
                source.sendSuccess(() -> Component.translatable("com.minecolonies.command.notenabledinconfig"), true);
                return 0;
            }
            if (!(source.source instanceof MinecraftServer) && !(source.isPlayer() && source.getPlayer().isCreative())) {
                source.sendSuccess(() -> Component.translatable("com.minecolonies.command.notcreative"), true);
                return 0;
            }

            IColony colony = ColonyIdArgument.getColony(context, "colonyID");
            int result = 0;
            for (int i = 0; i < colony.getCitizenManager().getMaxCitizens(); i++) {
                ICitizenData citizenData = colony.getCitizenManager().getCivilian(i);
                if (citizenData != null) {
                    result = action.applyAsInt(citizenData);
                }
            }
            return result;
        } catch (Throwable e) {
            Log.getLogger().warn("Error during running command:", e);
            return 0;
        }
    }
}