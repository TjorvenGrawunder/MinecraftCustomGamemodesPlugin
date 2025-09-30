package de.tjorven.customGamemodes.commands;

import de.tjorven.customGamemodes.exceptions.RoundNotOverException;
import de.tjorven.customGamemodes.inventory.ResultsInventory;
import de.tjorven.customGamemodes.utils.GameStorage;
import de.tjorven.customGamemodes.utils.Team;
import de.tjorven.customGamemodes.utils.TeamStorage;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class ResultsCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        Entity executor = commandSourceStack.getExecutor();
        if (executor == null) {
            return;
        }
        try {
            List<Team> standings = GameStorage.getActiveGamemode().getStandings();
            List<Team> allTeams = TeamStorage.getInstance().getTeams();
            Team last = standings.getFirst();
            ResultsInventory resultsInventory = new ResultsInventory(last.getName());
            // Show all Players Inventory
            for (Team team : allTeams) {
                for (Player player : team.getPlayers()) {
                    player.openInventory(resultsInventory.getInventory());
                }
            }

            resultsInventory.showResults(last.getItems());
            standings.remove(last);
        } catch (RoundNotOverException e) {
            Component component = MiniMessage.miniMessage().deserialize(
                    "<red>" + e.getMessage()
            );
            executor.sendMessage(component);
        }
    }
}
