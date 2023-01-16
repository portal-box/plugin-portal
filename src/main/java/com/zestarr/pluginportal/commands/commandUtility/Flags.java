package com.zestarr.pluginportal.commands.commandUtility;

import lombok.Getter;

public enum Flags {

    FORCE(new String[]{"-f", "--force"});

    @Getter
    private final String[] flagsString;

    Flags(String[] flagsString) {
        this.flagsString = flagsString;
    }
}
