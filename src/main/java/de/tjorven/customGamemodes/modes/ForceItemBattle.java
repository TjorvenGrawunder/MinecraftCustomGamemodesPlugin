package de.tjorven.customGamemodes.modes;

import de.tjorven.customGamemodes.CustomGamemodes;
import de.tjorven.customGamemodes.exceptions.RoundNotOverException;
import de.tjorven.customGamemodes.timer.GameTimer;
import de.tjorven.customGamemodes.ui.ForceItemVisualizer;
import de.tjorven.customGamemodes.utils.GameStorage;
import de.tjorven.customGamemodes.teams.Team;
import de.tjorven.customGamemodes.teams.TeamStorage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ForceItemBattle implements Gamemode {
    private CustomGamemodes plugin;
    private long duration; // in minutes
    private GameTimer timer;
    private List<Team> standings;

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
        // Give each team a random item
        Random rand = new Random();
        possibleItems = possibleItems.stream().filter(item ->
                !ForceItemBattleExclude.excludedItems.contains(item) && !ForceItemBattleExclude.chestExclusiveItems.contains(item) &&
                        !ForceItemBattleExclude.endExclusiveItems.contains(item) &&
                        !item.name().endsWith("_SPAWN_EGG") && !item.name().contains("CORAL") &&
                        item.isItem() && !item.name().contains("PALE") && !item.name().contains("RESIN") &&
                        !item.name().contains("OXIDIZED") && !item.name().contains("WEATHERED") &&
                        !item.name().contains("POTTERY") && !item.name().contains("MUSIC") && !item.name().contains("COMMAND") &&
                        !item.name().contains("EXPOSED") && !item.name().contains("WAXED") && !item.name().contains("TRIM"))
                .collect(Collectors.toList());

        for (Team team : TeamStorage.getInstance().getTeams()) {
            ItemStack skipBarriers = new ItemStack(Material.BARRIER, team.getSkipsLeft());
            team.getBackpack().addItem(skipBarriers);
            team.setItems(possibleItems.get(rand.nextInt(possibleItems.size())));
        }

        timer = new GameTimer(duration);
        timer.start();

    }

    @Override
    public void stop() {
        if (timer != null) {
            timer.stop();
        }

        ForceItemVisualizer.stopRound();

        // Sort teams by lowest rounds completed to highest
        standings = TeamStorage.getInstance().getTeams().stream()
                .sorted(Comparator.comparingInt(Team::roundsCompleted).reversed())
                .collect(Collectors.toList());

        final Component broadcastMessage = MiniMessage.miniMessage().deserialize(
                "<green>Round is over! Teleporting players to spawn..."
        );

        Bukkit.broadcast(broadcastMessage);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(Bukkit.getWorlds().getFirst().getSpawnLocation());
        }
    }

    @Override
    public void shutdown() {
        GameStorage.setActiveGamemode(null);
        GameStorage.getGamemodes().remove(this);
        TeamStorage.getInstance().clearTeams();
    }

    @Override
    public boolean hasSkips() {
        return true;
    }

    @Override
    public Material getIconMaterial() {
        return Material.CHAIN;
    }

    @Override
    public GameTimer getGameTimer() {
        return timer;
    }

    @Override
    public List<Team> getStandings() throws RoundNotOverException {
        if (standings == null) {
            throw new RoundNotOverException("Round is not over yet!");
        }
        return standings;
    }

}
