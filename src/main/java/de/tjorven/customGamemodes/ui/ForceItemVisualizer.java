package de.tjorven.customGamemodes.ui;

import de.tjorven.customGamemodes.teams.Team;
import de.tjorven.customGamemodes.teams.TeamStorage;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static de.tjorven.customGamemodes.CustomGamemodes.plugin;

public class ForceItemVisualizer {

    public static void startCountdown(int duration, Runnable onFinish) {
        int totalTicks = duration * 20; // Dauer in Ticks
        for (int i = 0; i <= duration; i++) {
            int remaining = duration - i;
            int delayTicks = i * 20;

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                for (Team team : TeamStorage.getInstance().getTeams()) {
                    Audience audience = team.getAudience();
                    audience.showTitle(Title.title(
                            Component.text(remaining).color(TextColor.color(82, 5, 123)),
                            Component.text("")
                    ));
                }

                // Am Ende des Countdowns Callback ausführen
                if (remaining == 0) {
                    onFinish.run();
                }

            }, delayTicks);
        }
    }


    public static void updateTimer(long remainingTime) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String remainingTimeFormatted = ForceItemVisualizer.getFormattedTime(remainingTime);
            Component actionBar = Component.text(remainingTimeFormatted).color(TextColor.color(124, 255, 0))
                    .shadowColor(ShadowColor.shadowColor(124, 194, 0, 128)).decorate(TextDecoration.BOLD);
            player.sendActionBar(actionBar);
        }
    }

    public static void updateForceItem(Team team, Material material) {
        BossBar bossBar = team.getBossBar();
        Audience audience = team.getAudience();
        if (bossBar != null){
            bossBar.removeViewer(audience);
        } else {
            bossBar = BossBar.bossBar(Component.text(""), 1.0f, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);
            team.setBossBar(bossBar);
        }

        String unicode = FontMappings.getUnicodeString(material);

        if (unicode == null) {
            unicode = "?";
        }

        bossBar.name(Component.translatable(material.translationKey())
                .append(Component.text(" ")
                .append(Component.text(unicode).shadowColor(ShadowColor.shadowColor(0, 0, 0, 0))))
        );

        bossBar.addViewer(audience);
    }

    public static void stopRound() {
        for (Team team : TeamStorage.getInstance().getTeams()) {
            BossBar bossBar = team.getBossBar();
            bossBar.removeViewer(team.getAudience());
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendActionBar(Component.text(""));
        }
    }

    public static void showTeamResult(Player p, String teamName, int score) {
        TextComponent title = Component.text(teamName + " completed")
                .color(TextColor.color(0, 255, 0))
                .decorate(TextDecoration.BOLD);
        TextComponent subtitle = Component.text(score + " rounds!")
                .color(TextColor.color(124, 255, 0))
                .decorate(TextDecoration.BOLD)
                .shadowColor(ShadowColor.shadowColor(124, 194, 0, 128));
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        p.showTitle(Title.title(title, subtitle));
    }

    public static String getFormattedTime(long remainingTime) {
        int hours = (int) (remainingTime / 3600);
        int minutes = (int) ((remainingTime % 3600) / 60);
        int seconds = (int) (remainingTime % 60);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
