package com.octanepvp.splityosis.octaneworlds.commands;

import com.octanepvp.splityosis.octaneworlds.creation.tasks.WorldTask;
import dev.splityosis.commandsystem.SYSArgument;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;

public class NewWorldNameArgument extends SYSArgument {

    @Override
    public boolean isValid(String s) {
        return Bukkit.getWorld(s) == null && !WorldTask.worldsBeingTasked.contains(s.toLowerCase());
    }

    @Override
    public List<String> getInvalidInputMessage(String s) {
        return Arrays.asList("&cInvalid world name, a world with this name already exists or is being created.");
    }
}
