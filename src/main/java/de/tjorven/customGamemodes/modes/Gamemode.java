package de.tjorven.customGamemodes.modes;

import de.tjorven.customGamemodes.CustomGamemodes;
import org.bukkit.Material;

public interface Gamemode {
    String getName();
    String getDescription();
    void start();
    void stop();
    Material getIconMaterial();
}
