package link.portalbox.pluginportal.commands.subcommands;

import link.portalbox.pluginportal.PluginPortal;
import link.portalbox.pluginportal.commands.commandutil.SubCommandManager;
import link.portalbox.pluginportal.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.ArrayUtils.removeElement;

public class DeleteSubCommand extends SubCommandManager {
    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length == 1) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &cPlugin does not exist."));
            return;
        }

        ArrayList<String> fileNames = new ArrayList<>();
        String fileName = args[1];

        for (File file : PluginPortal.getMainInstance().getDataFolder().listFiles()) {
            fileNames.add(file.getName());
        }

        if (fileNames.contains(fileName)) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &7Deleting " + fileName + "..."));
        }

    }
}
