package me.luucka.duels.arena;

import lombok.RequiredArgsConstructor;
import me.luucka.duels.DuelsPlugin;
import me.luucka.duels.utils.Chat;
import me.luucka.duels.utils.ConfigurationUtility;
import me.luucka.duels.utils.Message;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

@RequiredArgsConstructor
public class ArenaSetupManager implements Listener {

    private final DuelsPlugin plugin;
    private final ArenaManager arenaManager;

    private final Map<UUID, TemporaryArena> playerToTempArenaMap = new HashMap<>();
    private final List<UUID> waitingInput = new ArrayList<>();

    public void addToSetup(Player player, TemporaryArena temporaryArena) {
        if (inSetupMode(player)) {
            Chat.messageP(player, Message.ALREADY_IN_SETUP_MODE_ERROR);
            return;

        }

        arenaManager.getRollbackManager().save(player);
        player.setGameMode(GameMode.CREATIVE);
        playerToTempArenaMap.put(player.getUniqueId(), temporaryArena);


        for (String itemName : plugin.getConfig().getConfigurationSection("setup-item").getKeys(false)) {
            ConfigurationSection section = plugin.getConfig().getConfigurationSection("setup-item." + itemName);

            player.getInventory().addItem(ConfigurationUtility.createSetupItem(plugin, section));

        }

        Chat.messageP(player, Message.JOIN_SETUP_MODE.replace("%ARENA_NAME%", temporaryArena.getConfigName()));
    }

    public void removeToSetup(Player player) {
        if (!inSetupMode(player)) return;

        playerToTempArenaMap.remove(player.getUniqueId());
        arenaManager.getRollbackManager().restore(player, null);

        Chat.messageP(player, "&aExited setup!");
    }

    public boolean inSetupMode(Player player) {
        return playerToTempArenaMap.containsKey(player.getUniqueId());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!inSetupMode(event.getPlayer())) return;
        if (!event.hasItem()) return;
        if (!event.getItem().hasItemMeta()) return;

        Player player = event.getPlayer();

        TemporaryArena temporaryArena = playerToTempArenaMap.get(player.getUniqueId());
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

        NamespacedKey key = new NamespacedKey(plugin, "setup-value");
        PersistentDataContainer container = itemInMainHand.getItemMeta().getPersistentDataContainer();
        if (container.has(key, PersistentDataType.STRING)) {
            String sKey = container.get(key, PersistentDataType.STRING);
            if (sKey == null) return;
            if (sKey.equalsIgnoreCase("save")) {
                if (!arenaManager.saveArenaToConfig(temporaryArena.toArena())) {
                    Chat.messageP(player, Message.CANT_SAVE_ARENA);
                    return;
                }
                Chat.messageP(player, "&aSaved arena!");
                removeToSetup(player);


            } else if (sKey.equalsIgnoreCase("set-position-one")) {
                temporaryArena.setSpawnLocationOne(player.getLocation());
                Chat.messageP(player, "&aSave spawn location One");


            } else if (sKey.equalsIgnoreCase("set-position-two")) {
                temporaryArena.setSpawnLocationTwo(player.getLocation());
                Chat.messageP(player, "&aSave spawn location Two");


            } else if (sKey.equalsIgnoreCase("cancel")) {
                Chat.messageP(player, "&cCancelled setup!");
                removeToSetup(player);


            } else if (sKey.equalsIgnoreCase("set-display-name")) {
                waitingInput.add(player.getUniqueId());
                Chat.messageP(player, "&aWrite arena's display name. &cType Cancel to stop!");


            } else return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onChatInput(AsyncPlayerChatEvent event) {
        if (!inSetupMode(event.getPlayer())) return;
        if (!waitingInput.contains(event.getPlayer().getUniqueId())) return;

        event.setCancelled(true);

        Player player = event.getPlayer();

        TemporaryArena temporaryArena = playerToTempArenaMap.get(player.getUniqueId());

        if (event.getMessage().equalsIgnoreCase("cancel")) {
            Chat.message(player, "&cInput canceled!");
        } else {
            temporaryArena.setDisplayName(event.getMessage());
            Chat.messageP(player, "&aYou set &7" + temporaryArena.getConfigName() + "'s &adisplay name to " + event.getMessage());
        }

        waitingInput.remove(player.getUniqueId());
    }


}
