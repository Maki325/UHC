package me.maki325.mc.plugins.uhc;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.maki325.mc.plugins.uhc.api.Team;
import me.maki325.mc.plugins.uhc.api.UhcPvP;
import me.maki325.mc.plugins.uhc.api.UhcWorld;
import me.maki325.mc.plugins.uhc.api.border.Border;
import me.maki325.mc.plugins.uhc.api.border.Damage;
import me.maki325.mc.plugins.uhc.commands.CommandUhc;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class Uhc extends JavaPlugin implements Listener {

    @Override public void onEnable() {
        ConfigurationSerialization.registerClass(Team.class, "UhcTeam");
        ConfigurationSerialization.registerClass(UhcWorld.class, "UhcWorld");
        ConfigurationSerialization.registerClass(Border.class, "UhcBorder");
        ConfigurationSerialization.registerClass(Damage.class, "UhcDamage");
        ConfigurationSerialization.registerClass(UhcPvP.class, "UhcPvP");

        this.saveDefaultConfig();

        this.getCommand("uhc").setExecutor(new CommandUhc(this));

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ArrayList<Team> teams = (ArrayList<Team>) getConfig().getList("teams", new ArrayList<>());
        for(Team team : teams) {
            if(team.players.contains(player.getUniqueId())) {
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

    @Override public void onDisable() {
        // Plugin shutdown logic
    }
}
