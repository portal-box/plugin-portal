package link.portalbox.pluginportal.utils;

import link.portalbox.type.FileType;
import link.portalbox.type.SizeUnit;
import link.portalbox.type.SpigetPlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PreviewUtil {

    public static void sendPreview(Player player, SpigetPlugin spigetPlugin, boolean containDownloadPrompt) {
        player.sendMessage(ChatUtil.format("&8<---------------------- &7[&b&lPPM&7]&8 ---------------------->"));

        ArrayList<TextComponent> informationAsComponents = new ArrayList<>();
        try {
            TextComponent component = new TextComponent(ChatUtil.format("Name: &b" + spigetPlugin.getSpigotName()));
            informationAsComponents.add(component);

            component = new TextComponent(ChatUtil.format("Description: &b&l[Hover Here]"));
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatUtil.format("&b" + spigetPlugin.getTag()))));
            informationAsComponents.add(component);

            component = new TextComponent(ChatUtil.format("Downloads: &b" + String.format("%,d", spigetPlugin.getDownloads())));
            informationAsComponents.add(component);

            component = new TextComponent(ChatUtil.format("Rating: &e⭐&b" + spigetPlugin.getRating()));
            informationAsComponents.add(component);

            if (spigetPlugin.getFileType().equals(FileType.EXTERNAL)) {
                component = new TextComponent(ChatUtil.format("Spigot Link: &b"));

                TextComponent link = new TextComponent(ChatUtil.format("&b&l[Click Here]"));
                link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/" + spigetPlugin.getId()));
                link.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatUtil.format("&bClick to open the Spigot page"))));

                component.addExtra(link);
                informationAsComponents.add(component);
            } else {
                component = new TextComponent(ChatUtil.format("File Size: &b" + (spigetPlugin.getSizeUnit() != SizeUnit.NONE ? spigetPlugin.getFileSize() + spigetPlugin.getSizeUnit().getUnit() : SizeUnit.NONE.getUnit())));
                informationAsComponents.add(component);

                component = new TextComponent(ChatUtil.format("File Type: &b" + spigetPlugin.getFileType().getExtension()));
                informationAsComponents.add(component);
            }

            component = new TextComponent(ChatUtil.format("Directly Downloadable: &b" + (spigetPlugin.getFileType().isSupported() ? "Yes" : "No")));
            informationAsComponents.add(component);
        } catch (Exception exception) {
            exception.printStackTrace();
            informationAsComponents.add(new TextComponent(ChatUtil.format("&cError ID: " + spigetPlugin.getId() + ". Please report this to our discord.")));
        }

        try {
            String url = spigetPlugin.getIconUrl().length() == 0 ? "https://raw.githubusercontent.com/portal-box/plugin-portal/master/resources/PluginPortalLogo.png" : spigetPlugin.getIconUrl();

            URL imageUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");
            BufferedImage image = ImageIO.read(connection.getInputStream());

            // rows and columns
            int squareSize = 12;

            // array to hold sub-images
            BufferedImage[] imgs = new BufferedImage[squareSize * squareSize];

            // Equally dividing original image into images
            int subimage_Width = image.getWidth() / squareSize;
            int subimage_Height = image.getHeight() / squareSize;

            int current_img = 0;

            // iterating over rows and columns for each sub-image
            for (int i = 0; i < squareSize; i++) {
                for (int j = 0; j < squareSize; j++) {
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
                    ComponentBuilder componentBuilder = new ComponentBuilder();

                    componentBuilder.appendLegacy(ChatUtil.format(builder + " &7"));

                    if (informationAsComponents.size() > row && informationAsComponents.get(row) != null) {
                        componentBuilder.append(informationAsComponents.get(row));
                    }

                    if (containDownloadPrompt) {
                        if (row == squareSize - 4) {
                            componentBuilder.append(ChatUtil.format("&7Would you still like to download this plugin?"));
                        } else if (row == squareSize - 3) {
                            componentBuilder.append(ChatUtil.format("&7Please run /pp install PluginName &a-f"));
                        }
                    }

                    player.spigot().sendMessage(componentBuilder.create());
                    builder = new StringBuilder();
                    row++;
                }
                i++;
                Color color = ChatUtil.getAverageColor(bound);
                builder.append(ChatColor.of(color)).append("▉");
            }

            connection.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.sendMessage(ChatUtil.format("&8-----------------------------------------------------"));
    }

}
