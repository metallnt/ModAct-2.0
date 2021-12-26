package com.github.metallnt.modact.configs;

import com.github.metallnt.modact.ModAct;

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
public class DefaultConfig extends AbstractConfig {

    private final String fileName = "config.yml";

    public DefaultConfig(final ModAct modAct) {
        this.setFileName(fileName);
        this.setPlugin(modAct);
    }

    // Обновление конфига с новыми настройками
    public boolean updateConfigNewOptions() {
        File configFile = new File(this.getPlugin().getDataFolder(), fileName);

        try {
            ConfigUpdater.update(this.getPlugin(), fileName, configFile, Collections.emptyList());
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

    // Вставляем функции получения конкретных значений из конфига
    public boolean getUpdateConfig() {
        return this.getConfig().getBoolean("update_config", true);
    }

}
