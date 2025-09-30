package de.tjorven.customGamemodes.utils;

import de.tjorven.customGamemodes.exceptions.RoundNotOverException;
import de.tjorven.customGamemodes.modes.Gamemode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class GameStorage {
    @Getter
    private static List<Gamemode> gamemodes = new ArrayList<>();
    @Setter
    @Getter
    private static Gamemode activeGamemode = null;

    public static void addGamemode(Gamemode gamemode) {
        gamemodes.add(gamemode);
    }

    public static void clearGamemodes() {
        gamemodes.clear();
    }

    public static void removeGamemode(Gamemode gamemode) {
        gamemodes.remove(gamemode);
    }

}
