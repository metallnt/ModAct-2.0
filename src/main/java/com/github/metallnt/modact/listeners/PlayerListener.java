package com.github.metallnt.modact.listeners;

import com.github.metallnt.modact.ModAct;
import com.github.metallnt.modact.configs.DefaultConfig;
import com.github.metallnt.modact.configs.PlayerConfig;
import com.github.metallnt.modact.configs.RulesConfig;
import com.github.metallnt.modact.managers.MessageManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Class com.github.metallnt.modact.listeners
 * Взаимодействие с предметом
 * Date: 27.12.2021 4:35 27 12 2021
 *
 * @author Metall
 */
public class PlayerListener implements Listener {

    private final Logger log;
    private final DefaultConfig config;
    private final RulesConfig rules;
    private final PlayerConfig playerConfig;
    //    private final PermCheck perm;
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
    private final List<String> itemUse;
    private final List<String> blockUse;
    private final List<String> blockDestroys;
    // Other var
    private Player player;

    public PlayerListener(ModAct modAct) {
//        perm = modAct.getPermCheck();
        log = modAct.getLogger();                   // Логгер
        config = modAct.getDefaultConfig();         // Конфигурация
        msg = modAct.getMessageManager();           // Сообщение игроку
        rules = modAct.getRulesConfig();            // Правила по блокам
        playerConfig = modAct.getPlayerConfig();    // Данные игроков по крафту
        blocksMask = rules.getMascBlocks();         // Список масок блоков
//        modAct.getLogger().info(blocksMask.toString());
        blocksExact = rules.getBlocks();            // Список точных ID блоков
        woodBlocks = rules.getWoods();              // Настройки для деревянных инстр
        tools = rules.getTools();                   // Список инструментов
        stoneBlocks = rules.getStones();            // Для каменных инстр
        ironBlocks = rules.getIrons();              // Для железных инстр
        goldBlocks = rules.getGolds();              // Для золотых
        diamondBlocks = rules.getDiamonds();        // Для алмазных
        netherBlocks = rules.getNethers();          // Для незеритовых
        itemUse = rules.getItemBlockUse();          // Предметы запрещенные для использования
        blockUse = rules.getBlockUse();             // Запрещенные блоки для использования
        blockDestroys = rules.getBlockDestroy();    // Запрещенные блоки для разрушения
    }

    /**
     * Слушатель взаимодействия с сущностью
     *
     * @param e - действие
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        if (config.getItemUse()) {
            if (itemUse.contains(e.getPlayer().getInventory().getItemInMainHand().getType().name())) {
                msg.onActionBar(e.getPlayer(), "Вы не можете использовать "
                        + e.getPlayer().getInventory().getItemInMainHand().getType().name()
                        + " на " + e.getRightClicked().getName());
                e.setCancelled(true);
            }
        }
    }

    /**
     * Слушатель взаимодействия с блоком
     *
     * @param e - действие
     */
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




    // Чарование на столе зачарования
    // Кажется тут нужно все переделать на клик по ячейке стола зачарования

    /**
     * Слушатель зачарования
     *
     * @param e Event
     */
//    @EventHandler(priority = EventPriority.LOW)
//    public void onItemEnchant(EnchantItemEvent e) {
//        if (perm.permissionDenied(e.getEnchanter(), ModActPermission.ITEM_ENCHANT, e.getItem())) {
//            msg.onActionBar(player, "Вы не можете зачаровать " + e.getItem().getType().name());
//            e.setCancelled(true);
//        }
//    }

    /**
     * Проверка правого клика по правам, отмена
     *
     * @param e      PlayerInteractEvent
     * @param player Player
     * @param block  Block
     */
    private void checkRightClickBlock(PlayerInteractEvent e, Player player, Block block) {
        if (blockUse.contains(block.getType().name())) {
            msg.onActionBar(player, "Вы не можете использовать " + block.getType().name());
            e.setCancelled(true);
        }
    }

    /**
     * Проверка левого клика по правам, отмена
     *
     * @param e      PlayerInteractEvent
     * @param player Player
     * @param block  Block
     */
    private void checkLeftClickBlock(PlayerInteractEvent e, Player player, Block block) {
        String targetBlock = block.getType().name().toLowerCase().replace("_", "");
//        boolean can = false;
        // Проверка блоков по маске
        for (String blockMask : blocksMask) {
            if (targetBlock.contains(blockMask)) {
                debug("Сработала маска: " + blockMask + " == " + targetBlock);
                return;
            }
        }
        // Проверка блоков по точному значению
        debug(blocksExact.toString());
        for (String blockExact : blocksExact) {
            if (targetBlock.equals(blockExact)) {
                debug("Сработало точное значение: " + blockExact + " == " + targetBlock);
                return;
            }
        }
        // Проверка на инструмент
        String inHandItem = player.getInventory().getItemInMainHand().getType().toString().toLowerCase().replace("_", "");
        for (String tool : tools) {
            if (tool.equals(inHandItem)) {
                debug(tool);
                // GOTO Проверка запрета инструмента
                checkToolPerm(e, tool, block);
                return;
            }
        }


        // Далее с любой рукой
        // Проверка прав на случай неучтенного
        String text;
        if (player.getInventory().getItemInMainHand().getType().name().equals("AIR")) {
            text = "Вы не можете сломать " + block.getType().name() + " рукой ";
        } else {
            text = "Вы не можете сломать " + block.getType().name() + " используя " + player.getInventory().getItemInMainHand().getType().name();
        }
        msg.onActionBar(player, text);
        e.setCancelled(true);


    }

    private void debug(String message) {
        if (config.getDebug()) {
            log.info(message);
        }
    }

    /**
     * Проверка по инструментам из списка в lists.yml
     *
     * @param e     PlayerInteractEvent
     * @param tool  String Инструмент в руке
     * @param block Block
     */
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

    /**
     * Белый список предметов (только зелья и опыт. Не знаю, что еще нужно добавить)
     *
     * @param player Player
     */
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
