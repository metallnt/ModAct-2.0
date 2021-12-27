package com.github.metallnt.modact.listeners;

import com.github.metallnt.modact.ModAct;
import org.bukkit.event.Listener;

import java.util.logging.Logger;

/**
 * Class com.github.metallnt.modact.listeners
 * Взаимодействие с предметом
 * Date: 27.12.2021 4:35 27 12 2021
 *
 * @author Metall
 */
public class PlayerInteractListener implements Listener {

    private final ModAct plugin;
    private final Logger log;

    public PlayerInteractListener(ModAct modAct) {
        plugin = modAct;
        log = modAct.getLogger();
    }
}
