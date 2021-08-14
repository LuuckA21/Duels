package me.luucka.duels.commands;

import me.luucka.duels.DuelsPlugin;
import me.luucka.duels.commands.arena.DArenaDeleteCommand;
import me.luucka.duels.commands.arena.DArenaListCommand;
import me.luucka.duels.commands.arena.DArenaReloadCommand;
import me.luucka.duels.commands.arena.DArenaSetupCommand;
import me.luucka.duels.utils.Chat;
import me.luucka.duels.utils.Message;
import me.luucka.lcore.commands.MainCommand;
import me.luucka.lcore.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DArenaCommand extends MainCommand {

    public DArenaCommand(DuelsPlugin plugin) {
        subCommands.add(new DArenaSetupCommand(plugin));
        subCommands.add(new DArenaDeleteCommand(plugin));
        subCommands.add(new DArenaListCommand(plugin));
        subCommands.add(new DArenaReloadCommand(plugin));
    }

    public final String PERM = "duels.admin";

    /*
        /arena <setup | delete | list>
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            Chat.messageP(sender, Message.MUST_BE_PLAYER_ERROR);
            return true;
        }

        if (!sender.hasPermission(PERM)) {
            Chat.messageP(sender, Message.NO_PERM_ERROR);
            return true;
        }

        if (args.length > 0) {
            String _subCmd = args[0];
            for (SubCommand sub : subCommands) {
                if (_subCmd.equalsIgnoreCase(sub.name())) {
                    sub.perform(sender, args);
                    return true;
                }
            }
        }
        Chat.messageP(sender, "&3&lArena Help");
        for (SubCommand sub : subCommands) {
            Chat.message(sender, "&7\u27A4 &3" + sub.syntax() + " &7- " + sub.description());
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            subCommands.forEach(subCmd -> {
                if (sender.hasPermission(PERM)) {
                    suggestions.add(subCmd.name());
                }
            });
            return suggestions;
        }
        return null;
    }
}
