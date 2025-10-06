package de.tjorven.customGamemodes.commands;

import de.tjorven.customGamemodes.utils.SuggestionTools;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class JoinWorldCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        if (!(commandSourceStack.getExecutor() instanceof Player p)) return;
        if (args.length == 0) {
            Component component = MiniMessage.miniMessage().deserialize(
                    "<red>Usage: /joinworld <worldname>"
            );
            p.sendMessage(component);
            return;
        }
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

    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args) {
        if (!(commandSourceStack.getExecutor() instanceof Player p)) return List.of();
        List<String> worlds = p.getServer().getWorlds().stream().map(World::getName).toList();
        if (args.length == 0) return worlds;
        if (args.length > 1) return List.of();
        return SuggestionTools.generateCollectionSearchSuggestions(worlds, args[0]);
    }
}
