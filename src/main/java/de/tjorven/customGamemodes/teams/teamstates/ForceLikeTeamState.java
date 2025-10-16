package de.tjorven.customGamemodes.teams.teamstates;

import de.tjorven.customGamemodes.eventlistener.event.ItemRerollEvent;
import de.tjorven.customGamemodes.teams.Team;
import de.tjorven.customGamemodes.teams.taskitems.ForceItemItem;
import de.tjorven.customGamemodes.teams.taskitems.ForceTaskItem;
import de.tjorven.customGamemodes.ui.ForceItemVisualizer;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class ForceLikeTeamState implements TeamState {
    @Getter
    private final List<ForceTaskItem> items = new ArrayList<>();
    @Getter
    private int skipsLeft;
    private Team correspondingTeam;

    protected ForceLikeTeamState(Team team, int totalSkips) {
        this.skipsLeft = totalSkips;
        this.correspondingTeam = team;
    }

    public void setItem(ForceTaskItem nextItem) {
        ForceItemVisualizer.updateForceItem(this.correspondingTeam, nextItem);

        items.add(nextItem);
        for (Player player : this.correspondingTeam.getPlayers()) {
            ItemRerollEvent event = new ItemRerollEvent(player, nextItem);
            event.callEvent();
        }
    }
}
