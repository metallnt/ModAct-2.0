package com.github.metallnt.modact.commands.manager;

import com.github.metallnt.modact.ModAct;
import com.github.metallnt.modact.commands.AddCommand;
import com.github.metallnt.modact.commands.DeleteCommand;
import com.github.metallnt.modact.commands.HelpCommand;
import com.github.metallnt.modact.commands.ReloadCommand;
import com.github.metallnt.modact.util.ModActTools;
import com.google.common.collect.Lists;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Class com.github.metallnt.modact.commands.manager
 * Этот класс будет управлять всеми входящими запросами команд. Команды не
 * выполняются здесь, они только отправляются в нужное место.
 * <p>
 * Команды хранятся в хэш-карте. Ключ этой хэш-карты - это список
 * строк. Эти строки представляют, какой текст вы должны ввести, чтобы выполнить эту
 * команду. Например, «раз» сохраняется для команды «/modact раз». В
 * значение хэш-карты - это класс {@linkplain ModActCommand}, который выполняет
 * фактическую логику команды.
 * Date: 28.12.2021 19:34 28 12 2021
 *
 * @author Metall
 */
public class CommandsManager implements TabExecutor {

    private final ModAct plugin;
    private final Map<List<String>, ModActCommand> registeredCommands = new LinkedHashMap<>();

    /**
     * Здесь настраиваются все псевдонимы команд.
     */
    public CommandsManager(final ModAct modAct) {
        this.plugin = modAct;

        // Регистрируем классы команд
        registeredCommands.put(Collections.singletonList("help"), new HelpCommand(plugin));
        registeredCommands.put(Collections.singletonList("reload"), new ReloadCommand(plugin));
        registeredCommands.put(Collections.singletonList("add"), new AddCommand(plugin));
        registeredCommands.put(Collections.singletonList("delete"), new DeleteCommand(plugin));
    }

    // Получить хэш-карту используемых команд
    public Map<List<String>, ModActCommand> getRegisteredCommands() {
        return registeredCommands;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

        if (args.length == 0) {
            sender.sendMessage(ChatColor.BLUE + "-----------------------------------------------------");
            sender.sendMessage(ChatColor.GOLD + "Разработчики: " + ChatColor.GRAY + plugin.getDescription().getAuthors());
            sender.sendMessage(ChatColor.GOLD + "Версия: " + ChatColor.GRAY + plugin.getDescription().getVersion());
            sender.sendMessage(ChatColor.YELLOW + "Используйте /modact help для списка команд");
            return true;
        }

        final String action = args[0];
        List<String> suggestions = new ArrayList<>();
        List<String> bestSuggestions = new ArrayList<>();

        // Просматриваем каждый список и проверяем, есть ли там это действие.
        for (final Map.Entry<List<String>, ModActCommand> entry : registeredCommands.entrySet()) {
            String suggestion = ModActTools.findClosestSuggestion(action, entry.getKey());
            suggestions.add(suggestion);

            for (final String actionString : entry.getKey()) {
                if (actionString.equalsIgnoreCase(action)) {
                    return entry.getValue().onCommand(sender, cmd, label, args);
                }
            }
        }

        // Ищем предложения, если аргумент не найден
        for (String suggestion : suggestions) {
            String[] split = suggestion.split(";");
            int editDistance = Integer.parseInt(split[1]);

            // Предлагаем только если расстояние редактирования мало
            if (editDistance <= 2) {
                bestSuggestions.add(split[0]);
            }
        }
        sender.sendMessage(ChatColor.RED + "Команда не распознана!");

        if (!bestSuggestions.isEmpty()) {
            BaseComponent[] builder = new ComponentBuilder("Возможно, вы имели в виду ")
                    .color(net.md_5.bungee.api.ChatColor.DARK_AQUA).append("/modact ")
                    .color(net.md_5.bungee.api.ChatColor.GREEN).append(ModActTools.seperateList(bestSuggestions, "or"))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Это предложения, основанные на вашем вводе.")))
                    .append("?").color(net.md_5.bungee.api.ChatColor.DARK_AQUA).create();
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.spigot().sendMessage(builder);
            } else {
                sender.sendMessage(ChatColor.DARK_AQUA + "Возможно, вы имели в виду " + ChatColor.GREEN
                        + "/modact " + ModActTools.seperateList(bestSuggestions, "or") + ChatColor.DARK_AQUA + "?");
            }
        }
        sender.sendMessage(ChatColor.YELLOW + "Используйте /modact help для списка команд");
        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {

        // Показать список команд, соответствующих уже набранным символам (если есть).
        if (args.length <= 1) {
            final List<String> commands = new ArrayList<>();

            for (final Map.Entry<List<String>, ModActCommand> entry : registeredCommands.entrySet()) {
                final List<String> list = entry.getKey();
                commands.addAll(list);
            }
            return findSuggestedCommands(commands, args[0]);
        }

        final String subCommand = args[0].trim();

        // Давать предложения в зависимости от типа команды. - Мы можем предложить эти команды
        if (subCommand.equalsIgnoreCase("add") || subCommand.equalsIgnoreCase("delete")) {
            if (args.length > 2) {
                final String arg = args[2];
                int count;
                try {
                    count = Integer.parseInt(arg);
                } catch (final NumberFormatException e) {
                    count = 0;
                }
                return Lists.newArrayList("" + (count + 5));
            }
            return null;
        }

        // Возвращаемся на вкладку после подкоманды
        for (final Map.Entry<List<String>, ModActCommand> entry : registeredCommands.entrySet()) {
            for (final String alias : entry.getKey()) {
                if (subCommand.trim().equalsIgnoreCase(alias)) {
                    return entry.getValue().onTabComplete(sender, cmd, commandLabel, args);
                }
            }
        }
        return null;
    }

    /**
     * Возвращает подсписок из данного списка, содержащий элементы, которые начинаются с данной строки, если строка не пуста
     *
     * @param list Список для обработки
     * @param arg  Типизированная строка
     * @return Подсписок, если строка не пуста
     */
    private List<String> findSuggestedCommands(List<String> list, String arg) {
        if (arg.equals("")) return list;

        List<String> returnList = new ArrayList<>();
        for (String item : list) {
            if (item.toLowerCase().startsWith(arg.toLowerCase())) {
                returnList.add(item);
            }
        }
        return returnList;
    }
}
