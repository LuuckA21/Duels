package me.luucka.duels;

import lombok.Getter;
import me.luucka.duels.arena.ArenaManager;
import me.luucka.duels.commands.DArenaCommand;
import me.luucka.duels.commands.DuelsCommand;
import me.luucka.lcore.file.YamlFile;
import me.luucka.lcore.manager.YamlManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DuelsPlugin extends JavaPlugin {

    @Getter
    private ArenaManager arenaManager;

    public static YamlManager yamlManager = new YamlManager();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        arenaManager = new ArenaManager(this);

        yamlManager.addFile(new YamlFile(this, "messages"));

        getCommand("darena").setExecutor(new DArenaCommand(this));
        getCommand("duels").setExecutor(new DuelsCommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reloadAll() {
        reloadConfig();
        yamlManager.reload();
    }
}
