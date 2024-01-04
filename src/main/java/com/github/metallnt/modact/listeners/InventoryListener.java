package com.github.metallnt.modact.listeners;

import com.github.metallnt.modact.ModAct;
import com.github.metallnt.modact.configs.DefaultConfig;
import com.github.metallnt.modact.configs.PlayerConfig;
import com.github.metallnt.modact.managers.MessageManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Arrays;
import java.util.Objects;
import java.util.SplittableRandom;
import java.util.logging.Logger;

public class InventoryListener implements Listener {

    private final Logger log; // Логгер
    private final MessageManager msg; // Сообщение игроку
    private final DefaultConfig config; // Конфигурация
    private final PlayerConfig playerConfig; // Данные игрока
    Player player;

    public InventoryListener(ModAct modAct) {
        log = modAct.getLogger();
        msg = modAct.getMessageManager();
        config = modAct.getDefaultConfig();
        playerConfig = modAct.getPlayerConfig();
    }

    /**
     * Слушатель крафта
     *
     * @param e - действие
     */

    @EventHandler(priority = EventPriority.LOW)
    public void onItemCraft(CraftItemEvent e) {
        player = (Player) e.getWhoClicked();
        ClickType clickType = e.getClick();
        switch (clickType) {
            case LEFT:
                leftClick(player, e);
                return;
            case RIGHT:
                rightClick(player, e);
                return;
            case SHIFT_LEFT:
            case SHIFT_RIGHT:
                shiftClick(player, e);
                return;
            case MIDDLE:
        }
    }

    /**
     * Обработка через shift
     */
    private void shiftClick(Player player, CraftItemEvent e) {
        msg.onActionBar(player, "Вы не можете крафтить по несколько предметов одновременно");
        e.setCancelled(true);
    }

    /** Обработка правого клика */
    private void rightClick(Player player, CraftItemEvent e) {
        msg.onActionBar(player, "Вы можете крафтить только левой кнопкой мышки");
        e.setCancelled(true);
    }

    /** Обработка левого клика */
    private void leftClick(Player player, CraftItemEvent e) {
        InventoryType inventoryType = e.getInventory().getType();
        debug(inventoryType.toString());
        switch (inventoryType) {
            case CRAFTING: // Инвентарь
            case WORKBENCH: // Верстак
                e.getInventory().setMaxStackSize(1);
                changeExpChance(player, e);
                return;
            default:
                break;
        }
    }

    /**
     * Изменение уровней опыта
     * @param player Игрок
     * @param e Событие
     */
    private void changeExpChance(Player player, CraftItemEvent e) {
        int expLvl = player.getLevel();
        // Если нет опыта - отмена
        if (expLvl < 1) {
            msg.onActionBar(player, "У вас не хватает опыта для этого крафта");
            e.setCancelled(true);
            return;
        }
        expLvl = expLvl - 1;
        player.setLevel(expLvl);
        String username = player.getName();
        randomChance(username, e);
    }

    /**
     * Рандом обработки крафта в зависимости от опыта игрока
     * @param username Игрок
     * @param e Событие
     */
    private void randomChance(String username, CraftItemEvent e) {
        String itemName = Objects.requireNonNull(e.getInventory().getResult()).getType().toString();
        String result;
        debug(itemName);
        int chance = playerConfig.getUserData(username, itemName); // Шанс игрока
        SplittableRandom random = new SplittableRandom();
        int r = random.nextInt(1, 100);
        if (r <= chance) {
            result = " Удачно! ";
            debug("Выпало число " + r + ", Которое меньше " + chance);
            if (chance + 5 > 99) {
                chance = 99;
            } else {
                chance = chance + 5;
            }
            playerConfig.setUserData(username, itemName, chance);
        } else {
            result = " Не удачно! ";
            debug("Выпало число " + r + ", Которое больше " + chance);
            if (chance + 1 > 99) {
                chance = 99;
            } else {
                chance = chance + 1;
            }
            playerConfig.setUserData(username, itemName, chance);
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1f, 1f);
            log.info(itemName);
            log.info(Arrays.toString(e.getInventory().getMatrix()));
            e.getInventory().clear();

        }
        msg.onActionBar(player, result + "Ваш шанс на " + itemName + " = " + chance + "%");

    }

    private void debug(String message) {
        if (config.getDebug()) {
            log.info(message);
        }
    }
}
