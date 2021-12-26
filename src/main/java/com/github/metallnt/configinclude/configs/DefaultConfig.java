package com.github.metallnt.configinclude.configs;

import com.github.metallnt.configinclude.ConfigInclude;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

/**
 * Class com.github.metallnt.configinclude.configs
 * <p>
 * Date: 19.12.2021 15:44 19 12 2021
 *
 * @author Metall
 */
public class DefaultConfig extends AbstractConfig {

    private final String fileName = "config.yml";

    public DefaultConfig(final ConfigInclude configInclude) {
        this.setFileName(fileName);
        this.setPlugin(configInclude);
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

    // Вставляем фенкции получения конкретных значений из конфига
    public boolean getUpdateConfig() {
        return this.getConfig().getBoolean("update_config", true);
    }

}
