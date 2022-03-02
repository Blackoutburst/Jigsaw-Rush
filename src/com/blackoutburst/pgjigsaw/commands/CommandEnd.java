package com.blackoutburst.pgjigsaw.commands;

import com.blackoutburst.pgjigsaw.core.Core;
import com.blackoutburst.pgjigsaw.main.Main;
import org.bukkit.command.CommandSender;

public class CommandEnd {

    public void execute(CommandSender sender) {
        if (Main.gameRunning)
            Core.end();
        else
            sender.sendMessage("§cThere is no game running right now!");
    }

}
