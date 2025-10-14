package de.tjorven.customGamemodes.utils;

import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.List;

import static de.tjorven.customGamemodes.CustomGamemodes.plugin;

public class CommandArguments {
    public static Collection<String> generateCollectionSearchSuggestions(Collection<String> collection, String arg) {
        return collection.stream()
                .filter(item -> item.toLowerCase().startsWith(arg.toLowerCase()))
                .toList();
    }

    public static List<String> listWorlds(CommandSender sender, String[] strings) {
        return plugin.getServer().getWorlds().stream().map(World::getName)
                .filter(name -> !name.endsWith("_nether") && !name.endsWith("_the_end"))
                .toList();
    }

    public static List<String> listTeams(CommandSender sender, String[] args) {
        return TeamStorage.getInstance().getTeams().stream().map(Team::getName).toList();
    }
}
