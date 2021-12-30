package com.github.metallnt.modact.permissions;

import com.github.metallnt.modact.ModAct;
import com.github.metallnt.modact.configs.DefaultConfig;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Logger;

/**
 * Class com.github.metallnt.modact.permissions
 *
 * Date: 27.12.2021 5:31 27 12 2021
 *
 * @author Metall
 */
public class PermCheck {

    private final DefaultConfig config;
    private final Logger log;
    RegisteredServiceProvider<LuckPerms> provider;

    public PermCheck(ModAct modAct) {
        config = modAct.getDefaultConfig();
        log = modAct.getLogger();
    }

    /**
     * Проверка на отсутствие прав
     *
     * @param player         Игрок, которого проверяем
     * @param basePermission Базовая строка прав
     * @param args           Аргументы для составления полной строки прав
     * @return true, если у игрока нет прав. false, если права есть
     */
    public boolean permissionDenied(Player player, String basePermission, Object... args) {
        User user = getApi().getPlayerAdapter(Player.class).getUser(player);
        String permission = assemblePermission(basePermission, args);
        debug(user.getUsername() + " - Итог по правам: " + permission + " " + hasPermission(user, permission));
        return !hasPermission(user, permission);
    }

//    protected boolean _permissionDenied(Player player, String permission, Object... arguments) {
//        User user = getApi().getPlayerAdapter(Player.class).getUser(player);
//        return !hasPermission(user, assemblePermission(permission, arguments));
//    }

    /**
     * Проверка прав с готовой строкой прав. LuckPerms!!!!
     *
     * @param user       Пользователь взятый с API LuckPerms
     * @param permission Строка прав
     * @return true, если игрок имеет эти права
     */
    private boolean hasPermission(User user, String permission) {
        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }

    /**
     * Получение API LuckPerms
     *
     * @return LuckPerms.class
     */
    private LuckPerms getApi() {
        provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        assert provider != null;
        return provider.getProvider();
    }

    /**
     * Преобразователь строки прав из полученных данных
     *
     * @param permission Базовая строка прав
     * @param args       Аргументы
     * @return Готовая/Полная строка прав
     */
    private String assemblePermission(String permission, Object[] args) {
        StringBuilder builder = new StringBuilder(permission);
        if (args != null) {
            for (Object obj : args) {
                if (obj == null) {
                    continue;
                }
                builder.append('.');
                builder.append(getObjectPermission(obj));
            }
        }
        return builder.toString();
    }

    /**
     * Конструкция деббага
     *
     * @param message Сообщение, которое нужно передать, если деббаг включен в настройках
     */
    private void debug(String message) {
        if (config.getDebug()) {
            log.info(message);
        }
    }

    protected String getObjectPermission(Object obj) {
        if (obj instanceof Entity) {
            return (getEntityName((Entity) obj));
        } else if (obj instanceof EntityType) {
            return formatEnumString(((EntityType) obj).name());
        } else if (obj instanceof BlockState) {
            return (getBlockPermission(((BlockState) obj).getBlock()));
        } else if (obj instanceof ItemStack) {
            return (getItemPermission((ItemStack) obj));
        } else if (obj instanceof Material) {
            return (getMaterialPermission((Material) obj));
        } else if (obj instanceof Block) {
            return (getBlockPermission((Block) obj));
        } else if (obj instanceof InventoryType) {
            return getInventoryTypePermission((InventoryType) obj);
        }
        return (obj.toString());
    }

    private String getEntityName(Entity entity) {
        if (entity instanceof ComplexEntityPart) {
            return getEntityName(((ComplexEntityPart) entity).getParent());
        }
        String entityName = formatEnumString(entity.getType().toString());
        if (entity instanceof Item) {
            entityName = getItemPermission(((Item) entity).getItemStack());
        }
        if (entity instanceof Player) {
            return "player." + entity.getName();
        } else if (entity instanceof Tameable) {
            Tameable animal = (Tameable) entity;
            return "animal." + entityName + (animal.isTamed() && animal.getOwner() != null ? "." + animal.getOwner().getName() : " ");
        }

        EntityCategory category = EntityCategory.fromEntity(entity);
        if (category == null) {
            return entityName;
        }
        return category.getNameDot() + entityName;
    }

    /**
     * Редактор названия, который убирает лишние подчеркивания
     *
     * @param enumName Строка на редактирование
     * @return Отредактированная строка
     */
    private String formatEnumString(String enumName) {
        return enumName.toLowerCase().replace("_", "");
    }

    private String getInventoryTypePermission(InventoryType type) {
        return formatEnumString(type.name());
    }

    private String getMaterialPermission(Material type) {
        return formatEnumString(type.name());
    }

    private String getBlockPermission(Block block) {
        return getMaterialPermission(block.getType());
    }

    public String getItemPermission(ItemStack item) {
        return getMaterialPermission(item.getType());
    }
}
