package com.zestarr.pluginportal.commands.commandUtility;

import lombok.Getter;

public enum Flags {

    FORCE(new String[]{"-f", "-force"});

    @Getter
    private String[] flagsString;

    Flags(String[] flagsString) {
        this.flagsString = flagsString;
    }
}
