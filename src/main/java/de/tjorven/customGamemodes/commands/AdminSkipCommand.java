package de.tjorven.customGamemodes.commands;

import de.tjorven.customGamemodes.exceptions.NoMoreSkipsException;
import de.tjorven.customGamemodes.utils.Team;
import de.tjorven.customGamemodes.utils.TeamStorage;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class AdminSkipCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        Entity executor = commandSourceStack.getExecutor();
        if (executor instanceof Player player) {
            String teamName;
            if (args.length > 0) {
                teamName = args[0];
            } else {
                Component component = MiniMessage.miniMessage().deserialize(
                        "<red>You need to specify a team name!"
                );
                player.sendMessage(component);
                return;
            }
            Team team = TeamStorage.getInstance().getTeamByName(teamName);
            if (team != null) {

                team.rerollItem(true);

                final Component broadcastMessage = MiniMessage.miniMessage().deserialize(
                        "<red>Player <yellow>" + executor.getName() + " <red> skipped one item of team " + team.getName() + " <red>!"
                );

                Bukkit.broadcast(broadcastMessage);
            } else {
                Component component = MiniMessage.miniMessage().deserialize(
                        "<red>Team not found!"
                );
                player.sendMessage(component);
            }
        }
    }

    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args) {
        String[] teamNames = TeamStorage.getInstance().getTeams().stream().map(Team::getName).toArray(String[]::new);
        return BasicCommand.super.suggest(commandSourceStack, teamNames);
    }
}
