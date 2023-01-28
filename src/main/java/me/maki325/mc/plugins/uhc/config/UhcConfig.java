package me.maki325.mc.plugins.uhc.config;

import me.maki325.mc.plugins.uhc.api.Team;
import me.maki325.mc.plugins.uhc.api.UhcPvP;
import me.maki325.mc.plugins.uhc.api.UhcWorld;
import me.maki325.mc.plugins.uhc.api.border.Border;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UhcConfig implements ConfigurationSerializable {

    public Border border = Border.DEFAULT;
    public UhcPvP pvp = UhcPvP.DEFAULT;
    public ArrayList<UhcWorld> worlds = new ArrayList<>();
    public ArrayList<Team> teams = new ArrayList<>();

    public UhcConfig() {}

    public UhcConfig(Map<String, Object> map) {
        this.border = getOrDefault(map, "border", Border.DEFAULT, Border::new);
        this.pvp = getOrDefault(map, "pvp", UhcPvP.DEFAULT, UhcPvP::new);
        this.teams = getOrDefaultArray(map, "teams", Team::new);
        this.worlds = getOrDefaultArray(map, "worlds", UhcWorld::new);
    }

    public <T> T getOrDefault(Map<?, ?> input, String key, T def, Function<Map<String, Object>, T> create) {
        try {
            if(input.containsKey(key)) {
                Object obj = input.get(key);
                if(obj instanceof Map) {
                    return create.apply((Map<String, Object>) obj);
                }
            }
        } catch (Exception e) {
            System.out.println("getOrDefault exception for " + key);
            e.printStackTrace();
        }
        return def;
    }

    public <T> ArrayList<T> getOrDefaultArray(Map<?, ?> input, String key, Function<Map<String, Object>, T> create) {
        try{
            if(input.containsKey(key)) {
                Object obj = input.get(key);
                if(obj instanceof List) {
                    return ((List<Map<String, Object>>) obj).stream().map(create).collect(Collectors.toCollection(ArrayList::new));
                }
            }
        } catch (Exception e) {
            System.out.println("getOrDefault exception for " + key);
            e.printStackTrace();
        }
        return new ArrayList<T>();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("border", this.border.serialize());
        map.put("pvp", this.pvp.serialize());
        map.put("teams", this.teams.stream().map(Team::serialize).collect(Collectors.toList()));
        map.put("worlds", this.worlds.stream().map(UhcWorld::serialize).collect(Collectors.toList()));
        return map;
    }
}
