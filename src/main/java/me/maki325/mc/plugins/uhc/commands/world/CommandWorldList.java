package me.maki325.mc.plugins.uhc.commands.world;

import me.maki325.mc.plugins.uhc.Uhc;
import me.maki325.mc.plugins.uhc.api.CommandUsage;
import me.maki325.mc.plugins.uhc.api.UhcWorld;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommandWorldList implements CommandUsage {

    Uhc plugin;

    public CommandWorldList(Uhc plugin) {
        this.plugin = plugin;
    }

    @Override public boolean onCommand(
        @NotNull CommandSender sender,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    ) {
        ArrayList<UhcWorld> worlds = (ArrayList<UhcWorld>) plugin.getConfig().getList("worlds", new ArrayList<>());

        if(worlds.isEmpty()) {
            sender.sendMessage(Component.text("No worlds!"));
            return true;
        }

        sender.sendMessage(Component.text("Worlds:"));
        worlds.forEach(world -> {
            sender.sendMessage(Component.text("  - " + world.name + " (" + world.id + ")"));
        });

        return true;
    }

    @Override
    public void sendUsage(@NotNull CommandSender sender) {

    }

    @Override
    public String getUsage() {
        return null;
    }
}
