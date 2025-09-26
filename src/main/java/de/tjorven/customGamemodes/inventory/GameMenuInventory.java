package de.tjorven.customGamemodes.inventory;

import de.tjorven.customGamemodes.CustomGamemodes;
import de.tjorven.customGamemodes.modes.GameModeRegistry;
import de.tjorven.customGamemodes.modes.Gamemode;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;


public class GameMenuInventory implements InventoryHolder {
    private final Inventory inventory;

    public GameMenuInventory(CustomGamemodes plugin) {
        this.inventory = plugin.getServer().createInventory(this, 27, Component.text("CustomGamemodes Menu"));

        ItemStack borderItem = new ItemStack(Material.BARRIER);
        ItemMeta borderMeta = borderItem.getItemMeta();
        if (borderMeta != null) {
            borderMeta.displayName(Component.text("Exit Menu"));
            borderItem.setItemMeta(borderMeta);
        }
        this.inventory.setItem(inventory.getSize()-1, borderItem);

        for (Gamemode mode : GameModeRegistry.gameModes){
            ItemStack item = new ItemStack(mode.getIconMaterial());
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text(mode.getName()));
                item.setItemMeta(meta);
            }
            this.inventory.addItem(item);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
