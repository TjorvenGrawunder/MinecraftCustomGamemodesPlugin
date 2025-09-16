package de.tjorven.customGamemodes.utils;

import de.tjorven.customGamemodes.CustomGamemodes;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class BackpackInventory implements InventoryHolder {
    private final Inventory inventory;

    public BackpackInventory(CustomGamemodes plugin) {
        this.inventory = plugin.getServer().createInventory(this, 27);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
