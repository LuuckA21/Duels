package me.luucka.duels.arena;

import lombok.Getter;
import me.luucka.duels.DuelsPlugin;
import me.luucka.duels.PlayerRollbackManager;
import me.luucka.duels.arena.state.WaitingArenaState;
import me.luucka.duels.utils.ConfigurationUtility;
import me.luucka.lcore.file.YamlFile;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArenaManager {
    @Getter
    private final DuelsPlugin plugin;

    public final YamlFile arenaFile;

    @Getter
    private final List<Arena> arenaList = new ArrayList<>();

    @Getter
    private final ArenaSetupManager arenaSetupManager;

    @Getter
    private final PlayerRollbackManager rollbackManager;

    public ArenaManager(DuelsPlugin plugin) {
        this.plugin = plugin;
        arenaFile = new YamlFile(this.plugin, "arenas");

        for (String arenaConfigName : arenaFile.getConfig().getKeys(false)) {
            ConfigurationSection section = arenaFile.getConfig().getConfigurationSection(arenaConfigName);

            String displayName = section.getString("display-name");
            Location spawnLocationOne = ConfigurationUtility.readLocation(section.getConfigurationSection("spawn-location-one"));
            Location spawnLocationTwo = ConfigurationUtility.readLocation(section.getConfigurationSection("spawn-location-two"));

            Arena arena = new Arena(arenaConfigName, displayName, spawnLocationOne, spawnLocationTwo);
            this.arenaList.add(arena);
        }

        this.arenaSetupManager = new ArenaSetupManager(plugin, this);
        this.rollbackManager = new PlayerRollbackManager();

        plugin.getServer().getPluginManager().registerEvents(this.arenaSetupManager, plugin);
    }

    public boolean saveArenaToConfig(Arena arena) {
        if (arena == null) return false;

        arenaList.removeIf(a -> a.getConfigName().equalsIgnoreCase(arena.getConfigName()));

        this.arenaList.add(arena);

        arenaFile.getConfig().set(arena.getConfigName(), null);

        arenaFile.getConfig().set(arena.getConfigName() + ".display-name", arena.getDisplayName());
        ConfigurationUtility.saveLocation(arena.getSpawnLocationOne(), arenaFile.getConfig().createSection(arena.getConfigName() + ".spawn-location-one"));
        ConfigurationUtility.saveLocation(arena.getSpawnLocationTwo(), arenaFile.getConfig().createSection(arena.getConfigName() + ".spawn-location-two"));

        arenaFile.save();

        return true;
    }

    public Optional<Arena> findArena(String arenaName) {
        return arenaList.stream().filter(arena -> arena.getConfigName().equalsIgnoreCase(arenaName)).findAny();
    }

    public Optional<Arena> findWaitingArena() {
        return arenaList.stream().filter(arena -> arena.getArenaState() instanceof WaitingArenaState).findAny();
    }

    public Optional<Arena> findPlayerArena(Player player) {
        return arenaList.stream().filter(arena -> arena.getPlayers().contains(player.getUniqueId())).findAny();
    }

    public void deleteArena(Arena arena) {
        arenaFile.getConfig().set(arena.getConfigName(), null);
        arenaFile.save();
        arenaList.remove(arena);
    }

}
