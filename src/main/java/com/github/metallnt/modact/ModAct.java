package com.github.metallnt.modact;

import com.github.metallnt.modact.configs.DefaultConfig;
import com.github.metallnt.modact.configs.RulesConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class ModAct extends JavaPlugin {

    private static ModAct plugin;

    private DefaultConfig defaultConfig;
    private RulesConfig rulesConfig;

    public static ModAct getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        // Регистрируем конфиг
        setDefaultConfig(new DefaultConfig(this));
        setRulesConfig(new RulesConfig(this));

        // Загружаем конфиг
        if (!this.getDefaultConfig().loadConfig()) {
            this.getServer().getConsoleSender().sendMessage("Config не загружен!");
        } else {
            // Обновляем
            if (this.getDefaultConfig().updateConfigNewOptions()) {
                this.getServer().getConsoleSender().sendMessage("Config обновлен");
            } else {
                this.getServer().getConsoleSender().sendMessage("Config не смог обновиться");
            }
        }
        if (!this.getRulesConfig().loadConfig()) {
            this.getServer().getConsoleSender().sendMessage("Rules не загружен");
        } else {
            if (this.getRulesConfig().updateConfigNewOptions()) {
                this.getServer().getConsoleSender().sendMessage("Rules обновлен");
            } else {
                this.getServer().getConsoleSender().sendMessage("Rules не смог обновиться");
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

    public RulesConfig getRulesConfig() {
        return rulesConfig;
    }

    public void setRulesConfig(RulesConfig rulesConfig) {
        this.rulesConfig = rulesConfig;
    }
}
