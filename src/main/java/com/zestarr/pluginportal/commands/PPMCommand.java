package com.zestarr.pluginportal.commands;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.commands.commandUtility.SubCommandEnum;
import com.zestarr.pluginportal.commands.ppmSubCommands.*;
import com.zestarr.pluginportal.commands.commandUtility.CommandManager;
import com.zestarr.pluginportal.utils.FlagUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PPMCommand extends CommandManager {

    HelpSubCommand helpSubCommand = new HelpSubCommand();
    InstallSubCommand installSubCommand = new InstallSubCommand();
    ListSubCommand listSubCommand = new ListSubCommand();
    PreviewSubCommand previewSubCommand = new PreviewSubCommand();
    SettingsSubCommand settingsSubCommand = new SettingsSubCommand();
    UpdateSubCommand updateSubCommand = new UpdateSubCommand();

    private final PluginPortal plugin;


    public PPMCommand(PluginPortal plugin) {
        super(
                "ppm",
                new String[]{"PluginPackagemanager"},
                "Primary command to handle the Plugin Portal Package Manager",
                "ppm.primarycommand"
        );

        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            helpSubCommand.execute(sender, args, SubCommandEnum.HELP);
        } else {
            switch (args[0].toLowerCase()) {
                case "install" -> installSubCommand.execute(sender, args, SubCommandEnum.INSTALL);
                case "list" -> listSubCommand.execute(sender, args, SubCommandEnum.LIST);
                case "preview" -> previewSubCommand.execute(sender, args, SubCommandEnum.PREVIEW);
                case "settings" -> settingsSubCommand.execute(sender, args, SubCommandEnum.SETTINGS);
                case "update" -> updateSubCommand.execute(sender, args, SubCommandEnum.UPDATE);
                default -> helpSubCommand.execute(sender, args, SubCommandEnum.HELP);
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {



        if (args.length == 1) {
            ArrayList<String> tabComplete = new ArrayList<>();
            for (SubCommandEnum subCommandEnum : SubCommandEnum.values()) {
                tabComplete.add(subCommandEnum.getCommand());
            }
            return StringUtil.copyPartialMatches(args[0], tabComplete, new ArrayList<>());
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "install", "preview" -> {
                    if (args[1].length() <= 2) return List.of("Keep Typing...", args[1]);
                    return StringUtil.copyPartialMatches(args[1], plugin.getMarketplaceManager().getAllNames(), new ArrayList<>());
                }
                case "settings", "update" -> {
                    return StringUtil.copyPartialMatches(args[1], plugin.getLocalPluginManager().getAllNames(), new ArrayList<>());
                }
                default -> {
                    return null;
                }
            }
        } else if (args.length == 3) {
            return StringUtil.copyPartialMatches(args[2], FlagUtil.getFlagStrings(SubCommandEnum.valueOf(args[0].toUpperCase())), new ArrayList<>());
        }

        return null;
    }
}