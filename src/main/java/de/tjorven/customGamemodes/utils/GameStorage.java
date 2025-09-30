package de.tjorven.customGamemodes.utils;

import de.tjorven.customGamemodes.exceptions.RoundNotOverException;
import de.tjorven.customGamemodes.modes.Gamemode;

import java.util.ArrayList;
import java.util.List;

public class GameStorage {
    private static List<Gamemode> gamemodes = new ArrayList<>();
    private static Gamemode activeGamemode = null;

    public static List<Gamemode> getGamemodes() {
        return gamemodes;
    }

    public static void addGamemode(Gamemode gamemode) {
        gamemodes.add(gamemode);
    }

    public static void clearGamemodes() {
        gamemodes.clear();
    }

    public static void removeGamemode(Gamemode gamemode) {
        gamemodes.remove(gamemode);
    }

    public static Gamemode getActiveGamemode() {
        return activeGamemode;
    }
    public static void setActiveGamemode(Gamemode gamemode) {
        activeGamemode = gamemode;
    }

}
