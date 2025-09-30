package de.tjorven.customGamemodes.modes;

import de.tjorven.customGamemodes.CustomGamemodes;
import de.tjorven.customGamemodes.exceptions.RoundNotOverException;
import de.tjorven.customGamemodes.timer.GameTimer;
import de.tjorven.customGamemodes.utils.Team;
import org.bukkit.Material;

import java.util.List;

public interface Gamemode {
    String getName();
    String getDescription();
    void start();
    // Init stopping the gamemode, but keep all data
    void stop();
    Material getIconMaterial();
    GameTimer getGameTimer();
    List<Team> getStandings() throws RoundNotOverException;
    // Will deactivate the gamemode and clear all data
    void shutdown();
}
