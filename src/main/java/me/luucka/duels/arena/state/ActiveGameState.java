package me.luucka.duels.arena.state;

import lombok.Getter;
import me.luucka.duels.DuelsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActiveGameState extends ArenaState {

    @Getter
    private List<UUID> alivePlayers;
    private DuelsPlugin plugin;
    private boolean isOver = false;

    @Override
    public void onEnable(DuelsPlugin plugin) {
        super.onEnable(plugin);

        this.plugin = plugin;

        alivePlayers = new ArrayList<>(getArena().getPlayers());

        int lastSpawnId = 0;

        for (UUID uuid : alivePlayers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) continue;

            if (lastSpawnId == 0) {
                player.teleport(getArena().getSpawnLocationOne());
                lastSpawnId = 1;
            } else {
                player.teleport(getArena().getSpawnLocationTwo());
                lastSpawnId = 0;
            }

            player.getInventory().setHelmet(
                    new ItemStack(Material.IRON_HELMET)
            );
            player.getInventory().setChestplate(
                    new ItemStack(Material.IRON_CHESTPLATE)
            );
            player.getInventory().setLeggings(
                    new ItemStack(Material.IRON_LEGGINGS)
            );
            player.getInventory().setBoots(
                    new ItemStack(Material.IRON_BOOTS)
            );
            player.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD));
        }

        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (alivePlayers.size() > 1 || isOver) return;

            isOver = true;

            if (alivePlayers.size() == 1) {
                UUID winnerUUID = alivePlayers.get(0);
                Player winner = Bukkit.getPlayer(winnerUUID);
                if (winner == null || !winner.isOnline()) {
                    getArena().sendMessage("&cGame Over. But winner could no be found!");
                } else {
                    getArena().sendMessage(winner.getDisplayName() + " &ahas WON!");
                }
            } else {
                getArena().sendMessage("&cNo alive players? Game Over");
            }

            plugin.getServer().getScheduler().runTaskLater(plugin, () -> getArena().setState(new WaitingArenaState(), plugin), 20L * 5L);
        }, 0L, 10L);

    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if (!getArena().isPlayerIn(player)) return;

        if (isOver) {
            event.setCancelled(true);
            return;
        }

        event.setCancelled(false);

        if (event.getFinalDamage() >= player.getHealth()) {
            // game over, other player is winner
            alivePlayers.remove(player.getUniqueId());

            AttributeInstance instance = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            instance.setBaseValue(20D);
            player.setGameMode(GameMode.SPECTATOR);
            getArena().sendMessage(player.getDisplayName() + "&cdied!");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (!getArena().isPlayerIn(event.getPlayer())) return;

        // call personal event?
        alivePlayers.remove(event.getPlayer().getUniqueId());
    }

}
