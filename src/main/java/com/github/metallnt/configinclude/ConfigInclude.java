package com.github.metallnt.configinclude;

import com.github.metallnt.configinclude.configs.DefaultConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class ConfigInclude extends JavaPlugin {

    private static ConfigInclude plugin;

    private DefaultConfig defaultConfig;

    public static ConfigInclude getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        // Регистрируем конфиг
        setDefaultConfig(new DefaultConfig(this));

        // Загружаем конфиг
        if (!this.getDefaultConfig().loadConfig()) {
            this.getServer().getConsoleSender().sendMessage("Конфиг не загружен!");
        }

        // Обновляем конфиг
        if (this.getDefaultConfig().getUpdateConfig()) {
            if (this.getDefaultConfig().updateConfigNewOptions()) {
                this.getServer().getConsoleSender().sendMessage("Конфиг обновлен");
            } else {
                this.getServer().getConsoleSender().sendMessage("Конфиг не смог обновиться");
            }
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(DefaultConfig defaultConfig) {
        this.defaultConfig = defaultConfig;
    }
}
