package com.octanepvp.splityosis.octaneworlds.api;

import com.octanepvp.splityosis.octaneworlds.creation.tasks.*;
import com.octanepvp.splityosis.octaneworlds.exceptions.InvalidWorldFolderException;
import com.octanepvp.splityosis.octaneworlds.exceptions.InvalidWorldName;
import com.octanepvp.splityosis.octaneworlds.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.*;

public class OctaneWorldsAPI {

    public static TaskStatus createWorld(@NonNull String worldName, World.@NonNull Environment environment) throws InvalidWorldName{
        return createWorld(new File(Bukkit.getWorldContainer(), worldName), worldName, environment);
    }

    private static TaskStatus createWorld(@NonNull File parentDirectory, @NonNull String worldName, World.@NonNull Environment environment) throws InvalidWorldName {
        if (Bukkit.getWorld(worldName) != null || WorldTask.worldsBeingTasked.contains(worldName.toLowerCase()))
            throw new InvalidWorldName(worldName);
        TaskStatus taskStatus = new TaskStatus();
        WorldTask worldTask = new CreateWorldTask(taskStatus, parentDirectory, worldName, environment);
        worldTask.start();
        return taskStatus;
    }

    public static TaskStatus cloneWorld(@NonNull World world, @NonNull String newWorldName) throws InvalidWorldName {
        return cloneWorld(world, Bukkit.getWorldContainer(), newWorldName);
    }

    private static TaskStatus cloneWorld(@NonNull World world, @NonNull File parentDirectory, @NonNull String newWorldName) throws InvalidWorldName {
        if (Bukkit.getWorld(newWorldName) != null || WorldTask.worldsBeingTasked.contains(newWorldName.toLowerCase()))
            throw new InvalidWorldName(newWorldName);

        TaskStatus taskStatus = new TaskStatus();
        WorldTask worldTask = new CloneWorldTask(taskStatus, world.getWorldFolder(), parentDirectory, newWorldName);
        worldTask.start();
        return taskStatus;
    }

    public static TaskStatus loadWorld(@NonNull File worldFile) throws InvalidWorldName {
        String newWorldName = worldFile.getName();
        if (Bukkit.getWorld(newWorldName) != null || WorldTask.worldsBeingTasked.contains(newWorldName.toLowerCase()))
            throw new InvalidWorldName(newWorldName);

        TaskStatus taskStatus = new TaskStatus();
        WorldTask worldTask = new LoadWorldTask(taskStatus, worldFile);
        worldTask.start();
        return taskStatus;
    }

    public static void unloadWorld(@NonNull World world, boolean save, @Nullable Location fallBackLocation){
        long start = System.currentTimeMillis();
        if (fallBackLocation == null){
            fallBackLocation = Bukkit.getWorlds().get(0).getSpawnLocation();
        }
        for (Player player : world.getPlayers()) {
            player.teleport(fallBackLocation);
        }
        Bukkit.unloadWorld(world, save);
        Util.log("&aUnloaded world '"+world.getName()+"' in "+(System.currentTimeMillis() - start) + "ms (Synchronously)");
    }

    public static TaskStatus deleteWorld(@NonNull File worldFolder) throws InvalidWorldFolderException {
        if (!worldFolder.isDirectory())
            throw new InvalidWorldFolderException(worldFolder.getPath());
        TaskStatus taskStatus = new TaskStatus();
        WorldTask worldTask = new DeleteWorldTask(taskStatus, worldFolder);
        worldTask.start();
        return taskStatus;
    }

    public static TaskStatus deleteWorld(@NonNull World world) throws InvalidWorldFolderException {
        return deleteWorld(world.getWorldFolder());
    }
}
