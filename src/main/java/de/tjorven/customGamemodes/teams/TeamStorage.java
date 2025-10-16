package de.tjorven.customGamemodes.teams;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamStorage {
    private static TeamStorage instance;
    private List<Team> teams = new ArrayList<>();
    private boolean isActive = false;

    private TeamStorage() {
        // Private constructor to prevent instantiation
    }

    public static TeamStorage getInstance() {
        if (instance == null) {
            instance = new TeamStorage();
        }
        return instance;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    public void clearTeams() {
        teams.clear();
    }

    public void removeTeam(Team team) {
        teams.remove(team);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void toggleActive() {
        isActive = !isActive;
    }

    public Team getTeam(Player player) {
        for (Team team : teams) {
            if (team.getPlayers().contains(player)) {
                return team;
            }
        }
        return null;
    }

    public Team getTeamByName(String name) {
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(name)) {
                return team;
            }
        }
        return null;
    }

    public void makeSinglePlayerTeams(List<Player> players) {
        clearTeams();

        for (Player player : players) {
            List<Player> playerList = new ArrayList<>();
            playerList.add(player);
            Team team = new Team(playerList, player.getName());
            addTeam(team);
        }
    }
}
