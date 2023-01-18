package com.zestarr.pluginportal.utils;

import com.zestarr.pluginportal.commands.commandutil.Flags;
import com.zestarr.pluginportal.commands.commandutil.SubCommandEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class FlagUtil {

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

    public static ArrayList<String> getFlagStrings(SubCommandEnum subCommandEnum) {
        ArrayList<String> flagStrings = new ArrayList<>();
        for (Flags flags : subCommandEnum.getFlags()) {
            flagStrings.addAll(Arrays.asList(flags.getFlagsString()));
        }
        return flagStrings;
    }

}
