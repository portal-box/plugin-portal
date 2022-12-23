package com.zestarr.pluginportal.commands;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.types.LocalPlugin;
import com.zestarr.pluginportal.types.OnlinePlugin;
import com.zestarr.pluginportal.utils.HttpUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.zestarr.pluginportal.PluginPortal.getDataManager;
import static com.zestarr.pluginportal.PluginPortal.getPluginManager;
import static com.zestarr.pluginportal.utils.ChatUtils.format;
import static com.zestarr.pluginportal.utils.ConfigUtils.getPluginFolder;

public class PPMTestingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            // Install Command
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("install")) {
                    if (args[1] == null) {
                        sender.sendMessage(format("&7&l[&b&lPPM&7] &8&l> &cPlease specify a plugin to install!"));
                        return true;
                    }
                    Map<String, OnlinePlugin> plugins = getPluginManager().getPlugins();
                    if (plugins.get(args[1]) == null) {
                        sender.sendMessage(format("&7&l[&b&lPPM&7] &8&l> &cPlugin Not Found! &7Plugin List: "));

                        for (OnlinePlugin plugin : plugins.values()) {
                            sender.sendMessage(ChatColor.GREEN + " + " + ChatColor.GRAY + plugin.getDisplayName());
                        }

                        return true;
                    } else {
                        if (PluginPortal.getDataManager().isPluginInstalled(PluginPortal.getPluginManager().getPlugins().get(args[1]))) {
                            sender.sendMessage(format("&7&l[&b&lPPM&7] &8&l> &cPlugin already downloaded"));
                            return true;
                        } else {
                            HttpUtils.downloadPlugin(getPluginManager().getPlugins().get(args[1]));

                            sender.sendMessage(format("&7&l[&b&lPPM&7] &8&l> &7Plugin has been downloaded!"));
                        }
                    }
                } else if (args[0].equalsIgnoreCase("uninstall")) {

                    if (PluginPortal.getDataManager().getInstalledPlugins().containsKey(args[1])) {
                        File file = new File(getPluginFolder(), args[1] + ".jar");
                        file.delete();
                        sender.sendMessage(format("&7&l[&b&lPPM&7] &8&l> &7Plugin has been uninstalled!"));
                        getDataManager().savePluginToFile(getPluginManager().getPlugins().get(args[1]), false);

                    } else if (new File(getPluginFolder(), args[1] + ".jar").exists()) {
                        File file = new File(getPluginFolder(), args[1] + ".jar");
                        sender.sendMessage(format("&7&l[&b&lPPM&7] &8&l> &7Uninstalled Third-Party plugin... &8(" + file.getName() + ")"));
                        file.delete();

                    } else {
                        sender.sendMessage(format("&7&l[&b&lPPM&7] &8&l> &cPlugin not installed..."));
                    }
                } else if (args[0].equalsIgnoreCase("list")) {
                    sender.sendMessage(format("&7&l[&b&lPPM&7] &8&l> &7Plugin List: "));

                    for (LocalPlugin plugin : PluginPortal.getDataManager().getInstalledPlugins().values()) {
                        sender.sendMessage(format(plugin.getIsInstalled() ? "&c- &7PluginName &7(&aInstalled&7)" : "&a+ &7PluginName &7(&cUninstalled&7)").replace("PluginName", plugin.getOnlinePlugin().getDisplayName()));
                    }
                } else if (args[0].equalsIgnoreCase("update")) {
                    sender.sendMessage(format("&7&l[&b&lPPM&7] &8&l> &cThis Feature may not work correctly."));
                    if (PluginPortal.getDataManager().isPluginInstalled(PluginPortal.getPluginManager().getPlugins().get(args[1]))) {
                        try {
                            PluginPortal.getPluginManager().updatePlugin(PluginPortal.getDataManager().getInstalledPlugins().get(args[1]));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        sender.sendMessage(format("&7&l[&b&lPPM&7] &8&l> &7Updated plugin..."));
                    } else {
                        sender.sendMessage(format("&7&l[&b&lPPM&7] &8&l> &cPlugin not installed..."));
                    }
                } else if (args[0].equalsIgnoreCase("search")) {
                    Map<String, OnlinePlugin> plugins = getPluginManager().getPlugins();

                    if (plugins.containsKey(args[1])) {
                        sender.sendMessage("&7&l[&b&lPPM&7] &8&l> &7Plugin Found: &7" + plugins.get(args[1]).getDisplayName());
                    } else {
                        sender.sendMessage("&7&l[&b&lPPM&7] &8&l> &cPlugin not found!");
                    }

                } else if (args[0].equalsIgnoreCase("dontrunthis")) {
                    for (OnlinePlugin plugin : getPluginManager().getPlugins().values()) {
                        File folder = new File(getPluginFolder() + File.separator + "DebugPlugins");
                        folder.mkdirs();
                        HttpUtils.download(plugin, new File(getPluginFolder() + File.separator + "DebugPlugins"));
                        getDataManager().savePluginToFile(plugin, true);
                    }

                } else {
                    sender.sendMessage(format("&7&l[&b&lPPM&7] &8&l> &l&cUsage: /ppm <arg> <plugin>"));
                }

            } else {
                sender.sendMessage(format("&7&l[&b&lPPM&7] &8&l> &cUsage: /ppm <arg> <plugin>"));
            }

        return false;
    }
}
