package me.maki325.mc.plugins.uhc.api.border;

import org.bukkit.WorldBorder;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("UhcDamage")
public class Damage implements ConfigurationSerializable {

    final boolean enabled;
    final float amount;

    public Damage(boolean enabled, float amount) {
        this.enabled = enabled;
        this.amount = amount;
    }

    public Damage(Map<String, Object> map) {
        this.enabled = (boolean) map.get("enabled");
        this.amount = ((Double) map.get("amount")).floatValue();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("enabled", this.enabled);
        map.put("amount", this.amount);
        return map;
    }

    public static Damage deserialize(Map<String, Object> map) {
        return new Damage(map);
    }

    public void setupWorldBorder(WorldBorder worldBorder) {
        worldBorder.setDamageAmount(this.enabled ? this.amount : 0);
    }
}
