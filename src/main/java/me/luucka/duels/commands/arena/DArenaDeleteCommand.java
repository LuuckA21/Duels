package me.luucka.duels.commands.arena;

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
public class DArenaDeleteCommand extends SubCommand {

    private final DuelsPlugin plugin;

    @Override
    public String name() {
        return "delete";
    }

    @Override
    public String description() {
        return "Delete an arena";
    }

    @Override
    public String syntax() {
        return "/darena " + name() + " <arena>";
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

        if (args.length < 2) {
            Chat.messageP(player, "&cUsage: " + syntax());
            return;
        }

        String _arenaName = args[1];

        Optional<Arena> optionalArena = plugin.getArenaManager().findArena(_arenaName);
        if (optionalArena.isEmpty()) {
            Chat.messageP(player, Message.ARENA_NOT_EXISTS_ERROR);
            return;
        }

        plugin.getArenaManager().deleteArena(optionalArena.get());
        Chat.messageP(player, Message.DELETE_ARENA_SUCCESS.replace("%ARENA_NAME%", "&7" + optionalArena.get().getConfigName()));
    }

    @Override
    public List<String> getSubcommandArgs(CommandSender sender, String[] args) {
        return null;
    }
}
