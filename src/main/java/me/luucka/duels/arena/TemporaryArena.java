package me.luucka.duels.arena;

import lombok.Data;
import org.bukkit.Location;

@Data
public class TemporaryArena {

    private String configName;
    private String displayName;
    private Location spawnLocationOne;
    private Location spawnLocationTwo;

    public TemporaryArena(String configName) {
        this.configName = configName;
        this.displayName = null;
        this.spawnLocationOne = null;
        this.spawnLocationTwo = null;
    }

    public TemporaryArena(Arena arena) {
        this.configName = arena.getConfigName();
        this.displayName = arena.getDisplayName();
        this.spawnLocationOne = arena.getSpawnLocationOne();
        this.spawnLocationTwo = arena.getSpawnLocationTwo();
    }

    public Arena toArena() {
        if (spawnLocationOne == null || spawnLocationTwo == null) {
            return null;
        }

        if (displayName == null) displayName = configName;

        return new Arena(configName, displayName, spawnLocationOne, spawnLocationTwo);
    }

}
