package com.github.metallnt.modact;

import com.github.metallnt.modact.commands.manager.CommandsManager;
import com.github.metallnt.modact.configs.DefaultConfig;
import com.github.metallnt.modact.configs.RulesConfig;
import com.github.metallnt.modact.listeners.BlockListener;
import com.github.metallnt.modact.listeners.PlayerListener;
import com.github.metallnt.modact.managers.MessageManager;
import com.github.metallnt.modact.permissions.PermCheck;
import org.bukkit.plugin.java.JavaPlugin;

public final class ModAct extends JavaPlugin {

    private static ModAct plugin;

    private DefaultConfig defaultConfig;
    private RulesConfig rulesConfig;
    private PermCheck permCheck;
    private MessageManager messageManager;
    private CommandsManager commandsManager;

    public static ModAct getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        // Регистрируем конфиг
        setDefaultConfig(new DefaultConfig(this));
        setRulesConfig(new RulesConfig(this));

        setMessageManager(new MessageManager(this));
        setPermCheck(new PermCheck(this));
        setCommandsManager(new CommandsManager(this));
        // Загружаем конфиги
        SetDataFiles();

        // Listeners
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    /**
     * Конфиг-файлы. Установка + обновление
     */
    private void SetDataFiles() {
        // Обновляем
        if (this.getDefaultConfig().updateConfigNewOptions()) {
            getLogger().info("Config обновлен");
        } else {
            getLogger().info("Config не смог обновиться");
        }
        if (!this.getRulesConfig().loadConfig()) {
            getLogger().info("Rules не загружен");
        } else {
            getLogger().info("Rules loaded!");
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

    public PermCheck getPermCheck() {
        return permCheck;
    }

    public void setPermCheck(PermCheck permCheck) {
        this.permCheck = permCheck;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public void setMessageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    public CommandsManager getCommandsManager() {
        return commandsManager;
    }

    public void setCommandsManager(CommandsManager commandsManager) {
        this.commandsManager = commandsManager;
    }
}
