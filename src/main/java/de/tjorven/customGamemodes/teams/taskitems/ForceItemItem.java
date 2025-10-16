package de.tjorven.customGamemodes.teams.taskitems;

import lombok.Getter;
import org.bukkit.Material;

public class ForceItemItem extends ForceTaskItem{
    @Getter
    private final Material material;

    public ForceItemItem(Material material) {
        this.material = material;
    }
}
