package de.tjorven.customGamemodes.CustomWorlds;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.List;

@Getter
public class WorldGroup {

    private static class NetherPortalLink{
        public Location netherLocation;
        public Location overworldLocation;
        public NetherPortalLink(Location netherLocation, Location overworldLocation){
            this.netherLocation = netherLocation;
            this.overworldLocation = overworldLocation;
        }
    }

    private final World overworld;
    private final World nether;
    private final World end;

    private List<NetherPortalLink> links;

    public WorldGroup(World overworld, World nether, World end) {
        this.overworld = overworld;
        this.nether = nether;
        this.end = end;
    }

    public void deleteAllWorlds(){
        deleteWorld(overworld);
        deleteWorld(nether);
        deleteWorld(end);
    }

    public void addNetherPortalLink(Location netherLocation, Location overworldLocation){
        links.add(new NetherPortalLink(netherLocation, overworldLocation));
    }

    public Location getNetherLocation(Location overworldLocation){
        for (NetherPortalLink link : links) {
            if (link.overworldLocation.equals(overworldLocation)) {
                return link.netherLocation;
            }
        }
        return null;
    }

    public Location getOverworldLocation(Location netherLocation){
        for (NetherPortalLink link : links) {
            if (link.netherLocation.equals(netherLocation)) {
                return link.overworldLocation;
            }
        }
        return null;
    }

    private void deleteWorld(World world) {
        if (world == null) return;
        Bukkit.unloadWorld(world, false);
        File folder = world.getWorldFolder();
        deleteFolderRecursively(folder);
    }

    private void deleteFolderRecursively(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) deleteFolderRecursively(f);
                else f.delete();
            }
        }
        folder.delete();
    }
}
