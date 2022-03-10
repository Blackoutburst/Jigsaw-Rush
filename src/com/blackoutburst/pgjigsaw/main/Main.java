package com.blackoutburst.pgjigsaw.main;

import com.blackoutburst.pgjigsaw.commands.CommandEnd;
import com.blackoutburst.pgjigsaw.commands.CommandMaxScore;
import com.blackoutburst.pgjigsaw.commands.CommandStart;
import com.blackoutburst.pgjigsaw.core.Core;
import com.blackoutburst.pgjigsaw.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main  extends JavaPlugin implements Listener {

    public static int maxScore = 1;

    public static boolean gameRunning = false;

    public static Location spawn;
    public static Location gameSpawn;

    public static final List<Location> BOARD = new ArrayList<>();
    public static final List<Location> PLAYER_BOARD = new ArrayList<>();

    public static final Material[] MATERIALS = new Material[] {Material.DIRT, Material.STONE, Material.COBBLESTONE, Material.LOG, Material.WOOD, Material.BRICK, Material.GOLD_BLOCK, Material.NETHERRACK, Material.ENDER_STONE};

    public static  World world;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        world = Bukkit.getWorlds().get(0);

        spawn = new Location(world, 263.5f, 2, 1816.5f, 90, 0);
        gameSpawn = new Location(world, 250.5f, 2, 1823.5f, 0, 0);

        YamlConfiguration file = YamlConfiguration.loadConfiguration(getClass().getResourceAsStream("/board.yml"));
        Set<String> respawns = file.getConfigurationSection("loc").getKeys(false);
        for (final String i : respawns) {
            final double x = file.getDouble("loc."+i+".x");
            final double y = file.getDouble("loc."+i+".y");
            final double z = file.getDouble("loc."+i+".z");
            BOARD.add(new Location(world, x, y, z));
        }

        file = YamlConfiguration.loadConfiguration(getClass().getResourceAsStream("/player_board.yml"));
        respawns = file.getConfigurationSection("loc").getKeys(false);
        for (final String i : respawns) {
            final double x = file.getDouble("loc."+i+".x");
            final double y = file.getDouble("loc."+i+".y");
            final double z = file.getDouble("loc."+i+".z");
            PLAYER_BOARD.add(new Location(world, x, y, z));
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getPlayer().getItemInHand() == null) return;
        final Block clickedBlock = event.getClickedBlock();

        if (Utils.isFromPlayerBoard(clickedBlock.getLocation()) && !event.getPlayer().getItemInHand().getType().equals(clickedBlock.getType())) {
            if (!event.getPlayer().getItemInHand().getType().equals(Material.AIR))
                clickedBlock.setType(event.getPlayer().getItemInHand().getType());
            if (clickedBlock.getType().equals(Core.order[Utils.boardIndex(clickedBlock.getLocation())])) {
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ORB_PICKUP, 1, 1);
                if (Utils.correctBoard()) {
                    Core.boardEnd = Instant.now();
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1);
                    event.getPlayer().sendMessage("§aYou completed this board in: §b"+Utils.ROUND.format(((float) Duration.between(Core.boardBegin, Core.boardEnd).toMillis() / 1000.0f))+"s");
                    Core.currentScore++;
                    if (Core.currentScore >= maxScore) {
                        Core.end();
                    } else {
                        Utils.cleanBoard();
                        Utils.generateBoard();
                        Utils.giveItems(event.getPlayer());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().teleport(spawn);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(gameRunning);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(gameRunning);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(gameRunning);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(gameRunning);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(gameRunning);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        event.setCancelled(gameRunning);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName().toLowerCase()) {
            case "start": new CommandStart().execute(sender); break;
            case "end": new CommandEnd().execute(sender); break;
            case "maxscore": new CommandMaxScore().execute(sender, args); break;
            default: return (true);
        }
        return (true);
    }

}
