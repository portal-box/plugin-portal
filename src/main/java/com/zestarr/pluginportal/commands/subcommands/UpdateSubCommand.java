package com.zestarr.pluginportal.commands.subcommands;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.commands.commandutil.Flags;
import com.zestarr.pluginportal.commands.commandutil.SubCommandEnum;
import com.zestarr.pluginportal.commands.commandutil.SubCommandManager;
import com.zestarr.pluginportal.type.LocalPlugin;
import com.zestarr.pluginportal.type.PreviewingPlugin;
import com.zestarr.pluginportal.utils.ChatUtil;
import com.zestarr.pluginportal.utils.FlagUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class UpdateSubCommand extends SubCommandManager {
    @Override
    public void execute(CommandSender sender, String[] args, SubCommandEnum subCommandEnum) {
        if (args.length == 1) {

            if (PluginPortal.getMainInstance().getLocalPluginManager().getPlugins().values().size() == 0) {
                sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &7All plugins are up to date!."));
                return;
            }

            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &7Listing all plugins that can be updated: "));
            for (LocalPlugin plugin : PluginPortal.getMainInstance().getLocalPluginManager().getPlugins().values()) {
                if (plugin.updateNeeded()) {
                    sender.sendMessage(ChatUtil.format(" &a+ &7" + plugin.getPreviewingPlugin().getSpigotName()));
                }
            }
        }

        if (args.length != 2) return;
        String name = args[1];
        if (PluginPortal.getMainInstance().getLocalPluginManager().getPlugins().get(name) == null) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &c" + name + " &7is not installed. Did you mean to run &b/PP install " + name + "?"));
            return;
        }
        LocalPlugin plugin = PluginPortal.getMainInstance().getLocalPluginManager().getPlugins().get(name);
        if (!plugin.updateNeeded() && !FlagUtil.getFlags(SubCommandEnum.UPDATE, args).contains(Flags.FORCE)) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &7" + name + " is already up to date."));
            return;
        } else {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &7Updating &b" + name));
            Bukkit.getScheduler().runTaskAsynchronously(PluginPortal.getMainInstance(), () -> {
                PluginPortal.getMainInstance().getDownloadManager().download(new PreviewingPlugin(plugin.getPreviewingPlugin().getId()));
                sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &b" + name + " &7has been updated."));
            });

        }


    }
}
