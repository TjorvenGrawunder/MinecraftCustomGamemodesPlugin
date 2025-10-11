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
import org.bukkit.util.BlockIterator;

import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.StreamSupport;

import static de.tjorven.customGamemodes.CustomGamemodes.plugin;

public class WorldEventListener implements Listener {

    public static class ThreeDimensionalBlockIterator implements Iterator<Location> {
        private final Location current;
        private final int maxX;
        private final int maxY;
        private final int maxZ;
        private final int minX;
        private final int minZ;

        public ThreeDimensionalBlockIterator(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
            this.current = new Location(world, minX, minY, minZ);
            this.maxX = maxX;
            this.maxY = maxY;
            this.maxZ = maxZ;
            this.minX = minX;
            this.minZ = minZ;
        }


        @Override
        public boolean hasNext() {
            return current.getBlockX() < maxX && current.getBlockY() < maxY && current.getBlockZ() < maxZ;
        }

        @Override
        public Location next() {
            Location toReturn = current.clone();
            current.add(1, 0, 0);
            if (current.getBlockX() >= maxX) {
                current.setX(minX);
                current.add(0, 0, 1);
                if (current.getBlockZ() >= maxZ) {
                    current.setZ(minZ);
                    current.add(0, 1, 0);
                }
            }
            return toReturn;
        }
    }

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
            searchRadius = 3 * 16 / 2; // 3 chunks in Nether
        } else {
            searchRadius = 17 * 16 / 2; // 17 chunks in Overworld
        }

        ThreeDimensionalBlockIterator iterator = new ThreeDimensionalBlockIterator(targetWorld,
                targetLocation.getBlockX() - searchRadius, targetWorld.getMinHeight(), targetLocation.getBlockZ() - searchRadius,
                targetLocation.getBlockX() + searchRadius, targetWorld.getLogicalHeight(), targetLocation.getBlockZ() + searchRadius);

        val portalLocation = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false).filter(loc2 -> {
            Block block = loc2.getBlock();
            return block.getType().name().contains("PORTAL");
        }).min(Comparator.comparingDouble(loc2 -> loc2.distanceSquared(targetLocation))).or(() -> Optional.of(genNetherPortal(targetLocation))).get();

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
     * <a href="https://minecraft.wiki/w/Tutorial:Nether_portals">Tutorial</a>
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

        // Height ist beim suchen nach platz egal.
        // Nur wenn kein Platz gefunden wird,
        // also bei einem erzwungenen generieren eines Portals wird
        // "between 70, and 10 below the world height (i.e. 118 for the Nether or 246 for the Overworld)" gewählt.
        int maxHeight = world.getLogicalHeight() - 10;
        int minHeight = world.getMinHeight() + 40;
        Location portalLoc = searchForPortalSpace(x, y, z, world, searchRadius, maxHeight, minHeight, 3, 4);

        if (portalLoc == null) {
            portalLoc = searchForPortalSpace(x, y, z, world, searchRadius, maxHeight, minHeight, 1, 4);
        }

        if (portalLoc == null) {
            // Fallback: Just use the original location, but clamp y to valid range
            portalLoc = new Location(world, x, Math.min(maxHeight - 60, maxHeight), z);
            genAirBulb(portalLoc, 1, 1, 1, 2, 4, 1);
        }

        // Create a simple 4x5 Nether Portal frame
        for (int dx = -1; dx <= 2; dx++) {
            for (int dy = 0; dy <= 4; dy++) {
                if (dx == -1 || dy == 0 || dx == 2 || dy == 4) {
                    world.getBlockAt(portalLoc.getBlockX() + dx, portalLoc.getBlockY() + dy, portalLoc.getBlockZ()).setType(Material.OBSIDIAN);
                } else {
                    world.getBlockAt(portalLoc.getBlockX() + dx, portalLoc.getBlockY() + dy, portalLoc.getBlockZ()).setType(Material.AIR);
                }
            }
        }
        Location finalPortalLoc = portalLoc;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            world.getBlockAt(finalPortalLoc.getBlockX(), finalPortalLoc.getBlockY() + 1, finalPortalLoc.getBlockZ()).setType(Material.FIRE);
        }, 1L);
        return new Location(world, portalLoc.getBlockX() + 0.5, portalLoc.getBlockY() + 1, portalLoc.getBlockZ() + 0.5);
    }

    private static Location searchForPortalSpace(int x, int y, int z, World world, int searchRadius, int maxHeight, int minHeight, int width, int length) {
        ThreeDimensionalBlockIterator iterator = new ThreeDimensionalBlockIterator(world,
                x - searchRadius, minHeight, z - searchRadius,
                x + searchRadius, maxHeight, z + searchRadius);

        // 1. Filter: 3x4 ebene solide Blöcke finden.
        // 2. Filter: Alle darüberliegenden Blöcke müssen Air sein.
        // Sortieren nach Distanz zum originalen Zielort.
        val location = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED),
                        false).filter(loc -> {
                    // 1. Filter: 3x4 ebene solide Blöcke finden.
                    for (int dx = 0; dx < width; dx++)
                        for (int dz = 0; dz < length; dz++) {
                            val test_loc = loc.clone().add(dx, 0, dz);
                            boolean isSolid = test_loc.getBlock().getType().isSolid();
                            if (!isSolid)
                                return false;
                        }
                    return true;
                })
                .filter(loc -> {
                    // 2. Filter: Alle darüberliegenden Blöcke müssen Air sein.
                    for (int dy = 1; dy <= 4; dy++)
                        for (int dx = 0; dx < width; dx++)
                            for (int dz = 0; dz < length; dz++) {
                                val test_loc = loc.clone().add(dx, dy, dz);
                                if (!test_loc.getBlock().getType().isAir())
                                    return false;
                            }
                    return true;
                }).min(Comparator.comparingDouble(loc -> loc.distanceSquared(new Location(world, x, y, z))));

        // Wenn noch nicht gefunden, auf selbe Art und Weise nach 4x1 bzw 1x4 Platz suchen.
        return location.orElse(null);
    }

    // Der Name der Funktion und Name des Parameter `isPortalBlock` impliziert die Funktion von `isPortalBlock`.
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

    private static void genAirBulb(Location loc, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        World world = loc.getWorld();
        int centerX = loc.getBlockX();
        int centerY = loc.getBlockY();
        int centerZ = loc.getBlockZ();

        ThreeDimensionalBlockIterator iterator = new ThreeDimensionalBlockIterator(world,
                centerX - minX, centerY - minY, centerZ - minZ,
                centerX + maxX, centerY + maxY, centerZ + maxZ);

        StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false)
                .forEach(l -> {
                    Block block = l.getBlock();
                    if (!block.getType().isAir()) {
                        block.setType(Material.AIR);
                    }
                });
    }
}
