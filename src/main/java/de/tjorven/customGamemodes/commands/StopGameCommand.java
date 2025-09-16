package de.tjorven.customGamemodes.commands;

import de.tjorven.customGamemodes.utils.GameStorage;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public class StopGameCommand implements BasicCommand {

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        GameStorage.getActiveGamemode().stop();
    }
}
