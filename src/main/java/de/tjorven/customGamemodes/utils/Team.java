package de.tjorven.customGamemodes.utils;

import de.tjorven.customGamemodes.CustomGamemodes;
import de.tjorven.customGamemodes.eventlistener.event.ItemRerollEvent;
import de.tjorven.customGamemodes.exceptions.NoMoreSkipsException;
import de.tjorven.customGamemodes.inventory.BackpackInventory;
import de.tjorven.customGamemodes.ui.ForceItemVisualizer;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import static de.tjorven.customGamemodes.modes.ForceItemBattle.possibleItems;

public class Team {
    @Getter
    @Setter
    private BossBar bossBar;
    @Getter
    private final List<Player> players;
    @Getter
    private final List<ForceItemItem> items = new ArrayList<>();
    @Getter
    private String name;
    private BackpackInventory backpack;
    @Getter
    private int skipsLeft = 5;

    public Team(List<Player> players, String name) {
        this.players = players;
        skipsLeft = skipsLeft * players.size();
        this.name = name;
        this.backpack = new BackpackInventory(CustomGamemodes.plugin);
        CustomGamemodes.plugin.getLogger().log(Level.INFO, "Created team " + name + " with players: " + players);
    }

    public Material getCurrentItem() {
        return items.getLast().getMaterial();
    }

    public void setItems(Material nextItem) {
        ForceItemVisualizer.updateForceItem(this, nextItem);

        items.add(new ForceItemItem(nextItem));
        for (Player player : players) {
            ItemRerollEvent event = new ItemRerollEvent(player, nextItem);
            event.callEvent();
        }
    }

    public void updateItem(Material collectedItem) {
        if (collectedItem == items.getLast().getMaterial()) {
            items.getLast().setTime(GameStorage.getActiveGamemode().getGameTimer().getRemainingTime());
            Component component = MiniMessage.miniMessage().deserialize(
                    "<green>Team <yellow>" + name + " <green>has collected <yellow>" + items.getLast().getMaterial().name() + "<green>!"
            );

            for (Player player : players) {
                player.sendMessage(component);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            }
            Random rand = new Random();
            Material newItem;
            do {
                newItem = possibleItems.get(rand.nextInt(possibleItems.size()));
            } while (!newItem.isItem());
            setItems(newItem);
        }
    }

    public void rerollItem(boolean adminSkip){
        if (adminSkip) {
            items.removeLast();
            Random rand = new Random();
            Material newItem;
            do {
                newItem = possibleItems.get(rand.nextInt(possibleItems.size()));
            } while (!newItem.isItem());
            setItems(newItem);
        } else {
            rerollItem();
        }
    }

    public Material rerollItem() {
        if (skipsLeft > 0) {
            items.getLast().setSkipped(true);
            updateItem(items.getLast().getMaterial());
            skipsLeft--;
            return items.get(items.size() - 2).getMaterial();
        } else throw new NoMoreSkipsException("No skips left!");
    }

    public void replacePlayer(Player oldPlayer, Player newPlayer) {
        if (players.contains(oldPlayer)) {
            int index = players.indexOf(oldPlayer);
            players.set(index, newPlayer);
            ForceItemVisualizer.updateForceItem(this, items.getLast().getMaterial());
            Component component = MiniMessage.miniMessage().deserialize(
                    "<yellow>" + oldPlayer.getName() + " <green>has been replaced by <yellow>" + newPlayer.getName() + "<green> in team <yellow>" + name + "<green>!"
            );
            for (Player player : players) {
                player.sendMessage(component);
            }
        }
    }

    public int roundsCompleted() {
        return items.size() - 1;
    }

    public Audience getAudience() {
        return Audience.audience(players);
    }
    public Inventory getBackpack() {
        return backpack.getInventory();
    }
}
