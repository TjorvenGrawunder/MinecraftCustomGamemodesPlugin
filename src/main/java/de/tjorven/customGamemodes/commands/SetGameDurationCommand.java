package de.tjorven.customGamemodes.commands;

import de.tjorven.customGamemodes.CustomGamemodes;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.concurrent.Executor;

public class SetGameDurationCommand implements BasicCommand {

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        Entity executor = commandSourceStack.getExecutor();
        if (executor instanceof Player player) {
            if (args.length != 1) {
                Component component = MiniMessage.miniMessage().deserialize(
                        "<red>You need to specify a team name!"
                );
                player.sendMessage(component);
                return;
            }
            try {
                long duration = Long.parseLong(args[0]);
                if (duration <= 0) {
                    Component component = MiniMessage.miniMessage().deserialize(
                            "<red>The duration must be a positive number!"
                    );
                    player.sendMessage(component);
                    return;
                }
                CustomGamemodes.plugin.getConfig().set("gameDuration", duration);
                CustomGamemodes.plugin.saveConfig();
                Component component = MiniMessage.miniMessage().deserialize(
                        "<green>Set the game duration to <yellow>" + duration + " <green>minutes!"
                );
                Bukkit.broadcast(component);
            } catch (NumberFormatException e) {
                Component component = MiniMessage.miniMessage().deserialize(
                        "<red>The duration must be a valid number!"
                );
                player.sendMessage(component);
            }
        }

    }

}
