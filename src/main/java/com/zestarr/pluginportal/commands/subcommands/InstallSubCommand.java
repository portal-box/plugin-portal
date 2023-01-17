package com.zestarr.pluginportal.commands.subcommands;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.commands.commandutil.Flags;
import com.zestarr.pluginportal.commands.commandutil.SubCommandEnum;
import com.zestarr.pluginportal.commands.commandutil.SubCommandManager;
import com.zestarr.pluginportal.type.FileType;
import com.zestarr.pluginportal.type.LocalPlugin;
import com.zestarr.pluginportal.type.PreviewingPlugin;
import com.zestarr.pluginportal.utils.ChatUtil;
import com.zestarr.pluginportal.utils.FlagUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

// TODO: This needs to implement PreviewingPlugin#sendPreview
public class InstallSubCommand extends SubCommandManager {

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandEnum subCommandEnum) {

        HashSet<Flags> flags = FlagUtil.getFlags(SubCommandEnum.INSTALL, args);


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

        if (previewingPlugin.getFileType() == FileType.EXTERNAL && !flags.contains(Flags.FORCE)) {

            ArrayList<String> information = new ArrayList<>();
            try {
                information.add("Name: " + previewingPlugin.getSpigotName());
                //information.add("De    scription: " + previewingPlugin.getTag());
                information.add("Downloads: " + previewingPlugin.getDownloads());
                information.add("Rating: " + previewingPlugin.getRating());
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

                int squareSize = 12;
                int rows = squareSize;
                int columns = squareSize;

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
                    if (i == squareSize) {
                        i = 0;
                        String message = "";
                        if (information.size() > row && information.get(row) != null) {
                            message = information.get(row);
                        }

                        if (row == rows-4) {
                            message = "&7Want to still attempt install? &7Do:";
                        } else if (row == rows-3) {
                            message = "&b/ppm install PluginName &a-f";
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

                TextComponent clickable = new TextComponent("§8---------------- §bClick to open URL §8----------------");
                clickable.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/" + id));
                sender.spigot().sendMessage(new BaseComponent[]{clickable});
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