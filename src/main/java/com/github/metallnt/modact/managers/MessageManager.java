package com.github.metallnt.modact.managers;

import com.github.metallnt.modact.ModAct;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

/**
 * Class com.github.metallnt.modact.managers
 * <p>
 * Date: 27.12.2021 12:47 27 12 2021
 *
 * @author Metall
 */
public class MessageManager {

    private final ModAct plugin;
    private final Logger log;

    public MessageManager(ModAct modAct) {
        plugin = modAct;
        log = modAct.getLogger();
    }

    /**
     * Отправка сообщение игроку в экшн бар
     *
     * @param player Игрок Player
     * @param msg    Сообщение String
     */
    public void onActionBar(Player player, String msg) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
    }
}
