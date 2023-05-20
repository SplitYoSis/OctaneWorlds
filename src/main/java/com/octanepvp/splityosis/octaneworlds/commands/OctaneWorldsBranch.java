package com.octanepvp.splityosis.octaneworlds.commands;

import com.octanepvp.splityosis.octaneworlds.OctaneWorlds;
import com.octanepvp.splityosis.octaneworlds.api.OctaneWorldsAPI;
import com.octanepvp.splityosis.octaneworlds.commands.arguments.EnvironmentArgument;
import com.octanepvp.splityosis.octaneworlds.commands.arguments.NewWorldNameArgument;
import com.octanepvp.splityosis.octaneworlds.commands.arguments.WorldArgument;
import com.octanepvp.splityosis.octaneworlds.creation.tasks.TaskStatus;
import com.octanepvp.splityosis.octaneworlds.exceptions.InvalidWorldName;
import com.octanepvp.splityosis.octaneworlds.utils.Util;
import dev.splityosis.commandsystem.SYSCommand;
import dev.splityosis.commandsystem.SYSCommandBranch;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Objects;

public class OctaneWorldsBranch extends SYSCommandBranch {

    public OctaneWorldsBranch() {
        super("OctaneWorlds", "Worlds", "ow");

        setPermission("OctaneWorlds.admin");
        setUnknownCommandMessage(Arrays.asList("&cUnknown subcommand."));

        addCommand(new SYSCommand("create")
                .setArguments(new NewWorldNameArgument(), new EnvironmentArgument())
                .setUsage("/OctaneWorlds create [world-name] [environment]")
                .executes((sender, args) -> {
                    try {
                        TaskStatus taskStatus = OctaneWorldsAPI.createWorld(OctaneWorlds.plugin.getWorldContainer(), args[0], World.Environment.valueOf(args[1]));
                        Util.sendMessage(sender, "&bCreating world '"+args[1]+"'...");
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
                        Util.sendMessage(sender, "&bCloning world '"+args[0]+"'...");
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
                    OctaneWorldsAPI.unloadWorld(Objects.requireNonNull(Bukkit.getWorld(args[0])), true);
                }));

        addCommand(new SYSCommand("tp")
                .setArguments(new WorldArgument())
                .executesPlayer((player, args) -> {
                    World world = Bukkit.getWorld(args[0]);
                    assert world != null;
                    Util.sendMessage(player, "&bTelepoting to &e"+world.getName()+"&b...");
                    player.teleport(world.getSpawnLocation());
                }));
    }
}
