package de.tjorven.customGamemodes.commands;

import de.tjorven.customGamemodes.utils.Team;
import de.tjorven.customGamemodes.utils.TeamStorage;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SkipCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        Entity executor = commandSourceStack.getExecutor();
        if (executor instanceof Player player) {
            Team team = TeamStorage.getInstance().getTeam(player);
            if (team != null) {
                boolean success = team.rerollItem();
                if (success) {
                    player.sendMessage("§aYou have successfully skipped your current item!");
                } else {
                    player.sendMessage("§cYou don't have enough skips left!");
                }
            } else {
                player.sendMessage("§cYou need to be in a team to use this command!");
            }
        }
    }
}
