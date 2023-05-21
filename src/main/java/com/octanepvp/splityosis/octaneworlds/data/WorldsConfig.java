package com.octanepvp.splityosis.octaneworlds.data;

import dev.splityosis.configsystem.configsystem.AnnotatedConfig;
import dev.splityosis.configsystem.configsystem.ConfigField;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WorldsConfig extends AnnotatedConfig {

    public WorldsConfig(File parentDirectory, String name) {
        super(parentDirectory, name);
    }

    @ConfigField(path = "worlds")
    public List<String> pathList = new ArrayList<>();
}
