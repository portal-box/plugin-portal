package com.zestarr.pluginportal.utils;

import org.bukkit.ChatColor;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ChatUtil {

    public static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static Color getAverageColor(BufferedImage bi) {
        int step = 5;

        int sampled = 0;
        long sumr = 0, sumg = 0, sumb = 0;
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                if (x % step == 0 && y % step == 0) {
                    Color pixel = new Color(bi.getRGB(x, y));
                    sumr += pixel.getRed();
                    sumg += pixel.getGreen();
                    sumb += pixel.getBlue();
                    sampled++;
                }
            }
        }
        // Log.info("step=" + step + " sampled " + sampled + " out of " + dim + " pixels (" + String.format("%.1f", (float)(100*sampled/dim)) + " %)");
        return new Color(Math.round(sumr / sampled), Math.round(sumg / sampled), Math.round(sumb / sampled));
        // TODO sumr / sampled: Integer division in floating-point context
    }
}
