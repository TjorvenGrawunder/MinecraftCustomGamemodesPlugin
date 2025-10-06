package de.tjorven.customGamemodes.eventlistener;

import de.tjorven.customGamemodes.CustomWorlds.WorldGroup;
import de.tjorven.customGamemodes.CustomWorlds.WorldStorage;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
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

    /**
     * Generate a simple Nether Portal at the given location. Follows Minecraft rules for Nether Portal placement.
     * <a href="https://minecraft.fandom.com/wiki/Tutorials/Nether_portals">Tutorial</a>
     * @param loc Location to generate the portal near
     * @return Location to spawn in the portal
     */
    private static Location genNetherPortal(Location loc) {
        //Find a suitable location and create a Nether Portal structure
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        // Find suitable space
        int searchRadius = 16;
        int maxHeight = world.getLogicalHeight() - 10;
        int minHeight = world.getMinHeight() + 40;
        // TODO: Always fails. Fix search algorithm
        Location portalLoc = searchForPortalSpace(x, y, z, world, searchRadius, maxHeight, minHeight, 3, 4);

        if (portalLoc == null) {
            System.out.println("Could not find suitable portal location with 3x4 area, trying 1x4 area");
            // TODO: Always fails. Fix search algorithm
            portalLoc = searchForPortalSpace(x, y, z, world, searchRadius, maxHeight, minHeight, 1, 4);
        }

        if (portalLoc == null) {
            System.out.println("Could not find suitable portal location with 1x4 area, using fallback");
            // Fallback: Just use the original location, but clamp y to valid range
            portalLoc = new Location(world, x, Math.min(maxHeight - 60, maxHeight), z);
        }

        // Create a simple 4x5 Nether Portal frame
        for (int dx = -1; dx <= 2; dx++) {
            for (int dy = 0; dy <= 4; dy++) {
                if (dx == -1 || dy == 0 || dx == 2 || dy == 4) {
                    world.getBlockAt(portalLoc.getBlockX() + dx, portalLoc.getBlockY() + dy, portalLoc.getBlockZ()).setType(Material.OBSIDIAN);
                } else {
                    // TODO: Does not create portal blocks, fix it
                    world.getBlockAt(portalLoc.getBlockX() + dx, portalLoc.getBlockY() + dy, portalLoc.getBlockZ()).setType(Material.NETHER_PORTAL);
                }
            }
        }
        return new Location(world, portalLoc.getBlockX() + 0.5, portalLoc.getBlockY() + 1, portalLoc.getBlockZ() + 0.5);
    }

    private static boolean checkAreaIsClear(Location loc, int width, int length, int height, Predicate<Block> filter) {
        World world = loc.getWorld();
        int startX = loc.getBlockX();
        int startY = loc.getBlockY();
        int startZ = loc.getBlockZ();

        for (int dx = 0; dx < width; dx++) {
            for (int dy = 0; dy < height; dy++) {
                for (int dz = 0; dz < length; dz++) {
                    val block = world.getBlockAt(startX + dx, startY + dy, startZ + dz);
                    if (!filter.test(block)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static Location searchForPortalSpace(int x, int y, int z, World world, int searchRadius, int maxHeight, int minHeight, int width, int length) {
        boolean foundLocation = false;
        for (int dx = -16; x < searchRadius && !foundLocation; x++) {
            for (int dz = -16; z < searchRadius && !foundLocation; z++) {
                int highY = y;
                int lowY = y;
                while (highY < maxHeight && lowY > minHeight) {
                    // Check if portal would be on ground
                    if (!checkAreaIsClear(new Location(world, x + dx, highY-1, z + dz), width, length, 1, WorldEventListener::isSolidBlock)) {
                        highY++;
                        lowY--;
                        continue;
                    }
                    // Check for 3x4 area
                    if (checkAreaIsClear(new Location(world, x + dx, highY, z + dz), width, length, 4, WorldEventListener::isAirBlock)) {
                        y = highY;
                        x += dx;
                        z += dz;
                        foundLocation = true;
                        break;
                    }
                    // Check if portal would be on ground
                    if (!checkAreaIsClear(new Location(world, x + dx, lowY-1, z + dz), 3, 4, 1, WorldEventListener::isSolidBlock)) {
                        highY++;
                        lowY--;
                        continue;
                    }
                    // Check for 3x4 area
                    if (checkAreaIsClear(new Location(world, x + dx, lowY, z + dz), 3, 4, 4, WorldEventListener::isAirBlock)) {
                        y = lowY;
                        x += dx;
                        z += dz;
                        foundLocation = true;
                        break;
                    }

                    highY++;
                    lowY--;
                }
            }
        }
        return foundLocation ? new Location(world, x + 0.5, y + 1, z + 0.5) : null;
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

    private static boolean isAirBlock(Block block) {
        return block.getType().isAir();
    }

    private static boolean isSolidBlock(Block block) {
        return block.isSolid();
    }
}
