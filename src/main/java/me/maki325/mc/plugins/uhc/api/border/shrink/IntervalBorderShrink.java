package me.maki325.mc.plugins.uhc.api.border.shrink;

import me.maki325.mc.plugins.uhc.Uhc;
import me.maki325.mc.plugins.uhc.api.TimeUnit;
import me.maki325.mc.plugins.uhc.api.UhcTime;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@SerializableAs("UhcConstantBorderShrink")
public class IntervalBorderShrink extends BorderShrink implements ConfigurationSerializable {

    public final UhcTime wait;
    public final int blocks;
    public final UhcTime shrinkTime;
    public final UhcTime interval;
    public final int endSize;

    public int currentSize;

    BukkitTask task;

    public IntervalBorderShrink(boolean enabled, int startSize, int blocks, int waitInTicks, int shrinkTimeInTicks, int intervalInTicks, int endSize) {
        super(enabled, "interval");

        this.wait = new UhcTime(waitInTicks);
        this.shrinkTime = new UhcTime(shrinkTimeInTicks);
        this.interval = new UhcTime(intervalInTicks);
        this.blocks = blocks;
        this.endSize = endSize;

        this.currentSize = startSize;
    }

    public IntervalBorderShrink(int startSize, Map<String, Object> map) {
        super(map);

        this.wait = new UhcTime((String) map.get("wait"));
        this.shrinkTime = new UhcTime((String) map.get("shrinkTime"));
        this.interval = new UhcTime((String) map.get("interval"));
        this.blocks = (int) map.get("blocks");
        this.endSize = (int) map.get("endSize");

        this.currentSize = startSize;
    }

    @Override public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("wait", this.wait.time);
        map.put("shrinkTime", this.shrinkTime.time);
        map.put("interval", this.interval.time);
        map.put("endSize", this.endSize);
        map.put("blocks", this.blocks);
        return map;
    }

    public void startShrink(WorldBorder worldBorder) {
        if(this.currentSize <= this.endSize) {
            if(task != null) task.cancel();
            return;
        }
        int newSize = this.currentSize - this.blocks * 2;
        if(newSize < endSize) {
            int seconds = this.shrinkTime.timeInTicks / TimeUnit.SECOND.tickMultiplier / (this.blocks * 2);
            worldBorder.setSize(endSize, seconds * (this.currentSize - endSize));

            this.currentSize = endSize - 1;
            if(task != null) task.cancel();
        } else {
            worldBorder.setSize(this.currentSize - this.blocks * 2, this.shrinkTime.timeInTicks / TimeUnit.SECOND.tickMultiplier);
            this.currentSize -= this.blocks * 2;
        }
    }

    @Override public void setupWorldBorder(Uhc plugin, WorldBorder worldBorder) {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> startShrink(worldBorder), this.wait.timeInTicks, this.interval.timeInTicks);
    }


}
