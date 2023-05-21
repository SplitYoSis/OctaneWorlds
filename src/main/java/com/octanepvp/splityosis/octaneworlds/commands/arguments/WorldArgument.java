package com.octanepvp.splityosis.octaneworlds.commands.arguments;

import dev.splityosis.commandsystem.SYSArgument;
import dev.splityosis.commandsystem.SYSCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorldArgument extends SYSArgument {
    @Override
    public boolean isValid(String s) {
        return Bukkit.getWorld(s) != null;
    }

    @Override
    public List<String> getInvalidInputMessage(String s) {
        return Arrays.asList("&cWorld '"+s+"' doesn't exist or isn't loaded.");
    }

    @Override
    public @NonNull List<String> tabComplete(CommandSender sender, SYSCommand command, String input) {
        List<String> options = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            if (world.getName().toLowerCase().startsWith(input.toLowerCase()))
                options.add(world.getName());
        }
        return options;
    }
}
