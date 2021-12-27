package com.github.metallnt.modact.listeners;

import com.github.metallnt.modact.ModAct;
import org.bukkit.event.Listener;

import java.util.logging.Logger;

/**
 * Class com.github.metallnt.modact.listeners
 * Крафт
 * Date: 27.12.2021 4:37 27 12 2021
 *
 * @author Metall
 */
public class CraftItemListener implements Listener {

    private final ModAct plugin;
    private final Logger log;

    public CraftItemListener(ModAct modAct) {
        plugin = modAct;
        log = modAct.getLogger();
    }
}
