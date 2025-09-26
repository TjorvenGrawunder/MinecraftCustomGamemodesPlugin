package de.tjorven.customGamemodes.eventlistener;

import de.tjorven.customGamemodes.eventlistener.event.ItemRerollEvent;
import de.tjorven.customGamemodes.utils.GameStorage;
import de.tjorven.customGamemodes.utils.Team;
import de.tjorven.customGamemodes.utils.TeamStorage;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CheckForceItemListener implements Listener {

    @EventHandler
    public void checkForceItem(EntityPickupItemEvent e) {
        if (GameStorage.getActiveGamemode() == null) return;
        LivingEntity entity = e.getEntity();
        if (entity instanceof Player player) {
            Material collectedItem = e.getItem().getItemStack().getType();
            Team team = TeamStorage.getInstance().getTeam(player);
            team.updateItem(collectedItem);
        }
    }

    @EventHandler
    public void checkForceItemChest(InventoryClickEvent e){
        if (GameStorage.getActiveGamemode() == null) return;

        Player player = (Player) e.getWhoClicked();
        Team team = TeamStorage.getInstance().getTeam(player);
        Material needItem = team.getItems();
        if (player.getInventory().contains(needItem)) {
            team.updateItem(needItem);
        }
    }

    @EventHandler
    public void checkForceItemCraftingTable(CraftItemEvent e) {
        if (GameStorage.getActiveGamemode() == null) return;

        Player player = (Player) e.getWhoClicked();
        Team team = TeamStorage.getInstance().getTeam(player);
        Material needItem = team.getItems();
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType().equals(needItem)) {
            team.updateItem(needItem);
        }
    }

    @EventHandler
    public void checkForceItemReroll(ItemRerollEvent e){
        if (GameStorage.getActiveGamemode() == null) return;

        Player player = (Player) e.getEntity();
        Team team = TeamStorage.getInstance().getTeam(player);
        Material needItem = team.getItems();
        if (player.getInventory().contains(needItem)) {
            team.updateItem(needItem);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Team team = TeamStorage.getInstance().getTeam(player);
        for (Player p : team.getPlayers()) {
            if (p.getUniqueId().equals(player.getUniqueId()) && p != player) {
                team.replacePlayer(p, player);
                break;
            }
        }
    }
}
