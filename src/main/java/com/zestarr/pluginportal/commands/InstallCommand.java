package com.zestarr.pluginportal.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.utils.HttpUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.net.http.HttpClient;

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
            HttpUtils.download(getPluginManager().getPlugins().get(pluginKey), getPluginFolder());
        }
        return true;
    }
}
