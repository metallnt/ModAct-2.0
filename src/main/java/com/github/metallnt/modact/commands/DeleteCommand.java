package com.github.metallnt.modact.commands;

import com.github.metallnt.modact.ModAct;
import com.github.metallnt.modact.commands.manager.ModActCommand;
import com.github.metallnt.modact.permissions.ModActPermission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Class com.github.metallnt.modact.commands
 * Обработчик /modact delete
 * Команда для удаления значения из одного из списков в файле lists.yml
 * <p>
 * Date: 29.12.2021 11:54 29 12 2021
 *
 * @author Metall
 */
public class DeleteCommand extends ModActCommand {

    private final ModAct plugin;

    public DeleteCommand(ModAct modAct) {
        plugin = modAct;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return false;
    }

    @Override
    public String getDescription() {
        return "Удалить блок из списков по умолчанию";
    }

    @Override
    public String getPermission() {
        return ModActPermission.DEL_PERM;
    }

    @Override
    public String getUsage() {
        return "/modact delete (mask|exact|wood|stone|iron|gold|diamond|nether) [block id]";
    }
}
