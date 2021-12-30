package com.github.metallnt.modact.commands;

import com.github.metallnt.modact.ModAct;
import com.github.metallnt.modact.commands.manager.ModActCommand;
import com.github.metallnt.modact.permissions.ModActPermission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Class com.github.metallnt.modact.commands.manager
 * Обработчик /modact add
 * Команда для добаления значения в один из списков в файле lists.yml
 * Date: 29.12.2021 11:50 29 12 2021
 *
 * @author Metall
 */
public class AddCommand extends ModActCommand {

    private final ModAct plugin;

    public AddCommand(ModAct modAct) {
        plugin = modAct;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return false;
    }

    @Override
    public String getDescription() {
        return "Добавить блок в списки по умолчанию";
    }

    @Override
    public String getPermission() {
        return ModActPermission.ADD_PERM;
    }

    @Override
    public String getUsage() {
        return "/modact add (mask|exact|wood|stone|iron|gold|diamond|nether) [block id]";
    }
}
