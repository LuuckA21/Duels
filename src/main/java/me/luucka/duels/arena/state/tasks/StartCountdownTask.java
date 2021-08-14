package me.luucka.duels.arena.state.tasks;

import lombok.AllArgsConstructor;
import me.luucka.duels.DuelsPlugin;
import me.luucka.duels.arena.Arena;
import me.luucka.duels.arena.state.ActiveGameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@AllArgsConstructor
public class StartCountdownTask extends BukkitRunnable {

    private final DuelsPlugin plugin;
    private final Arena arena;
    private int secondsUntilStart;

    @Override
    public void run() {
        if (secondsUntilStart <= 0) {
            arena.setState(new ActiveGameState(), plugin);
            cancel();
            return;
        }

        for (UUID uuid : arena.getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            player.sendTitle("&aStarting in: " + secondsUntilStart + "...", "", 1, 1, 1);
        }

        secondsUntilStart--;
    }
}
