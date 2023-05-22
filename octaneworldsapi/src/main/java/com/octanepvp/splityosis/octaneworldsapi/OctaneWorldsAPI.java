package com.octanepvp.splityosis.octaneworldsapi;

import com.octanepvp.splityosis.octaneworldsapi.exceptions.InvalidWorldFolderException;
import com.octanepvp.splityosis.octaneworldsapi.exceptions.InvalidWorldName;
import org.bukkit.Location;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;

public interface OctaneWorldsAPI {

    TaskStatus createWorld(@NonNull String worldName, World.@NonNull Environment environment) throws InvalidWorldName;

    TaskStatus cloneWorld(@NonNull World world, @NonNull String newWorldName) throws InvalidWorldName;

    TaskStatus loadWorld(@NonNull File worldFile) throws InvalidWorldName;

    void unloadWorld(@NonNull World world, boolean save, @Nullable Location fallBackLocation);

    TaskStatus deleteWorld(@NonNull File worldFolder, @Nullable Location fallBackLocation) throws InvalidWorldFolderException;

    TaskStatus deleteWorld(@NonNull World world, @Nullable Location fallBackLocation) throws InvalidWorldFolderException;
}
