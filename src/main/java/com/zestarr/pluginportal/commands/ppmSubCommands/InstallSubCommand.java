package com.zestarr.pluginportal.commands.ppmSubCommands;

import com.zestarr.pluginportal.commands.commandUtility.SubCommandEnum;
import com.zestarr.pluginportal.commands.commandUtility.SubCommandManager;
import org.bukkit.command.CommandSender;

public class InstallSubCommand extends SubCommandManager {

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandEnum subCommandEnum) {

        if (args.length == 1) {
            sender.sendMessage("Please specify a plugin to install.");
        } else {
            sender.sendMessage("Installing plugin: " + args[1]);
        }



    }
}


/*
PreviewingPlugin previewingPlugin = new PreviewingPlugin(PluginPortal.getMainInstance().getMarketplaceManager().getId(args[1]));
        if (previewingPlugin.equals(-1)) {
            sender.sendMessage(ChatUtil.format("&7[&b&lPPM&7] &cThat plugin does not exist!"));
            return;
        }

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
            information.add("Error, Name: " + args[1] +  ". Please report this to our discord.");
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
            BufferedImage imgs[] = new BufferedImage[256];

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
            String builder = "";
            for (BufferedImage bound : imgs) {
                if (i == 16) {
                    i = 0;
                    String message = "";
                    if (information.size() > row && information.get(row) != null) {
                        message = information.get(row);
                    }

                            switch (row) {
                                case 9:
                                    message = "        &7&lInstall Plugin?           ";

                                    break;
                                case 12:


                                    message = "        Run /ppm install                ";

                                    break;
                                }



                    sender.sendMessage(ChatUtil.format(builder + " &7" + message.replaceAll(":", ":&b")));
                            builder = "";
                            row++;
                            }
                            i++;
                            Color color = getAverageColor(bound);
                            builder += ChatColor.of(color) + "\u2589";

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
 */