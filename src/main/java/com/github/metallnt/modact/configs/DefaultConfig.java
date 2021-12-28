package com.github.metallnt.modact.configs;

import com.github.metallnt.modact.ModAct;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

/**
 * Class com.github.metallnt.modact.configs
 * <p>
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

    // Обновление конфига с новыми настройками
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
        this.loadConfig();
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

    public boolean getItemUse() {
        assert this.getConfig() != null;
        return this.getConfig().getBoolean("item_use_check", true);
    }

    public boolean getDebug() {
        assert this.getConfig() != null;
        return this.getConfig().getBoolean("debug", true);
    }
}
