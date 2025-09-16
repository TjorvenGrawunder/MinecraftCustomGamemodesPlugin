package de.tjorven.customGamemodes.modes;

import de.tjorven.customGamemodes.CustomGamemodes;

public abstract class Gamemode {
    public Gamemode(CustomGamemodes plugin, long duration) {
    }
    public abstract String getName();
    public abstract String getDescription();
    public abstract void start();
    public abstract void stop();
}
