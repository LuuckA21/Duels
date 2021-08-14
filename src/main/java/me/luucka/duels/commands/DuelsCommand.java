package me.luucka.duels.commands;

import me.luucka.duels.DuelsPlugin;
import me.luucka.duels.commands.duels.DuelsJoinCommand;
import me.luucka.duels.commands.duels.DuelsQuitCommand;
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

public class DuelsCommand extends MainCommand {

    public DuelsCommand(DuelsPlugin plugin) {
        subCommands.add(new DuelsJoinCommand(plugin));
        subCommands.add(new DuelsQuitCommand(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            Chat.messageP(sender, Message.MUST_BE_PLAYER_ERROR);
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
        Chat.messageP(sender, "&3&lDuels Help");
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
                suggestions.add(subCmd.name());
            });
            return suggestions;
        }
        return null;
    }
}
