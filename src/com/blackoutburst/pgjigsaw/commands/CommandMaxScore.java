package com.blackoutburst.pgjigsaw.commands;

import com.blackoutburst.pgjigsaw.main.Main;
import org.bukkit.command.CommandSender;

public class CommandMaxScore {

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cInvalid usage! Try §e/maxscore <value>");
            return;
        }

        int value;

        try {
            value = Integer.parseInt(args[0]);
        } catch (Exception ignored) {
            sender.sendMessage("§cThe score must be a valid number!");
            return;
        }

        if (value <= 0) {
            sender.sendMessage("§cThe score must be at least 1!");
            return;
        }

        Main.maxScore = value;
        sender.sendMessage("§bThe amount of craft needed to complete the game has been set to §6"+value);
    }

}
