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
            Material.TEST_BLOCK,
            Material.TEST_INSTANCE_BLOCK,

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
            Material.MUSIC_DISC_RELIC,

            // Silktouch-only items
            Material.AMETHYST_CLUSTER,
            Material.BUDDING_AMETHYST,
            Material.COPPER_ORE,
            Material.DEEPSLATE_COPPER_ORE,
            Material.DIAMOND_ORE,
            Material.DEEPSLATE_DIAMOND_ORE,
            Material.EMERALD_ORE,
            Material.DEEPSLATE_EMERALD_ORE,
            Material.GOLD_ORE,
            Material.DEEPSLATE_GOLD_ORE,
            Material.IRON_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.LAPIS_ORE,
            Material.DEEPSLATE_LAPIS_ORE,
            Material.REDSTONE_ORE,
            Material.DEEPSLATE_REDSTONE_ORE,
            Material.NETHER_QUARTZ_ORE,
            Material.NETHER_GOLD_ORE,
            Material.BEE_NEST,
            Material.ICE,
            Material.BLUE_ICE,
            Material.GRASS_BLOCK,
            Material.MYCELIUM,
            Material.PODZOL,
            Material.FARMLAND,
            Material.COBWEB,
            Material.CREAKING_HEART,
            Material.PACKED_ICE,
            Material.FROSTED_ICE,
            Material.INFESTED_COBBLESTONE,
            Material.INFESTED_STONE,
            Material.INFESTED_CRACKED_STONE_BRICKS,
            Material.INFESTED_CHISELED_STONE_BRICKS,
            Material.INFESTED_DEEPSLATE,
            Material.INFESTED_MOSSY_STONE_BRICKS,
            Material.INFESTED_STONE_BRICKS,
            Material.MELON,
            Material.MUSHROOM_STEM,
            Material.BROWN_MUSHROOM_BLOCK,
            Material.RED_MUSHROOM_BLOCK,
            Material.CRIMSON_NYLIUM,
            Material.WARPED_NYLIUM,
            Material.SCULK,
            Material.SCULK_VEIN,
            Material.SCULK_SHRIEKER,
            Material.TURTLE_EGG,
            Material.SNOW

    );
}
