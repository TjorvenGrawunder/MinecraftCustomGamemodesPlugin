package de.tjorven.customGamemodes.utils;

import de.tjorven.customGamemodes.ui.ForceItemVisualizer;
import org.bukkit.Material;

public class ForceItemItem {
    private final Material material;
    private boolean skipped;
    private String time;

    public ForceItemItem(Material material) {
        this.material = material;
        this.skipped = false;
        this.time = "";
    }

    public Material getMaterial() {
        return material;
    }

    public boolean isSkipped() {
        return skipped;
    }

    public String getTime() {
        return time;
    }

    public void setSkipped(boolean skipped) {
        this.skipped = skipped;
    }

    public void setTime(long time) {
        this.time = ForceItemVisualizer.getFormattedTime(time);
    }
}
