package com.github.metallnt.modact.configs;

import com.github.metallnt.modact.ModAct;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Class com.github.metallnt.modact.configs
 * Данные файла lists.yml
 * В нем содержатся списки значений по умолчанию.
 * Этот класс читает и изменяет эти списки.(пока что только чтение реализовано)
 * Date: 26.12.2021 23:51 26 12 2021
 *
 * @author Metall
 */
public class PlayerConfig {

    private final ModAct plugin;
    private final String fileName = "data.yml";
    private YamlConfig yamlConfig;

    public PlayerConfig(final ModAct modAct) {
        this.plugin = modAct;
    }

    public void reloadConfig() {
        this.loadConfig();
    }

    public boolean loadConfig() {
        if (this.getConfig() == null) {
            try {
                this.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private void createNewFile() throws InvalidConfigurationException {
        yamlConfig = new YamlConfig(plugin, fileName, fileName);
        plugin.getServer().getConsoleSender().sendMessage("Создан файл " + fileName);
    }

    public FileConfiguration getConfig() {
        // Если файл YAML не пустой, то возвращаем его
        if (yamlConfig != null) {
            return yamlConfig;
        }
        // Иначе null
        return null;
    }

    // Установка значения
    private void setData(String player, boolean b) {
        this.getConfig().set(player + ".notification", b);
        this.saveConfig();
    }

    private void saveConfig() {
        if (yamlConfig == null) {
            return;
        }
        yamlConfig.saveFile();
    }

    // Получение значений
    public boolean getNotification(String player) {
        return this.getConfig().getBoolean(player + ".notification", true);
    }

    // Получение списка записаных игроков
    public List<String> getPlayers() {
        return new ArrayList<>(this.getConfig().getKeys(false));
    }

    // Задаем значение
    public void setNotification(String player) {
        setData(player, !getNotification(player));
    }
}
