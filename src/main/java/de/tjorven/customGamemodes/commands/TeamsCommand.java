package de.tjorven.customGamemodes.commands;

import de.tjorven.customGamemodes.commands.commandtree.CommandNode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeamsCommand implements CommandExecutor, TabCompleter {
    CommandNode root = CommandNode.root("teams");

    public TeamsCommand() {
        root
            .sub("create", "<team_name>", "Creates a Team", (sender, args) -> {
                // Implementation for creating a team
            })
            .sub("join", "<team_name>", "Joins a Team", (sender, args) -> {
                // Implementation for joining a team
            })
            .sub("leave", "", "Leaves your current Team", (sender, args) -> {
                // Implementation for leaving a team
            })
            .sub("list", "", "Lists all Teams", (sender, args) -> {
                // Implementation for listing teams
            });
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
