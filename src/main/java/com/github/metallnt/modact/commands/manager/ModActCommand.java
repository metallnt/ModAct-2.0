package com.github.metallnt.modact.commands.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Class com.github.metallnt.modact.commands.manager
 * <p>
 * Date: 28.12.2021 19:40 28 12 2021
 *
 * @author Metall
 */
public abstract class ModActCommand implements TabExecutor {

    // Получить описание, которое используется для этой команды, может быть null или empty.
    public abstract String getDescription();

    // Прверка разрешений
    public abstract String getPermission();

    // способ использования этой команды
    public abstract String getUsage();

    @Override
    public abstract boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args);

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        return null;
    }

    /**
     * Имеет ли данный отправитель данное разрешение.
     * Также будет отправлено сообщение «У вас нет этого разрешения», если отправитель
     * не имеет данного разрешения.
     */
    public boolean hasPermission(String permission, CommandSender sender) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(ChatColor.RED + "У вас нет прав!");
            return false;
        }
        return true;
    }

    /**
     * Вернуть все аргументы, которые были предоставлены в данном списке строк. Аргумент указывается в виде строки с
     * две черточки (-) перед ней. Обратите внимание, что все возвращаемые аргументы отображаются в нижнем регистре, а дефисы удаляются.
     */
    public static List<String> getArgumentOptions(String[] strings) {
        List<String> arguments = new ArrayList<>();

        Arrays.stream(strings).forEach(string -> {
            if (string.matches("[-]{2}[a-zA-Z_-]+")) {
                arguments.add(string.replace("--", "").toLowerCase());
            }
        });
        return arguments;
    }

    /**
     * Запустить задачу типа { @link CompletableFuture} в отдельном потоке. Это удобство для простого запуска задач
     * по отдельным потокам.
     */
    public void runCommandTask(CompletableFuture<?> task) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("ModAct");

        assert plugin != null;
        Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                task.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * список параметров строки, соответствующих началу данной строки.
     */
    public static List<String> getOptionsStartingWith(Collection<String> options, String started) {
        return options.stream().filter(s -> s.toLowerCase().startsWith(started.toLowerCase())).collect(Collectors.toList());
    }

    /**
     * Создайте одну строку из массива строк, начиная с заданной позиции в массиве.
     */
    public static String getStringFromArgs(final String[] args, final int startArgs) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = startArgs; i < args.length; i++) {
            if (i == startArgs) {
                stringBuilder.append(args[i]);
            } else {
                stringBuilder.append(" ").append(args[i]);
            }
        }
        return stringBuilder.toString();
    }
}
