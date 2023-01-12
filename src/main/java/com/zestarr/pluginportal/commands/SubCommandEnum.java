package com.zestarr.pluginportal.commands;

public enum SubCommandEnum {

    HELP("help", "/ppm help", "pluginportal.ppm.help", new String[]{}),
    INSTALL("install", "/ppm install (Plugin Name) <Optional Flags>", "pluginportal.ppm.install", new String[]{"-f", "-force"}),
    LIST("list", "/ppm list", "pluginportal.ppm.list", new String[]{}),
    PREVIEW("preview", "/ppm preview (Plugin Name)", "pluginportal.ppm.preview", new String[]{}),
    SETTINGS("settings", "/ppm settings", "pluginportal.ppm.settings", new String[]{}),
    UPDATE("update", "/ppm update (Plugin Name) <Optional Flags>", "pluginportal.ppm.update", new String[]{"-f", "-force"});


    SubCommandEnum(String command, String commandUsage, String permission, String[] flags) {
        this.command = command;
        this.commandUsage = commandUsage;
        this.permission = permission;
        this.flags = flags;
    }

    private String command;
    private String commandUsage;
    private String permission;
    private String[] flags;

    public String getCommand() {
        return command;
    }

    public String getCommandUsage() {
        return commandUsage;
    }

    public String getPermission() {
        return permission;
    }

    public String[] getFlags() {
        return flags;
    }

}
