package com.github.metallnt.modact;

import com.github.metallnt.modact.configs.DefaultConfig;
import com.github.metallnt.modact.configs.RulesConfig;
import com.github.metallnt.modact.listeners.*;
import org.bukkit.event.Listener;
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

        // Загружаем конфиги
        SetDataFiles();

        // Listeners
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        getServer().getPluginManager().registerEvents(new CraftItemListener(this), this);
        getServer().getPluginManager().registerEvents(new EnchantmentItemListener(this), this);
        getServer().getPluginManager().registerEvents(new HangingBreakByEntityListener(this), this);
        getServer().getPluginManager().registerEvents(new HangingPlaceListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractEntityListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
    }


    // Конфиг-файлы. Установка + обновление
    private void SetDataFiles() {
        if (!this.getDefaultConfig().loadConfig()) {
            getLogger().info("Config не загружен!");
        } else {
            // Обновляем
            if (this.getDefaultConfig().updateConfigNewOptions()) {
                getLogger().info("Config обновлен");
            } else {
                getLogger().info("Config не смог обновиться");
            }
        }
        if (!this.getRulesConfig().loadConfig()) {
            getLogger().info("Rules не загружен");
        } else {
            if (this.getRulesConfig().updateConfigNewOptions()) {
                getLogger().info("Rules обновлен");
            } else {
                getLogger().info("Rules не смог обновиться");
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
