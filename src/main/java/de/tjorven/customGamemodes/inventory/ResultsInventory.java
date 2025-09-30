package de.tjorven.customGamemodes.inventory;

import de.tjorven.customGamemodes.utils.ForceItemItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.List;

import static de.tjorven.customGamemodes.CustomGamemodes.plugin;

public class ResultsInventory implements InventoryHolder {
    private final List<Inventory> resultInventoryList = new ArrayList<>();
    private final String teamName;

    public ResultsInventory(String name) {
        this.teamName = name;
        this.resultInventoryList.add(plugin.getServer().createInventory(this, 54, Component.text("Results - " + teamName)));
    }

    public void showResults(List<ForceItemItem> items) {
        for (int i = 0; i <= items.size() - 1; i++) {
            final int index = i;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                ForceItemItem item = items.get(index);
                Material mat = item.getMaterial();
                ItemStack itemStack = new ItemStack(mat);
                List<Component> lore = new ArrayList<>();
                if (!item.getTime().isEmpty()){
                    lore.add(Component.text("Collected at: " + item.getTime()));
                } else {
                    lore.add(Component.text("Not collected").color(TextColor.color(255, 0, 0)));
                }
                if (item.isSkipped())
                    lore.add(Component.text("<Skipped>").color(TextColor.color(255, 0, 0)));
                ItemMeta meta = itemStack.getItemMeta();
                meta.lore(lore);
                itemStack.setItemMeta(meta);
                addItemToResults(itemStack);
            }, 20L * index);
        }
    }

    public void addItemToResults(ItemStack item) {
        Inventory inv = resultInventoryList.getLast();
        if (inv.firstEmpty() != -1) {
                int nextEmpty = inv.firstEmpty();

                if (nextEmpty % 9 == 7) {
                    nextEmpty += 2;
                } else if (nextEmpty % 9 == 8) {
                    nextEmpty += 1;
                }
                if (nextEmpty >= inv.getSize()) {
                    ItemStack nextPanel = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
                    ItemMeta nextMeta = nextPanel.getItemMeta();
                    nextMeta.displayName(Component.text("<green>Next Page"));
                    nextPanel.setItemMeta(nextMeta);
                    ItemStack previousPanel = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                    ItemMeta previousMeta = previousPanel.getItemMeta();
                    previousMeta.displayName(Component.text("<red>Previous Page"));
                    previousPanel.setItemMeta(previousMeta);

                    int half = (inv.getSize()/2);
                    inv.setItem(half*9, nextPanel);
                    inv.setItem((half+1)*9, previousPanel);

                    Inventory newInv = plugin.getServer().createInventory(this, 54, Component.text("Results - " + teamName));
                    newInv.addItem(item);
                    resultInventoryList.add(newInv);
                    return;
                }
                inv.setItem(nextEmpty, item);
                return;
        }
        Inventory newInv = plugin.getServer().createInventory(this, 54);
        newInv.addItem(item);
        resultInventoryList.add(newInv);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return resultInventoryList.getFirst();
    }

    public int getInventoryIndex(Inventory inv) {
        return resultInventoryList.indexOf(inv);
    }

    public int getTotalPages() {
        return resultInventoryList.size();
    }

    public Inventory getInventoryAt(int index) {
        return resultInventoryList.get(index);
    }

}
