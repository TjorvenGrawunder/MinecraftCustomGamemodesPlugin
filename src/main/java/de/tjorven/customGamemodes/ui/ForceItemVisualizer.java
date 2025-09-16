package de.tjorven.customGamemodes.ui;

import de.tjorven.customGamemodes.utils.Team;
import de.tjorven.customGamemodes.utils.TeamStorage;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBarViewer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.translation.TranslationStore;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

public class ForceItemVisualizer {
    public static void updateTimer(long remainingTime) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            int hours = (int) (remainingTime / 3600);
            int minutes = (int) ((remainingTime % 3600) / 60);
            int seconds = (int) (remainingTime % 60);
            String remainingTimeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            Component actionBar = Component.text(remainingTimeFormatted);
            player.sendActionBar(actionBar);
        }
    }

    public static void updateForceItem(Team team, Material material) {
        BossBar bossBar = team.getBossBar();
        Audience audience = team.getAudience();
        if (bossBar != null){
            bossBar.removeViewer(audience);
        } else {
            bossBar = BossBar.bossBar(Component.text(""), 1.0f, BossBar.Color.GREEN, BossBar.Overlay.PROGRESS);
            team.setBossBar(bossBar);
        }

        bossBar.name(Component.translatable(material.translationKey()));

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
}
