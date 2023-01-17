package com.zestarr.pluginportal.commands.subcommands;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.commands.commandutil.SubCommandEnum;
import com.zestarr.pluginportal.commands.commandutil.SubCommandManager;
import com.zestarr.pluginportal.type.PreviewingPlugin;
import com.zestarr.pluginportal.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PreviewSubCommand extends SubCommandManager {
    @Override
    public void execute(CommandSender sender, String[] args, SubCommandEnum subCommandEnum) {
        Bukkit.getScheduler().runTaskAsynchronously(PluginPortal.getMainInstance(), () -> {

            String spigotName = args[1];
            int id = PluginPortal.getMainInstance().getMarketplaceManager().getId(spigotName);
            PreviewingPlugin previewingPlugin = new PreviewingPlugin(id);

            if (!PluginPortal.getMainInstance().getMarketplaceManager().getAllNames().contains(spigotName)) {
                sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cPlugin does not exist."));
                return;
            }

            previewingPlugin.sendPreview((Player) sender, true);
        });
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
        int dim = bi.getWidth() * bi.getHeight();
        // Log.info("step=" + step + " sampled " + sampled + " out of " + dim + " pixels (" + String.format("%.1f", (float)(100*sampled/dim)) + " %)");
        return new Color(Math.round(sumr / sampled), Math.round(sumg / sampled), Math.round(sumb / sampled));
        // TODO sumr / sampled: Integer division in floating-point context
    }
}