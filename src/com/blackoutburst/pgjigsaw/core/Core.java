package com.blackoutburst.pgjigsaw.core;

import com.blackoutburst.pgjigsaw.main.Main;
import com.blackoutburst.pgjigsaw.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Material;
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

    public static Material[] order;

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
                Utils.generateBoard();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Utils.giveItems(player);
                }
            }
        }.runTaskLater(Main.getPlugin(Main.class), 60L);
    }

    public static void end() {
        Main.gameRunning = false;
        gameEnd = Instant.now();
        Utils.cleanBoard();

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage("§aYou completed "+currentScore+" board in: §b"+Utils.ROUND.format(((float) Duration.between(Core.gameBegin, Core.gameEnd).toMillis() / 1000.0f))+"s");
            p.getInventory().clear();
            p.teleport(Main.spawn);
        }
    }
}
