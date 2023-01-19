package link.portalbox.pluginportal.commands.subcommands;

import link.portalbox.pluginportal.PluginPortal;
import link.portalbox.pluginportal.commands.commandutil.Flags;
import link.portalbox.pluginportal.commands.commandutil.SubCommandEnum;
import link.portalbox.pluginportal.commands.commandutil.SubCommandManager;
import link.portalbox.pluginportal.type.FileType;
import link.portalbox.pluginportal.type.LocalPlugin;
import link.portalbox.pluginportal.type.PreviewingPlugin;
import link.portalbox.pluginportal.utils.ChatUtil;
import link.portalbox.pluginportal.utils.FlagUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class InstallSubCommand extends SubCommandManager {

    @Override
    public void execute(CommandSender sender, String[] args) {
        Set<Flags> flags = FlagUtil.getFlags(SubCommandEnum.INSTALL, args);

        if (args.length <= 1) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &cPlease specify a plugin to install!"));
            return;
        }

        String spigotName = args[1];
        int id = PluginPortal.getMainInstance().getMarketplaceManager().getId(spigotName);

        if (!PluginPortal.getMainInstance().getMarketplaceManager().getAllNames().contains(spigotName)) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &cPlugin does not exist."));
            return;
        }

        PreviewingPlugin previewingPlugin = new PreviewingPlugin(id);

        if (PluginPortal.getMainInstance().getLocalPluginManager().getPlugins().containsKey(PluginPortal.getMainInstance().getMarketplaceManager().getMarketplaceCache().get(id))) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &7Plugin is already installed."));
            return;
        }

        if (previewingPlugin.isPremium()) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &cThis plugin is a premium plugin. Please purchase it on spigotmc.org to install it!"));
            return;
        }

        if (previewingPlugin.getFileType() == FileType.EXTERNAL && !flags.contains(Flags.FORCE)) {
            previewingPlugin.sendPreview((Player) sender, true);
            return;
        }

        sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &7Starting to download " + spigotName + "..."));
        Bukkit.getScheduler().runTaskAsynchronously(PluginPortal.getMainInstance(), () -> {
            LocalPlugin plugin = PluginPortal.getMainInstance().getDownloadManager().download(previewingPlugin);
            if (plugin == null) {
                sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &cThere was an error installing " + spigotName + "."));
                return;
            }
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &b" + spigotName + " &7has been installed. Please restart your server for the download to take effect."));
        });
    }
}