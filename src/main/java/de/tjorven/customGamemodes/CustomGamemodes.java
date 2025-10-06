package de.tjorven.customGamemodes;

import de.tjorven.customGamemodes.commands.*;
import de.tjorven.customGamemodes.eventlistener.CheckForceItemListener;
import de.tjorven.customGamemodes.eventlistener.GameMenuListener;
import de.tjorven.customGamemodes.modes.ForceItemBattle;
import de.tjorven.customGamemodes.modes.GameModeRegistry;
import de.tjorven.customGamemodes.ui.FontMappings;
import io.papermc.paper.command.brigadier.BasicCommand;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class CustomGamemodes extends JavaPlugin {

    public static CustomGamemodes plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        //printBlockList();
        FontMappings.init();
        setPlugin(this);
        this.getConfig().set("gameDuration", 60);
        this.saveConfig();
        registerGameModes();
        registerCommands();
        registerListener();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void setPlugin(CustomGamemodes plugin) {
        CustomGamemodes.plugin = plugin;
    }

    private void registerCommands() {
        BasicCommand openGameMenuCommand = new OpenGameMenuCommand();
        registerCommand("gamemenu", openGameMenuCommand);
        BasicCommand startGameCommand = new StartGameCommand();
        registerCommand("startgame", startGameCommand);
        BasicCommand stopGameCommand = new StopGameCommand();
        registerCommand("stopgame", stopGameCommand);
        OpenBackpackCommand openBackpackCommand = new OpenBackpackCommand();
        registerCommand("bp", openBackpackCommand);
        SkipCommand skipCommand = new SkipCommand();
        registerCommand("skip", skipCommand);
        AdminSkipCommand adminSkipCommand = new AdminSkipCommand();
        registerCommand("adminskip", adminSkipCommand);
        SetGameDurationCommand setGameDurationCommand = new SetGameDurationCommand();
        registerCommand("setgametime", setGameDurationCommand);
        ResultsCommand resultsCommand = new ResultsCommand();
        registerCommand("results", resultsCommand);
        CreateWorldCommand createWorldCommand = new CreateWorldCommand();
        registerCommand("createworld", createWorldCommand);
        JoinWorldCommand joinWorldCommand = new JoinWorldCommand();
        registerCommand("joinworld", joinWorldCommand);
    }

    private void registerGameModes(){
        GameModeRegistry.register(new ForceItemBattle());
    }

    private void registerListener(){
        getServer().getPluginManager().registerEvents(new CheckForceItemListener(), plugin);
        getServer().getPluginManager().registerEvents(new GameMenuListener(), plugin);
    }

    private void printBlockList(){
        File BlockListFile = new File("C:\\Users\\admin\\PycharmProjects\\CreateAssetFont\\BlockList.txt");
        try (FileWriter writer = new FileWriter(BlockListFile)) {
            for (Material material : Material.values()) {
                if (material.isBlock() && material.isItem()) {
                    writer.write(material.name() + "\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
