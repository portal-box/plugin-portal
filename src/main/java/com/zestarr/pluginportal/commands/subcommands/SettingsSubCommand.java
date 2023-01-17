package com.zestarr.pluginportal.commands.subcommands;

import com.zestarr.pluginportal.commands.commandutil.SubCommandEnum;
import com.zestarr.pluginportal.commands.commandutil.SubCommandManager;
import com.zestarr.pluginportal.utils.ChatUtil;
import org.bukkit.command.CommandSender;

public class SettingsSubCommand extends SubCommandManager {
    @Override
    public void execute(CommandSender sender, String[] args, SubCommandEnum subCommandEnum) {
        sender.sendMessage(ChatUtil.format("&7[&bPPM&7] &cSettings are not yet implemented."));
    }
}
