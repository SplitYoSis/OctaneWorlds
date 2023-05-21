package com.octanepvp.splityosis.octaneworlds.creation.tasks;

import com.octanepvp.splityosis.octaneworlds.OctaneWorlds;
import com.octanepvp.splityosis.octaneworlds.creation.EmptyWorldGenerator;
import com.octanepvp.splityosis.octaneworlds.utils.Util;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.io.File;

public class LoadWorldTask extends WorldTask{

    private TaskStatus taskStatus;
    private File toLoad;
    private String worldName;

    public LoadWorldTask(TaskStatus taskStatus, File toLoad) {
        this.taskStatus = taskStatus;
        this.toLoad = toLoad;
        this.worldName = toLoad.getName();
    }

    @Override
    public void start() {
        Util.log("&7Loading world '" + worldName + "'...");
        worldsBeingTasked.add(worldName.toLowerCase());
        taskStatus.hasStarted = true;
        long start = System.currentTimeMillis();

        Bukkit.getScheduler().runTaskAsynchronously(OctaneWorlds.plugin, () -> {
            WorldCreator worldCreator = new WorldCreator(toLoad.getName());
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
                Util.log("&aSuccessfully loaded world '" + worldName + "' in " + (current - start) + " ms.");
                taskStatus.isComplete = true;
                worldsBeingTasked.remove(worldName.toLowerCase());
                Bukkit.getScheduler().runTaskAsynchronously(OctaneWorlds.plugin, () -> {
                    String path = toLoad.getAbsolutePath();
                    if (!OctaneWorlds.plugin.getWorldsConfig().pathList.contains(path)) {
                        OctaneWorlds.plugin.getWorldsConfig().pathList.add(path);
                        OctaneWorlds.plugin.getWorldsConfig().saveToFile();
                    }
                });
            });
        });
    }
}
