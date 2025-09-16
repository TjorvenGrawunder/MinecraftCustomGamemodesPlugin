package de.tjorven.customGamemodes.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamStorage {
    private static TeamStorage instance;
    private List<Team> teams = new ArrayList<Team>();
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

    public void makeSinglePlayerTeams(List<Player> players) {
        clearTeams();

        for (Player player : players) {
            Team team = new Team(List.of(player), player.getName());
            addTeam(team);
        }
    }
}
