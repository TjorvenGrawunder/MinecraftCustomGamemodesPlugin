package de.tjorven.customGamemodes;

import de.tjorven.customGamemodes.commands.OpenBackpackCommand;
import de.tjorven.customGamemodes.commands.SkipCommand;
import de.tjorven.customGamemodes.commands.StartGameCommand;
import de.tjorven.customGamemodes.commands.StopGameCommand;
import de.tjorven.customGamemodes.eventlistener.CheckForceItemListener;
import io.papermc.paper.command.brigadier.BasicCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomGamemodes extends JavaPlugin {

    public static CustomGamemodes plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        setPlugin(this);
        registerCommands();
        getServer().getPluginManager().registerEvents(new CheckForceItemListener(), plugin);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void setPlugin(CustomGamemodes plugin) {
        CustomGamemodes.plugin = plugin;
    }

    private void registerCommands() {
        BasicCommand startGameCommand = new StartGameCommand();
        registerCommand("startgame", startGameCommand);
        BasicCommand stopGameCommand = new StopGameCommand();
        registerCommand("stopgame", stopGameCommand);
        OpenBackpackCommand openBackpackCommand = new OpenBackpackCommand();
        registerCommand("bp", openBackpackCommand);
        SkipCommand skipCommand = new SkipCommand();
        registerCommand("skip", skipCommand);
    }
}
