package me.maki325.mc.plugins.uhc.api.border;

import me.maki325.mc.plugins.uhc.Uhc;
import me.maki325.mc.plugins.uhc.api.border.shrink.BorderShrink;
import me.maki325.mc.plugins.uhc.api.border.shrink.ConstantBorderShrink;
import me.maki325.mc.plugins.uhc.api.border.shrink.IntervalBorderShrink;
import me.maki325.mc.plugins.uhc.api.border.shrink.NoneBorderShrink;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("UhcBorder")
public class Border implements ConfigurationSerializable {

    public int startSize;
    public Damage damage;
    public BorderShrink shrink;


    public Border(int startSize, Damage damage, BorderShrink shrink) {
        this.startSize = startSize;
        this.damage = damage;
        this.shrink = shrink;
    }

    public Border(Map<String, Object> map) {
        this.startSize = (int) map.get("startSize");
        this.damage = (Damage) map.get("damage");
        this.shrink = parseBorderShrink((Map<String, Object>) map.get("shrink"));
    }

    public BorderShrink parseBorderShrink(Map<String, Object> map) {
        String type = (String) map.get("type");
        switch (type) {
            case "constant": return new ConstantBorderShrink(startSize, map);
            case "interval": return new IntervalBorderShrink(startSize, map);
            default: return new NoneBorderShrink();
        }
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("startSize", this.startSize);
        map.put("damage", this.damage);
        map.put("shrink", this.shrink.serialize());
        return map;
    }

    public static Border deserialize(Map<String, Object> map) {
        return new Border(map);
    }

    public void setupWorldBorder(Uhc plugin, WorldBorder worldBorder) {
        worldBorder.setSize(this.startSize);
        this.damage.setupWorldBorder(worldBorder);
        this.shrink.setupWorldBorder(plugin, worldBorder);
    }
}
