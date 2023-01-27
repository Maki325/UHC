package me.maki325.mc.plugins.uhc.api;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@SerializableAs("UhcTeam")
public class Team implements ConfigurationSerializable {

    public String name;
    public ArrayList<UUID> players;
    public UUID owner;
    public String prefix;
    public String suffix;

    public Team(String name) {
        this.name = name;
        this.players = new ArrayList<>();
        this.owner = new UUID(0, 0);
    }

    public Team(String name, UUID uuid) {
        this(name);
        this.owner = uuid;
    }

    public Team(Map<String, Object> map) {
        this.name = (String) map.get("name");
        this.owner = UUID.fromString((String) map.get("owner"));
        this.prefix = (String) map.get("prefix");
        this.suffix = (String) map.get("suffix");

        Object playersObj = map.get("players");
        if(!(playersObj instanceof List)) {
            this.players = new ArrayList<>();
        } else {
            List<String> players = (List<String>) playersObj;
            this.players = players.stream().map(UUID::fromString).collect(Collectors.toCollection(ArrayList::new));
        }
    }

    public void addPlayer(UUID uuid) {
        this.players.add(uuid);
    }

    public void addPlayer(Player player) {
        this.players.add(player.getUniqueId());
    }

    @Override public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("owner", this.owner.toString());
        map.put("name", this.name);
        map.put("players", this.players.stream().map(UUID::toString).collect(Collectors.toList()));
        map.put("prefix", this.prefix);
        map.put("suffix", this.suffix);
        return map;
    }

    public static Team deserialize(Map<String, Object> map) {
        return new Team(map);
    }
}
