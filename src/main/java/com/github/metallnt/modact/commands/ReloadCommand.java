package com.github.metallnt.modact.commands;

import com.github.metallnt.modact.ModAct;
import com.github.metallnt.modact.commands.manager.ModActCommand;
import com.github.metallnt.modact.permissions.ModActPermission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Class com.github.metallnt.modact.commands
 * <p>
 * Date: 28.12.2021 20:11 28 12 2021
 *
 * @author Metall
 */
public class ReloadCommand extends ModActCommand {

    private final ModAct plugin;

    public ReloadCommand(final ModAct modAct) {
        plugin = modAct;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

        if (!this.hasPermission(ModActPermission.RELOAD_PERM, sender)) {
            return true;
        }

        plugin.getDefaultConfig().reloadConfig();
        plugin.getRulesConfig().reloadConfig();
        plugin.getLogger().info("ModAct перезагружен");
        sender.sendMessage("ModAct перезагружен");
        return true;
    }

    @Override
    public String getDescription() {
        return "Reload ModAct";
    }

    @Override
    public String getPermission() {
        return ModActPermission.RELOAD_PERM;
    }

    @Override
    public String getUsage() {
        return "/modact reload";
    }
}
