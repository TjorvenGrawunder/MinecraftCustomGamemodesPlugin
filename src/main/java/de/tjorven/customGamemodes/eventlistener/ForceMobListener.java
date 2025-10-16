package de.tjorven.customGamemodes.eventlistener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class ForceMobListener implements Listener {

    public void onEntityKill(EntityDeathEvent event) {
        Entity killer = event.getDamageSource().getCausingEntity();
        Entity killed = event.getEntity();
        if (!(killer instanceof Player p)) return;

        EntityType type = killed.getType();
    }

}
