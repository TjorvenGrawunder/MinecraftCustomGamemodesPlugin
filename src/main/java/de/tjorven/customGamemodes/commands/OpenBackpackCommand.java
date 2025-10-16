package de.tjorven.customGamemodes.commands;

import de.tjorven.customGamemodes.teams.Team;
import de.tjorven.customGamemodes.teams.TeamStorage;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class OpenBackpackCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        Entity executor = commandSourceStack.getExecutor();
        if (executor instanceof Player player) {
            Team team = TeamStorage.getInstance().getTeam(player);
            if (team != null) {
                player.openInventory(team.getBackpack());
            } else {
                player.sendMessage("§cYou need to be in a team to use this command!");
            }
        }
    }
}
