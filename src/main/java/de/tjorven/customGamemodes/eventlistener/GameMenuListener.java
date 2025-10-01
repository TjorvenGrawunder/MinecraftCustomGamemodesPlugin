package de.tjorven.customGamemodes.eventlistener;

import com.google.common.collect.ImmutableList;
import de.tjorven.customGamemodes.inventory.GameMenuInventory;
import de.tjorven.customGamemodes.inventory.ResultsInventory;
import de.tjorven.customGamemodes.modes.ForceItemBattle;
import de.tjorven.customGamemodes.modes.Gamemode;
import de.tjorven.customGamemodes.ui.ForceItemVisualizer;
import de.tjorven.customGamemodes.utils.GameStorage;
import de.tjorven.customGamemodes.utils.Team;
import de.tjorven.customGamemodes.utils.TeamStorage;
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
        if (!(e.getWhoClicked() instanceof Player p)) return;

        Inventory inv = e.getInventory();

        if (inv.getHolder() instanceof GameMenuInventory) {
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

                if (!TeamStorage.getInstance().isActive()){
                    TeamStorage.getInstance().makeSinglePlayerTeams(ImmutableList.copyOf(Bukkit.getOnlinePlayers()));
                }

                ForceItemVisualizer.startCountdown(5 , gamemode::start);

                p.closeInventory();
            } else if (plainName.contains("Exit Menu")) {
                p.closeInventory();
            }
        }

        if (inv.getHolder() instanceof ResultsInventory resultsInventory) {
            e.setCancelled(true);

            ItemStack clicked = e.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR) return;

            Component displayName = clicked.getItemMeta().customName();

            if (displayName == null) return;

            String name = PlainTextComponentSerializer.plainText().serialize(displayName);

            if (clicked.getType() == Material.GREEN_STAINED_GLASS_PANE && name.contains("Next Page")) {
                int index = resultsInventory.getInventoryIndex(inv);
                if (index < resultsInventory.getTotalPages() - 1) {
                    p.openInventory(resultsInventory.getInventoryAt(index + 1));
                }
            } else if (clicked.getType() == Material.RED_STAINED_GLASS_PANE && name.contains("Previous Page")) {
                int index = resultsInventory.getInventoryIndex(inv);
                if (index > 0) {
                    p.openInventory(resultsInventory.getInventoryAt(index - 1));
                }
            }
        }
    }


}
