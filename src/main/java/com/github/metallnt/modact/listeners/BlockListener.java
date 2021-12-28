package com.github.metallnt.modact.listeners;

import com.github.metallnt.modact.ModAct;
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

/**
 * Class com.github.metallnt.modact.listeners
 * Удаление блоков
 * Date: 27.12.2021 4:41 27 12 2021
 *
 * @author Metall
 */
public class BlockListener implements Listener {

    //    private final ModAct plugin;
//    private final Logger log;
    private final MessageManager message;
    private final PermCheck perm;

    public BlockListener(ModAct modAct) {
//        plugin = modAct;
//        log = modAct.getLogger();
        message = modAct.getMessageManager();
        perm = modAct.getPermCheck();
    }

    @EventHandler(priority = EventPriority.LOW) // Разбивание блока
    public void onBlockBreak(BlockBreakEvent e) {
        if (perm.permissionDenied(e.getPlayer(), ModActPermission.BLOCK_DESTROY, e.getBlock())) {
            message.onActionBar(e.getPlayer(), "Вы не можете сломать блок: " + e.getBlock().getType().name());
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW) // Размещение блока
    public void onBlockPlace(BlockPlaceEvent e) {
        if (perm.permissionDenied(e.getPlayer(), ModActPermission.BLOCK_PLACE, e.getBlock())) {
            message.onActionBar(e.getPlayer(), "Вы не можете установить: " + e.getBlock().getType().name());
            e.setCancelled(true);
        }
    }

    // Удаление висячего блока
    @EventHandler(priority = EventPriority.LOW)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent e) {
        if (e.getRemover() instanceof Player) {
            Player player = ((Player) e.getRemover()).getPlayer();
            assert player != null;
            if (perm.permissionDenied((Player) e.getRemover(), ModActPermission.BLOCK_DESTROY, e.getEntity().getType())) {
                message.onActionBar(player, "Вы не можете сломать блок: " + e.getEntity().getType().name());
                e.setCancelled(true);
            }
        }
    }

    // Разместить висячий блок
    @EventHandler(priority = EventPriority.LOW)
    public void onPaintingPlace(HangingPlaceEvent e) {
        if (perm.permissionDenied(Objects.requireNonNull(e.getPlayer()), ModActPermission.BLOCK_PLACE, e.getEntity().getType())) {
            message.onActionBar(e.getPlayer(), "Вы не можете установить: " + e.getBlock().getType().name());
            e.setCancelled(true);
        }
    }
}
