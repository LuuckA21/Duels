package me.luucka.duels.arena.state;

import lombok.Getter;
import lombok.Setter;
import me.luucka.duels.DuelsPlugin;
import me.luucka.duels.arena.Arena;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class ArenaState implements Listener {

    @Getter
    @Setter
    private Arena arena;

    @Getter
    private CommonStateListener commonStateListener;

    public void onEnable(DuelsPlugin plugin) {
        commonStateListener = new CommonStateListener(plugin, arena);

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getServer().getPluginManager().registerEvents(commonStateListener, plugin);

    }

    public void onDisable(DuelsPlugin plugin) {
        HandlerList.unregisterAll(this);
        HandlerList.unregisterAll(commonStateListener);
    }

}
