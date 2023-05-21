package com.octanepvp.splityosis.octaneworlds.creation.tasks;

import com.octanepvp.splityosis.octaneworlds.OctaneWorlds;
import com.octanepvp.splityosis.octaneworlds.creation.EmptyWorldGenerator;
import com.octanepvp.splityosis.octaneworlds.utils.Util;
import org.bukkit.*;

import java.io.File;

public class CloneWorldTask extends WorldTask{

    private TaskStatus taskStatus;
    private File parentDir;
    private File cloneOf;
    private String worldName;

    public CloneWorldTask(TaskStatus taskStatus, File cloneOf, File parentDir, String worldName) {
        this.taskStatus = taskStatus;
        this.parentDir = parentDir;
        this.worldName = worldName;
        this.cloneOf = cloneOf;
    }

    @Override
    public void start() {
        Util.log("&7Cloning world '" + cloneOf.getName() + "' into new world '"+worldName+"'");
        worldsBeingTasked.add(worldName.toLowerCase());
        taskStatus.hasStarted = true;
        long start = System.currentTimeMillis();
        Bukkit.getScheduler().runTaskAsynchronously(OctaneWorlds.plugin, () -> {
            File worldFolder = new File(parentDir, worldName);
            copyFileStructure(cloneOf, worldFolder);
            WorldCreator worldCreator = new WorldCreator(worldName);
            worldCreator.generator(new EmptyWorldGenerator());
            worldCreator.generateStructures(false);
            worldCreator.type(WorldType.FLAT);
            Util.log("&bPhase 1 successfully finished in " + (System.currentTimeMillis() - start) + " ms (Asynchronously)");
            Bukkit.getScheduler().runTask(OctaneWorlds.plugin, () -> {
                //Phase 2 - Load world from file
                long start2 = System.currentTimeMillis();
                worldCreator.createWorld();
                long current = System.currentTimeMillis();
                Util.log("&bPhase 2 successfully finished in " + (current - start2) + " ms (Synchronously)");
                Util.log("&aSuccessfully cloned world '" + cloneOf.getName() + "' into new world '" + worldName + "' in " + (current - start) + " ms.");
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
