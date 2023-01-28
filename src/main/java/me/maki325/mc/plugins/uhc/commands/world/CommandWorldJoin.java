package me.maki325.mc.plugins.uhc.commands.world;

import me.maki325.mc.plugins.uhc.Uhc;
import me.maki325.mc.plugins.uhc.api.CommandUsage;
import me.maki325.mc.plugins.uhc.api.UhcWorld;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommandWorldJoin implements CommandUsage {

    Uhc plugin;

    public CommandWorldJoin(Uhc plugin) {
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
        if(!(sender instanceof Player))  {
            sender.sendMessage(Component.text("Need to be player!"));
            return true;
        }
        Player player = (Player) sender;

        String name = args[0];
        if("main".equals(name)) {
            player.teleport(new Location(Bukkit.getWorld("world"), 0, 0, 0));
            return true;
        }

        ArrayList<UhcWorld> worlds = plugin.getUhcConfig().worlds;

        for(UhcWorld world : worlds) {
            if(name.equals(world.name)) {
                player.teleport(new Location(world.getBukkitWorld(plugin), 0, 0, 0));
            }
        }

        sender.sendMessage(Component.text("Teleported to world!"));
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
