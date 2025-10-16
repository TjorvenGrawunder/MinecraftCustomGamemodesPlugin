package de.tjorven.customGamemodes.commands;

import de.tjorven.customGamemodes.commands.commandtree.CommandNode;
import de.tjorven.customGamemodes.utils.CommandArguments;
import de.tjorven.customGamemodes.teams.Team;
import de.tjorven.customGamemodes.teams.TeamStorage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeamsCommand implements CommandExecutor, TabCompleter {
    CommandNode root = CommandNode.root("teams");

    public TeamsCommand() {
        root
            .sub("create", "<team_name>", "Creates a Team", this::createTeam)
            .sub("join", "<team_name>", "Joins a Team", this::joinTeam, CommandArguments::listTeams)
            .sub("leave", "", "Leaves your current Team", this::leaveTeam)
            .sub("list", "", "Lists all Teams", this::listTeams);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }

    private void createTeam(CommandSender sender, String[] args) {
        if (!(sender instanceof Player p)) return;
        if (args.length == 0) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red> You need to specify a team name."));
        }
        String teamName = args[0];
        Team team = TeamStorage.getInstance().getTeamByName(teamName);

        if (team == null) {
            TeamStorage.getInstance().addTeam(new Team(List.of(p), teamName));
        } else {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("Team " + teamName + " already exists. Use /teams join <teamname> to join."));
        }
    }

    private void joinTeam(CommandSender sender, String[] args) {
        if  (!(sender instanceof Player p)) return;
        if (args.length == 0) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red> You need to specify a team name."));
        }

        Team team = TeamStorage.getInstance().getTeamByName(args[0]);
        if (team == null) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("Team " + args[0] + " does not exist. To create a new team, /teams create <teamname>"));
        } else {
            team.addPlayer(p);
        }
    }

    private void leaveTeam(CommandSender sender, String[] args) {
        if  (!(sender instanceof Player p)) return;

        Team team = TeamStorage.getInstance().getTeam(p);
        if (team == null) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("You are not in a Team!"));
        } else {
            team.removePlayer(p);
        }
    }

    private void  listTeams(CommandSender sender, String[] args) {
        List<Team> teams = TeamStorage.getInstance().getTeams();
        Component msg = MiniMessage.miniMessage().deserialize("Teams");
        for (Team team : teams) {
            msg = msg.append(MiniMessage.miniMessage().deserialize(" - " + team.getName()));
        }
        sender.sendMessage(msg);
    }
}
