package de.tjorven.customGamemodes.modes;

import com.google.common.collect.ImmutableList;
import de.tjorven.customGamemodes.CustomGamemodes;
import de.tjorven.customGamemodes.ui.ForceItemVisualizer;
import de.tjorven.customGamemodes.utils.GameStorage;
import de.tjorven.customGamemodes.utils.Team;
import de.tjorven.customGamemodes.utils.TeamStorage;
import io.papermc.paper.util.Tick;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.tjorven.customGamemodes.modes.ForceItemBattleExclude.excludedItems;

public class ForceItemBattle implements Gamemode {
    private CustomGamemodes plugin;
    private long duration; // in minutes

    BukkitTask timerTask;
    BukkitTask timerShutdownTask;

    public static List<Material> possibleItems = Stream.of(Material.values()).toList();

    public ForceItemBattle(){
        this.plugin = CustomGamemodes.plugin;
        this.duration = this.plugin.getConfig().getLong("gameDuration");
    }

    public ForceItemBattle(long duration) {
        this.plugin = CustomGamemodes.plugin;
        this.duration = duration;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getName() {
        return "Force Item Battle";
    }

    @Override
    public void start() {
        // List all players
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        // Give each team a random item
        Random rand = new Random();
        possibleItems = possibleItems.stream().filter(item ->
                !ForceItemBattleExclude.excludedItems.contains(item) &&
                        !item.name().endsWith("_SPAWN_EGG") && !item.name().contains("CORAL") &&
                        item.isItem() && !item.name().contains("PALE") && !item.name().contains("RESIN") &&
                        !item.name().contains("OXIDIZED") && !item.name().contains("WEATHERED"))
                .collect(Collectors.toList());
        if (TeamStorage.getInstance().isActive()){
            for (Team team : TeamStorage.getInstance().getTeams()) {
                team.setItems(possibleItems.get(rand.nextInt(possibleItems.size())));
            }
        } else {
            TeamStorage.getInstance().makeSinglePlayerTeams(ImmutableList.copyOf(players));
            for (Team team : TeamStorage.getInstance().getTeams()) {
                team.setItems(possibleItems.get(rand.nextInt(possibleItems.size())));
            }
        }

        BukkitScheduler scheduler = Bukkit.getScheduler();

        long startTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

        timerTask = scheduler.runTaskTimerAsynchronously(plugin, () -> {
            long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
            long elapsedTime = currentTime - startTime;
            long remainingTime = duration * 60 - elapsedTime;
            ForceItemVisualizer.updateTimer(remainingTime);
        }, 0, Tick.tick().fromDuration(Duration.ofSeconds(1)));

        timerShutdownTask = scheduler.runTaskLater(plugin, () -> {
            timerTask.cancel();
            final Component endMessage = MiniMessage.miniMessage().deserialize(
                    "<red>The game mode <yellow>" + this.getName() + " <red>has ended!"
            );
            Bukkit.broadcast(endMessage);
            GameStorage.removeGamemode(this);
            GameStorage.setActiveGamemode(null);
            this.stop();
        }, Tick.tick().fromDuration(Duration.ofMinutes(duration)));

    }

    @Override
    public void stop() {
        if (!timerTask.isCancelled()){
            timerTask.cancel();
            timerShutdownTask.cancel();
        }

        ForceItemVisualizer.stopRound();

        Team winningTeam = null;
        int highestScore = -1;
        for (Team team : TeamStorage.getInstance().getTeams()) {
            if (highestScore < team.roundsCompleted()) {
                highestScore = team.roundsCompleted();
                winningTeam = team;
            }
        }
        final Component broadcastMessage = MiniMessage.miniMessage().deserialize(
                "<green>Winning Team is <yellow> <green> " + winningTeam.getName() + " <green> with <yellow> " + highestScore + " <green> rounds completed!"
        );

        Bukkit.broadcast(broadcastMessage);

        TeamStorage.getInstance().clearTeams();

        GameStorage.setActiveGamemode(null);
    }

    @Override
    public Material getIconMaterial() {
        return Material.CHAIN;
    }


}
