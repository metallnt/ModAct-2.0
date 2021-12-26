package com.github.metallnt.configinclude.configs;

import com.github.metallnt.configinclude.ConfigInclude;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;

/**
 * Class com.github.metallnt.configinclude.configs
 * <p>
 * Date: 19.12.2021 15:45 19 12 2021
 *
 * @author Metall
 */
public class YamlConfig extends YamlConfiguration {
    File file;

    // Создаем новый файл YAML
    public YamlConfig(final ConfigInclude configInclude, final String fileName, final String name) throws InvalidConfigurationException {
        final String folderPath = configInclude.getDataFolder().getAbsolutePath() + File.separator;
        file = new File(folderPath + fileName);

        // Если файл не существует
        if (!file.exists()) {
            // Если ресурс файла существует
            if (configInclude.getResource(fileName) != null) {
                // Сохраняем конфиг из ресурсов (копированием)
                configInclude.saveResource(fileName, false);
                // И загружаем файл
                try {
                    this.load(file);
                } catch (final Exception e) {
                    throw new InvalidConfigurationException(e.getMessage());
                }
            }
        } else {
            // Иначе просто загружаем файл
            try {
                this.load(file);
            } catch (final Exception e) {
                throw new InvalidConfigurationException(e.getMessage());
            }
        }
    }

    // Получаем внутренний файл YAML
    public File getInternalFile() {
        return file;
    }

    // Загружаем файл YAML
    public void loadFile() {
        try {
            this.load(file);
        } catch (final IOException | InvalidConfigurationException ignored) {

        }
    }

    // Сохраняем файл YAML
    public void saveFile() {
        try {
            // Если файл пустой
            if (file == null) {
                ConfigInclude.getInstance().getServer().getConsoleSender().sendMessage("Невозможно сохранить файл, т.к. он пустой");
                return;
            }
            this.save(file);
        } catch (ConcurrentModificationException e) {
            saveFile();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    // Перезагрузка файла YAML
    public void reloadFile() {
        loadFile();
        saveFile();
    }
}
