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
            Material.COAL_ORE,
            Material.DEEPSLATE_COAL_ORE,
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
            Material.SNOW,
            Material.REINFORCED_DEEPSLATE,
            Material.DRAGON_EGG

    );

    public static List<Material> chestExclusiveItems = List.of(
            Material.PIGLIN_BANNER_PATTERN,
            Material.FLOW_BANNER_PATTERN,
            Material.GUSTER_BANNER_PATTERN,
            Material.ENCHANTED_GOLDEN_APPLE,
            Material.CHAINMAIL_CHESTPLATE,
            Material.CHAINMAIL_LEGGINGS,
            Material.CHAINMAIL_HELMET,
            Material.CHAINMAIL_BOOTS,
            Material.DIAMOND_HORSE_ARMOR,
            Material.GOLDEN_HORSE_ARMOR,
            Material.IRON_HORSE_ARMOR
    );

    public static List<Material> endExclusiveItems = new ArrayList<>(List.of(
            Material.ELYTRA,
            Material.DRAGON_HEAD,
            Material.DRAGON_BREATH,
            Material.DRAGON_WALL_HEAD,
            Material.END_CRYSTAL,
            Material.END_PORTAL_FRAME,
            Material.END_STONE,
            Material.END_STONE_BRICKS,
            Material.CHORUS_PLANT,
            Material.CHORUS_FLOWER,
            Material.POPPED_CHORUS_FRUIT,
            Material.PURPUR_BLOCK,
            Material.PURPUR_PILLAR,
            Material.PURPUR_SLAB,
            Material.PURPUR_STAIRS,
            Material.SHULKER_BOX,
            Material.WHITE_SHULKER_BOX,
            Material.ORANGE_SHULKER_BOX,
            Material.MAGENTA_SHULKER_BOX,
            Material.LIGHT_BLUE_SHULKER_BOX,
            Material.YELLOW_SHULKER_BOX,
            Material.LIME_SHULKER_BOX,
            Material.PINK_SHULKER_BOX,
            Material.GRAY_SHULKER_BOX,
            Material.LIGHT_GRAY_SHULKER_BOX,
            Material.CYAN_SHULKER_BOX,
            Material.PURPLE_SHULKER_BOX,
            Material.BLUE_SHULKER_BOX,
            Material.BROWN_SHULKER_BOX,
            Material.GREEN_SHULKER_BOX,
            Material.RED_SHULKER_BOX,
            Material.BLACK_SHULKER_BOX,
            Material.SHULKER_SHELL
    ));
}
