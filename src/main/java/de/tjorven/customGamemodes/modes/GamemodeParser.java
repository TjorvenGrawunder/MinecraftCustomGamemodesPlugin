package de.tjorven.customGamemodes.modes;

import de.tjorven.customGamemodes.CustomGamemodes;

import java.util.List;

public class GamemodeParser {
    public static Gamemode parse(String input, long duration) {
        return switch (input.toLowerCase()) {
            case "forceitembattle" -> new ForceItemBattle(duration);
            default -> null;
        };
    }

    public static List<String> getAvailableGamemodes() {
        return List.of("ForceItemBattle");
    }
}
