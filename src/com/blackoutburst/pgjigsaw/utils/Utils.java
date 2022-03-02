package com.blackoutburst.pgjigsaw.utils;

import com.blackoutburst.pgjigsaw.main.Main;
import org.bukkit.Location;
import org.bukkit.Material;


public class Utils {

    public static void cleanBoard() {
        for (Location l : Main.BOARD) {
            Main.world.getBlockAt(l).setType(Material.WOOL);
        }
        for (Location l : Main.PLAYER_BOARD) {
            Main.world.getBlockAt(l).setType(Material.SNOW_BLOCK);
        }
    }

    public static boolean isFromPlayerBoard(Location block) {
        boolean fromBoard = false;

        for (Location l : Main.PLAYER_BOARD) {
            if (block.getBlockX() == l.getBlockX() &&
                block.getBlockY() == l.getBlockY() &&
                block.getBlockZ() == l.getBlockZ()) {
                fromBoard = true;
            }
        }

        return (fromBoard);
    }

}
