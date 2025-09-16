package de.tjorven.customGamemodes.modes;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class ForceItemBattleExclude {
    public static List<Material> excludedItems = List.of(
            // schon vorhanden
            Material.AIR,
            Material.BEDROCK,
            Material.BARRIER,
            Material.COMMAND_BLOCK,
            Material.REPEATING_COMMAND_BLOCK,
            Material.CHAIN_COMMAND_BLOCK,
            Material.STRUCTURE_BLOCK,
            Material.STRUCTURE_VOID,
            Material.JIGSAW,
            Material.DEBUG_STICK,

            // Spawn Eggs (alle!) – hier nur Beispiel, musst du ggf. per Code abfragen:
            // Material.ZOMBIE_SPAWN_EGG, Material.CREEPER_SPAWN_EGG, etc.

            // Monster Spawner (Block)
            Material.SPAWNER,

            // Light Block (unsichtbare Lichtquelle)
            Material.LIGHT,

            // Player Head mit bestimmten Texturen (manuell gesetzt)
            Material.PLAYER_HEAD,

            // Command-Items
            Material.KNOWLEDGE_BOOK,

            // Nicht craftbare Schallplatten (Beispiele)
            Material.MUSIC_DISC_5,
            Material.MUSIC_DISC_11,
            Material.MUSIC_DISC_PIGSTEP,
            Material.MUSIC_DISC_OTHERSIDE,
            Material.MUSIC_DISC_RELIC

    );
}
