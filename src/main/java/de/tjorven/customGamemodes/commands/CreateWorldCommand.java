package de.tjorven.customGamemodes.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

public class CreateWorldCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        if (!(commandSourceStack.getExecutor() instanceof Player p)) {
            System.err.println("CommandSourceStack is not a Player");
            return;
        }
        if (args.length < 1) {
            Component component = MiniMessage.miniMessage().deserialize(
                    "<red>Usage: /createworld <worldname>"
            );
            p.sendMessage(component);
            return;
        }
        String worldName = args[0];
        p.sendMessage(MiniMessage.miniMessage().deserialize("<green>Creating world <yellow>" + worldName + "<green>..."));
        WorldCreator worldCreator = WorldCreator.name(worldName);
        World world = p.getServer().createWorld(worldCreator);
        System.out.println(p.getServer().getWorlds());

        if (world != null) {
            p.teleport(world.getSpawnLocation());
            p.sendMessage(MiniMessage.miniMessage().deserialize("<green>Teleported to world <yellow>" + worldName + "<green>!"));
        } else {
            p.sendMessage(MiniMessage.miniMessage().deserialize("<red>Failed to create world <yellow>" + worldName + "<red>!"));
        }
    }
}
