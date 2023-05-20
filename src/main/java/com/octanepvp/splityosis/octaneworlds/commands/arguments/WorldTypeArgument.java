package com.octanepvp.splityosis.octaneworlds.commands;

import dev.splityosis.commandsystem.SYSArgument;
import dev.splityosis.commandsystem.SYSCommand;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorldTypeArgument extends SYSArgument {

    @Override
    public boolean isValid(String s) {
        try {
            WorldType.valueOf(s.toUpperCase());
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<String> getInvalidInputMessage(String s) {
        return Arrays.asList("&cInvalid world type, options:", "&7 - Normal", "&7 - Flat", "&7 - Amplified", "&7 - Large_Biomes");
    }

    @Override
    public @NonNull List<String> tabComplete(CommandSender sender, SYSCommand command, String input) {
        List<String> options = new ArrayList<>();
        for (WorldType value : WorldType.values()) {
            if (value.name().toLowerCase().startsWith(input.toLowerCase()))
                options.add(value.name());
        }
        return options;
    }
}