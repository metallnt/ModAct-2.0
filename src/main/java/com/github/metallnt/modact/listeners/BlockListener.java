package com.github.metallnt.modact.listeners;

import com.github.metallnt.modact.ModAct;
import com.github.metallnt.modact.configs.DefaultConfig;
import com.github.metallnt.modact.managers.MessageManager;
import com.github.metallnt.modact.permissions.ModActPermission;
import com.github.metallnt.modact.permissions.PermCheck;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;

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
    private final PermCheck perm;
    private final DefaultConfig config;

    public BlockListener(ModAct modAct) {
//        plugin = modAct;
        log = modAct.getLogger();
        message = modAct.getMessageManager();
        perm = modAct.getPermCheck();
        config = modAct.getDefaultConfig();
    }

    @EventHandler(priority = EventPriority.LOW) // Разбивание блока
    public void onBlockBreak(BlockBreakEvent e) {
        debug("BlockBreakEvent");
        if (perm.permissionDenied(e.getPlayer(), ModActPermission.BLOCK_DESTROY, e.getBlock())) {
            debug("Игроку " + e.getPlayer() + " нельзя сломать блок " + e.getBlock().getType().name());
            message.onActionBar(e.getPlayer(), "Вы не можете сломать блок: " + e.getBlock().getType().name());
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW) // Размещение блока
    public void onBlockPlace(BlockPlaceEvent e) {
        debug("BlockPlaceEvent");
        if (perm.permissionDenied(e.getPlayer(), ModActPermission.BLOCK_PLACE, e.getBlock())) {
            debug("Игроку " + e.getPlayer() + " нельзя поставить блок " + e.getBlock().getType().name());
            message.onActionBar(e.getPlayer(), "Вы не можете установить: " + e.getBlock().getType().name());
            e.setCancelled(true);
        }
    }

    // Удаление висячего блока
    @EventHandler(priority = EventPriority.LOW)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent e) {
        debug("HangingBreakByEntityEvent");
        if (e.getRemover() instanceof Player) {
            Player player = ((Player) e.getRemover()).getPlayer();
            debug("Не игрок вызвал event");
            assert player != null;
            if (perm.permissionDenied(player, ModActPermission.BLOCK_DESTROY, e.getEntity().getType())) {
                debug("Игроку " + player + " нельзя сломать " + e.getEntity().getType().name());
                message.onActionBar(player, "Вы не можете сломать блок: " + e.getEntity().getType().name());
                e.setCancelled(true);
            }
        }
    }

    // Разместить висячий блок
    @EventHandler(priority = EventPriority.LOW)
    public void onPaintingPlace(HangingPlaceEvent e) {
        debug("HangingPlaceEvent");
        if (perm.permissionDenied(Objects.requireNonNull(e.getPlayer()), ModActPermission.BLOCK_PLACE, e.getEntity().getType())) {
            debug("Игроку " + e.getPlayer() + " нельзя поставить блок " + e.getBlock().getType().name());
            message.onActionBar(e.getPlayer(), "Вы не можете установить: " + e.getBlock().getType().name());
            e.setCancelled(true);
        }
    }

    private void debug(String message) {
        if (config.getDebug()) {
            log.info(message);
        }
    }
}
