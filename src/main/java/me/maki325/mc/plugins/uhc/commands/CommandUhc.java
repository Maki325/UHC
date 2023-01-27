package me.maki325.mc.plugins.uhc.commands;

import me.maki325.mc.plugins.uhc.Uhc;
import me.maki325.mc.plugins.uhc.api.CommandUsage;
import me.maki325.mc.plugins.uhc.commands.team.CommandTeam;
import me.maki325.mc.plugins.uhc.commands.world.CommandWorld;
import me.maki325.mc.plugins.uhc.utils.CommandUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CommandUhc implements CommandExecutor {

    Uhc plugin;
    public final HashMap<String, CommandUsage> subcommands = new HashMap<>();

    public CommandUhc(Uhc plugin) {
        this.plugin = plugin;

        subcommands.put("help", new CommandHelp());
        subcommands.put("team", new CommandTeam(plugin));
        subcommands.put("world", new CommandWorld(plugin));
        subcommands.put("start", new CommandStart(plugin));
    }

    @Override public boolean onCommand(
        @NotNull CommandSender sender,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    ) {
        if(args.length == 0) {
            sendUsage(sender);
            return true;
        }

        String subcommandKey = args[0];
        CommandUsage subcommand = subcommands.get(subcommandKey);

        if(subcommand == null) {
            sender.sendMessage(Component.text("Unknown command \"" + subcommandKey + "\"."));
            sendUsage(sender);
            return true;
        }

        String[] subcommandArgs = CommandUtils.popFirst(args);

        return subcommand.onCommand(sender, command, label, subcommandArgs);
    }

    public void sendUsage(@NotNull CommandSender sender) {
        sender.sendMessage(Component.text("Usage: "));
        subcommands.values().forEach(command -> {
            sender.sendMessage(Component.text("  - " + command.getUsage()));
        });
    }
}
