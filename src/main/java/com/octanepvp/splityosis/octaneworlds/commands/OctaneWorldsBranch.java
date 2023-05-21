package com.octanepvp.splityosis.octaneworlds.commands;

import com.octanepvp.splityosis.octaneworlds.OctaneWorlds;
import com.octanepvp.splityosis.octaneworlds.api.OctaneWorldsAPI;
import com.octanepvp.splityosis.octaneworlds.commands.arguments.EnvironmentArgument;
import com.octanepvp.splityosis.octaneworlds.commands.arguments.NewWorldNameArgument;
import com.octanepvp.splityosis.octaneworlds.commands.arguments.UnloadedWorldFileArgument;
import com.octanepvp.splityosis.octaneworlds.commands.arguments.WorldArgument;
import com.octanepvp.splityosis.octaneworlds.creation.tasks.TaskStatus;
import com.octanepvp.splityosis.octaneworlds.exceptions.InvalidWorldFolderException;
import com.octanepvp.splityosis.octaneworlds.exceptions.InvalidWorldName;
import com.octanepvp.splityosis.octaneworlds.utils.Util;
import dev.splityosis.commandsystem.SYSCommand;
import dev.splityosis.commandsystem.SYSCommandBranch;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OctaneWorldsBranch extends SYSCommandBranch {

    public OctaneWorldsBranch() {
        super("OctaneWorlds", "Worlds", "ow", "World");

        setPermission("OctaneWorlds.admin");
        setUnknownCommandMessage(Arrays.asList("&cUnknown subcommand."));

        addCommand(new SYSCommand("create")
                .setArguments(new NewWorldNameArgument(), new EnvironmentArgument())
                .setUsage("/OctaneWorlds create [world-name] [environment]")
                .executes((sender, args) -> {
                    try {
                        Util.sendMessage(sender, "&7Creating world '"+args[0]+"'...");
                        TaskStatus taskStatus = OctaneWorldsAPI.createWorld(args[0], World.Environment.valueOf(args[1]));
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                if (taskStatus.isComplete()){
                                    Util.sendMessage(sender, "&aSuccessfully created world '"+args[0]+"'!");
                                    cancel();
                                }
                            }
                        }.runTaskTimer(OctaneWorlds.plugin, 0, 0);
                    } catch (InvalidWorldName e) {
                        throw new RuntimeException(e);
                    }
                }));


        addCommand(new SYSCommand("clone")
                .setArguments(new WorldArgument(), new NewWorldNameArgument())
                .setUsage("/OctaneWorlds clone [world-name] [new-name]")
                .executes((sender, args) -> {
                    try {
                        Util.sendMessage(sender, "&7Cloning world '"+args[0]+"'...");
                        TaskStatus taskStatus = OctaneWorldsAPI.cloneWorld(Objects.requireNonNull(Bukkit.getWorld(args[0])), args[1]);
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                if (taskStatus.isComplete()){
                                    Util.sendMessage(sender, "&aSuccessfully created world '"+args[1]+"'!");
                                    cancel();
                                }
                            }
                        }.runTaskTimer(OctaneWorlds.plugin, 0, 0);
                    } catch (InvalidWorldName e) {
                        throw new RuntimeException(e);
                    }
                }));

        addCommand(new SYSCommand("unload")
                .setArguments(new WorldArgument())
                .setUsage("/OctaneWorlds unload [world-name]")
                .executes((sender, args) -> {
                    World world = Objects.requireNonNull(Bukkit.getWorld(args[0]));
                    Util.sendMessage(sender, "&7Unloading world '"+world.getName()+"'...");
                    OctaneWorldsAPI.unloadWorld(world, true, null);
                    Util.sendMessage(sender, "&aSuccessfully unloaded world '"+world.getName()+"'!");
                }));

        addCommand(new SYSCommand("tp")
                .setArguments(new WorldArgument())
                .executesPlayer((player, args) -> {
                    World world = Bukkit.getWorld(args[0]);
                    assert world != null;
                    Util.sendMessage(player, "&7Teleporting to &e"+world.getName()+"&7...");
                    player.teleport(world.getSpawnLocation());
                }));

        addCommand(new SYSCommand("load")
                .setUsage("/OctaneWorlds load [world-file]")
                .setArguments(new UnloadedWorldFileArgument())
                .executes((sender, args) -> {
                    File file = new File(Bukkit.getWorldContainer(), args[0]);
                    Util.sendMessage(sender, "&7Loading world '"+file.getName()+"'...");
                    try {
                        TaskStatus taskStatus = OctaneWorldsAPI.loadWorld(file);
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                if (taskStatus.isComplete()){
                                    Util.sendMessage(sender, "&aSuccessfully loaded world '"+file.getName()+"'!");
                                    cancel();
                                }
                            }
                        }.runTaskTimer(OctaneWorlds.plugin, 0, 0);
                    } catch (InvalidWorldName e) {
                        throw new RuntimeException(e);
                    }
                }));

        addCommand(new SYSCommand("delete")
                .setArguments(new WorldArgument())
                .setUsage("/OctaneWorlds delete [world]")
                .executes((sender, args) -> {
                    World world = Bukkit.getWorld(args[0]);
                    Util.sendMessage(sender, "&7Deleting world '"+world.getName()+"'...");
                    try {
                        TaskStatus taskStatus = OctaneWorldsAPI.deleteWorld(world, null);
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                if (taskStatus.isComplete()){
                                    Util.sendMessage(sender, "&aSuccessfully deleted world '"+world.getName()+"'!");
                                    cancel();
                                }
                            }
                        }.runTaskTimer(OctaneWorlds.plugin, 0, 0);
                    } catch (InvalidWorldFolderException e) {
                        throw new RuntimeException(e);
                    }
                }));

        addCommand(new SYSCommand("list")
                .executes((sender, args) -> {
                    Util.sendMessage(sender, "&e&lWorlds recognised by OctaneWorlds:");
                    List<String> list = new ArrayList<>();
                    for (World world : Bukkit.getWorlds())
                        list.add(" &e- &b"+world.getName());
                    for (File file : Objects.requireNonNull(Bukkit.getWorldContainer().listFiles())) {
                        if (Bukkit.getWorld(file.getName()) == null){
                            if (file.isDirectory()){
                                list.add(" &e- &7"+file.getName());
                            }
                        }
                    }
                    Util.sendMessage(sender, list);
                }));

        addCommand(new SYSCommand("info")
                .executesPlayer((player, args) -> {
                    Util.sendMessage(player, "&7You are currently in world '&e"+player.getWorld().getName()+"&7`.");
                }));
    }
}
