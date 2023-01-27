package me.maki325.mc.plugins.uhc.api.border.shrink;

import me.maki325.mc.plugins.uhc.Uhc;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class BorderShrink implements ConfigurationSerializable {
    boolean enabled;
    String type;

    public BorderShrink(boolean enabled, String type) {
        this.enabled = enabled;
        this.type = type;
    }


    public BorderShrink(Map<String, Object> map) {
        this.enabled = (boolean) map.get("enabled");
        this.type = (String) map.get("type");
    }

    @Override public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("enabled", this.enabled);
        map.put("type", this.type);
        return map;
    }

    public abstract void setupWorldBorder(Uhc plugin, WorldBorder worldBorder);
}
