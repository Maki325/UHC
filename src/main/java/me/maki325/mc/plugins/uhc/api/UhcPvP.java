package me.maki325.mc.plugins.uhc.api;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("UhcPvP")
public class UhcPvP implements ConfigurationSerializable {
    public static final UhcPvP DEFAULT = new UhcPvP(new UhcTime("0t"), false);

    public UhcTime timeout;
    public boolean friendlyFire;

    public UhcPvP(UhcTime timeout, boolean friendlyFire) {
        this.timeout = timeout;
        this.friendlyFire = friendlyFire;
    }


    public UhcPvP(Map<String, Object> map) {
        this.timeout = new UhcTime((String) map.get("timeout"));
        this.friendlyFire = (boolean) map.get("friendlyFire");
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("timeout", this.timeout.time);
        map.put("friendlyFire", this.friendlyFire);
        return map;
    }

    public static UhcPvP deserialize(Map<String, Object> map) {
        return new UhcPvP(map);
    }
}
