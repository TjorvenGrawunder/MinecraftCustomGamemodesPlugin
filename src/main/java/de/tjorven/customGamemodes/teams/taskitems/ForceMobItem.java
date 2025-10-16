package de.tjorven.customGamemodes.teams.taskitems;

import lombok.Getter;
import org.bukkit.entity.EntityType;

public class ForceMobItem extends ForceTaskItem{
    @Getter
    private EntityType type;

    public ForceMobItem(EntityType type) {
        this.type = type;
    }
}
