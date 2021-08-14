package me.luucka.duels.arena;

import lombok.Data;
import lombok.Getter;
import me.luucka.duels.DuelsPlugin;
import me.luucka.duels.arena.state.ActiveGameState;
import me.luucka.duels.arena.state.ArenaState;
import me.luucka.duels.arena.state.StartingArenaState;
import me.luucka.duels.arena.state.WaitingArenaState;
import me.luucka.duels.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

@Data
public class Arena {

    private String configName;
    private String displayName;
    private Location spawnLocationOne;
    private Location spawnLocationTwo;
    private ArenaState arenaState;

    public Arena(String configName, String displayName, Location spawnLocationOne, Location spawnLocationTwo) {
        this.configName = configName;
        this.displayName = displayName;
        this.spawnLocationOne = spawnLocationOne;
        this.spawnLocationTwo = spawnLocationTwo;
        this.arenaState = new WaitingArenaState();
    }

    private final List<UUID> players = new ArrayList<>();

    @Getter
    private final Map<Location, Material> changedBlockMap = new HashMap<>();

    private final int MAX_PLAYERS = 2;

    public void setState(ArenaState arenaState, DuelsPlugin plugin) {
        if (this.arenaState.getClass() == arenaState.getClass()) return;

        this.arenaState.onDisable(plugin);

        this.arenaState = arenaState;
        this.arenaState.setArena(this);
        this.arenaState.onEnable(plugin);
    }

    public void addPlayer(Player player, DuelsPlugin plugin) {
        arenaState.setArena(this);

        players.add(player.getUniqueId());
        sendMessage(player.getDisplayName() + " &aJoin!");

        plugin.getArenaManager().getRollbackManager().save(player);
        player.setGameMode(GameMode.SURVIVAL);

        player.teleport(players.size() == 0 ? spawnLocationOne : spawnLocationTwo);

        if (players.size() == MAX_PLAYERS) {
            setState(new StartingArenaState(), plugin);
        }
    }

    public void removePlayer(Player player, DuelsPlugin plugin) {
        players.remove(player.getUniqueId());
        sendMessage(player.getDisplayName() + " &cQuit!");

        plugin.getArenaManager().getRollbackManager().restore(player, null);
        if (arenaState instanceof ActiveGameState) {
            ((ActiveGameState) arenaState).getAlivePlayers().remove(player.getUniqueId());
        } else if (arenaState instanceof StartingArenaState) {
            setState(new WaitingArenaState(), plugin);
        }
    }

    public boolean isPlayerIn(Player player) {
        return players.contains(player.getUniqueId());
    }

    public void sendMessage(String message) {
        for (UUID playerUUID : players) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) continue;

            Chat.message(player, getDisplayName() + " &7\u27A4 " + message);
        }
    }
}
