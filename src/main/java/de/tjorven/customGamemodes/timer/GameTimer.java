package de.tjorven.customGamemodes.timer;

import de.tjorven.customGamemodes.CustomGamemodes;
import de.tjorven.customGamemodes.ui.ForceItemVisualizer;
import de.tjorven.customGamemodes.utils.GameStorage;
import io.papermc.paper.util.Tick;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static de.tjorven.customGamemodes.CustomGamemodes.plugin;

public class GameTimer {
    private BukkitTask timerTask;
    private BukkitTask shutdownTask;
    private long durationInMinutes;
    private BukkitScheduler scheduler;
    private boolean running;
    @Getter
    private long remainingTime;

    public GameTimer(long durationInMinutes) {
        this.scheduler = Bukkit.getScheduler();
        this.durationInMinutes = durationInMinutes;
    }

    public void start() {
        long startTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        timerTask = scheduler.runTaskTimerAsynchronously(plugin, () -> {
            long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
            long elapsedTime = currentTime - startTime;
            remainingTime = durationInMinutes * 60 - elapsedTime;
            ForceItemVisualizer.updateTimer(remainingTime);
        }, 0, Tick.tick().fromDuration(Duration.ofSeconds(1)));

        shutdownTask = scheduler.runTaskLater(plugin, () -> {
            timerTask.cancel();
            final Component broadcastMessage = MiniMessage.miniMessage().deserialize(
                    "<red>Time is up! Stopping the current game mode..."
            );
            Bukkit.broadcast(broadcastMessage);
            GameStorage.getActiveGamemode().stop();
        }, Tick.tick().fromDuration(Duration.ofMinutes(durationInMinutes)));

        running = true;
    }

    public void stop() {
        if (running) {
            if (timerTask != null) {
                timerTask.cancel();
            }
            if (shutdownTask != null) {
                shutdownTask.cancel();
            }
            running = false;
        }
    }

}
