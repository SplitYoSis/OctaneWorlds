package com.octanepvp.splityosis.octaneworlds.creation;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

public class CreationListeners implements Listener {

    @EventHandler
    public void init (WorldInitEvent e){
        e.getWorld().setKeepSpawnInMemory(false);
    }
}
