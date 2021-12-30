package com.github.metallnt.modact.configs;

import com.github.metallnt.modact.ModAct;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

/**
 * Class com.github.metallnt.modact.configs
 * Этот класс используется для доступа к свойствам файла config.yml
 * Date: 19.12.2021 15:44 19 12 2021
 *
 * @author Metall
 */
public class DefaultConfig {

    private final ModAct plugin;
    private final String fileName = "config.yml";
    private YamlConfig yamlConfig;

    public DefaultConfig(final ModAct modAct) {
        this.plugin = modAct;
    }

    /**
     * Этот метод проверяет, есть ли в файле (в JAR) параметры, которых нет в файле на диске. Если так,
     * он добавит эти значения при сохранении комментариев файла на диске.
     *
     * @return true, если файл был успешно проверен (и при необходимости обновлен), иначе false.
     */
    public boolean updateConfigNewOptions() {
        File configFile = new File(plugin.getDataFolder(), fileName);

        try {
            ConfigUpdater.update(plugin, fileName, configFile, Collections.emptyList());
            this.reloadConfig();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void reloadConfig() {
        if (loadConfig()) {
            plugin.getServer().getConsoleSender().sendMessage("config reloaded!");
        }
    }

    public boolean loadConfig() {
        try {
            this.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void createNewFile() throws InvalidConfigurationException {
        yamlConfig = new YamlConfig(plugin, fileName, fileName);
        plugin.getServer().getConsoleSender().sendMessage("Создан файл " + fileName);
    }

    private FileConfiguration getConfig() {
        if (yamlConfig != null) {
            return yamlConfig;
        }
        return null;
    }

    // Вставляем функции получения конкретных значений из конфига

    /**
     * Включена ли функция проверки использования предметов
     *
     * @return true, если нужна проверка, false, если пропустить и работать по умолчанию
     */
    public boolean getItemUse() {
        assert this.getConfig() != null;
        return this.getConfig().getBoolean("item_use_check", true);
    }

    /**
     * Включение сообщений деббага в консоль
     *
     * @return true - сообщения включены
     */
    public boolean getDebug() {
        assert this.getConfig() != null;
        return this.getConfig().getBoolean("debug", true);
    }
}
