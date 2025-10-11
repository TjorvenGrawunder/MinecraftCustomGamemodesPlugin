package de.tjorven.customGamemodes.utils;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.List;

public class SuggestionTools {
    public static Collection<String> generateCollectionSearchSuggestions(Collection<String> collection, String arg) {
        return collection.stream()
                .filter(item -> item.toLowerCase().startsWith(arg.toLowerCase()))
                .toList();
    }

    public static List<String> emptySuggestions(CommandSender sender, String[] strings) {
        return List.of();
    }
}
