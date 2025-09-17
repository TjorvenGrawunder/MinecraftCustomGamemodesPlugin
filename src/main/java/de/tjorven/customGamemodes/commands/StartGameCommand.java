package de.tjorven.customGamemodes.commands;

import de.tjorven.customGamemodes.CustomGamemodes;
import de.tjorven.customGamemodes.modes.Gamemode;
import de.tjorven.customGamemodes.modes.GamemodeParser;
import de.tjorven.customGamemodes.ui.ForceItemVisualizer;
import de.tjorven.customGamemodes.utils.GameStorage;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.util.Tick;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@NullMarked
public class StartGameCommand implements BasicCommand {
    private CustomGamemodes plugin;

    public StartGameCommand() {
        this.plugin = CustomGamemodes.plugin;
    }

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] strings) {

        if (GameStorage.getActiveGamemode() != null) GameStorage.getActiveGamemode().stop();

        if (strings.length == 0) {
            final Component broadcastMessage = MiniMessage.miniMessage().deserialize(
                    "<red>Please provide a game mode to start! Possible values are: <yellow>" +
                            String.join(", ", GamemodeParser.getAvailableGamemodes())
            );
            Bukkit.broadcast(broadcastMessage);
            return;
        }

        long duration;

        if (strings.length == 2) {
            duration = Long.parseLong(strings[1]);
        } else {
            duration = 10;
        }

        Gamemode gamemode = GamemodeParser.parse(strings[0], duration);
        if (gamemode == null) {
            final Component broadcastMessage = MiniMessage.miniMessage().deserialize(
                    "<red>Unknown game mode: <yellow>" + strings[0]
            );
            Bukkit.broadcast(broadcastMessage);
            return;
        }

        final Component broadcastMessage = MiniMessage.miniMessage().deserialize(
                "<green>Starting <yellow>" + gamemode.getName() + " <green>game mode!"
        );

        Bukkit.broadcast(broadcastMessage);

        GameStorage.addGamemode(gamemode);
        GameStorage.setActiveGamemode(gamemode);

        gamemode.start();
    }
}
