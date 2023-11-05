package me.gravityio.varhopper.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.gravityio.varhopper.ModConfig;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModCommands {

    private static LiteralArgumentBuilder<ServerCommandSource> buildInt(String name, Supplier<Integer> getter, Consumer<Integer> setter, Runnable onSet) {
        var cmd = CommandManager.literal(name);
        var arg = CommandManager.argument(name, IntegerArgumentType.integer(1, 32));
        arg.executes(context -> {
            var v = IntegerArgumentType.getInteger(context, name);
            setter.accept(v);

            var setText = Text.translatable("command.varhopper.set_int", name, getter.get());
            context.getSource().sendFeedback(() -> setText, false);
            onSet.run();
            return 1;

        });

        cmd.executes(context -> {
            var getText = Text.translatable("command.varhopper.get_int", name, getter.get());
            context.getSource().sendFeedback(() -> getText, false);
            return 1;

        });

        cmd.then(arg);
        return cmd;
    }

    public static LiteralArgumentBuilder<ServerCommandSource> build() {
        var root = CommandManager.literal("varhopper").requires(source -> source.hasPermissionLevel(4));
        var slopperHopper = buildInt("slopper_cooldown", () -> ModConfig.INSTANCE.slopperCooldown, v -> ModConfig.INSTANCE.slopperCooldown = v, ModConfig.INSTANCE::save);
        var hyperHopper = buildInt("hyper_cooldown", () -> ModConfig.INSTANCE.blazeCooldown, v -> ModConfig.INSTANCE.blazeCooldown = v, ModConfig.INSTANCE::save);
        var superHopper = buildInt("super_cooldown", () -> ModConfig.INSTANCE.phantomCooldown, v -> ModConfig.INSTANCE.phantomCooldown = v, ModConfig.INSTANCE::save);
        var echoHopper = buildInt("echo_cooldown", () -> ModConfig.INSTANCE.echoCooldown, v -> ModConfig.INSTANCE.echoCooldown = v, ModConfig.INSTANCE::save);
        var splotterHopper = buildInt("splotter_cooldown", () -> ModConfig.INSTANCE.splotterCooldown, v -> ModConfig.INSTANCE.splotterCooldown = v, ModConfig.INSTANCE::save);

        root.then(slopperHopper);
        root.then(hyperHopper);
        root.then(superHopper);
        root.then(echoHopper);
        root.then(splotterHopper);
        root.executes(context -> {
            var s = context.getSource();
            s.sendFeedback(() -> Text.translatable("command.varhopper.print_all", "slopper_cooldown", ModConfig.INSTANCE.slopperCooldown), false);

            s.sendFeedback(() -> Text.translatable("command.varhopper.get_int", "slopper_cooldown", ModConfig.INSTANCE.slopperCooldown), false);
            s.sendFeedback(() -> Text.translatable("command.varhopper.get_int", "hyper_cooldown", ModConfig.INSTANCE.blazeCooldown), false);
            s.sendFeedback(() -> Text.translatable("command.varhopper.get_int", "super_cooldown", ModConfig.INSTANCE.phantomCooldown), false);
            s.sendFeedback(() -> Text.translatable("command.varhopper.get_int", "echo_cooldown", ModConfig.INSTANCE.echoCooldown), false);
            s.sendFeedback(() -> Text.translatable("command.varhopper.get_int", "splotter_cooldown", ModConfig.INSTANCE.splotterCooldown), false);
            return 1;

        });
        return root;
    }

    private static void sendFeedback(ServerCommandSource source, Text text) {
        source.sendFeedback(() -> text, false);
    }
}
