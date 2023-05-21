package com.octanepvp.splityosis.octaneworlds.creation.tasks;

import com.octanepvp.splityosis.octaneworlds.OctaneWorlds;
import com.octanepvp.splityosis.octaneworlds.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.File;

public class DeleteWorldTask extends WorldTask{

    private TaskStatus taskStatus;
    private File worldFolder;
    private String worldName;
    private Location fallBackLocation;

    public DeleteWorldTask(TaskStatus taskStatus, File worldFolder, Location fallBackLocation) {
        this.taskStatus = taskStatus;
        this.worldFolder = worldFolder;
        this.fallBackLocation = fallBackLocation;
        this.worldName = worldFolder.getName();
    }

    @Override
    public void start() {
        Util.log("&7Deleting world '" + worldName + "'...");
        worldsBeingTasked.add(worldName.toLowerCase());
        taskStatus.hasStarted = true;
        long start = System.currentTimeMillis();
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            for (Player player : world.getPlayers()) {
                player.teleport(fallBackLocation);
            }
            Bukkit.unloadWorld(world, false);
        }
        Util.log("&bPhase 1 successfully finished in " + (System.currentTimeMillis() - start) + " ms (Synchronously)");
        Bukkit.getScheduler().runTaskAsynchronously(OctaneWorlds.plugin, () -> {
            //Phase 2 - Load world from file
            long start2 = System.currentTimeMillis();
            deleteDirectory(worldFolder);
            long current = System.currentTimeMillis();
            Util.log("&bPhase 2 successfully finished in " + (current - start2) + " ms (Asynchronously)");
            Util.log("&aSuccessfully deleted world '" + worldName + "' in " + (current - start) + " ms.");
            taskStatus.isComplete = true;
            worldsBeingTasked.remove(worldName.toLowerCase());
            OctaneWorlds.plugin.getWorldsConfig().pathList.remove(worldFolder.getAbsolutePath());
            OctaneWorlds.plugin.getWorldsConfig().saveToFile();
        });
    }

    private static void deleteDirectory(File directory) {
        if (!directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }

        directory.delete();
    }
}

