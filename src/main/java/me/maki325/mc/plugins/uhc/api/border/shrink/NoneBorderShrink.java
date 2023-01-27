package me.maki325.mc.plugins.uhc.api.border.shrink;

import me.maki325.mc.plugins.uhc.Uhc;
import org.bukkit.WorldBorder;

public class NoneBorderShrink extends BorderShrink {

    public NoneBorderShrink() {
        super(false, "none");
    }

    @Override public void setupWorldBorder(Uhc plugin, WorldBorder worldBorder) {}
}
