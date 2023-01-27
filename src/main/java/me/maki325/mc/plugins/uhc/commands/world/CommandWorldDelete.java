package me.maki325.mc.plugins.uhc.commands.world;

import me.maki325.mc.plugins.uhc.Uhc;
import me.maki325.mc.plugins.uhc.api.CommandUsage;
import me.maki325.mc.plugins.uhc.api.UhcWorld;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommandWorldDelete implements CommandUsage {

    Uhc plugin;

    public CommandWorldDelete(Uhc plugin) {
        this.plugin = plugin;
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
        String name = args[0];

        ArrayList<UhcWorld> worlds = (ArrayList<UhcWorld>) plugin.getConfig().getList("worlds", new ArrayList<>());

        for(UhcWorld world : worlds) {
            if(name.equals(world.name)) {
                Bukkit.unloadWorld(world.name, false);

                world.getBukkitWorld(plugin).getWorldFolder().delete();

                sender.sendMessage(Component.text("Deleted world \"" + name + "\"!"));

                worlds.remove(world);
                plugin.getConfig().set("worlds", worlds);
                plugin.saveConfig();

                return true;
            }
        }

        sender.sendMessage(Component.text("No world with name \"" + name + "\"!"));

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
