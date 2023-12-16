package com.github.metallnt.modact;

import com.github.metallnt.modact.commands.manager.CommandsManager;
import com.github.metallnt.modact.configs.DefaultConfig;
import com.github.metallnt.modact.configs.PlayerConfig;
import com.github.metallnt.modact.configs.RulesConfig;
import com.github.metallnt.modact.listeners.BlockListener;
import com.github.metallnt.modact.listeners.InventoryListener;
import com.github.metallnt.modact.listeners.PlayerListener;
import com.github.metallnt.modact.managers.MessageManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ModAct extends JavaPlugin {

    private static ModAct plugin;

    private DefaultConfig defaultConfig;                    // Конфиг
    private RulesConfig rulesConfig;                        // Правила
    private PlayerConfig playerConfig;                      // Данные по игрокам и шансам
    //    private PermCheck permCheck;
    private MessageManager messageManager;                  // Обработчик сообщений
    private CommandsManager commandsManager;                // Обработчик команд
//    private LuckPerms apiLp;
//    private static Permission perm = null;

    public static ModAct getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        // Регистрируем конфиг и правила
        setDefaultConfig(new DefaultConfig(this));
        setRulesConfig(new RulesConfig(this));
        setPlayerConfig(new PlayerConfig(this));

        // Регистрируем обработчики
        setMessageManager(new MessageManager(this));
//        setPermCheck(new PermCheck(this));
//        setPermission();
        setCommandsManager(new CommandsManager(this));

        // Загружаем конфиги
        SetDataFiles();

        // Listeners
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);

        // LuckPerms
//        setApiLp();

    }

//    private boolean setPermission() {
//        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
//        assert rsp != null;
//        perm = rsp.getProvider();
//        return true;
//    }

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
        if (!this.getPlayerConfig().loadConfig()) {
            getLogger().info("UserData не загружен");
        } else {
            getLogger().info("UserData loaded!");
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

    public PlayerConfig getPlayerConfig() {
        return playerConfig;
    }

    public void setPlayerConfig(PlayerConfig playerConfig) {
        this.playerConfig = playerConfig;
    }

//    public PermCheck getPermCheck() {
//        return permCheck;
//    }

//    public void setApiLp() {
//        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
//        if (provider != null) {
//            this.apiLp = provider.getProvider();
//        } else {
//            this.apiLp = LuckPermsProvider.get();
//        }
//    }

//    public LuckPerms getApiLp() {return apiLp;}

//    public Permission getPermission() {
//        return perm;
//    }
//    public void setPermCheck(PermCheck permCheck) {
//        this.permCheck = permCheck;
//    }

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
