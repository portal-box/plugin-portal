package com.zestarr.pluginportal.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static String extractNumbers(String input) {
        // Use a regular expression to find all digits in the input string
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(input);

        // Append all the digits to a StringBuilder
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            sb.append(m.group());
        }

        // Return the result as a string
        return sb.toString();
    }

}
