package de.tjorven.customGamemodes.teams.taskitems;

import de.tjorven.customGamemodes.ui.ForceItemVisualizer;
import lombok.Getter;
import lombok.Setter;

public abstract class ForceTaskItem {
    @Setter @Getter
    private boolean skipped = false;
    @Getter
    private String time = "";

    public void setTime(long time) {
        this.time = ForceItemVisualizer.getFormattedTime(time);
    }

}
