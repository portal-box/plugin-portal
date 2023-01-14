package com.zestarr.pluginportal.commands.ppmSubCommands;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.commands.commandUtility.SubCommandEnum;
import com.zestarr.pluginportal.commands.commandUtility.SubCommandManager;
import com.zestarr.pluginportal.type.FileType;
import com.zestarr.pluginportal.type.LocalPlugin;
import com.zestarr.pluginportal.type.PreviewingPlugin;
import com.zestarr.pluginportal.utils.ChatUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class InstallSubCommand extends SubCommandManager {

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandEnum subCommandEnum) {



        if (args.length <= 1) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cPlease specify a plugin to install!"));
            return;
        }

        String spigotName = args[1];
        int id = PluginPortal.getMainInstance().getMarketplaceManager().getId(spigotName);

        if (!PluginPortal.getMainInstance().getMarketplaceManager().getAllNames().contains(spigotName)) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cPlugin does not exist."));
            return;
        }

        PreviewingPlugin previewingPlugin = new PreviewingPlugin(id);

        if (previewingPlugin.isPremium()) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cThis plugin is premium. Please purchase it on spigotmc.org to install it."));
            return;
        }

        if (previewingPlugin.getFileType() == FileType.EXTERNAL) {

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

                            switch (row) {
                                case 16:
                                    message = "&7Want to still install? Do /ppm install &bPluginName &a-f";
                                    break;
                                }

                        sender.sendMessage(ChatUtil.format(builder + " &7" + message.replaceAll(":", ":&b")));
                        builder = new StringBuilder();
                        row++;
                    }
                    i++;
                    Color color = PreviewSubCommand.getAverageColor(bound);
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


            return;
        }

        sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &7Starting to download " + spigotName + "..."));
        Bukkit.getScheduler().runTaskAsynchronously(PluginPortal.getMainInstance(), () -> {
            LocalPlugin plugin = PluginPortal.getMainInstance().getDownloadManager().download(new PreviewingPlugin(id));
            if (plugin == null) {
                sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cThere was an error installing " + spigotName + "."));
                return;
            }
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &b" + spigotName + " &7has been installed."));
        });



    }
}