package me.luucka.duels.arena.state;

import me.luucka.duels.DuelsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class WaitingArenaState extends ArenaState {

    @Override
    public void onEnable(DuelsPlugin plugin) {
        super.onEnable(plugin);

        for (UUID uuid : getArena().getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;

            plugin.getArenaManager().getRollbackManager().restore(player, plugin);
        }

        getArena().getPlayers().clear();
    }
}
