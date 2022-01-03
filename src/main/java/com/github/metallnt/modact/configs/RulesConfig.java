package com.github.metallnt.modact.configs;

import com.github.metallnt.modact.ModAct;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
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
public class RulesConfig {

    private final ModAct plugin;
    private final String fileName = "lists.yml";
    private YamlConfig yamlConfig;

    public RulesConfig(final ModAct modAct) {
        this.plugin = modAct;
    }

    // Обновление правил с новыми настройками (возможно не понадобится никогда)
    public boolean updateConfigNewOptions() {
        File rulesFile = new File(plugin.getDataFolder(), fileName);

        try {
            ConfigUpdater.update(plugin, fileName, rulesFile, Collections.emptyList());
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

    public boolean addData(String arg, List<String> ids) {
        if (arg.equals("mask") || arg.equals("exact") || arg.equals("wood") || arg.equals("stone")
                || arg.equals("iron") || arg.equals("gold") || arg.equals("diamond") || arg.equals("nether")) {
            for (String id : ids) {
                id = id.replace("_", "").toLowerCase();
                this.getConfig().set(arg, id);
                this.saveConfig();
            }
        } else {
            return false;
        }
        return true;
    }

    private void saveConfig() {
        if (yamlConfig == null) {
            return;
        }
        yamlConfig.saveFile();
    }

    public FileConfiguration getConfig() {
        // Если файл YAML не пустой, то возвращаем его
        if (yamlConfig != null) {
            return yamlConfig;
        }
        // Иначе null
        return null;
    }

    // Получение значений
    public List<String> getTools() {
        return this.getConfig().getStringList("tools");
    }

    public List<String> getMascBlocks() {
        return this.getConfig().getStringList("mask");
    }

    public List<String> getBlocks() {
        return this.getConfig().getStringList("exact");
    }

    public List<String> getWoods() {
        return this.getConfig().getStringList("wood");
    }

    public List<String> getStones() {
        return this.getConfig().getStringList("stone");
    }

    public List<String> getIrons() {
        return this.getConfig().getStringList("iron");
    }

    public List<String> getGolds() {
        return this.getConfig().getStringList("gold");
    }

    public List<String> getDiamonds() {
        return this.getConfig().getStringList("diamond");
    }

    public List<String> getNethers() {
        return this.getConfig().getStringList("nether");
    }
}
