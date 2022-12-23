package com.zestarr.pluginportal.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static String extractNumbers(String urlString) {
        // Compile a regular expression to match the numbers at the end of the URL
        Pattern pattern = Pattern.compile(".*?([0-9]+)$");
        Matcher matcher = pattern.matcher(urlString);

        // If the regular expression matches the URL, extract the numbers
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

}
