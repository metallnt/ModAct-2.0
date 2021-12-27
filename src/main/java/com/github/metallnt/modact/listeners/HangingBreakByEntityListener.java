package com.github.metallnt.modact.listeners;

import com.github.metallnt.modact.ModAct;
import org.bukkit.event.Listener;

import java.util.logging.Logger;

/**
 * Class com.github.metallnt.modact.listeners
 * Удаление висячих блоков
 * Date: 27.12.2021 4:42 27 12 2021
 *
 * @author Metall
 */
public class HangingBreakByEntityListener implements Listener {

    private final ModAct plugin;
    private final Logger log;

    public HangingBreakByEntityListener(ModAct modAct) {
        plugin = modAct;
        log = modAct.getLogger();
    }
}
