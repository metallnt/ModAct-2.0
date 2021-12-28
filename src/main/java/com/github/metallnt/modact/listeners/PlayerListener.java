package com.github.metallnt.modact.listeners;

import com.github.metallnt.modact.ModAct;
import com.github.metallnt.modact.configs.DefaultConfig;
import com.github.metallnt.modact.configs.RulesConfig;
import com.github.metallnt.modact.managers.MessageManager;
import com.github.metallnt.modact.permissions.ModActPermission;
import com.github.metallnt.modact.permissions.PermCheck;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;
import java.util.Objects;

/**
 * Class com.github.metallnt.modact.listeners
 * Взаимодействие с предметом
 * Date: 27.12.2021 4:35 27 12 2021
 *
 * @author Metall
 */
public class PlayerListener implements Listener {

    private final DefaultConfig conf;
    private final PermCheck perm;
    private final MessageManager msg;
    // List tools
    private final List<String> blocksMask;
    private final List<String> blocksExact;
    private final List<String> tools;
    private final List<String> woodBlocks;
    private final List<String> stoneBlocks;
    private final List<String> ironBlocks;
    private final List<String> goldBlocks;
    private final List<String> diamondBlocks;
    private final List<String> netherBlocks;
    // Other var
    private Player player;

    public PlayerListener(ModAct modAct) {
        perm = modAct.getPermCheck();
        conf = modAct.getDefaultConfig();
        msg = modAct.getMessageManager();
        RulesConfig rules = modAct.getRulesConfig();
        blocksMask = rules.getMascBlocks();
        modAct.getLogger().info(blocksMask.toString());
        blocksExact = rules.getBlocks();
        woodBlocks = rules.getWoods();
        tools = rules.getTools();
        stoneBlocks = rules.getStones();
        ironBlocks = rules.getIrons();
        goldBlocks = rules.getGolds();
        diamondBlocks = rules.getDiamonds();
        netherBlocks = rules.getNethers();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        if (conf.getItemUse()) {
            if (perm.permissionDenied(e.getPlayer(),
                    ModActPermission.ITEM_USE,
                    e.getPlayer().getInventory().getItemInMainHand(),
                    e.getRightClicked())) {
                msg.onActionBar(e.getPlayer(), "Вы не можете использовать "
                        + e.getPlayer().getInventory().getItemInMainHand().getType().name()
                        + " на " + e.getRightClicked().getName());
                e.setCancelled(true);
            }
        }
    }

