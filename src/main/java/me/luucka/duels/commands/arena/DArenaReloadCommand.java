package me.luucka.duels.commands.arena;

import lombok.AllArgsConstructor;
import me.luucka.duels.DuelsPlugin;
import me.luucka.duels.utils.Chat;
import me.luucka.duels.utils.Message;
import me.luucka.lcore.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

@AllArgsConstructor
public class DArenaReloadCommand extends SubCommand {

    private final DuelsPlugin plugin;

    @Override
    public String name() {
        return "reload";
    }

    @Override
    public String description() {
        return "Reload plugin";
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
        plugin.reloadAll();
        Chat.messageP(sender, Message.RELOAD);
    }

    @Override
    public List<String> getSubcommandArgs(CommandSender sender, String[] args) {
        return null;
    }
}
