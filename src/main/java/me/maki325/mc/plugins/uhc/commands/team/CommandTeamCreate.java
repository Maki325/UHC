package me.maki325.mc.plugins.uhc.commands.team;

import me.maki325.mc.plugins.uhc.Uhc;
import me.maki325.mc.plugins.uhc.api.CommandUsage;
import me.maki325.mc.plugins.uhc.api.Team;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CommandTeamCreate implements CommandUsage {

    Uhc plugin;

    public CommandTeamCreate(Uhc plugin) {
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

        List<Team> teams = (List<Team>) plugin.getConfig().getList("teams", new ArrayList<>());

        if(teams.stream().anyMatch(team -> Objects.equals(team.name, name))) {
            sender.sendMessage(Component.text("Team with the name \"" + name + "\" already exists!"));
            return true;
        }

        UUID ownerId = new UUID(0, 0);
        boolean isPlayer = sender instanceof Player;
        if(isPlayer) {
            ownerId = ((Player) sender).getUniqueId();
        }

        UUID finalOwnerId = ownerId;
        if(teams.stream().anyMatch(team -> team.players.contains(finalOwnerId))) {
            sender.sendMessage(Component.text("You are already in a team, please leave the current team to create a new one."));
            return true;
        }

        Team team = new Team(name, ownerId);
        if(isPlayer) team.addPlayer(ownerId);

        teams.add(team);

        this.plugin.getConfig().set("teams", teams);
        this.plugin.saveConfig();

        sender.sendMessage(Component.text("Created team \"" + name + "\"!"));

        return true;
    }

    @Override public void sendUsage(@NotNull CommandSender sender) {

    }

    @Override public String getUsage() {
        return null;
    }
}
