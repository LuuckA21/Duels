package me.luucka.duels.commands.duels;

import lombok.AllArgsConstructor;
import me.luucka.duels.DuelsPlugin;
import me.luucka.duels.arena.Arena;
import me.luucka.duels.utils.Chat;
import me.luucka.duels.utils.Message;
import me.luucka.lcore.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class DuelsQuitCommand extends SubCommand {

    private final DuelsPlugin plugin;

    @Override
    public String name() {
        return "quit";
    }

    @Override
    public String description() {
        return "Quit from an Arena";
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

        Optional<Arena> playerArena = plugin.getArenaManager().findPlayerArena(player);
        if (playerArena.isEmpty()) {
            Chat.messageP(player, Message.NOT_IN_ARENA_ERROR);
            return;
        }

        Arena arena = playerArena.get();
        arena.removePlayer(player, plugin);
        Chat.messageP(player, Message.LEFT_ARENA);
    }

    @Override
    public List<String> getSubcommandArgs(CommandSender sender, String[] args) {
        return null;
    }
}
