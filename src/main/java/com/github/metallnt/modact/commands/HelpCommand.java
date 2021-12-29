package com.github.metallnt.modact.commands;

import com.github.metallnt.modact.ModAct;
import com.github.metallnt.modact.commands.manager.ModActCommand;
import com.github.metallnt.modact.permissions.ModActPermission;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class com.github.metallnt.modact.commands.manager
 * <p>
 * Date: 28.12.2021 20:10 28 12 2021
 *
 * @author Metall
 */
public class HelpCommand extends ModActCommand {

    private final ModAct plugin;

    public HelpCommand(final ModAct modAct) {
        plugin = modAct;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

        if (args.length == 1) {
            showHelpPages(sender, 1);
        } else {
            int page;
            try {
                page = Integer.parseInt(args[1]);
            } catch (final Exception e) {
                sender.sendMessage(ChatColor.RED + " INVALID_NUMBER");
                return true;
            }
            showHelpPages(sender, page);
        }
        return true;
    }

    private void showHelpPages(final CommandSender sender, int page) {
        List<ModActCommand> commands = new ArrayList<>(plugin.getCommandsManager().getRegisteredCommands().values())
                .stream().sorted(Comparator.comparing(ModActCommand::getUsage)).collect(Collectors.toList());

        if (!sender.isOp()) {
            commands = commands.stream()
                    .filter(cmd -> sender.hasPermission(cmd.getPermission())).collect(Collectors.toList());
        }

        final int listSize = commands.size();
        final int maxPages = (int) Math.ceil(listSize / 6D);

        if (page > maxPages || page == 0)
            page = maxPages;

        int start = 0;
        int end = 6;

        if (page != 1) {
            final int pageDifference = page - 1;
            start += 1;
            start += (6 * pageDifference);
            end = start + 6;
        }

        sender.sendMessage(ChatColor.GREEN + "-- Autorank Commands --");

        for (int i = start; i < end; i++) {
            // Can't go any further
            if (i >= listSize)
                break;

            final ModActCommand command = commands.get(i);

            sender.sendMessage(ChatColor.AQUA + command.getUsage() + ChatColor.GRAY + " - " + command.getDescription());
        }
        sender.sendMessage(ChatColor.BLUE + "Страница " + page + "/" + maxPages);
    }

    @Override
    public String getDescription() {
        return "Список комманд";
    }

    @Override
    public String getPermission() {
        return ModActPermission.HELP_PERM;
    }

    @Override
    public String getUsage() {
        return "/modact help <page>";
    }
}
