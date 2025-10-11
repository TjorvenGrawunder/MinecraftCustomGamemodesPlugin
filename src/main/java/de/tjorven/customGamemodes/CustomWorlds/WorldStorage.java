package de.tjorven.customGamemodes.CustomWorlds;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldStorage {
    @Getter
    private static Map<String, WorldGroup> worldGroups = new HashMap<>();

    public static void addWorldGroup(String name, WorldGroup worldGroup){
        worldGroups.put(name, worldGroup);
    }

    public static void clearWorldGroups(){
        worldGroups.clear();
    }

    public static void removeWorldGroup(String name){
        worldGroups.remove(name);
    }

    public static WorldGroup getWorldGroupByName(String name){
        return worldGroups.get(name);
    }

    public static void deleteAllWorlds(){
        worldGroups.forEach((k,v) -> v.deleteAllWorlds());
    }

    public static void deleteWorld(String name){
        WorldGroup group = worldGroups.get(name);
        if(group != null){
            group.deleteAllWorlds();
            worldGroups.remove(name);
        }
    }
}
