package com.zestarr.pluginportal.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.types.Plugin;
import com.zestarr.pluginportal.utils.HttpUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.zestarr.pluginportal.PluginPortal.getPluginManager;
import static com.zestarr.pluginportal.utils.ConfigUtils.getPluginFolder;

@CommandAlias("ppm")
public class InstallCommand extends BaseCommand {

    @Default
    @CommandPermission("ppm.install")
    @Description("Main Install Command for the ppm (PluginPortal Package Manager)")
    public boolean onCommand(CommandSender sender, String pluginKey) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Check if plugin is valid
            Map<String, Plugin> plugins = getPluginManager().getPlugins();
            if (plugins.get(pluginKey) == null) {
                player.sendMessage(ChatColor.RED + "Plugin not found! Plugin List: ");

                for (Plugin plugin : plugins.values()) {
                    player.sendMessage(ChatColor.GREEN + " + " + ChatColor.GRAY + plugin.getDisplayName());
                }

                return true;
            } else {
                if (new File(getPluginFolder(), pluginKey + ".jar").exists()) { // Broken, Method is to save all saved plugins "display name" and the file name to a hashmap/file and check from there
                    player.sendMessage(ChatColor.RED + "Plugin already installed!");
                    return true;
                } else {
                    HttpUtils.download(getPluginManager().getPlugins().get(pluginKey), getPluginFolder());
                }
            }




        }
        return true;
    }
}
