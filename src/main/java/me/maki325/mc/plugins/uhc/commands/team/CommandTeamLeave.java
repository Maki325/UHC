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

public class CommandTeamLeave implements CommandUsage {

    Uhc plugin;

    public CommandTeamLeave(Uhc plugin) {
        this.plugin = plugin;
    }

    @Override public boolean onCommand(
        @NotNull CommandSender sender,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    ) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Component.text("Must be a player to leave a team!"));
            return true;
        }
        Player player = (Player) sender;

        ArrayList<Team> teams = plugin.getUhcConfig().teams;

        for(Team team : teams) {
            if(team.players.contains(player.getUniqueId())) {
                team.players.remove(player.getUniqueId());

                if(player.getUniqueId().equals(team.owner)) {
                    if(team.players.isEmpty()) {
                        teams.remove(team);
                    } else {
                        team.owner = team.players.get(0);
                    }
                }

                this.plugin.getUhcConfig().teams = teams;
                this.plugin.saveUhcConfig();

                sender.sendMessage(Component.text("Left the team \"" + team.name + "\"!"));
                return true;
            }
        }
        sender.sendMessage(Component.text("You are not in any team!"));

        return true;
    }

    @Override public void sendUsage(@NotNull CommandSender sender) {
        sender.sendMessage("Join team!");
    }

    @Override public String getUsage() {
        return null;
    }
}
