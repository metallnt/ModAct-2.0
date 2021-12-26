package com.github.metallnt.modact.configs;

import com.github.metallnt.modact.ModAct;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Class com.github.metallnt.modact.configs
 * <p>
 * Date: 26.12.2021 23:51 26 12 2021
 *
 * @author Metall
 */
public class RulesConfig extends AbstractConfig {

    private final String fileName = "lists.yml";
    public RulesConfig(ModAct modAct) {
        this.setFileName(fileName);
        this.setPlugin(modAct);
    }

    // Обновление правил с новыми настройками
    public boolean updateConfigNewOptions() {
        File rulesFile = new File(this.getPlugin().getDataFolder(), fileName);

        try {
            ConfigUpdater.update(this.getPlugin(), fileName, rulesFile, Collections.emptyList());
            this.reloadConfig();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void reloadConfig() {
        this.loadConfig();
    }

    // Получение значений
    public List<String> getTools() {
        return this.getConfig().getStringList("tools");
    }

    public List<String> getMascBlocks() {
        return this.getConfig().getStringList("blockmasks");
    }

    public List<String> getBlocks() {
        return this.getConfig().getStringList("blockexact");
    }

    public List<String> getWoods() {
        return this.getConfig().getStringList("wooden");
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
