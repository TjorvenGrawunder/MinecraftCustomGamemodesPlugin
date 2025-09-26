package de.tjorven.customGamemodes.commands;

import de.tjorven.customGamemodes.CustomGamemodes;
import de.tjorven.customGamemodes.inventory.GameMenuInventory;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class OpenGameMenuCommand implements BasicCommand {
    GameMenuInventory gameMenuInventory;

    public OpenGameMenuCommand(){
        gameMenuInventory = new GameMenuInventory(CustomGamemodes.plugin);
    }

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        Entity executor = commandSourceStack.getExecutor();
        if (executor instanceof Player player) {
            Inventory menu = gameMenuInventory.getInventory();
            player.openInventory(menu);
        }
    }
}
