package com.github.metallnt.modact.listeners;

import com.github.metallnt.modact.ModAct;
import org.bukkit.event.Listener;

import java.util.logging.Logger;

/**
 * Class com.github.metallnt.modact.listeners
 * Размещение блоков
 * Date: 27.12.2021 4:41 27 12 2021
 *
 * @author Metall
 */
public class BlockPlaceListener implements Listener {

    private final ModAct plugin;
    private final Logger log;

    public BlockPlaceListener(ModAct modAct) {
        plugin = modAct;
        log = modAct.getLogger();
    }
}
