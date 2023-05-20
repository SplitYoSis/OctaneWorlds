package com.octanepvp.splityosis.octaneworlds.creation;

import com.octanepvp.splityosis.octaneworlds.OctaneWorlds;
import com.octanepvp.splityosis.octaneworlds.utils.Util;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;

public class CreateWorldTask {

    private TaskStatus taskStatus;
    private File parentDir;
    private String worldName;
    private World.Environment environment;
    private Long start = 0L;

    public CreateWorldTask(TaskStatus taskStatus, File parentDir, String worldName, World.Environment environment) {
        this.taskStatus = taskStatus;
        this.parentDir = parentDir;
        this.worldName = worldName;
        this.environment = environment;
    }

    public void cloneWorld(@NonNull File cloneOf){
        Util.log("&7Cloning world '" + cloneOf.getName() + "' into new world '"+worldName+"'");
        beingCreated.add(worldName.toLowerCase());
        taskStatus.hasStarted = true;
        start = System.currentTimeMillis();
        Bukkit.getScheduler().runTaskAsynchronously(OctaneWorlds.plugin, () -> {
            File worldFolder = new File(parentDir, worldName);
            copyFileStructure(cloneOf, worldFolder);
            WorldCreator worldCreator = new WorldCreator(worldFolder.getAbsolutePath());
            worldCreator.seed(123456789L);
            worldCreator.generator(new EmptyWorldGenerator());
            if (environment != null)
                worldCreator.environment(environment);
            worldCreator.generateStructures(false);
            worldCreator.type(WorldType.FLAT);
            Util.log("&bPhase 1 successfully finished in " + (System.currentTimeMillis() - start) + " ms (Asynchronously)");
            Bukkit.getScheduler().runTask(OctaneWorlds.plugin, () -> {
                //Phase 2 - Load world from file
                long start2 = System.currentTimeMillis();
                TasksData.worldsCreated.add(worldName);
                Block block = new Location(worldCreator.createWorld(), 0, 99, 0).getBlock();
                block.setType(Material.BEDROCK);
                long current = System.currentTimeMillis();
                Util.log("&bPhase 2 successfully finished in " + (current - start2) + " ms (Synchronously)");
                Util.log("&aSuccessfully cloned world '" + cloneOf.getName() + "' into new world '" + worldName + "' in " + (current - start) + " ms.");
                taskStatus.isComplete = true;
            });
        });
    }

    public void createNewWorld() {
        Util.log("&7Creating world '" + worldName + "'...");
        TasksData.beingCreated.add(worldName.toLowerCase());
        taskStatus.hasStarted = true;
        start = System.currentTimeMillis();
        Bukkit.getScheduler().runTaskAsynchronously(OctaneWorlds.plugin, () -> {
            File worldFolder = new File(parentDir, worldName);
            WorldCreator worldCreator = new WorldCreator(worldFolder.getAbsolutePath());
            worldCreator.seed(123456789L);
            worldCreator.generator(new EmptyWorldGenerator());
            worldCreator.environment(environment);
            worldCreator.generateStructures(false);
            worldCreator.type(WorldType.FLAT);
            Util.log("&bPhase 1 successfully finished in " + (System.currentTimeMillis() - start) + " ms (Asynchronously)");
            Bukkit.getScheduler().runTask(OctaneWorlds.plugin, () -> {
                //Phase 2 - Load world from file
                long start2 = System.currentTimeMillis();
                TasksData.worldsCreated.add(worldName);
                Block block = new Location(worldCreator.createWorld(), 0, 99, 0).getBlock();
                block.setType(Material.BEDROCK);
                long current = System.currentTimeMillis();
                Util.log("&bPhase 2 successfully finished in " + (current - start2) + " ms (Synchronously)");
                Util.log("&aSuccessfully created new world '" + worldName + "' in " + (current - start) + " ms.");
                taskStatus.isComplete = true;
            });
        });
    }
}
