package de.tjorven.customGamemodes.eventlistener;

import de.tjorven.customGamemodes.CustomWorlds.WorldGroup;
import de.tjorven.customGamemodes.CustomWorlds.WorldStorage;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class WorldEventListener implements Listener {

    @EventHandler
    public void onPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        World fromWorld = event.getFrom().getWorld();
        String fromWorldName = fromWorld.getName().replaceAll("_nether", "").replaceAll("_the_end", "");
        WorldGroup group = WorldStorage.getWorldGroupByName(fromWorldName);

        if (group == null) return;

        World targetWorld;

        switch (event.getTo().getWorld().getEnvironment()) {
            case NETHER -> targetWorld = group.getNether();
            case THE_END -> targetWorld = group.getEnd();
            default -> targetWorld = group.getOverworld();
        }

        if (targetWorld != null) {
            event.setCancelled(true);
            Location targetLocation = ConvertCoords(event.getTo(), targetWorld);
            player.teleport(targetLocation);
        }
    }

    private static Location ConvertCoords(Location loc, World targetWorld) {
        Location targetLocation = new Location(targetWorld, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        int searchRadius;
        if (targetWorld.getEnvironment() == World.Environment.NETHER) {
            searchRadius = 3 * 16; // 3 chunks in Nether
        } else {
            searchRadius = 17 * 16; // 17 chunks in Overworld
        }
        Location portalLocation = searchForPortal(targetLocation, blockLoc -> {
            blockLoc.getBlock();
            return blockLoc.getBlock().getType().name().contains("PORTAL");
        }, searchRadius);


        if (portalLocation == null) {
            portalLocation = genNetherPortal(targetLocation);
        }

        portalLocation.setX(portalLocation.getBlockX() + 0.5);
        portalLocation.setZ(portalLocation.getBlockZ() + 0.5);
        int floorBlock = portalLocation.getBlockY();
        boolean isFloorFound = false;
        while (!isFloorFound) {
            val block = portalLocation.getWorld().getBlockAt(portalLocation.getBlockX(), floorBlock - 1, portalLocation.getBlockZ());
            if (block.getType().isSolid()) {
                isFloorFound = true;
            } else {
                floorBlock--;
            }
        }
        portalLocation.setY(floorBlock);

        return portalLocation;
    }

    private static Location genNetherPortal(Location loc) {
        //TODO: Implement Nether Portal Generation Logic
        System.out.println("Generating Nether Portal at: " + loc);
        return loc;
    }

    private static Location searchForPortal(Location loc, Predicate<Location> isPortalBlock, int searchRadius) {
        for (int dx = -searchRadius; dx <= searchRadius; dx++) {
            for (int dy = -searchRadius; dy <= searchRadius; dy++) {
                for (int dz = -searchRadius; dz <= searchRadius; dz++) {
                    Location checkLoc = loc.clone().add(dx, dy, dz);
                    if (isPortalBlock.test(checkLoc)) {
                        return checkLoc;
                    }
                }
            }
        }
        return null;
    }
}
