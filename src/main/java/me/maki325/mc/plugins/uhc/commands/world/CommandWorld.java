package me.maki325.mc.plugins.uhc.commands.world;

import me.maki325.mc.plugins.uhc.Uhc;
import me.maki325.mc.plugins.uhc.api.CommandUsage;
import me.maki325.mc.plugins.uhc.utils.CommandUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CommandWorld implements CommandUsage {

    Uhc plugin;
    public final HashMap<String, CommandUsage> subcommands = new HashMap<>();

    public CommandWorld(Uhc plugin) {
        this.plugin = plugin;

        subcommands.put("create", new CommandWorldCreate(plugin));
        subcommands.put("list", new CommandWorldList(plugin));
        subcommands.put("delete", new CommandWorldDelete(plugin));
        subcommands.put("join", new CommandWorldJoin(plugin));
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
            sender.sendMessage(Component.text("Unknown command for world \"" + subcommandKey + "\"."));
            sendUsage(sender);
            return true;
        }

        String[] subcommandArgs = CommandUtils.popFirst(args);

        return subcommand.onCommand(sender, command, label, subcommandArgs);
    }

    @Override public void sendUsage(@NotNull CommandSender sender) {
        sender.sendMessage(Component.text("World usage!"));
    }

    @Override public String getUsage() {
        return "/uhc world - Commands for creating / deleting worlds";
    }
}
