package me.maki325.mc.plugins.uhc.commands.world;

import me.maki325.mc.plugins.uhc.Uhc;
import me.maki325.mc.plugins.uhc.api.CommandUsage;
import me.maki325.mc.plugins.uhc.api.UhcWorld;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class CommandWorldCreate implements CommandUsage {

    Uhc plugin;

    public CommandWorldCreate(Uhc plugin) {
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

        if(worlds.stream().anyMatch(world -> name.equals(world.name))) {
            sender.sendMessage(Component.text("World with that name \"" + name + "\" already exists!"));
            return true;
        }

        UUID worldId = new WorldCreator(name, new NamespacedKey(this.plugin, name))
            .environment(World.Environment.NORMAL)
            .type(WorldType.NORMAL)
            .createWorld()
            .getUID();

        worlds.add(new UhcWorld(name, worldId));

        sender.sendMessage(Component.text("Created world \"" + name + "\" (\"" + worldId + "\")!"));

        plugin.getConfig().set("worlds", worlds);
        plugin.saveConfig();

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
