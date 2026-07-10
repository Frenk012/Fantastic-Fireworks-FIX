package com.rnr.rnrjointmod.firework;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.rnr.rnrjointmod.RnRJointMod;
import com.rnr.rnrjointmod.block.entity.FireworkCakeEntity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * /firework launch <x y z>   - launch the cake at those coordinates
 * /firework launchid <id>    - launch the cake with that id (shown in the GUI)
 * /firework list             - list all loaded cake ids
 * Usable from command blocks and mcfunction files.
 */
@EventBusSubscriber(modid = RnRJointMod.MOD_ID)
public class FireworkCommands {

    private static final SuggestionProvider<CommandSourceStack> CAKE_IDS =
            (context, builder) -> SharedSuggestionProvider.suggest(FireworkCakeRegistry.all().keySet(), builder);

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("firework")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("launch")
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                .executes(ctx -> launchAt(ctx.getSource(),
                                        ctx.getSource().getLevel(),
                                        BlockPosArgument.getLoadedBlockPos(ctx, "pos")))))
                .then(Commands.literal("launchid")
                        .then(Commands.argument("id", StringArgumentType.string())
                                .suggests(CAKE_IDS)
                                .executes(ctx -> launchById(ctx.getSource(), StringArgumentType.getString(ctx, "id")))))
                .then(Commands.literal("list")
                        .executes(ctx -> list(ctx.getSource())))
                .then(Commands.literal("show")
                        .then(Commands.literal("stop")
                                .executes(ctx -> stopShow(ctx.getSource())))
                        .then(Commands.argument("sequence", StringArgumentType.greedyString())
                                .executes(ctx -> startShow(ctx.getSource(), StringArgumentType.getString(ctx, "sequence"))))));
    }

    /**
     * Sequence format: space-separated {@code id@seconds} steps, e.g.
     * {@code /firework show opener@0 ring1@1.5 palm@3 finale@6}.
     * Seconds are measured from when the command runs; ids may repeat.
     */
    private static int startShow(CommandSourceStack source, String sequence) {
        int scheduled = 0;
        float lastTime = 0;
        for (String token : sequence.trim().split("\\s+")) {
            String[] parts = token.split("@", 2);
            if (parts.length != 2) {
                source.sendFailure(Component.literal("Bad step '" + token + "' - use id@seconds, e.g. cake1@2.5"));
                return 0;
            }
            float seconds;
            try {
                seconds = Float.parseFloat(parts[1]);
            } catch (NumberFormatException e) {
                source.sendFailure(Component.literal("Bad time in '" + token + "' - use id@seconds, e.g. cake1@2.5"));
                return 0;
            }
            if (FireworkCakeRegistry.get(parts[0]) == null) {
                source.sendFailure(Component.literal("No loaded firework cake with id '" + parts[0] + "'"));
                return 0;
            }
            FireworkShowScheduler.schedule(source.getServer(), parts[0], (int) (seconds * 20));
            lastTime = Math.max(lastTime, seconds);
            scheduled++;
        }
        if (scheduled == 0) {
            source.sendFailure(Component.literal("Empty show - give steps as id@seconds"));
            return 0;
        }
        final int count = scheduled;
        final float duration = lastTime;
        source.sendSuccess(() -> Component.literal(
                "Show started: " + count + " launches over " + duration + "s"), true);
        return count;
    }

    private static int stopShow(CommandSourceStack source) {
        int cancelled = FireworkShowScheduler.cancelAll();
        source.sendSuccess(() -> Component.literal("Show stopped, " + cancelled + " pending launches cancelled"), true);
        return cancelled;
    }

    private static int launchAt(CommandSourceStack source, ServerLevel level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof FireworkCakeEntity cake) {
            cake.triggerLaunch(level);
            source.sendSuccess(() -> Component.literal("Launched firework at " + pos.toShortString()), true);
            return 1;
        }
        source.sendFailure(Component.literal("No firework cake at " + pos.toShortString()));
        return 0;
    }

    private static int launchById(CommandSourceStack source, String id) {
        GlobalPos globalPos = FireworkCakeRegistry.get(id);
        if (globalPos == null) {
            source.sendFailure(Component.literal("No loaded firework cake with id '" + id + "'"));
            return 0;
        }
        ServerLevel level = source.getServer().getLevel(globalPos.dimension());
        if (level == null) {
            source.sendFailure(Component.literal("Dimension for '" + id + "' is not loaded"));
            return 0;
        }
        return launchAt(source, level, globalPos.pos());
    }

    private static int list(CommandSourceStack source) {
        var all = FireworkCakeRegistry.all();
        if (all.isEmpty()) {
            source.sendSuccess(() -> Component.literal("No firework cakes loaded"), false);
            return 0;
        }
        all.forEach((id, pos) -> source.sendSuccess(
                () -> Component.literal(id + " -> " + pos.pos().toShortString() + " (" + pos.dimension().location() + ")"), false));
        return all.size();
    }
}
