package com.octanepvp.splityosis.octaneworlds.commands;

import dev.splityosis.commandsystem.SYSArgument;
import dev.splityosis.commandsystem.SYSCommand;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnvironmentArgument extends SYSArgument {

    @Override
    public boolean isValid(String s) {
        try {
            World.Environment.valueOf(s.toUpperCase());
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<String> getInvalidInputMessage(String s) {
        return Arrays.asList("&cInvalid environment.");
    }

    @Override
    public @NonNull List<String> tabComplete(CommandSender sender, SYSCommand command, String input) {
        List<String> options = new ArrayList<>();
        for (World.Environment value : World.Environment.values()) {
            if (value.name().toLowerCase().startsWith(input.toLowerCase()))
                options.add(value.name());
        }
        return options;
    }
}
