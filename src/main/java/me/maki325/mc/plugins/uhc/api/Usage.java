package me.maki325.mc.plugins.uhc.api;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface Usage {
    void sendUsage(@NotNull CommandSender sender);

    String getUsage();
}
