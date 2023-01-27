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

public class CommandTeamJoin implements CommandUsage {

    Uhc plugin;

    public CommandTeamJoin(Uhc plugin) {
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
        if(!(sender instanceof Player)) {
            sender.sendMessage(Component.text("Must be a player to join a team!"));
            return true;
        }
        Player player = (Player) sender;
        String name = args[0];

        List<Team> teams = (List<Team>) plugin.getConfig().getList("teams", new ArrayList<>());

        if(teams.stream().anyMatch(team -> team.players.contains(player.getUniqueId()))) {
            sender.sendMessage(Component.text("You are already in a team, please leave the current team to join another one."));
            return true;
        }

        for(Team team : teams) {
            if(name.equals(team.name)) {
                team.players.add(player.getUniqueId());

                this.plugin.getConfig().set("teams", teams);
                this.plugin.saveConfig();

                sender.sendMessage(Component.text("Joined the team \"" + name + "\"!"));
                return true;
            }
        }
        sender.sendMessage(Component.text("No team with the name \"" + name + "\" exist!"));

        return true;
    }

    @Override public void sendUsage(@NotNull CommandSender sender) {
        sender.sendMessage("Join team!");
    }

    @Override public String getUsage() {
        return null;
    }
}
