package com.zestarr.pluginportal.commands.subcommands;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.commands.commandutil.SubCommandEnum;
import com.zestarr.pluginportal.commands.commandutil.SubCommandManager;
import com.zestarr.pluginportal.type.PreviewingPlugin;
import com.zestarr.pluginportal.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PreviewSubCommand extends SubCommandManager {
    @Override
    public void execute(CommandSender sender, String[] args, SubCommandEnum subCommandEnum) {
        Bukkit.getScheduler().runTaskAsynchronously(PluginPortal.getMainInstance(), () -> {

            String spigotName = args[1];
            int id = PluginPortal.getMainInstance().getMarketplaceManager().getId(spigotName);
            PreviewingPlugin previewingPlugin = new PreviewingPlugin(id);

            if (!PluginPortal.getMainInstance().getMarketplaceManager().getAllNames().contains(spigotName)) {
                sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cPlugin does not exist."));
                return;
            }

            previewingPlugin.sendPreview((Player) sender, true);
        });
    }
}