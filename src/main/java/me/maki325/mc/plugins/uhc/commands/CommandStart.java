package me.maki325.mc.plugins.uhc.commands;

import me.maki325.mc.plugins.uhc.Uhc;
import me.maki325.mc.plugins.uhc.api.*;
import me.maki325.mc.plugins.uhc.api.border.Border;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandStart implements CommandUsage {

    TextColor GREEN = TextColor.color(84, 252, 84);
    TextColor YELLOW = TextColor.color(252, 168, 0);
    TextColor RED = TextColor.color(255, 0, 0);

    Random random = new Random();
    Uhc plugin;

    public CommandStart(Uhc plugin) {
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
        // /uhc start <world name>
        UhcWorld uhcWorld = getWorld(args[0]);
        if(uhcWorld == null) {
            sender.sendMessage(Component.text("No world with that name!"));
            return true;
        }

        World world = uhcWorld.getBukkitWorld(plugin);

        world.setGameRule(GameRule.NATURAL_REGENERATION, false);
        world.getWorldBorder().setCenter(0, 0);

        Border border = plugin.getUhcConfig().border;
        if(border == null) {
            sender.sendMessage(Component.text("No border info!"));
            return true;
        }

        int size = border.startSize;
        world.getWorldBorder().setSize(size);
        border.setupWorldBorder(plugin, world.getWorldBorder());

        ArrayList<Team> teams = plugin.getUhcConfig().teams;
        for(Team team : teams) {
            int x = random.nextInt(size) - size / 2;
            int z = random.nextInt(size) - size / 2;
            int y = 30;

            while(true) {
                while(!world.getBlockAt(x, y, z).isEmpty() && !world.getBlockAt(x, y + 1, z).isEmpty() && !world.getBlockAt(x, y + 2, z).isEmpty()) {
                    y++;
                }
                if(world.getBlockAt(x, y - 1, z).isLiquid()) {
                    x = random.nextInt(size) - size / 2;
                    z = random.nextInt(size) - size / 2;
                    y = 30;
                } else {
                    break;
                }
            }


            int finalX = x, finalY = y + 1, finalZ = z;
            team.players.forEach(uuid -> {
                Player player = Bukkit.getPlayer(uuid);
                if(player == null) return;
                player.teleport(new Location(world, finalX, finalY, finalZ));

                player.setHealth(19);
                player.setFoodLevel(20);
                player.setSaturation(5);
            });
        }

        UhcPvP pvp = plugin.getUhcConfig().pvp;
        setPvP(pvp, world);

        Map<UUID, Scoreboard> scoreboardMap = new HashMap<>();
        Component name = Component
            .text("UHC")
            .color(TextColor.fromCSSHexString("#ff0000"))
            .decorate(TextDecoration.BOLD);

        Bukkit.getOnlinePlayers().forEach(player -> {
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            scoreboard.registerNewObjective("UHC", "dummy", name);

            for(Team team : teams) {
                if(!team.players.contains(player.getUniqueId())) continue;
                org.bukkit.scoreboard.Team scoreboardTeam = scoreboard.registerNewTeam(team.name.length() > 16 ? team.name.substring(0, 16) : team.name);
                Component prefix = Component.text("[" + team.name + "] ").color(TextColor.fromCSSHexString("#03b6fc"));

                scoreboardTeam.setAllowFriendlyFire(pvp.friendlyFire);
                team.players.forEach(playerId -> {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerId);
                    scoreboardTeam.addEntry(offlinePlayer.getName());
                    if(offlinePlayer.getPlayer() != null) {
                        offlinePlayer.getPlayer().playerListName();

                        Component displayName = Component.join(Component.empty(), prefix, Component.text(offlinePlayer.getPlayer().getName()));
                        offlinePlayer.getPlayer().displayName(displayName);
                        offlinePlayer.getPlayer().playerListName(displayName);
                    }
                });

                scoreboardTeam.prefix(prefix);
            }

            Objective healthObjective = scoreboard.registerNewObjective("playerHealth", "health", Component.text("Player Health"));
            healthObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
            healthObjective.setRenderType(RenderType.HEARTS);

            scoreboardMap.put(player.getUniqueId(), scoreboard);
        });

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                Scoreboard scoreboard = scoreboardMap.get(player.getUniqueId());
                if(scoreboard == null) return;
                scoreboard.getObjective("UHC").unregister();

                Objective objective = scoreboard.registerNewObjective("UHC", "dummy", name);
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                objective.displayName(name);
                objective.getScore("Border:").setScore(15);
                objective.getScore(String.valueOf(world.getWorldBorder().getSize())).setScore(14);

                for(Team team : teams) {
                    if(team.players.contains(player.getUniqueId())) {
                        objective.getScore("Team:").setScore(13);
                        objective.getScore(team.name).setScore(12);

                        if(team.players.size() > 1) {
                            objective.getScore("").setScore(11);
                            objective.getScore("Teammates:").setScore(10);
                            int score = 9;
                            for(UUID playerId : team.players) {
                                if(player.getUniqueId().equals(playerId)) continue;
                                OfflinePlayer teammate = Bukkit.getOfflinePlayer(playerId);
                                String text = (teammate.getPlayer() == null ? ChatColor.RED : ChatColor.GREEN) + "●" + ChatColor.RESET + " " + teammate.getName();
                                String value = ChatColor.translateAlternateColorCodes('&', text);
                                /*String value = Component.join(Component.text(" "),
                                    Component.text("○").color(RED),
                                    Component.text(teammate.getName())
                                ).toString();*/
                                // String value = "○ " + teammate.getName();
                                objective.getScore(value).setScore(score);
                                score--;
                            }
                        }
                        break;
                    }
                }

                player.setScoreboard(scoreboard);
            });
        }, 0, 20);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for(Team team : teams) {
                team.players.forEach(uuid -> {
                    Player player = Bukkit.getPlayer(uuid);
                    if(player == null) return;
                    player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                    player.setFoodLevel(20);
                    player.setSaturation(5);
                });
            }
        }, 5);

        return true;
    }

    private void setPvP(UhcPvP pvp, World world) {
        if(pvp.timeout.timeInTicks > 0) {
            world.sendMessage(Component.join(Component.empty(),
                Component.text("PvP is "),
                Component
                    .text("disabled")
                    .decorate(TextDecoration.BOLD),
                Component.text("! It will be enabled in "),
                pvp.timeout.toComponent().color(TextColor.color(84, 252, 84)),
                Component.text(".")
            ));
            world.setPVP(false);

            start10sCounter(world, pvp.timeout.timeInTicks);
        }
        else {
            world.sendMessage(Component.join(Component.empty(),
                Component.text("PvP is "),
                Component
                    .text("enabled")
                    .color(TextColor.color(255, 0, 0))
                    .decorate(TextDecoration.BOLD),
                Component.text("!")
            ));
        }

        world.sendMessage(Component.join(Component.empty(),
            Component.text("Friendly fire is "),
            pvp.friendlyFire ? Component.text("enabled").color(RED) : Component.text("disabled").color(GREEN),
            Component.text("!")
        ));
    }

    @Override public void sendUsage(@NotNull CommandSender sender) {

    }

    @Override public String getUsage() {
        return null;
    }

    private UhcWorld getWorld(String worldName) {
        ArrayList<UhcWorld> worlds = plugin.getUhcConfig().worlds;

        for(UhcWorld world : worlds) {
            if(worldName.equals(world.name)) {
                return world;
            }
        }

        return null;
    }

    private void startSecondsTimer(World world, final int seconds) {
        if(seconds == 0) {
            world.sendMessage(Component.join(Component.empty(),
                Component.text("PvP is "),
                Component.text("enabled").color(RED).decorate(TextDecoration.BOLD),
                Component.text("!")
            ));
            return;
        }

        sendPvPStartsInSecondsMessage(world, seconds);
        Bukkit.getScheduler().runTaskLater(plugin, () -> startSecondsTimer(world, seconds - 1), TimeUnit.SECOND.tickMultiplier);
    }

    private void sendPvPStartsInSecondsMessage(World world, int seconds) {
        final int redStart = 3;

        TextColor color = seconds == 10 ? GREEN : seconds > redStart ? YELLOW : RED;
        world.sendMessage(Component.join(Component.empty(),
            Component.text("PvP starts in "),
            new UhcTime(seconds * TimeUnit.SECOND.tickMultiplier)
                .toComponent()
                .color(color)
                .decoration(TextDecoration.BOLD, seconds <= redStart),
            Component.text("!")
        ));
    }

    private void start10sCounter(World world, int ticksLeft) {
        int delay = ticksLeft - 10 * TimeUnit.SECOND.tickMultiplier;
        if(delay >= 0) {
            Bukkit.getScheduler().runTaskLater(
                plugin,
                () -> sendPvPStartsInSecondsMessage(world, 10),
                delay
            );
            Bukkit.getScheduler().runTaskLater(plugin, () -> startSecondsTimer(world, 5), delay + 5L * TimeUnit.SECOND.tickMultiplier);

            return;
        }
        delay = ticksLeft - 5 * TimeUnit.SECOND.tickMultiplier;
        if(delay >= 0) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> startSecondsTimer(world, 5), delay);
            return;
        }

        int secondsLeft = ticksLeft / TimeUnit.SECOND.tickMultiplier;
        if(secondsLeft <= 0) {
            startSecondsTimer(world, 0);
            return;
        }
        delay = ticksLeft - secondsLeft * TimeUnit.SECOND.tickMultiplier;
        Bukkit.getScheduler().runTaskLater(plugin, () -> startSecondsTimer(world, secondsLeft), delay);
    }

}
