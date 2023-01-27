package me.maki325.mc.plugins.uhc.commands;

import me.maki325.mc.plugins.uhc.api.CommandUsage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandHelp implements CommandUsage {
    @Override public boolean onCommand(
        @NotNull CommandSender sender,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    ) {

        return false;
    }

    @Override public void sendUsage(@NotNull CommandSender sender) {

    }

    @Override public String getUsage() {
        return "/uhc help [subcommand] - Prints usage, and if subcommand provided specifically about it";
    }


}
