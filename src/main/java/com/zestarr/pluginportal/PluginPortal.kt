package com.zestarr.pluginportal;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import com.zestarr.pluginportal.commands.InstallCommand;
import com.zestarr.pluginportal.managers.PluginManager;
import com.zestarr.pluginportal.types.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public final class PluginPortal extends JavaPlugin {

    public static PluginManager pluginManager;
    public static PaperCommandManager manager;

    public static ArrayList<BaseCommand> commands = new ArrayList<>();
    public static ArrayList<String> tabComplete = new ArrayList<>();

    @Override
    public void onEnable() {

        System.out.println(getDataFolder().getAbsolutePath());
        pluginManager = new PluginManager();
        pluginManager.setupFolder(new File("plugins"));
        try {
            pluginManager.loadPlugins();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Register Commands and Listeners

        manager = new PaperCommandManager(this);

        commands.add(new InstallCommand());

        registerCommands(manager);

        for (Plugin plugin : pluginManager.getPlugins().values()) {
            tabComplete.add(plugin.getDisplayName());
        }
    }

    private void registerCommands(PaperCommandManager manager) {

        // Enable the help api.
        manager.enableUnstableAPI("help");

        // Register tab-autocomplete options.
        manager.getCommandCompletions().registerAsyncCompletion("install", c ->
                tabComplete
        );

        for (BaseCommand command : commands) {
            manager.registerCommand(command);
        }

        // Generic exception handler
        manager.setDefaultExceptionHandler((command, registeredCommand, sender, args, t) -> {
            getLogger().warning("Error occured while executing command: " + command.getName());
            return false;
        });
    }


    public static PluginManager getPluginManager() {
        return pluginManager;
    }

    public static PaperCommandManager getManager() {
        return manager;
    }
}
