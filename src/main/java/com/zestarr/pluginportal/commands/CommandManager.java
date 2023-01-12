package com.zestarr.pluginportal.commands;

import com.zestarr.pluginportal.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public abstract class CommandManager extends BukkitCommand {

    public CommandManager(String command, String[] aliases, String description, String permission) {
        super(command);

        this.setAliases(Arrays.asList(aliases));
        this.setDescription(description);
        this.setPermission(permission);
        this.setPermissionMessage(ChatUtil.format("You do not have permission to use this command."));

        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap map = (CommandMap) field.get(Bukkit.getServer());
            map.register(command, this);

        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        execute(sender, args);
        return false;
    }

    public abstract void execute(CommandSender sender, String[] args);


    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return onTabComplete(sender, args);
    }

    public abstract List<String> onTabComplete(CommandSender sender, String[] args);

}
