package me.maki325.mc.plugins.uhc;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.maki325.mc.plugins.uhc.api.Team;
import me.maki325.mc.plugins.uhc.commands.CommandUhc;
import me.maki325.mc.plugins.uhc.config.UhcConfig;
import me.maki325.mc.plugins.uhc.config.UhcConfiguration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

public final class Uhc extends JavaPlugin implements Listener {

    UhcConfiguration configuration = new UhcConfiguration();
    File configFile = new File(getDataFolder(), "config.yml");

    @Override public void onEnable() {
        this.saveDefaultConfig();
        reloadUhcConfig();

        Objects.requireNonNull(this.getCommand("uhc")).setExecutor(new CommandUhc(this));

        getServer().getPluginManager().registerEvents(this, this);

    }

    @EventHandler public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ArrayList<Team> teams = getUhcConfig().teams;
        for (Team team : teams) {
            if (team.players.contains(player.getUniqueId())) {
                Component prefix = Component.text("[" + team.name + "]").color(TextColor.fromCSSHexString("#03b6fc"));

                player.displayName(Component.join(Component.text(" "), prefix, Component.text(player.getName())));
                return;
            }
        }
    }

    @EventHandler public void onMessage(AsyncChatEvent event) {
        event.renderer((source, sourceDisplayName, message, viewer) ->
            Component.join(Component.text(": "), sourceDisplayName, message));
    }

    public UhcConfig getUhcConfig() {
        return configuration.config;
    }

    public void saveUhcConfig() {
        try {
            configuration.save(configFile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }

    public void reloadUhcConfig() {
        try {
            configuration = UhcConfiguration.loadConfiguration(configFile);
            configuration.setDefaults(getResource("config.yml"));
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Could not reload config from " + configFile, e);
        }
    }

}
