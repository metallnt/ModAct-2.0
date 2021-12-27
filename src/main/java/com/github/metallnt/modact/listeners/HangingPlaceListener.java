package com.github.metallnt.modact.listeners;

import com.github.metallnt.modact.ModAct;
import org.bukkit.event.Listener;

import java.util.logging.Logger;

/**
 * Class com.github.metallnt.modact.listeners
 * Размещение висячих блоков
 * Date: 27.12.2021 4:42 27 12 2021
 *
 * @author Metall
 */
public class HangingPlaceListener implements Listener {

    private final ModAct plugin;
    private final Logger log;

    public HangingPlaceListener(ModAct modAct) {
        plugin = modAct;
        log = modAct.getLogger();
    }
}
