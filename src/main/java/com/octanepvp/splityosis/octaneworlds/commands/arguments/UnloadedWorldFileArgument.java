package com.octanepvp.splityosis.octaneworlds.commands.arguments;

import dev.splityosis.commandsystem.SYSArgument;
import dev.splityosis.commandsystem.SYSCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnloadedWorldFileArgument extends SYSArgument {

    @Override
    public boolean isValid(String name) {
        File file = new File(Bukkit.getWorldContainer(), name);
        return file.exists() && file.isDirectory() && Bukkit.getWorld(name) == null;
    }

    @Override
    public List<String> getInvalidInputMessage(String name) {
        File file = new File(Bukkit.getWorldContainer(), name);
        if (!file.exists()) return Arrays.asList("&cWorld file '"+name+"' doesn't exist.");
        if (!file.isDirectory()) return Arrays.asList("&cInvalid world file '"+name+"', it must be a directory.");
        return Arrays.asList("&cWorld '"+name+"' is loaded.");
    }

    @Override
    public @NonNull List<String> tabComplete(CommandSender sender, SYSCommand command, String input) {
        List<String> options = new ArrayList<>();
        for (File file : Bukkit.getWorldContainer().listFiles()) {
            if (file.isDirectory())
                if (file.getName().toLowerCase().startsWith(input.toLowerCase()))
                    if (Bukkit.getWorld(file.getName()) == null)
                        options.add(file.getName());
        }
        return options;
    }
}
