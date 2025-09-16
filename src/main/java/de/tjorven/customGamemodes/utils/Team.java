package de.tjorven.customGamemodes.utils;

import de.tjorven.customGamemodes.CustomGamemodes;
import de.tjorven.customGamemodes.ui.ForceItemVisualizer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

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
    private short skipsLeft = 3;

    public Team(List<Player> players, String name) {
        this.players = players;
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
    }

    public void updateItem(Material collectedItem) {
        if (collectedItem == items.getLast()) {
            Random rand = new Random();
            setItems(possibleItems.get(rand.nextInt(possibleItems.size())));
        }
    }

    public boolean rerollItem() {
        if (skipsLeft > 0) {
            Random rand = new Random();
            setItems(possibleItems.get(rand.nextInt(possibleItems.size())));
            skipsLeft--;
            return true;
        } else return false;
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
