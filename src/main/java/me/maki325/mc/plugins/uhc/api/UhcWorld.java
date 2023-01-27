package me.maki325.mc.plugins.uhc.api;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SerializableAs("UhcWorld")
public class UhcWorld implements ConfigurationSerializable {

    public final String name;
    public final UUID id;

    public UhcWorld(String name, UUID id) {
        this.name = name;
        this.id = id;
    }

    public UhcWorld(Map<String, Object> map) {
        this.name = (String) map.get("name");
        this.id = UUID.fromString((String) map.get("id"));
    }

    @Override public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.id.toString());
        map.put("name", this.name);
        return map;
    }

    public org.bukkit.World getBukkitWorld(Plugin plugin) {
        org.bukkit.World world = Bukkit.getWorld(this.id);
        if(world != null) return world;

        world = new WorldCreator(name, new NamespacedKey(plugin, name)).createWorld();
        if(world != null) return world;

        return null;
    }

    public static UhcWorld deserialize(Map<String, Object> map) {
        return new UhcWorld(map);
    }
}
