package link.portalbox.pluginportal.commands.subcommands;

import link.portalbox.pluginportal.PluginPortal;
import link.portalbox.pluginportal.commands.commandutil.Flags;
import link.portalbox.pluginportal.commands.commandutil.SubCommandEnum;
import link.portalbox.pluginportal.commands.commandutil.SubCommandManager;
import link.portalbox.pluginportal.type.LocalPlugin;
import link.portalbox.pluginportal.type.PreviewingPlugin;
import link.portalbox.pluginportal.utils.ChatUtil;
import link.portalbox.pluginportal.utils.FlagUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

public class UpdateSubCommand extends SubCommandManager {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<LocalPlugin> localPlugins = PluginPortal.getMainInstance().getLocalPluginManager().getPlugins().values().stream().filter(LocalPlugin::updateNeeded).toList();

            if (localPlugins.size() == 0) {
                sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &7No plugins require an update :)"));
                return;
            } else {
                sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &7Listing all plugins that can be updated: "));
                for (LocalPlugin localPlugin : localPlugins) {
                    sender.sendMessage(ChatUtil.format(" &a+ &7" + localPlugin.getPreviewingPlugin().getSpigotName()));
                }
            }
        }

        if (args.length != 2) return;
        String name = args[1];
        if (PluginPortal.getMainInstance().getLocalPluginManager().getPlugins().get(name) == null) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &c" + name + " &7is not installed. Did you mean to run &b/ppm install " + name + "?"));
            return;
        }
        LocalPlugin plugin = PluginPortal.getMainInstance().getLocalPluginManager().getPlugins().get(name);
        if (!plugin.updateNeeded() && !FlagUtil.getFlags(SubCommandEnum.UPDATE, args).contains(Flags.FORCE)) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &7" + name + " is already up to date."));
        } else {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &7Updating &b" + name));
            Bukkit.getScheduler().runTaskAsynchronously(PluginPortal.getMainInstance(), () -> {
                PluginPortal.getMainInstance().getDownloadManager().download(new PreviewingPlugin(plugin.getPreviewingPlugin().getId()));
                sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &b" + name + " &7has been updated."));
            });

        }
    }
}