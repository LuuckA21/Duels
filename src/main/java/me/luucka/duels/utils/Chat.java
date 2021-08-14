package me.luucka.duels.utils;

import me.luucka.duels.DuelsPlugin;
import me.luucka.lcore.utils.ColorTranslate;
import org.bukkit.command.CommandSender;

public class Chat {

    public static void messageP(CommandSender sender, String msg) {
        sender.sendMessage(ColorTranslate.translate(DuelsPlugin.yamlManager.cfg("messages").getString("prefix") + msg));
    }

    public static void message(CommandSender sender, String msg) {
        sender.sendMessage(ColorTranslate.translate(msg));
    }

}
