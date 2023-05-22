package com.octanepvp.splityosis.octaneworlds.creation.tasks;

import com.octanepvp.splityosis.octaneworlds.OctaneWorlds;
import com.octanepvp.splityosis.octaneworlds.creation.EmptyWorldGenerator;
import com.octanepvp.splityosis.octaneworlds.utils.Util;
import com.octanepvp.splityosis.octaneworldsapi.TaskStatus;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.io.File;

public class CreateWorldTask extends WorldTask{

    private TaskStatus taskStatus;
    private File parentDir;
    private String worldName;
    private World.Environment environment;

    public CreateWorldTask(TaskStatus taskStatus, File parentDir, String worldName, World.Environment environment) {
        this.taskStatus = taskStatus;
        this.parentDir = parentDir;
        this.worldName = worldName;
        this.environment = environment;
    }

    @Override
    public void start() {
        Util.log("&7Creating world '" + worldName + "'...");
        worldsBeingTasked.add(worldName.toLowerCase());
        taskStatus.hasStarted = true;
        long start = System.currentTimeMillis();
        Bukkit.getScheduler().runTaskAsynchronously(OctaneWorlds.plugin, () -> {
            File worldFolder = new File(parentDir, worldName);
            WorldCreator worldCreator = new WorldCreator(worldName);
            worldCreator.generator(new EmptyWorldGenerator());
            worldCreator.environment(environment);
            worldCreator.generateStructures(false);
            worldCreator.type(WorldType.FLAT);
            Util.log("&bPhase 1 successfully finished in " + (System.currentTimeMillis() - start) + " ms (Asynchronously)");
            Bukkit.getScheduler().runTask(OctaneWorlds.plugin, () -> {
                //Phase 2 - Load world from file
                long start2 = System.currentTimeMillis();
                Block block = new Location(worldCreator.createWorld(), 0, 99, 0).getBlock();
                block.setType(Material.BEDROCK);
                long current = System.currentTimeMillis();
                Util.log("&bPhase 2 successfully finished in " + (current - start2) + " ms (Synchronously)");
                Util.log("&aSuccessfully created new world '" + worldName + "' in " + (current - start) + " ms.");
                taskStatus.isComplete = true;
                worldsBeingTasked.remove(worldName.toLowerCase());
                OctaneWorlds.plugin.getWorldsConfig().pathList.add(worldFolder.getAbsolutePath());
                Bukkit.getScheduler().runTaskAsynchronously(OctaneWorlds.plugin, () -> {
                    OctaneWorlds.plugin.getWorldsConfig().saveToFile();
                });
            });
        });
    }
}
