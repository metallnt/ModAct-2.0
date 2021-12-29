package com.github.metallnt.modact.permissions;

/**
 * Классласс ModActPermission
 * <p>
 * Содержит переменные разрешений
 * <p>
 * Дата изменения: 18:34:00 04 ноя. 2021
 *
 * @author Metall
 */
public final class ModActPermission {
    public static final String RELOAD_PERM = "modact.reload"; // Разрешение на перезагрузку
    public static final String HELP_PERM = "modact.help"; // Разрешение на справку
    public static final String ADD_PERM = "modact.add"; // Разрешение на добавление в списки
    public static final String DEL_PERM = "modact.delete"; // Разрешение на удаление из списков

    public static final String BLOCK_DESTROY = "modact.block.destroy"; // Разрушение блока
    public static final String BLOCK_PLACE = "modact.block.place"; // Размещение блока
    public static final String DAMAGE_DEAL = "modact.damage.deal"; // Нанесение урона
    public static final String DAMAGE_TAKE = "modact.damage.take"; // Получение урона
    public static final String TAME = "modact.tame"; // Приручение
    public static final String TARGET = "modact.target"; // Цель
    public static final String BLOCK_USE = "modact.block.use"; // Использование блока
    public static final String ITEM_USE = "modact.item.use"; // Использование предмета
    public static final String ITEM_THROW = "modact.item.throw"; //
    public static final String ITEM_LEFT = "modact.item.left"; // ЛКМ чем и по чему
    public static final String ITEM_RIGHT = "modact.item.right"; // ПКМ чем и по чему
    public static final String ITEM_ENCHANT = "modact.item.enchant"; // Чарование на столе
    public static final String ITEM_CRAFT = "modact.item.craft"; // Крафт
    public static final String CAN_CRAFT = "modact.can"; // Уже крафтил?
    public static final String ITEM_INTERACT = "modact.item.interact"; // Получение ошибок в чат
    public static final String VEHICLE_DESTROY = "modact.vehicle.destroy"; // Разрушение транспорта
    public static final String VEHICLE_ENTER = "modact.vehicle.enter"; // Посадка в транспорт
    public static final String VEHICLE_COLLIDE = "modact.vehicle.collide"; //
    public static final String NOTICE_ON_WARNINGS = "modact.warning"; // Получение ошибок в чат

    private ModActPermission() {
    }
}
