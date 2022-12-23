package com.zestarr.pluginportal.commands;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.types.LocalPlugin;
import com.zestarr.pluginportal.types.OnlinePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import static com.zestarr.pluginportal.utils.ConfigUtils.getPluginFolder;

public class PPMTab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], List.of("install", "update", "list", "search", "devtoggle"), new ArrayList<>());
            // Removed Uninstall and Delete due to it being not very possible to do without doing a lot of magic.

        } else if (args.length == 2) {
            List<String> results = new ArrayList<>();

            switch (args[0].toLowerCase()) {

                case "install": case "search":
                    for (OnlinePlugin plugin : PluginPortal.getPluginManager().getPlugins().values()) {

                        results.add(plugin.getDisplayName());

                    }

                    if (PluginPortal.getDeveloperMode()) {
                        results.add("*");
                    }

                    return StringUtil.copyPartialMatches(args[1], results, new ArrayList<>());

                case "uninstall": case "delete":
                    if (getPluginFolder().list() == null) return results;
                    for (String file : getPluginFolder().list()) {
                        if (file.endsWith(".jar")) {
                            results.add(file.replace(".jar", ""));
                        }

                    }
                    return StringUtil.copyPartialMatches(args[1], results, new ArrayList<>());

                case "update":
                    for (LocalPlugin plugin : PluginPortal.getDataManager().getInstalledPlugins().values()) {
                        results.add(plugin.getOnlinePlugin().getDisplayName());
                    }

                    return StringUtil.copyPartialMatches(args[1], results, new ArrayList<>());
            }
        }

        return new ArrayList<>();
    }
}
