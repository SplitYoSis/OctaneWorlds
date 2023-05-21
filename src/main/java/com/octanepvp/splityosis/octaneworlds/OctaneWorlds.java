package com.octanepvp.splityosis.octaneworlds;

import com.octanepvp.splityosis.octaneworlds.api.OctaneWorldsAPI;
import com.octanepvp.splityosis.octaneworlds.commands.OctaneWorldsBranch;
import com.octanepvp.splityosis.octaneworlds.creation.CreationListeners;
import com.octanepvp.splityosis.octaneworlds.data.WorldsConfig;
import com.octanepvp.splityosis.octaneworlds.exceptions.InvalidWorldName;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class OctaneWorlds extends JavaPlugin {

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
                OctaneWorldsAPI.loadWorld(file);
            } catch (InvalidWorldName e) {
                e.printStackTrace();
            }
        }
    }
}
