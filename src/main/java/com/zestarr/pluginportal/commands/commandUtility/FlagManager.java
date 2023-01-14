package com.zestarr.pluginportal.commands.commandUtility;

import java.util.HashSet;

public class FlagManager {

    public static HashSet<Flags> getFlags(SubCommandEnum subCommandEnum, String[] args) {
        HashSet<Flags> flags = new HashSet<>();

        for (Flags loopedFlags : subCommandEnum.getFlags()) {
            for (String arg : args) {
                for (String flag : loopedFlags.getFlagsString()) {
                    if (arg.equalsIgnoreCase(flag)) {
                        flags.add(loopedFlags);

                    }
                }
            }
        }

        return flags;
    }

}
