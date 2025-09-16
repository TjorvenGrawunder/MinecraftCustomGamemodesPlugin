package de.tjorven.customGamemodes.eventlistener;

import de.tjorven.customGamemodes.utils.Team;
import de.tjorven.customGamemodes.utils.TeamStorage;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CheckForceItemListener implements Listener {

    @EventHandler
    public void checkForceItem(EntityPickupItemEvent e) {
        LivingEntity entity = e.getEntity();
        if (entity instanceof Player player) {
            Material collectedItem = e.getItem().getItemStack().getType();
            Team team = TeamStorage.getInstance().getTeam(player);
            team.updateItem(collectedItem);
        }
    }

    @EventHandler
    public void checkForceItemChest(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        Team team = TeamStorage.getInstance().getTeam(player);
        Material needItem = team.getItems();
        if (player.getInventory().contains(needItem)) {
            team.updateItem(needItem);
        }
    }
}
