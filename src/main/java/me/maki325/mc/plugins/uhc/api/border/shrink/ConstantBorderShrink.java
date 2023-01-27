package me.maki325.mc.plugins.uhc.api.border.shrink;

import me.maki325.mc.plugins.uhc.Uhc;
import me.maki325.mc.plugins.uhc.api.UhcTime;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@SerializableAs("UhcConstantBorderShrink")
public class ConstantBorderShrink extends BorderShrink implements ConfigurationSerializable {

    public final UhcTime wait;
    public final float blocksPerSecond;

    public final int secondsToShrink;
    public final int endSize;

    public ConstantBorderShrink(boolean enabled, int startSize, int waitInTicks, float blocksPerSecond, int endSize) {
        super(enabled, "constant");

        this.wait = new UhcTime(waitInTicks);
        this.blocksPerSecond = blocksPerSecond;
        this.endSize = endSize;

        this.secondsToShrink = (int) Math.ceil((startSize - endSize) / blocksPerSecond);
    }

    public ConstantBorderShrink(int startSize, Map<String, Object> map) {
        super(map);

        this.wait = new UhcTime((String) map.get("wait"));
        this.blocksPerSecond = ((Double) map.get("blocksPerSecond")).floatValue();
        this.endSize = (int) map.get("endSize");

        this.secondsToShrink = (int) Math.ceil((startSize - endSize) / blocksPerSecond);
    }

    @Override public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("wait", this.wait.time);
        map.put("blocksPerSecond", this.blocksPerSecond);
        map.put("endSize", this.endSize);
        return map;
    }

    public void startShrink(WorldBorder worldBorder) {
        worldBorder.setSize(endSize, this.secondsToShrink);
    }

    public void setupWorldBorder(Uhc plugin, WorldBorder worldBorder) {
        if(!this.enabled) return;
        if(wait.timeInTicks == 0) {
            startShrink(worldBorder);
            return;
        }

        Bukkit
            .getScheduler()
            .runTaskLater(plugin, () -> startShrink(worldBorder), wait.timeInTicks);
    }
}
