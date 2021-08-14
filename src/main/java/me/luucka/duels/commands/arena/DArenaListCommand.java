package me.luucka.duels.commands.arena;

import lombok.AllArgsConstructor;
import me.luucka.duels.DuelsPlugin;
import me.luucka.duels.utils.Chat;
import me.luucka.duels.utils.Message;
import me.luucka.lcore.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AllArgsConstructor
public class DArenaListCommand extends SubCommand {

    private final DuelsPlugin plugin;

    @Override
    public String name() {
        return "list";
    }

    @Override
    public String description() {
        return "Show Arena list";
    }

    @Override
    public String syntax() {
        return "/darena " + name();
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

        if (plugin.getArenaManager().getArenaList().size() == 0) {
            Chat.messageP(player, Message.NO_ARENAS_SETUP_YET_ERROR);
            return;
        }

        Chat.message(player, "&3&lArena list");
        plugin.getArenaManager().getArenaList().forEach(arena -> Chat.message(player, "&7\u27A4 " + arena.getDisplayName()));
    }

    @Override
    public List<String> getSubcommandArgs(CommandSender sender, String[] args) {
        return null;
    }
}
