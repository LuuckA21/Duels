package me.luucka.duels.arena.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.luucka.duels.DuelsPlugin;
import me.luucka.duels.arena.state.tasks.StartCountdownTask;

@RequiredArgsConstructor
public class StartingArenaState extends ArenaState {

    @Getter
    private StartCountdownTask startCountdownTask;

    @Override
    public void onEnable(DuelsPlugin plugin) {
        super.onEnable(plugin);

        this.startCountdownTask = new StartCountdownTask(plugin, getArena(), 5);
        this.startCountdownTask.runTaskTimer(plugin, 0, 20);
    }

    @Override
    public void onDisable(DuelsPlugin plugin) {
        super.onDisable(plugin);

        startCountdownTask.cancel();
    }
}
