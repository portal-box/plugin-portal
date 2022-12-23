package com.zestarr.pluginportal.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatUtils {

    public static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    // Used to send messages to console/player/commandblock without any issues
    public static void sendMessage(CommandSender sender) {
        if (sender instanceof Player) {
            sender.sendMessage(format("&7&l[&b&lPPM&7] &8&l> &cPlease specify a plugin to install!"));
        }

    }
}
