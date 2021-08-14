package me.luucka.duels.commands.duels;

import lombok.AllArgsConstructor;
import me.luucka.duels.DuelsPlugin;
import me.luucka.duels.arena.Arena;
import me.luucka.duels.utils.Chat;
import me.luucka.duels.utils.Message;
import me.luucka.lcore.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class DuelsJoinCommand extends SubCommand {

    private final DuelsPlugin plugin;

    @Override
    public String name() {
        return "join";
    }

    @Override
    public String description() {
        return "Join a Duel";
    }

    @Override
    public String syntax() {
        return "/duels " + name();
    }

    @Override
    public String permission() {
        return null;
    }

    @Override
    public boolean isOnlyPlayer() {
        return true;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        Optional<Arena> optionalArena = plugin.getArenaManager().findWaitingArena();
        if (optionalArena.isEmpty()) {
            Chat.messageP(player, Message.CANT_JOIN_ERROR);
            return;
        }

        if (plugin.getArenaManager().findPlayerArena(player).isPresent()) {
            Chat.messageP(player, Message.ALREADY_JOIN_ERROR);
            return;
        }

        optionalArena.get().addPlayer(player, plugin);
    }

    @Override
    public List<String> getSubcommandArgs(CommandSender sender, String[] args) {
        return null;
    }
}
