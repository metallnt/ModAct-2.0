package com.github.metallnt.configinclude.configs;

import com.github.metallnt.configinclude.ConfigInclude;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Class com.github.metallnt.configinclude.configs
 * <p>
 * Date: 19.12.2021 15:44 19 12 2021
 *
 * @author Metall
 */
public class AbstractConfig {

    private YamlConfig yamlConfig;
    private ConfigInclude plugin;
    private String fileName;
    private boolean isLoaded = false;

    // Создаем новый файл конфига
    public void createNewFile() throws InvalidConfigurationException {
        yamlConfig = new YamlConfig(plugin, fileName, fileName);
        plugin.getServer().getConsoleSender().sendMessage("Загружен файл " + fileName);
    }

    // Получаем файл конфига
    public FileConfiguration getConfig() {
        // Если файл YAML не пустой, то возвращаем его
        if (yamlConfig != null) {
            return yamlConfig;
        }
        // Иначе null
        return null;
    }

    // Перезагрузка конфига
    public void reloadConfig() {
        if (yamlConfig != null) {
            yamlConfig.reloadFile();
        }
    }

    // Сохраняем файл YAML
    public void saveConfig() {
        if (yamlConfig != null) {
            yamlConfig.saveFile();
        }
    }

    // Загружаем файл YAML
    public boolean loadConfig() {
        try {
            this.createNewFile();
            isLoaded = true;
        } catch (Exception e) {
            isLoaded = false;
            return false;
        }
        return true;
    }

    // Чекаем загружен ли файл конфига
    public boolean isLoaded() {
        return isLoaded;
    }

    // Получаем имя файла
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ConfigInclude getPlugin() {
        return plugin;
    }

    public void setPlugin(ConfigInclude plugin) {
        this.plugin = plugin;
    }
}
