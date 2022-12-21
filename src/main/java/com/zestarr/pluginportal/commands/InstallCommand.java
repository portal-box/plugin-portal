package com.zestarr.pluginportal.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("install")
public class InstallCommand extends BaseCommand {
    @Default
    public boolean onCommand(CommandSender sender) {
        if (sender instanceof Player) {
            ((Player) sender).sendMessage("Installed");
        }
        return true;
    }
}
