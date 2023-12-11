package com.github.metallnt.modact.listeners;

import com.github.metallnt.modact.ModAct;
import com.github.metallnt.modact.configs.DefaultConfig;
import com.github.metallnt.modact.configs.RulesConfig;
import com.github.metallnt.modact.managers.MessageManager;
import com.github.metallnt.modact.permissions.ModActPermission;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Class com.github.metallnt.modact.listeners
 * Удаление блоков
 * Date: 27.12.2021 4:41 27 12 2021
 *
 * @author Metall
 */
public class BlockListener implements Listener {

    //    private final ModAct plugin;
    private final Logger log;
    private final MessageManager message;
    //    private final PermCheck perm;
    private final DefaultConfig config;
    private final RulesConfig rules;
    private String blockName;
    private final List<String> blockDestroys;
    private final List<String> blockPlaces;

    public BlockListener(ModAct modAct) {
//        plugin = modAct;
        log = modAct.getLogger();
        message = modAct.getMessageManager();
//        perm = modAct.getPermCheck();
        config = modAct.getDefaultConfig();
        rules = modAct.getRulesConfig();
        blockDestroys = rules.getBlockDestroy();
        blockPlaces = rules.getBlockPlace();
    }

    @EventHandler(priority = EventPriority.LOW) // Разбивание блока
    public void onBlockBreak(BlockBreakEvent e) {
        blockName = e.getBlock().getType().name().toLowerCase().replace("_", "");
        debug("BlockBreakEvent");
        debug(e.getBlock().getType().name());
        for (String blockDestroy : blockDestroys) {
            if (blockName.equals(blockDestroy)) {
                debug("Игроку " + e.getPlayer() + " нельзя сломать блок " + blockName);
                message.onActionBar(e.getPlayer(), "Вы не можете сломать блок: " + blockName + "(" + ModActPermission.BLOCK_DESTROY + ")");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW) // Размещение блока
    public void onBlockPlace(BlockPlaceEvent e) {
        blockName = e.getBlock().getType().name();
        debug("BlockPlaceEvent");
        if (blockPlaces.contains(blockName)) {
            debug("Игроку " + e.getPlayer() + " нельзя поставить блок " + blockName);
            message.onActionBar(e.getPlayer(), "Вы не можете установить: " + blockName + "(" + ModActPermission.BLOCK_PLACE + ")");
            e.setCancelled(true);
        }
    }

    // Удаление висячего блока
    @EventHandler(priority = EventPriority.LOW)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent e) {
        blockName = e.getEntity().getType().name();
        debug("HangingBreakByEntityEvent");
        if (e.getRemover() instanceof Player) {
            Player player = ((Player) e.getRemover()).getPlayer();
            debug("Не игрок вызвал event");
            assert player != null;
            if (blockDestroys.contains(blockName)) {
                debug("Игроку " + player + " нельзя сломать " + blockName);
                message.onActionBar(player, "Вы не можете сломать блок: " + blockName + "(" + ModActPermission.BLOCK_DESTROY + ")");
                e.setCancelled(true);
            }
        }
    }

    // Разместить висячий блок
    @EventHandler(priority = EventPriority.LOW)
    public void onPaintingPlace(HangingPlaceEvent e) {
        blockName = e.getEntity().getType().name();
        debug("HangingPlaceEvent");
        if (blockPlaces.contains(blockName)) {
            debug("Игроку " + e.getPlayer() + " нельзя поставить блок " + blockName);
            message.onActionBar(Objects.requireNonNull(e.getPlayer()), "Вы не можете установить: " + blockName + "(" + ModActPermission.BLOCK_PLACE + ")");
            e.setCancelled(true);
        }
    }

    private void debug(String message) {
        if (config.getDebug()) {
            log.info(message);
        }
    }
}
