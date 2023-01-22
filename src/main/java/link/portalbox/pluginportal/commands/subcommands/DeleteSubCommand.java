package link.portalbox.pluginportal.commands.subcommands;

import link.portalbox.pluginportal.PluginPortal;
import link.portalbox.pluginportal.commands.commandutil.SubCommandManager;
import link.portalbox.pluginportal.utils.ChatUtil;
import link.portalbox.pluginportal.utils.FileUtil;
import link.portalbox.pluginportal.utils.PluginUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class DeleteSubCommand extends SubCommandManager {
    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length == 1) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &cPlugin does not exist."));
            return;
        }

        if (PluginUtil.getAllPlugins().contains(args[1])) {
            {
                new File(PluginPortal.getMainInstance().getDataFolder(), args[1]).deleteOnExit();
                sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &7Attempted to delete &b" + args[1]));

                Plugin plugin = PluginUtil.getPluginByName(args[1]);
                if (plugin == null) return;
                boolean isPluginDisabled = PluginUtil.disableServerPlugin(plugin);
                if (isPluginDisabled) {
                    FileUtil.scanDeletedPlugins(PluginPortal.getMainInstance());
                    sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &b" + args[1] + " &7has been disabled."));
                } else {
                    sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &b" + args[1] + " &7is already disabled or an error occurred."));
                }


            }

        }
    }
}
