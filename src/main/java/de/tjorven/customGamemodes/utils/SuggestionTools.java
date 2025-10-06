package de.tjorven.customGamemodes.utils;

import io.papermc.paper.command.brigadier.CommandSourceStack;

import java.util.Collection;

public class SuggestionTools {
    public static Collection<String> generateCollectionSearchSuggestions(Collection<String> collection, String arg) {
        return collection.stream()
                .filter(item -> item.toLowerCase().startsWith(arg.toLowerCase()))
                .toList();
    }
}
