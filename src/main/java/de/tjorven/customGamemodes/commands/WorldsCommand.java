package de.tjorven.customGamemodes.commands;

import de.tjorven.customGamemodes.CustomGamemodes;
import de.tjorven.customGamemodes.CustomWorlds.WorldGroup;
import de.tjorven.customGamemodes.CustomWorlds.WorldStorage;
import de.tjorven.customGamemodes.commands.commandtree.CommandNode;
import de.tjorven.customGamemodes.utils.SuggestionTools;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

import static de.tjorven.customGamemodes.CustomGamemodes.plugin;

public class WorldsCommand implements CommandExecutor, TabCompleter {

    private final CommandNode root = CommandNode.root("worlds");

    public WorldsCommand() {
        root
            .sub("create", "<world_name>", "Creates a Team", this::createWorld)
            .sub("join", "<world_name>", "Joins a World", this::joinWorld, (sender, args) ->
                    plugin.getServer().getWorlds().stream().map(World::getName)
                            .filter(name -> !name.endsWith("_nether") && !name.endsWith("_the_end"))
                            .toList())
            .sub("delete", "<world_name>", "Deletes a World", this::deleteWorld,(sender, args) ->
                    plugin.getServer().getWorlds().stream().map(World::getName)
                            .filter(name -> !name.endsWith("_nether") && !name.endsWith("_the_end"))
                            .toList());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return root.handleExecute(sender, label, args);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return root.handleTabComplete(sender, args);
    }


    private void createWorld(CommandSender sender, String[] args) {
        if (!(sender instanceof Player p)) {
            System.err.println("Sender is not a Player");
            return;
        }
        String worldName = args[0];
        p.sendMessage(MiniMessage.miniMessage().deserialize("<green>Creating world <yellow>" + worldName + "<green>..."));

        WorldCreator worldCreator = WorldCreator.name(worldName);
        World overworld = p.getServer().createWorld(worldCreator);

        WorldCreator netherCreator = WorldCreator.name(worldName + "_nether").environment(World.Environment.NETHER);
        World nether = p.getServer().createWorld(netherCreator);

        WorldCreator endCreator = WorldCreator.name(worldName + "_the_end").environment(World.Environment.THE_END);
        World end = p.getServer().createWorld(endCreator);
        System.out.println(p.getServer().getWorlds());

        WorldStorage.addWorldGroup(worldName, new WorldGroup(overworld, nether, end));

        if (overworld != null) {
            p.teleport(overworld.getSpawnLocation());
            p.sendMessage(MiniMessage.miniMessage().deserialize("<green>Teleported to world <yellow>" + worldName + "<green>!"));
        } else {
            p.sendMessage(MiniMessage.miniMessage().deserialize("<red>Failed to create world <yellow>" + worldName + "<red>!"));
        }
    }

    private void joinWorld(CommandSender sender, String args[]) {
        if (!(sender instanceof Player p)) return;
        String worldName = args[0];
        World world = p.getServer().getWorld(worldName);
        if (world == null) {
            Component component = MiniMessage.miniMessage().deserialize(
                    "<red>World not found!"
            );
            p.sendMessage(component);
            return;
        }
        p.teleport(world.getSpawnLocation());
        Component component = MiniMessage.miniMessage().deserialize(
                "<green>Teleported to world <yellow>" + worldName
        );
        p.sendMessage(component);
    }

    private void deleteWorld(CommandSender sender, String[] args) {
        WorldStorage.deleteWorld(args[0]);
    }
}
