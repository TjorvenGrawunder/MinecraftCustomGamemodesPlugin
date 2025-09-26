package de.tjorven.customGamemodes.eventlistener;

import de.tjorven.customGamemodes.modes.ForceItemBattle;
import de.tjorven.customGamemodes.modes.Gamemode;
import de.tjorven.customGamemodes.utils.GameStorage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class GameMenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getInventory();

        if (inv.contains(Material.BARRIER)) {
            e.setCancelled(true);

            ItemStack clicked = e.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR) return;

            Component name = clicked.getItemMeta().customName();

            if (name == null) return;

            String plainName = PlainTextComponentSerializer.plainText().serialize(name);

            if (plainName.contains("Force Item Battle")) {
                Gamemode gamemode = new ForceItemBattle();
                final Component broadcastMessage = MiniMessage.miniMessage().deserialize(
                        "<green>Starting <yellow>" + gamemode.getName() + " <green>game mode!"
                );

                Bukkit.broadcast(broadcastMessage);

                GameStorage.addGamemode(gamemode);
                GameStorage.setActiveGamemode(gamemode);

                gamemode.start();
                p.closeInventory();
            } else if (plainName.contains("Exit Menu")) {
                System.out.println("Closing menu for " + p.getName());
                p.closeInventory();
            }
        }
    }


}
