package com.octanepvp.splityosis.octaneworlds;

import com.octanepvp.splityosis.octaneworlds.commands.OctaneWorldsBranch;
import com.octanepvp.splityosis.octaneworlds.creation.CreationListeners;
import com.octanepvp.splityosis.octaneworlds.data.WorldsConfig;
import com.octanepvp.splityosis.octaneworldsapi.TaskStatus;
import com.octanepvp.splityosis.octaneworldsapi.exceptions.InvalidWorldFolderException;
import com.octanepvp.splityosis.octaneworldsapi.exceptions.InvalidWorldName;
import com.octanepvp.splityosis.octaneworldsapi.OctaneWorldsAPI;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;

public final class OctaneWorlds extends JavaPlugin implements OctaneWorldsAPI {

    public static OctaneWorlds plugin;
    private File worldContainer;
    private WorldsConfig worldsConfig;

    @Override
    public void onEnable() {
        plugin = this;
        getServer().getPluginManager().registerEvents(new CreationListeners(), this);
        if (!getDataFolder().exists())
            getDataFolder().mkdirs();
//        worldContainer = new File(getDataFolder(), "worlds");
//        if (!worldContainer.exists())
//            worldContainer.mkdirs();

        worldsConfig = new WorldsConfig(getDataFolder(), "worlds-config");
        worldsConfig.initialize();

        loadWorlds();
        new OctaneWorldsBranch().registerCommandBranch(this);
    }

    @Override
    public void onDisable() {
        worldsConfig.saveToFile();
    }

    public WorldsConfig getWorldsConfig() {
        return worldsConfig;
    }

    public File getWorldContainer() {
        return worldContainer;
    }

    private void loadWorlds(){
        for (String s : worldsConfig.pathList) {
            File file = new File(s);
            if (!file.isDirectory()) continue;
            try {
                OctaneWorldsPanel.loadWorld(file);
            } catch (InvalidWorldName e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public TaskStatus createWorld(@NonNull String worldName, World.@NonNull Environment environment) throws InvalidWorldName {
        return OctaneWorldsPanel.createWorld(worldName, environment);
    }

    @Override
    public TaskStatus cloneWorld(@NonNull World world, @NonNull String newWorldName) throws InvalidWorldName {
        return OctaneWorldsPanel.cloneWorld(world, newWorldName);
    }

    @Override
    public TaskStatus loadWorld(@NonNull File worldFile) throws InvalidWorldName {
        return OctaneWorldsPanel.loadWorld(worldFile);
    }

    @Override
    public void unloadWorld(@NonNull World world, boolean save, @Nullable Location fallBackLocation) {
        OctaneWorldsPanel.unloadWorld(world, save, fallBackLocation);
    }

    @Override
    public TaskStatus deleteWorld(@NonNull File worldFolder, @Nullable Location fallBackLocation) throws InvalidWorldFolderException {
        return OctaneWorldsPanel.deleteWorld(worldFolder, fallBackLocation);
    }

    @Override
    public TaskStatus deleteWorld(@NonNull World world, @Nullable Location fallBackLocation) throws InvalidWorldFolderException {
        return OctaneWorldsPanel.deleteWorld(world, fallBackLocation);
    }
}
