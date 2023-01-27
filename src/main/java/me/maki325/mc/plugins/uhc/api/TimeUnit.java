package me.maki325.mc.plugins.uhc.api;

public enum TimeUnit {
    TICK(1, 't', "tick", null),
    SECOND(20, 's', "second", TICK),
    MINUTE(60, 'm', "minute", SECOND),
    HOUR(60, 'h', "hour", MINUTE);

    public final int multiplier;
    public final int tickMultiplier;

    public final TimeUnit smaller;
    public TimeUnit bigger;
    public final char shortUnit;
    public final String longUnit;

    TimeUnit(int multiplier, char shortUnit, String longUnit, TimeUnit smaller) {
        this.shortUnit = shortUnit;
        this.longUnit = longUnit;
        this.smaller = smaller;
        this.bigger = null;
        if(smaller != null) smaller.bigger = this;
        this.multiplier = multiplier;
        this.tickMultiplier = multiplier * (smaller == null ? 1 : smaller.tickMultiplier);
    }

    public static TimeUnit getByUnit(char unit) {
        switch (unit) {
            case 'm': return TimeUnit.MINUTE;
            case 's': return TimeUnit.SECOND;
            case 'h': return TimeUnit.HOUR;
            case 't':
            default: return TimeUnit.TICK;
        }
    }
}
