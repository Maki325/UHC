package me.maki325.mc.plugins.uhc.commands.team;

import me.maki325.mc.plugins.uhc.Uhc;
import me.maki325.mc.plugins.uhc.api.CommandUsage;
import me.maki325.mc.plugins.uhc.utils.CommandUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CommandTeam implements CommandUsage {

    Uhc plugin;
    public final HashMap<String, CommandUsage> subcommands = new HashMap<>();

    public CommandTeam(Uhc plugin) {
        this.plugin = plugin;

        subcommands.put("create", new CommandTeamCreate(plugin));
        subcommands.put("list", new CommandTeamList(plugin));
        subcommands.put("join", new CommandTeamJoin(plugin));
        subcommands.put("leave", new CommandTeamLeave(plugin));
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

    @Override public void sendUsage(@NotNull CommandSender sender) {
        sender.sendMessage(Component.text("Team usage!"));
    }

    @Override public String getUsage() {
        return "/uhc team - Commands for creating / deleting / joining / leaving teams";
    }
}
