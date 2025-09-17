package de.tjorven.customGamemodes.utils;

import de.tjorven.customGamemodes.CustomGamemodes;
import de.tjorven.customGamemodes.eventlistener.event.ItemRerollEvent;
import de.tjorven.customGamemodes.exceptions.NoMoreSkipsException;
import de.tjorven.customGamemodes.ui.ForceItemVisualizer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static de.tjorven.customGamemodes.modes.ForceItemBattle.possibleItems;

public class Team {
    private BossBar bossBar;
    private Audience audience;
    private final List<Player> players;
    private final List<Material> items = new ArrayList<>();
    private String name;
    private BackpackInventory backpack;
    private int skipsLeft = 5;

    public Team(List<Player> players, String name) {
        this.players = players;
        skipsLeft = skipsLeft * players.size();
        this.name = name;
        this.audience = Audience.audience(players);
        this.backpack = new BackpackInventory(CustomGamemodes.plugin);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Material getItems() {
        return items.getLast();
    }
    public void setItems(Material nextItem) {
        ForceItemVisualizer.updateForceItem(this, nextItem);
        items.add(nextItem);
        for (Player player : players) {
            ItemRerollEvent event = new ItemRerollEvent(player, nextItem);
            event.callEvent();
        }
    }

    public void updateItem(Material collectedItem) {
        if (collectedItem == items.getLast()) {
            Component component = MiniMessage.miniMessage().deserialize(
                    "<green>Team <yellow>" + name + " <green>has collected <yellow>" + items.getLast().name() + "<green>!"
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

    public Material rerollItem() {
        if (skipsLeft > 0) {
            updateItem(items.getLast());
            skipsLeft--;
            return items.get(items.size() - 2);
        } else throw new NoMoreSkipsException("No skips left!");
    }

    public int roundsCompleted() {
        return items.size() - 1;
    }

    public String getName() {
        return name;
    }
    public BossBar getBossBar() {
        return bossBar;
    }
    public void setBossBar(BossBar bossBar) {
        this.bossBar = bossBar;
    }
    public Audience getAudience() {
        return audience;
    }
    public Inventory getBackpack() {
        return backpack.getInventory();
    }
}
