package me.maki325.mc.plugins.uhc.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class PlayerUtils {

    public static Component getUsernameFromUUID(Plugin plugin, UUID id) {
        if(id.equals(new UUID(0, 0))) {
            return Component.text("Console");
        }

        OfflinePlayer player = plugin.getServer().getOfflinePlayer(id);
        if(player.getPlayer() == null) {
            if(player.getName() == null) {
                return Component.text("Unknown");
            }
            return Component.text(player.getName());
        }

        return player.getPlayer().displayName();
    }

}
