package com.github.metallnt.modact.listeners;

import com.github.metallnt.modact.ModAct;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.logging.Logger;

/**
 * Class com.github.metallnt.modact.listeners
 * Использование предмета в руке
 * Date: 27.12.2021 4:30 27 12 2021
 *
 * @author Metall
 */
public class PlayerInteractEntityListener implements Listener {

    private final ModAct plugin;
    private final Logger log;

    public PlayerInteractEntityListener(ModAct modAct) {
        plugin = modAct;
        log = modAct.getLogger();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

    }
}
