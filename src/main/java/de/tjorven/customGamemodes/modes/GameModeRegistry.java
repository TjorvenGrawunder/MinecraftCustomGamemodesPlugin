package de.tjorven.customGamemodes.modes;


import java.util.ArrayList;
import java.util.List;

public class GameModeRegistry {
    public static List<Gamemode> gameModes = new ArrayList<>();

    public static void register(Gamemode gamemode) {
        gameModes.add(gamemode);
    }
}
