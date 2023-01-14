package com.zestarr.pluginportal.commands;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.type.LocalPlugin;
import com.zestarr.pluginportal.type.PreviewingPlugin;
import com.zestarr.pluginportal.utils.ChatUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PPMCommand2 implements CommandExecutor, TabCompleter {

    private final PluginPortal portal;

    public PPMCommand2(PluginPortal portal) {
        this.portal = portal;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &7Listing all installed plugins..."));
            for (LocalPlugin plugin : portal.getLocalPluginManager().getPlugins().values()) {
                if (plugin.getPreviewingPlugin().getSpigotName() != null && !plugin.getPreviewingPlugin().getSpigotName().isEmpty()) {
                    sender.sendMessage(ChatUtil.format(" &a+ &7" + plugin.getPreviewingPlugin().getSpigotName()));
                }
            }
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cUsage: /ppm <arg> <plugin>"));
            return false;
        }

        String spigotName = args[1];
        int id = portal.getMarketplaceManager().getId(spigotName);
        if (!portal.getMarketplaceManager().getAllNames().contains(spigotName)) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cThere is no plugin with this name. Use tab complete to find all usable plugins."));
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "preview" -> {
                PreviewingPlugin previewingPlugin = new PreviewingPlugin(id);
                ArrayList<String> information = new ArrayList<>();
                try {
                    information.add("Name: " + previewingPlugin.getSpigotName());
                    //information.add("De    scription: " + previewingPlugin.getTag());
                    information.add("Downloads: " + previewingPlugin.getDownloads());
                    information.add("Rating: " + previewingPlugin.getRating());
                    information.add("File Size: " + previewingPlugin.getFileSize() + previewingPlugin.getSizeUnit().getUnit());
                    information.add("File Type: " + previewingPlugin.getFileType().getExtension());
                    information.add("Supported: " + previewingPlugin.getFileType().isSupported());
                } catch (Exception exception) {
                    exception.printStackTrace();
                    information.add("Error, ID: " + id + ". Please report this to our discord.");
                }
                try {
                    sender.sendMessage(ChatUtil.format("&8<---------------------- &7[&b&lPPM&7]&8 ---------------------->"));
                    String url = previewingPlugin.getIconUrl();
                    if (url.length() == 0) {
                        //url = "https://i.imgur.com/V9jfjSJ.png"; // White
                        url = "https://i.imgur.com/bbxn0Zy.png"; // Gray
                    }
                    URL imageUrl = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                    connection.setRequestProperty(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");
                    BufferedImage image = ImageIO.read(connection.getInputStream());

                    // initalizing rows and columns
                    int rows = 16;
                    int columns = 16;

                    // initializing array to hold subimages
                    BufferedImage[] imgs = new BufferedImage[256];

                    // Equally dividing original image into subimages
                    int subimage_Width = image.getWidth() / columns;
                    int subimage_Height = image.getHeight() / rows;

                    int current_img = 0;

                    // iterating over rows and columns for each sub-image
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < columns; j++) {
                            // Creating sub image
                            imgs[current_img] = new BufferedImage(subimage_Width, subimage_Height, image.getType());
                            Graphics2D img_creator = imgs[current_img].createGraphics();

                            // coordinates of source image
                            int src_first_x = subimage_Width * j;
                            int src_first_y = subimage_Height * i;

                            // coordinates of sub-image
                            int dst_corner_x = subimage_Width * j + subimage_Width;
                            int dst_corner_y = subimage_Height * i + subimage_Height;

                            img_creator.drawImage(image, 0, 0, subimage_Width, subimage_Height, src_first_x, src_first_y, dst_corner_x, dst_corner_y, null);
                            current_img++;
                        }
                    }

                    int i = 0;
                    int row = 0;
                    StringBuilder builder = new StringBuilder();
                    for (BufferedImage bound : imgs) {
                        if (i == 16) {
                            i = 0;
                            String message = "";
                            if (information.size() > row && information.get(row) != null) {
                                message = information.get(row);
                            }
/*
                            switch (row) {
                                case 9:
                                    message = "        &7&lInstall Plugin?           ";

                                    break;
                                case 12:


                                    message = "        [\"\",{\"text\":\"Yes\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ppm forceinstall LuckPerms\"}}]        &c&lNo        ";

                                    break;
                                }

 */

                            sender.sendMessage(ChatUtil.format(builder + " &7" + message.replaceAll(":", ":&b")));
                            builder = new StringBuilder();
                            row++;
                        }
                        i++;
                        Color color = getAverageColor(bound);
                        builder.append(ChatColor.of(color)).append("\u2589");

                        switch (i) {
                            case 0:

                                break;
                            case 1:

                                break;
                            // etc
                        }
                    }


                    connection.getInputStream().close();
                } catch (Exception e) {
                    e.printStackTrace();
                    //throw new RuntimeException(e);
                }
            }
            case "install" -> {
                if (portal.getLocalPluginManager().isInstalled(spigotName)) {
                    if (args.length == 3 && !(args[2].equalsIgnoreCase("-f") || args[2].equalsIgnoreCase("--force"))) {
                        break;

                    } else {
                        sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &c" + spigotName + " is already installed. Did you mean to run /ppm update " + spigotName + "?"));
                        return false;
                    }
                }
                if (new PreviewingPlugin(id).isPremium()) {
                    sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cThis plugin is premium. Please purchase it on spigotmc.org to install it."));
                    return false;
                }
                sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cStarting to download " + spigotName + "..."));
                asyncInstall(sender, spigotName, id);
            }
            case "update" -> {
                if (args.length == 2) {
                    sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &7Listing all plugins that can be updated: "));
                    for (LocalPlugin plugin : portal.getLocalPluginManager().getPlugins().values()) {
                        if (plugin.updateNeeded(portal)) {
                            sender.sendMessage(ChatUtil.format(" &a+ &7" + plugin.getPreviewingPlugin().getSpigotName()));
                        }
                    }
                    sender.sendMessage(ChatUtil.format("&8-----------------------------------------------------"));
                }
                if (portal.getLocalPluginManager().getPlugins().get(spigotName) == null) {
                    sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &c" + spigotName + " is not installed. Did you mean to run /ppm install " + spigotName + "?"));
                    return false;
                }
                LocalPlugin plugin = portal.getLocalPluginManager().getPlugins().get(spigotName);
                if (plugin.matchesVersion(new PreviewingPlugin(id).getVersion())) {
                    sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &c" + spigotName + " is already up to date."));
                    return false;
                } else {
                    portal.getDownloadManager().update(plugin);
                    // def will cause no issues/errors! we need a better detection system for deleting plugins btw
                    sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &c" + spigotName + " has been updated to version " + new PreviewingPlugin(id).getVersion() + "."));
                }
            }
        }

        return false;
    }

    @Override

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList("install", "update", "list", "preview"), new ArrayList<>());
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "preview", "install" -> {
                    if (args[1].length() <= 2) return List.of("Keep Typing...", args[1]);
                    return StringUtil.copyPartialMatches(args[1], portal.getMarketplaceManager().getAllNames(), new ArrayList<>());
                }
                /*
                    case "uninstall":
                    return StringUtil.copyPartialMatches(args[1], portal.getPluginStatusListener().getPluginMap().keySet(), new ArrayList<>());
                                 */
                case "update" -> {
                    return StringUtil.copyPartialMatches(args[1], portal.getLocalPluginManager().getAllNames(), new ArrayList<>());
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("install")) {
            return StringUtil.copyPartialMatches(args[2], Arrays.asList("-f", "--force"), new ArrayList<>());
        }

        return new ArrayList<>();
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

    private void asyncInstall(CommandSender sender, String spigotName, int id) {
        Bukkit.getScheduler().runTaskAsynchronously(portal, () -> {
            LocalPlugin plugin = portal.getDownloadManager().download(new PreviewingPlugin(id));
            if (plugin == null) {
                sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cThere was an error installing " + spigotName + "."));
                return;
            }
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &c" + spigotName + " has been installed."));
        });
    }
}
