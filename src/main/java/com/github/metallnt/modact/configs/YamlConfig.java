package com.github.metallnt.modact.configs;

import com.github.metallnt.modact.ModAct;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;

/**
 * Class com.github.metallnt.modact.configs
 * Это любой файл YAML, который использует ModAct.
 * config.yml, lists.yml...
 * Date: 19.12.2021 15:45 19 12 2021
 *
 * @author Metall
 */
public class YamlConfig extends YamlConfiguration {
    File file;

    //

    /**
     * Создаем новый файл YAML
     *
     * @param modAct   Плагин
     * @param fileName Путь к файлу
     * @param name     Имя файла для консоли
     * @throws InvalidConfigurationException Когда не может быть загружен
     */
    public YamlConfig(final ModAct modAct, final String fileName, final String name) throws InvalidConfigurationException {

        // ринимает null как configDefaults -> проверяет наличие ресурса и копирует его, если найден,
        // делает пустой конфиг, если ничего не найдено.
         
        final String folderPath = modAct.getDataFolder().getAbsolutePath() + File.separator;
        file = new File(folderPath + fileName);

        // Если файл не существует
        if (!file.exists()) {
            // Если ресурс файла существует
            if (modAct.getResource(fileName) != null) {
                // Сохраняем конфиг из ресурсов (копированием)
                modAct.saveResource(fileName, false);
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
        } catch (final InvalidConfigurationException | IOException ignored) {

        }
    }

    // Сохраняем файл YAML
    public void saveFile() {
        try {
            // Если файл пустой
            if (file == null) {
                ModAct.getInstance().getServer().getConsoleSender().sendMessage("Невозможно сохранить файл, т.к. он пустой");
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
