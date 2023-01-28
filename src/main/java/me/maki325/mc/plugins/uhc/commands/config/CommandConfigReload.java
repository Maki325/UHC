package me.maki325.mc.plugins.uhc.commands.config;

import me.maki325.mc.plugins.uhc.Uhc;
import me.maki325.mc.plugins.uhc.api.CommandUsage;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandConfigReload implements CommandUsage {

    Uhc plugin;

    public CommandConfigReload(Uhc plugin) {
        this.plugin = plugin;
    }

    @Override public boolean onCommand(
        @NotNull CommandSender sender,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    ) {
        plugin.reloadUhcConfig();
        sender.sendMessage(Component.text("Reloaded config!"));
        return true;
    }

    @Override public void sendUsage(@NotNull CommandSender sender) {

    }

    @Override public String getUsage() {
        return null;
    }
}
