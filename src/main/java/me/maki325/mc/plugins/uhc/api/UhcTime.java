package me.maki325.mc.plugins.uhc.api;

import net.kyori.adventure.text.Component;

public class UhcTime {

    public final String time;
    public final int timeInTicks;
    public final int value;
    public final TimeUnit unit;

    public UhcTime(String time) {
        this.time = time;

        int len = time.length() - 1;
        int value = 0;
        try {
            value = Integer.parseInt(time.substring(0, len));
        } catch (Exception ignored) {}
        this.value = value;
        this.unit = TimeUnit.getByUnit(time.charAt(len));
        this.timeInTicks = this.value * this.unit.tickMultiplier;
    }

    public UhcTime(int timeInTicks) {
        this(timeInTicks + "t");
    }

    public boolean isInteger(float value) {
        return value % 1 == 0;
    }

    public Component toComponent() {
        float value = this.value;
        TimeUnit unit = this.unit;
        while(unit.bigger != null) {
            if(value < unit.bigger.multiplier) {
                break;
            }
            unit = unit.bigger;
            value /= unit.multiplier;
        }
        String longUnit = unit.longUnit;
        if(value != 1) longUnit += "s";

        Component component = isInteger(value) ? Component.text((int) value) : Component.text(value);
        return component.append(Component.text(" ")).append(Component.text(longUnit));
    }
}
