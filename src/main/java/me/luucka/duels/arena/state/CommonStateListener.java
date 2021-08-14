package me.luucka.duels.arena.state;

import lombok.RequiredArgsConstructor;
import me.luucka.duels.DuelsPlugin;
import me.luucka.duels.arena.Arena;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class CommonStateListener implements Listener {

    private final DuelsPlugin plugin;
    private final Arena arena;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!arena.isPlayerIn(event.getPlayer())) return;

        if (event.getBlock().getType() == Material.FIRE) {
            event.setCancelled(false);
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!arena.isPlayerIn(event.getPlayer())) return;

        if (event.getBlockPlaced().getType() == Material.FIRE) {
            arena.getChangedBlockMap().put(event.getBlockPlaced().getLocation(), event.getBlockPlaced().getType());
            event.setCancelled(false);
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!arena.isPlayerIn((Player) event.getEntity())) return;
        if (arena.getArenaState() instanceof ActiveGameState) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!arena.isPlayerIn(event.getPlayer())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        if (event.getEntityType() != EntityType.ARROW) return;
        if (!arena.isPlayerIn((Player) event.getEntity().getShooter())) return;

        event.getEntity().remove();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (!arena.isPlayerIn(event.getPlayer())) return;

        arena.removePlayer(event.getPlayer(), plugin);
    }

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!arena.isPlayerIn((Player) event.getEntity())) return;

        event.setCancelled(true);
    }
}