    // Взаимодействие с предметом
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!e.hasBlock()) {
            return;
        }
        Action action = e.getAction();
        player = e.getPlayer();
        Block block = e.getClickedBlock();
        if (Objects.equals(e.getHand(), EquipmentSlot.HAND)) {
            switch (action) {
                case LEFT_CLICK_BLOCK:
                    assert block != null;
                    checkLeftClickBlock(e, player, block);
                    return;
                case RIGHT_CLICK_BLOCK:
                    assert block != null;
                    checkWhiteListItems(player);
                    checkRightClickBlock(e, player, block);
                    return;
                case LEFT_CLICK_AIR:
                case RIGHT_CLICK_AIR:
                case PHYSICAL:
            }
        }
    }

    // Крафт вещей
    @EventHandler(priority = EventPriority.LOW)
    public void onItemCraft(CraftItemEvent e) {
        player = (Player) e.getWhoClicked();
        ClickType click = e.getClick();
        int expLvl = player.getLevel();
        // Отсечка использования ШИФТ+КЛИК
        if (click.isShiftClick()) {
            msg.onActionBar(player, "Вы не можете крафтить по несколько предметов одновременно");
            e.setCancelled(true);
            return;
        }
        // Проверка прав на крафт
        if (click.isLeftClick()) {
            // Отсекаем, если нет профессии
            if (perm.permissionDenied(player, ModActPermission.ITEM_CRAFT, e.getRecipe().getResult())) {
                msg.onActionBar(player, "Вы несможете скрафтить " + e.getRecipe().getResult().getType().name() + " не имея нужной профессии");
                e.setCancelled(true);
                return;
            }
            if (expLvl < 1) {
//            if (expLvl - e.getRecipe().getResult().getAmount() < 0) {
                msg.onActionBar(player, "У вас не хватает опыта для этого крафта");
                e.setCancelled(true);
                return;
            }
            if (Objects.requireNonNull(e.getCursor()).getAmount() + e.getRecipe().getResult().getAmount() < e.getRecipe().getResult().getMaxStackSize()) {
//            expLvl = expLvl - e.getRecipe().getResult().getAmount();
                expLvl = expLvl - 1;
                player.setLevel(expLvl);
            }
        }
    }

    // Чарование на столе зачарования
    // Кажется тут нужно все переделать на клик по ячейке стола зачарования
    @EventHandler(priority = EventPriority.LOW)
    public void onItemEnchant(EnchantItemEvent e) {
        if (perm.permissionDenied(e.getEnchanter(), ModActPermission.ITEM_ENCHANT, e.getItem())) {
            msg.onActionBar(player, "Вы не можете зачаровать " + e.getItem().getType().name());
            e.setCancelled(true);
        }
    }

    private void checkRightClickBlock(PlayerInteractEvent e, Player player, Block block) {
        if (perm.permissionDenied(player, ModActPermission.BLOCK_USE, block.getType())) {
            msg.onActionBar(player, "Вы не можете использовать " + block.getType().name());
            e.setCancelled(true);
        }
    }

    private void checkLeftClickBlock(PlayerInteractEvent e, Player player, Block block) {
        String targetBlock = block.getType().name().toLowerCase().replace("_", "");
        // Проверка блоков по маске
        for (String blockMask : blocksMask) {
            if (targetBlock.contains(blockMask)) {
                return;
            }
        }
        // Проверка блоков по точному значению
        for (String blockExact : blocksExact) {
            if (targetBlock.equals(blockExact)) {
                return;
            }
        }
        // Проверка на инструмент
        String inHandItem = player.getInventory().getItemInMainHand().getType().toString().toLowerCase().replace("_", "");
        for (String tool : tools) {
            if (tool.equals(inHandItem)) {
                // GOTO Проверка запрета инструмента
                checkToolPerm(e, tool, block);
                return;
            }
        }
        // Далее с любой рукой
        // Проверка прав на случай неучтеного
        if (perm.permissionDenied(player, ModActPermission.ITEM_LEFT, player.getInventory().getItemInMainHand().getType(), block.getType())) {
            String text;
            if (player.getInventory().getItemInMainHand().getType().name().equals("AIR")) {
                text = "Вы не можете сломать " + block.getType().name() + " рукой ";
            } else {
                text = "Вы не можете сломать " + block.getType().name() + " используя " + player.getInventory().getItemInMainHand().getType().name();
            }
            msg.onActionBar(player, text);
            e.setCancelled(true);
        }
    }

    // Проверка по спискам инструментов
    private void checkToolPerm(PlayerInteractEvent e, String tool, Block block) {
        player = e.getPlayer();
        String blockName = block.getType().name();
        String toolName = player.getInventory().getItemInMainHand().getType().name();
        if (tool.contains("netherite")) {
            for (String netherBlock : netherBlocks) {
                if (netherBlock.equals(block.getType().toString().toLowerCase().replace("_", ""))) {
                    msg.onActionBar(player, "Вы не можете сломать " + blockName + " используя " + toolName);
                    e.setCancelled(true);
                }
            }
        }
        if (tool.contains("diamond")) {
            for (String diamondBlock : diamondBlocks) {
                if (diamondBlock.equals(block.getType().toString().toLowerCase().replace("_", ""))) {
                    msg.onActionBar(player, "Вы не можете сломать " + blockName + " используя " + toolName);
                    e.setCancelled(true);
                }
            }
        }
        if (tool.contains("gold")) {
            for (String goldBlock : goldBlocks) {
                if (goldBlock.equals(block.getType().toString().toLowerCase().replace("_", ""))) {
                    msg.onActionBar(player, "Вы не можете сломать " + blockName + " используя " + toolName);
                    e.setCancelled(true);
                }
            }
        }
        if (tool.contains("iron")) {
            for (String ironBlock : ironBlocks) {
                if (ironBlock.equals(block.getType().toString().toLowerCase().replace("_", ""))) {
                    msg.onActionBar(player, "Вы не можете сломать " + blockName + " используя " + toolName);
                    e.setCancelled(true);
                }
            }
        }
        if (tool.contains("stone")) {
            for (String stoneBlock : stoneBlocks) {
                if (stoneBlock.equals(block.getType().toString().toLowerCase().replace("_", ""))) {
                    msg.onActionBar(player, "Вы не можете сломать " + blockName + " используя " + toolName);
                    e.setCancelled(true);
                }
            }
        }
        if (tool.contains("wooden")) {
            for (String woodBlock : woodBlocks) {
                if (woodBlock.equals(block.getType().toString().toLowerCase().replace("_", ""))) {
                    msg.onActionBar(player, "Вы не можете сломать " + blockName + " используя " + toolName);
                    e.setCancelled(true);
                }
            }
        }
    }

    private void checkWhiteListItems(Player player) {
        switch (player.getInventory().getItemInMainHand().getType()) {
            case POTION: //Только проверяйте зелье взрывное.
                if ((player.getInventory().getItemInMainHand().getItemMeta() instanceof Damageable)) {
                    break;
                }
            case EXPERIENCE_BOTTLE:
                //Отказ от зелья работает нормально, но клиент должен быть обновлен, потому что он уже уменьшил предмет.
                if (player.getInventory().getItemInMainHand().getType() == Material.POTION) {
                    player.updateInventory();
                }
                return; // нет необходимости проверять дальше
            default:
                break;
        }
    }
}
