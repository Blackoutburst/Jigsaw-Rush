package com.blackoutburst.pgjigsaw.core;

import com.blackoutburst.pgjigsaw.main.Main;
import com.blackoutburst.pgjigsaw.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.Instant;

public class Core {

    public static Instant boardBegin;
    public static Instant boardEnd;
    public static Instant gameBegin;
    public static Instant gameEnd;

    public static int currentScore = 0;

    public static void start() {
        Main.gameRunning = true;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(Main.gameSpawn);
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
        }
        Main.world.setDifficulty(Difficulty.PEACEFUL);
        Utils.cleanBoard();
        Utils.countdown();

        new BukkitRunnable() {
            @Override
            public void run() {
                gameBegin = Instant.now();
                boardBegin = Instant.now();
            }
        }.runTaskLater(Main.getPlugin(Main.class), 60L);
    }

    public static void end() {

    }
}
