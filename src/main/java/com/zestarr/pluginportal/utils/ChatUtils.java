package com.zestarr.pluginportal.utils;

import org.bukkit.ChatColor;

public class ChatUtils {

    public static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
