package me.maki325.mc.plugins.uhc.commands.team;

import me.maki325.mc.plugins.uhc.Uhc;
import me.maki325.mc.plugins.uhc.api.CommandUsage;
import me.maki325.mc.plugins.uhc.api.Team;
import me.maki325.mc.plugins.uhc.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandTeamList implements CommandUsage {

    Uhc plugin;

    public CommandTeamList(Uhc plugin) {
        this.plugin = plugin;
    }

    @Override public boolean onCommand(
        @NotNull CommandSender sender,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    ) {
        List<Team> teams = (List<Team>) plugin.getConfig().getList("teams", new ArrayList<Team>());
        if(teams.isEmpty()) {
            sender.sendMessage(Component.text("No teams!"));
            return true;
        }

        sender.sendMessage(Component.text("Teams:"));
        for(Team team : teams) {
            sender.sendMessage(
                Component
                    .text("  - " + team.name + " | Owner: ")
                    .append(PlayerUtils.getUsernameFromUUID(plugin, team.owner))
            );
            team.players.forEach(player -> {
                sender.sendMessage(
                    Component
                        .text("     - ")
                        .append(PlayerUtils.getUsernameFromUUID(plugin, player))
                );
            });
        }

        return true;
    }

    @Override public void sendUsage(@NotNull CommandSender sender) {

    }

    @Override public String getUsage() {
        return null;
    }
}
