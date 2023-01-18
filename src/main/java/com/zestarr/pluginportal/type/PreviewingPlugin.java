package com.zestarr.pluginportal.type;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zestarr.pluginportal.utils.ChatUtil;
import com.zestarr.pluginportal.utils.JsonUtil;
import lombok.Data;
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

@Data
public class PreviewingPlugin {

    private String spigotName, version, tag, iconUrl = null;
    private String[] testedVersions, authors = null;
    private int id, downloads = 0;
    private long releaseData, updateDate = 0;
    private double price, rating, fileSize = 0;
    private boolean premium = false;
    private FileType fileType = null;
    private SizeUnit sizeUnit = null;

    public PreviewingPlugin(int id) {
        this.id = id;
        try {
            String responseBody = JsonUtil.getJsonData(id);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readValue(responseBody, JsonNode.class);

            this.spigotName = root.get("name").asText();
            this.tag = root.get("tag").asText();
            this.version = root.get("version").asText();
            //this.authors = root.get("authors").findValuesAsText("name").toArray(new String[0]); Not sure about which value this is. There's contributors and auths
            //this.testedVersions = root.get("testedVersions").findValuesAsText("name").toArray(new String[0]);
            this.downloads = root.get("downloads").asInt();
            this.releaseData = root.get("releaseDate").asLong();
            this.updateDate = root.get("updateDate").asLong();
            this.price = root.get("price").asDouble();
            this.rating = root.get("rating").get("average").asDouble();
            this.premium = false;
            this.fileSize = root.get("file").get("size").asDouble();

            String url = root.get("icon").get("url").asText();
            this.iconUrl = url.isEmpty() ? "https://i.imgur.com/V9jfjSJ.png" : "https://www.spigotmc.org/" + root.get("icon").get("url").asText();

            String sizeUnit = root.get("file").get("sizeUnit").asText();
            this.sizeUnit = sizeUnit.isEmpty() ? SizeUnit.NONE : SizeUnit.valueOf(sizeUnit);

            switch (root.get("file").get("type").asText().toLowerCase()) {
                case ".jar" -> this.fileType = FileType.JAR;
                case ".zip" -> this.fileType = FileType.ZIP;
                case ".sk" -> this.fileType = FileType.SKRIPT;
                default -> this.fileType = FileType.EXTERNAL; // Includes "external"
            }

        } catch (JacksonException exception) {
            exception.printStackTrace();
        }

    }

    public void sendPreview(Player player, boolean containDownloadPrompt) {
        player.sendMessage(ChatUtil.format("&8<---------------------- &7[&b&lPPM&7]&8 ---------------------->"));

        ArrayList<TextComponent> informationAsComponents = new ArrayList<>();
        try {
            TextComponent component = new TextComponent(ChatUtil.format("Name: &b" + this.getSpigotName()));
            informationAsComponents.add(component);

            component = new TextComponent(ChatUtil.format("Description: &b&l[Hover Here]"));
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatUtil.format("&b" + this.getTag()))));
            informationAsComponents.add(component);

            component = new TextComponent(ChatUtil.format("Downloads: &b" + this.getDownloads()));
            informationAsComponents.add(component);

            component = new TextComponent(ChatUtil.format("Rating: &b" + this.getRating()));
            informationAsComponents.add(component);

            if (this.getFileType().equals(FileType.EXTERNAL)) {
                component = new TextComponent(ChatUtil.format("Spigot Link: &b"));

                TextComponent link = new TextComponent(ChatUtil.format("&b&l[Click Here]"));
                link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/" + this.getId()));
                link.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatUtil.format("&bClick to open the Spigot page"))));

                component.addExtra(link);
                informationAsComponents.add(component);
            } else {
                component = new TextComponent(ChatUtil.format("File Size: &b" + (this.getSizeUnit() != SizeUnit.NONE ? this.getFileSize() + this.getSizeUnit().getUnit() : SizeUnit.NONE.getUnit())));
                informationAsComponents.add(component);

                component = new TextComponent(ChatUtil.format("File Type: &b" + this.getFileType().getExtension()));
                informationAsComponents.add(component);
            }

            component = new TextComponent(ChatUtil.format("Directly Downloadable: &b" + (this.getFileType().isSupported() ? "Yes" : "No")));
            informationAsComponents.add(component);
        } catch (Exception exception) {
            exception.printStackTrace();
            informationAsComponents.add(new TextComponent(ChatUtil.format("&cError ID: " + this.getId() + ". Please report this to our discord.")));
        }

        try {
            // "https://i.imgur.com/V9jfjSJ.png"; // White
            String url = this.getIconUrl().length() == 0 ? "https://i.imgur.com/bbxn0Zy.png" : this.getIconUrl();

            URL imageUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");
            BufferedImage image = ImageIO.read(connection.getInputStream());

            // rows and columns
            int squareSize = 12;
            int rows = squareSize;
            int columns = squareSize;

            // array to hold sub-images
            BufferedImage[] imgs = new BufferedImage[rows * columns];

            // Equally dividing original image into images
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
                    ComponentBuilder componentBuilder = new ComponentBuilder();

                    componentBuilder.appendLegacy(ChatUtil.format(builder + " &7"));

                    if (informationAsComponents.size() > row && informationAsComponents.get(row) != null) {
                        componentBuilder.append(informationAsComponents.get(row));
                    }

                    if (containDownloadPrompt) {
                        if (row == rows - 4) {
                            componentBuilder.append(ChatUtil.format("&7Would you still like to download this plugin?"));
                        } else if (row == rows - 3) {
                            componentBuilder.append(ChatUtil.format("&7Please run /ppm install " + this.spigotName + " &a-f"));
                        }
                    }

                    player.spigot().sendMessage(componentBuilder.create());
                    builder = new StringBuilder();
                    row++;
                }
                i++;
                Color color = ChatUtil.getAverageColor(bound);
                builder.append(ChatColor.of(color)).append("▉");

                switch (i) {
                    case 0:
                        break;
                    case 1:
                        break;
                    // etc
                } // TODO
            }

            connection.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.sendMessage(ChatUtil.format("&8-----------------------------------------------------"));
    }
}