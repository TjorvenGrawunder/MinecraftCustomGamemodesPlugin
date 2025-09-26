package de.tjorven.customGamemodes.commands;

import de.tjorven.customGamemodes.exceptions.NoMoreSkipsException;
import de.tjorven.customGamemodes.utils.Team;
import de.tjorven.customGamemodes.utils.TeamStorage;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SkipCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        Entity executor = commandSourceStack.getExecutor();
        if (executor instanceof Player player) {
            Team team = TeamStorage.getInstance().getTeam(player);
            if (team != null) {
                try {
                    Material returnItem = team.rerollItem();
                    player.give(new ItemStack(returnItem));
                    String name = team.getName();
                    Component component = MiniMessage.miniMessage().deserialize(
                            "<green>Team <yellow>" + name + " <green>has skipped <yellow>" +
                                    returnItem.name() + "<green>!"
                    );
                    team.getAudience().sendMessage(component);
                    for (Player p : team.getPlayers()) {
                        System.out.println(p == player);
                    }
                } catch (NoMoreSkipsException e) {
                    Component component = MiniMessage.miniMessage().deserialize(
                            "<red>You have no skips left!"
                    );
                    player.sendMessage(component);
                }
            } else {
                Component component = MiniMessage.miniMessage().deserialize(
                        "<red>You need to be in a team to use this command!"
                );
                player.sendMessage(component);
            }
        }
    }
}
