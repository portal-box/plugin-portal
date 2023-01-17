package com.zestarr.pluginportal.type;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zestarr.pluginportal.utils.ChatUtil;
import com.zestarr.pluginportal.utils.JsonUtil;
import lombok.Data;
import net.md_5.bungee.api.ChatColor;
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
            this.rating = root.get("rating").asDouble();
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

            component = new TextComponent(ChatUtil.format("File Size: &b" + (this.getSizeUnit() != SizeUnit.NONE ? this.getFileSize() + this.getSizeUnit().getUnit() : SizeUnit.NONE.getUnit())));
            informationAsComponents.add(component);

            component = new TextComponent(ChatUtil.format("File Type: &b" + this.getFileType().getExtension()));
            informationAsComponents.add(component);

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
                    player.spigot().sendMessage(componentBuilder.create());
                    builder = new StringBuilder();
                    row++;
                }
                i++;
                Color color = getAverageColor(bound);
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

    public Color getAverageColor(BufferedImage bi) {
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
/* JSON EXAMPLE

{
  "external": false,
  "file": {
    "type": ".jar",
    "size": 37.7,
    "sizeUnit": "KB",
    "url": "resources/hubkick.2/download?version=203285"
  },
  "description": "PGRpdiBzdHlsZT0idGV4dC1hbGlnbjogY2VudGVyIj4KIDxpPjxiPjxhIGhyZWY9Imh0dHBzOi8vZGlzY29yZC5nZy9TdkZFQkdXIiB0YXJnZXQ9Il9ibGFuayIgY2xhc3M9ImV4dGVybmFsTGluayIgcmVsPSJub2ZvbGxvdyI+PGltZyBzcmM9Ii8vcHJveHkuc3BpZ290bWMub3JnL2MzNGNmZWNkYzc0MzM1MDVlMGFkMTkwNmRkNTYwYmFhYjVkOWNkYzg/dXJsPWh0dHBzJTNBJTJGJTJGaS5pbWd1ci5jb20lMkZpbm13YUszLnBuZyIgY2xhc3M9ImJiQ29kZUltYWdlIExiSW1hZ2UiIGFsdD0iW+KAi0lNR10iIGRhdGEtdXJsPSJodHRwczovL2kuaW1ndXIuY29tL2lubXdhSzMucG5nIj4gPC9hPnwgPGEgaHJlZj0iaHR0cHM6Ly93d3cucGF5cGFsLm1lL0xheFdhc0hlcmUiIHRhcmdldD0iX2JsYW5rIiBjbGFzcz0iZXh0ZXJuYWxMaW5rIiByZWw9Im5vZm9sbG93Ij48aW1nIHNyYz0iLy9wcm94eS5zcGlnb3RtYy5vcmcvNmFjZmNiOTA0NDE4M2I1ODdkZDVjNzFhZGFhZGFiYjcwMjQ5MzJkZT91cmw9aHR0cHMlM0ElMkYlMkZpLmltZ3VyLmNvbSUyRmg1WWdkTEMucG5nIiBjbGFzcz0iYmJDb2RlSW1hZ2UgTGJJbWFnZSIgYWx0PSJb4oCLSU1HXSIgZGF0YS11cmw9Imh0dHBzOi8vaS5pbWd1ci5jb20vaDVZZ2RMQy5wbmciPjwvYT4gfCA8YSBocmVmPSJodHRwczovL2dpdGh1Yi5jb20vQXdlc29tZVBvd2VyZWQvSHViS2ljayIgdGFyZ2V0PSJfYmxhbmsiIGNsYXNzPSJleHRlcm5hbExpbmsiIHJlbD0ibm9mb2xsb3ciPjxpbWcgc3JjPSIvL3Byb3h5LnNwaWdvdG1jLm9yZy82OGMyYjg5ZThhYWM1M2M2Zjc1ZGNiODczMmQzZTEzNDJiY2QzZTJlP3VybD1odHRwcyUzQSUyRiUyRmkuaW1ndXIuY29tJTJGaWtqTHdYaC5wbmciIGNsYXNzPSJiYkNvZGVJbWFnZSBMYkltYWdlIiBhbHQ9IlvigItJTUddIiBkYXRhLXVybD0iaHR0cHM6Ly9pLmltZ3VyLmNvbS9pa2pMd1hoLnBuZyI+PC9hPjwvYj48L2k+4oCLCjwvZGl2Pgo8aT48Yj5JbmZvPC9iPjxicj4gV2hlbiBhIHBsYXllciBnZXQncyBraWNrZWQgZnJvbSB0aGUgc2VydmVyLCB0aGlzIHBsdWdpbiB3aWxsIGZvcndhcmQgaGltL2hlciB0byB0aGUgc2VydmVyIHlvdSBzcGVjaWZpZWQgaW4gdGhlIGNvbmZpZy55bWwuPGJyPiBLaWNrIGV2ZXJ5b25lIGFuZCBzaHV0ZG93biB0aGUgc2VydmVyIG9yIGp1c3Qga2ljayBldmVyeW9uZS48YnI+IDxicj4gPGI+Q29uZmlndXJhdGlvbjwvYj48YnI+IAogPGRpdiBjbGFzcz0iYmJDb2RlQmxvY2sgYmJDb2RlQ29kZSI+IAogIDxkaXYgY2xhc3M9InR5cGUiPgogICBDb2RlIChUZXh0KToKICA8L2Rpdj4gCiAgPGRpdiBjbGFzcz0iY29kZSI+CiAgICNIdWIgaXMgdGhlIHNlcnZlciB5b3Ugd2FudCB0aGUgcGxheWVycyB0byBiZSBzZW50IHRvLgogICA8YnI+IEh1YlNlcnZlcjogaHViCiAgIDxicj4gCiAgIDxicj4gI0VuYWJsZSB0aGlzIGFuZCBpdCB3aWxsIHBpY2sgYSBodWIgb24gcmFuZG9tIHdoZW4ga2lja2VkCiAgIDxicj4gSHViczoKICAgPGJyPiAtIEh1YgogICA8YnI+IC0gTG9iYnkKICAgPGJyPiByYW5kb21IdWJzOiBmYWxzZQogICA8YnI+IAogICA8YnI+ICNSZWdleCBwYXR0ZXJuIGZvciBmb3JjZWtpY2tpbmcgYSBwbGF5ZXIuIE5vIHRvdWNoeSB0b3VjaHkgcGxzCiAgIDxicj4gaWdub3JlUGF0dGVybjogJyhcYmFma3xBRkt8LWZcYiknCiAgIDxicj4gCiAgIDxicj4gI1ByZWZpeAogICA8YnI+IHByZWZpeDogJyZhbXA7NFsmYW1wO2FIdWJLaWNrJmFtcDs0XScKICAgPGJyPiAKICAgPGJyPiAjTWVzc2FnZSB0byBiZSBzZW50IHdoZW4ga2lja2luZyBldmVyeW9uZS8gc2h1dHRpbmcgZG93biB0aGUgc2VydmVyLgogICA8YnI+IEtpY2thbGxNZXNzYWdlOiAnJmFtcDthJmFtcDtsU2VydmVyIHNodXR0aW5nIGRvd24sIHlvdSBoYXZlIGJlZW4ga2lja2VkIHRvIHRoZSBodWIhJwogICA8YnI+IAogICA8YnI+ICNTaG91bGQgdGhlIHBsdWdpbiBzZW5kIHRoZSB1c2VycyBvbiBodWIgb24ga2ljaz8KICAgPGJyPiBIdWJPbktpY2s6IHRydWUKICAgPGJyPiAmbmJzcDsKICA8L2Rpdj4gCiA8L2Rpdj48Yj5JbnN0YWxsYXRpb248L2I+PGJyPiA8L2k+Cjxicj4gCjx1bD4gCiA8bGk+PGJyPiA8aT48YnI+IAogICA8dWw+IAogICAgPGxpPkRyb3AgdGhlIEh1YktpY2suamFyIGF0IHlvdXIgL3BsdWdpbnMgZm9sZGVyPC9saT4gCiAgIDwvdWw+CiAgIDx1bD4gCiAgICA8bGk+UmVzdGFydCBTZXJ2ZXI8L2xpPiAKICAgPC91bD4KICAgPHVsPiAKICAgIDxsaT5HZXQgS2lja2VkPC9saT4gCiAgIDwvdWw+PC9pPjwvbGk+IAo8L3VsPgo8aT48Yj5Db21tYW5kczo8L2I+PGJyPiAKIDxkaXYgY2xhc3M9ImJiQ29kZUJsb2NrIGJiQ29kZUNvZGUiPiAKICA8ZGl2IGNsYXNzPSJ0eXBlIj4KICAgQ29kZSAoVGV4dCk6CiAgPC9kaXY+IAogIDxkaXYgY2xhc3M9ImNvZGUiPgogICAvaHViIChzZW5kcyB5b3UgdG8gaGUgaHViIHNlcnZlcikKICAgPGJyPiAvbG9iYnkgKHNhbWUgYXMgYWJvdmUpCiAgIDxicj4gL2FsbHRvbG9iYnkgKHNlbmRzIGV2ZXJ5b25lIHRvIGxvYmJ5KQogICA8YnI+IC9sb2JieWFsbCAoU2FtZSBhcyBhYm92ZSkKICAgPGJyPiAvc2VuZHBsYXllcihTZW5kcyBwbGF5ZXIgdG8gYSBzZXJ2ZXIpCiAgIDxicj4gL3NlbmRwKF4pCiAgIDxicj4gL3NodXRkb3duIChzZW5kcyBldmVyeW9uZSB0byBsb2JieSBhbmQgc2h1dHMgZG93biB0aGUgc2VydmVyKQogICA8YnI+IC9mb3JjZWtpY2sgKGtpY2tlZCB0aGUgcGxheWVyIG9mZiB0aGUgbmV0d29yaykgKG9yIHNpbXBseSBhZGQgLWYgdG8gYSBraWNrIG1lc3NhZ2UpCiAgIDxicj4gL2ZraWNrIChzYW1lIGFzIGFib3ZlKQogICA8YnI+ICZuYnNwOwogIDwvZGl2PiAKIDwvZGl2PjxiPlBlcm1pc3Npb25zOjwvYj48YnI+IDwvaT4KPGJyPiAKPHVsPiAKIDxsaT48YnI+IDxpPjxicj4gCiAgIDx1bD4gCiAgICA8bGk+aHVia2ljay5jb21tYW5kPC9saT4gCiAgIDwvdWw+CiAgIDx1bD4gCiAgICA8bGk+aHVia2ljay5raWNrYWxsPC9saT4gCiAgIDwvdWw+CiAgIDx1bD4gCiAgICA8bGk+aHVia2ljay5zZW5kPC9saT4gCiAgIDwvdWw+CiAgIDx1bD4gCiAgICA8bGk+aHVia2ljay5zaHV0ZG93bjwvbGk+IAogICA8L3VsPgogICA8dWw+IAogICAgPGxpPmh1YmtpY2suZm9yY2VraWNrPC9saT4gCiAgIDwvdWw+PC9pPjwvbGk+IAo8L3VsPgo8aT48YSBocmVmPSJodHRwczovL2dpdGh1Yi5jb20vQXdlc29tZVBvd2VyZWQvSHViS2ljayIgdGFyZ2V0PSJfYmxhbmsiIGNsYXNzPSJleHRlcm5hbExpbmsiIHJlbD0ibm9mb2xsb3ciPkdpdGh1YjwvYT48L2k+",
  "likes": 7,
  "testedVersions": [
    "1.7",
    "1.8",
    "1.9"
  ],
  "versions": [
    {
      "id": 203285,
      "uuid": "00000003-c001-1c6b-0000-0179a7ceb11d"
    },
    {
      "id": 192297,
      "uuid": "00000003-c001-1bd1-0000-0179a7c0f194"
    },
    {
      "id": 153537,
      "uuid": "00000003-c001-1bd1-0000-0179a7bfe2df"
    },
    {
      "id": 130477,
      "uuid": "00000003-c001-1bd0-0000-0179a7b1dbda"
    },
    {
      "id": 86761,
      "uuid": "00000003-c001-1c5d-0000-0179a7b0c429"
    },
    {
      "id": 529,
      "uuid": "00000003-c001-1c5c-0000-0179a7835164"
    },
    {
      "id": 469,
      "uuid": "00000003-c001-1c5c-0000-0179a7834c4f"
    },
    {
      "id": 273,
      "uuid": "00000003-c001-1c5a-0000-0179a783059a"
    },
    {
      "id": 253,
      "uuid": "00000003-c001-1c59-0000-0179a783046d"
    },
    {
      "id": 237,
      "uuid": "00000003-c001-1c58-0000-0179a78303a4"
    },
    {
      "id": 216,
      "uuid": "00000003-c001-1c57-0000-0179a78302db"
    },
    {
      "id": 215,
      "uuid": "00000003-c001-1c57-0000-0179a7830276"
    },
    {
      "id": 211,
      "uuid": "00000003-c001-1bcf-0000-0179a7830275"
    },
    {
      "id": 208,
      "uuid": "00000003-c001-1c52-0000-0179a7830210"
    },
    {
      "id": 201,
      "uuid": "00000003-c001-1c51-0000-0179a7830147"
    },
    {
      "id": 171,
      "uuid": "00000003-c001-1bce-0000-0179a782fe8a"
    },
    {
      "id": 136,
      "uuid": "00000003-c001-1c48-0000-0179a782e0d9"
    },
    {
      "id": 120,
      "uuid": "00000003-c001-1c46-0000-0179a782df48"
    },
    {
      "id": 88,
      "uuid": "00000003-c001-1c44-0000-0179a782dc8b"
    },
    {
      "id": 67,
      "uuid": "00000003-c001-1c42-0000-0179a782d9ce"
    },
    {
      "id": 18,
      "uuid": "00000003-c001-1c38-0000-0179a782b961"
    },
    {
      "id": 2,
      "uuid": "00000003-c001-1bca-0000-0179a782b8fc"
    }
  ],
  "updates": [
    {
      "id": 206708
    },
    {
      "id": 195355
    },
    {
      "id": 155827
    },
    {
      "id": 132176
    },
    {
      "id": 87713
    },
    {
      "id": 529
    },
    {
      "id": 466
    },
    {
      "id": 272
    },
    {
      "id": 252
    },
    {
      "id": 236
    },
    {
      "id": 215
    },
    {
      "id": 214
    },
    {
      "id": 213
    },
    {
      "id": 209
    },
    {
      "id": 206
    },
    {
      "id": 199
    },
    {
      "id": 169
    },
    {
      "id": 135
    },
    {
      "id": 119
    },
    {
      "id": 87
    },
    {
      "id": 66
    },
    {
      "id": 17
    }
  ],
  "links": {
    "R2l0aHVi": "https://github.com/AwesomePowered/HubKick",
    "discussion": "threads/hubkick.1277/"
  },
  "name": "HubKick",
  "tag": "Send players to lobby on kick. /lobby / hub",
  "version": {
    "id": 203285,
    "uuid": "00000003-c001-1c6b-0000-0179a7ceb11d"
  },
  "author": {
    "id": 106
  },
  "category": {
    "id": 2
  },
  "rating": {
    "count": 12,
    "average": 5
  },
  "icon": {
    "url": "data/resource_icons/0/2.jpg?1482076430",
    "data": "iVBORw0KGgoAAAANSUhEUgAAAGAAAABgCAYAAADimHc4AAAohElEQVR42uWdebQvV1XnP/tU/cY75ua+l5eXOYGYDgFiCBgxDQHHZasLJ5QGRVlKizjRrdKKtkOLKErTEUTQlgYFFkojbbetEVyQRMAAAWIghCGE5E1577777vC7v7GGs/uPmk7Vr36/e5/Q7Vrd963fu3Xrd+pUnb332cN373NKVJW6HxExqmqZ8/N/s83/qz+SMWAWEdzzImIA5hEra5P9uNdW+6m2mddPtV31ec/n2VTVzhuv26auz+r56thmPWd27J4z84hTd7H7YPPaV9vWDWhef9U2VSLsJxTz+qw+z7w28wi/36yeRXy3jV/3EC6xZxG5KqF1f7sEnkXs/Rg8b2bNE4R5M2m/Z9+vj1k0myVo82b8eamgA+m0fdrP+/6f2xZ8Oc8+T6jmtgHMPFVxUH13EN03z6bUnbdb96ID0wz2Nq/zFtav0zBYjwa9tfGZR1eGX/rMePDQp76kQfBQ94Klh8JxsPG433pfdD7P+ZU6Piij6s5LZgdcItRNyVlTf7+favtZDC5d84k/Rg9/9aU6OPsSxr3vJ5pcyXgHHe+CjcD40F0jkg57pzbY/sTfB/He7v3G925vtlrvvOo377D73WvWeOuEYeZz1jgBs5yPut8lFfSVmqZf1nQUMfaTb22q6fw44eiXddJfJQ5heBYNJ4CCavI7u95rYC98PFsPHbfb994Fqm9rL3V/8qrfuKP/lVYX/ydU3L4q6CAG8CCz4iB92U+8Y1GN/BFR8Fwd7UAwgDgEjdBwDGpTJhQPj4CqIt0L6U86nP67/2lV9c72Uvs7r/z1O3pViTuoapnnsp4PPfY7Nvt5E9WOD+pFzHLLZnkb9v53N9WYt6r4z8U0IJokDLARiI94DUQEQRABkVIfMNxi0dvmyC1PNYg+Y7w3es3Z13xXs258LpFnHdepqaprWfXyzmdmZdfP9LnrPgdp80/6fOgNaDz5eZDnYGMIh9Bog/FQjQELpgHioQKKpB9QFTSbFMGQxSWfC598s1HlRVuntn7MHeysscyKPWbRpkrUWTHLvH6ya2cGYu7vL8ffrvZXd2w9cz2T/s9hPCQaoyKI14LWEoIkakgE/FYyAxzJl1QPSTotNJ5wwZoxzdVVa63+8ud+5hmXumObNa5Zz7wfHWaN8aA/Zl5QUQ2O9nswd5rW6b26h9Vjfw7Ky9TGi4y2AYvYGIwHzQXwGsnDZUzwGqiQyX/yEUVT4yyAiLJ22SGD6qqKvJxfvW1uIHWQCH8eNFNHu4MwKbcB1WlYnUqzvqtOp3nTr256AujGzjrod4DCaAuNw4T4KGI8aC8nkq1xYoRTW5Con5TwKfHzv7EsXrhovIYH8D1f2LWLLqGrn7rxzRvDLLVW931dP+79TB3x6sLr85Wgg07hOOJmRNYlJaiMd9LniRMXx29Do5OoGBsmF3ut1AMSRw0VKklVML5He2URhcOxyLPnuZJ1383DhA6KBx3kfrXqYx6xDzLFzgfzMca7AQRslBDVxhD0k2BLQIyHNJdAvETSbZQcew3E8aZFJLXEBUNaS12TjvhZB1GXdX9Xx74fPequn0czM0t91Fns6iyZNUWrrlqdz3znbbeZ97/4B7rj3uDxmcRrSlKd9JEoQK0FVdTzob2S2oIINAavCWJytaMKKoqSqSHwGg0DWLV6k37gLbXgYN146p59nsqqu34WDar339fC74eXV/3keXj7rzzzmXzDV928+o3XPfWbfufs+Pd/7x8e+vh7P/yFF0VhhI1i1GoR6I53ExuQhLsJwf12IuRRkNqDVmKYs6mgeXiWR2pijG+arScFC93nhPf9j8VZQnE+KuUgKumgKs/fD97dz+pX1U5dm+vWv8r/mqtuvBnP+4FWw//WRsO/vNlq4Dc9xuOA3mYPIcRolKoiBY0xgxC/u4whTvS76SA6Sgx0NAG/jXhNiIMiLE5tA4DGFhRMs7UMvFvD4LPBvf/trRj5k0bLnjY3PHdmbmEehlMX+c6DHubBHP5BIrbzzW7lAzn58ebe2XPfcurMuX8XBNEty8uL/sJCh3bT4MUBfjRB1WI1cT01CtA4ROMItTEah8SxRxjFxFFIHAQwiTHRAFC8xhg1DYxGCDFGBOMZRARjhNHukHBiodOGOESR6zDmVah5WTiUt0w+9ue/3wz1RFWd1o15lhdzEHrNi4RFVffl/Lx0YK2BfeQeY2O9BXglIs8ANaoKUYhOBgmwpha1UaLT4zgH2lSSQEtVEy+n/MCojbHBkHi8RzzaJZxMiMIQDTKsSFCb6LEz9z1COIo4fOMTueSWW8p9igFjNkHeKOjrml/7/I1/lhxEFg3/U1ypOgAqeuhDa6i8AuHFCt0siNJxHw3HiWGNwxRk0/RaUvexjPHgqvZZ31lLHE4Ih3uEe9sEg13iyYjxzoCdYzusXnEpR266kdbS8rzhPAK8UkTf0br1h8cHAeDm0WEm8llnU7MZ8OVCsfHD9xhUvxV4jcK1khJHx71EOq2FOFExaOGpZITPpEFTf15RRIsG+fkStxxUNPOgVLFRSDgc0FxYxPgN0jGiKZKauawqClp0IsqdCC9vteRenvqDdj86TP7ytWtWuRk4gnBCkA92nvOy6CCweykjNg9GPQh8Gz98T1ut/VmEVyg0UdBghI56iV8fBYXEZ0R01Ermu88/zghXMGOKMfscT1+bxgxaMB60h/IrIvLG5q0/OK4b7/g9r1218GPAvwEu1yRqt7Sa/8UsdF7a/pc/ZA8Me9cxYF5OoDrloi99ZA3VP0LtczKVoKMeOhklRI8CB8Mv9Mj5JIKqqknLzuZUu3xGZbGZtRDFyReeQXx/ShNnsyTpX62o3oHwc621hc/KExJvafye3zWK9y0Kvw1cnwQbBrpdaDQQkaGIXtO69YWn6zygOpWdJ2T2y+LUcc8e+/hRtfoutdHTITWyw91E5UQBmiVQMoLlEpjyJKPqDGZoqmYkl1CXj1XpJlExbqYmjmE4giAsS3+nDZ1ObpBL/ZDGIsl9t4DXiMjr7ebWOvAqhe/S1H2XZhMWu5DFs4kAPKV96w9+4iDJHREx/n6EnhWIxcc/cTnIuxCelkSvI3S4C+E48WxQxPhIZxkNRhCO0gFrTnzJxVkcPmg50ULKBKaDLcnxz4T4jgRDGMLeIDH6LtNVkdEouVm3W/G0tDrb1hB5pZ0Ez1NYB44kd5Tk2k4r6Wc0hmYzZYQszpL2Oib4Vbh4npXPiX/sk1cr+h6EG7AROuwl+j6coDYuYiK/AX4DQRMPKHMDpeKH4cZPQi7LKjUzctqNKzELwCrsDXJDLQj4XgroWYgsjCfg+dBuOXY4nW4urwGdBDcoTptuBzpJbsKe3kR7e8jSInL0MKia/SJjNz7w96uIqM4Me/wfLwf7l8D1xBG2dy5xMeMQ8XzE89E4SgYajNE4TvEbzd0crQwwk/LihFRaiKP5dTY3suuGQ1RtkhvwfWRxMdHV2SybTGAwTNo1G2DEUSGOkyCSMDOKigdvtxMVhqCTAO3tJe2DMONd/3zKHf39YIdS0vzEJ1dB3g7meuKIeHcDHffBWrxDVySYTTTB7m5gg3Ey2ihgSqTqwhGd8vwdgivlkEwqbVIVhCQqZxKk0iywtJjmFxw11W4n4N1ggAxHyOKC063m+YZcWGzqvnoeLHTSeSDYXh+1qRJsNQECkI15UH1VyGthhDpVZE/cZ1B5lSK3ahxiexvoaA+iALNyCDGNRPpTSEFyw5s9ruTpxPL54rhIcpXboVKgnjjGVl0DnhItCIvETLudEz9XH9mF7VbiDU2CAgRE05xzRV4S3Q4LC4iY3HDr3qDwytptQB5R1c262KGOCSU4ug52zXO29l7U6veoyIuxMba/hR3sJEEVmScRo+MBdvtUCi2rI9wZ2bSkccpKSHNgM7MFmdcimSF2DGbmqWgmtRn34rjQTs3U/jj3KnnDnU4CW0Rhofur1BdJpLvVhIZPnoALguSTtem2Ab3X92RcB93UVV+oqvVnJWBcQ2GP33ctyO3Y2OhoF9s/h9oQUUFFiXc3kGYnwXlsnCbIKzZW6w1vHoXO+i7LfOU2JPV9xGGkFAwpioYM4nmolhHSjJmqCYPwDBqESLNRPGLVLe60cv5kcYb2hynmRKKWGj7A3zZu/QF7PqWN/jx8Q1WtPXFfW+FNoIc1GGH7WxCHmdwnv22cqKPcGyn8+CxFKJmOzuMCcdpUotdS3CAlG6C1XCvssWbKKyseKjiDhhH2zFnEGOSidfC8RLKDsPD9XSnQYuYVMUt6w+zaMMQsLyEipwW9Y146s04l+fNq9+EzRpUfUriNKEAHu2gwzCMiFSl5LkUUmlUnSMGEBHhyYACX4EXAlQ1cxYX4034yNjgBnLqBmAAmU1mSMzrvdns319sYDzmynjOA1O3Vio3KIZHSRFZkZQmztJB4e54HqneYpt0837pSM69sJD4RLgOvyCTcjnYKQ0mhr3ONo1TgY0c/5GiEVnycIlJWKWxBoa81JT556UlRiZU7LkVI5nm5BsmJlUW3QVjMpCxIM15irHMfedrY5xYsG4dmUbpJiJ/8fFscelfvlxeozgIzt5hI7feo6qVMhkmglRq4csifCY/mOVlX6goFIqkGFididQOgspspqT5QkdzYJqogOaeiOedFnFnR8JPj2CaET9vnbVKh0DiCMM20tZKIVvLAz5GAnOdld7dkeFRQZF0tbxp84C3tWZ5PbVXErDqZ6NFPGFRfSDRBJ8ME3czDoQI6ULdKLa9QozguO58VL4iyXRAp2ZDiSinZ7+Jc1cNJpFKazUSSwzh1b9N2vpf6Ylp2YcU1K2U0r+T2Zs+rZQjF+fLZIC9S/RVT59rXVVbMLklRe7lG4S06HiSVyTZmbqhU8eKqUW9BUMkVg1DoaZzpXRcr48w9dV3a0jeF16Ka4EHqcrvTTojWbqf6v1EQWBz1KEXcIZXArxB6LWZHGsOkNHj56O6rV+vWidWhy7PXRsXxdxBOfI3T0nAnlNIpjzHVs64kpmqpjK1kbcr9aN4DNdahKufloEpK8yL9v+EjK0sJQKYgKoiCLHYx11yOufISZHWl6EWlXFGRBYbuzFVJ1FDm/qbH6kzp9KtLVeWHZpX6VJlQu6oj+txdaDj+VxpO0oRKWJK/Gogs1ZFllSFIEiuUTG1JWqqt3dLb8gypxNKZgXdlFC3O02jmBrJQlQbxGwlDKlNZkKlJ5yJQOI6Glma9lKGUJHJ/yfDOty0eJBCrN8LKosbhzao2QThLul/cbGB+7IKRmud61TFaBfOymZG5eXmMlacqpYg4XZ3rwBV5JZx73qmQc2dTeaaVIKYC89Fp0dJqm6myo0x1qiMUADxOrT7jIKtF66PgOLqBOF4FQVMwrWQIpZC07FhVpjNMJZjByb2KlOEHqbRxArq8H6mxCu5KDbdAyzHm6oiwSF0/Uu8AzvQMHVWcS195Rqea4nnBnW+pjQnc4/qK4Ti6BSVxO+Mo0XdaXqLlSmzJM1HXS3FBtULiyYMqmSJAgetr2SvSSpyhlHRB1q/mfzOFhbgzt8QNJ5BxR5OfVyfCzsddnVZawCTJbPmGwHqL85L6peroXBV9+gMG1ackMGyYBx1TLqjzoFqRHHUgZs31tFPpUGpXzvFmq17QXJIqGQLXk9KKKZZKf1JxeR01MwV/yxS8kTC17AhXYSLFVWeaR9OoHAGetF8x11QpYRAHRtOEs6aJFHUsWc59rQYqbhYrVVFSNqyJ5+AMKMN/tILul2yEo/pFc3WFpBhS1lYo4Uzq2qKS1Ffi26ywN1vgoZQYrzXe3fSxG5VnVl1BuW3Wdgg5HD21XspIE7g8r3CQsu50p6ZKgUfkYBXFA+SLKFzvQaczX+owREsBhVYQUynrIdWyIFSDvczeZOvI8vZSqqLIgkimctKuI6olOFAclThdG5x5hfo1/ff96cw1FFMqSEQMaleBVaWS7HA9i9QWiDuQUkRQGGJKsEOhsjRlZE5vlYpakyL8d2eZFj65um1LOjoz7FIyjqXsm2Z5jGJs2XHOJJWpBYHu82cCpWJQ4ycZQfGKsYncqJ5tzytmnsoHaKyH1YhJqmNsUbRUNZZOgFKC4V3VgtTkeaspSDkPF4T6HHDlwoz4SUmQJRhNiIIItRZjPJqdJo1Wo8S0amatlH2uzK4MXMfzwfhIUmeaMMBdw2D1UtDLgc/PKnrwq0Zh8sm/XsfakqaTtGh2Oqtyfqvsc2OnOs8DPHjPs4q7RAhGE3bObDPsDdF4ul2z02Tl8AV0VxaK++m0DNTdQYyHmEaqozyk2QLjJ3VQNq2FihU1IBpf17v9lz6/9FP/0c6sjh7e/bamqL0ZBVlYPYLad2NjNJrMHqQDKtTVq0nJR9Eab0drYQ2dOlbHc8nyBlIxuOV7757dZeuxcyWpLpI7ZbJ2lxc5dNk6xjO1Ho4LFoIk6xGMSYjfaCPpKk4XMrd7Pez2FjocQhh+XsPoTWLMG1d/9BeGU4Z4eNfbloG/QeTp6V22QNZcL6BSJO6opTna4IAhjs4gfv3MmT6tlQKJ3bM7nDt5zoGe50zG9KDVbXPR1RdjzHSuVIvCUcRvplJvkGY3X82JJk6I7u1hz5zB9nbzbRWc6PpOjfW7L/zxX9opuaEIP6XI07XIc6y5UEKRgCj8Yq2NhQp3TiuuabGOS52+pBTqV3MtuYFTTQ1u1fPVFJFMfwtMhmPOndpyPCTH1UQrz1E8z3gwZvP42cp9Ha8pW5+c6X6Twto2TtpFIfGjjxA99AXszlZ6PqVDHEMYoZPgNoLwrbu/9x98N+g1qHyvg9rviPJQWb0KbjilZdNVxSFLcJo6BqzqBZVwmikXsmykq3B3gRGlT54yc/PkVom56nqq7m91PJv0098Z0N/uF1F/BY/Kx6egcYyGAViLjoZEX/widvMsGkVFXBQrGoQJ8dOlUip8W2T5ieFrf9HJByhdR+rej/KaAkgThwhSBq50GjmUKbjCSbZXvIhMSkXr2VllrTjxTSn9mdIrGAWM+qOpyH0qaHT86OpM235si9jaSrI/UTnZoDUrfYlC7GhA/Ogx7F4v9Rhtcu8wSqrppp5DUeUVI2msFYEY3Jv77MpzFF5SJMCd1ecuOul6Ca6KKmH4jtRqnRrSNLU4S1WpIwRS8ukrMBAAezv9slRrVerTZcZWiSJLXFKHySeYRAx3B0UMk11rbfHctiCqPb1R6PusfUp4N14qOQPCGvCC3ut/IQ3ERF6nQiBZihKeVAQ0UsHlySEFKeE9FbOrdUVAUi4nESo4TwW0cQGyIgeYZ59KeQUVhr1RGRqeCpaF0XDC8eObPPLoBtsbuwSjIKdd9tnb7lfUZfpFHKVLq5K6V90bYDc30dgmTLFJCrTsypaTOnkQCc+LIj/dNdHoPcS8VOF2sjVdWi5MrhLYDXREqiGa5rlU162RarLbQTmnUjxTUIWLPmglqS9Yq0zGgVtfAQrWsUZ7e0P+8I6Pc9/xTWK1XLq6yPNvuY7rr7mYRrORXzYaToijGM/z8lIYt0IjOYqJHztDHkHFWi01no4pShUjcpO1ehjYMJ1b/7Wl5b8Z5GtVeQPwYRUq3o+r98tQbJ1XQZYCdHV2OVat6ORygiTP02bQdQVEUgczUlWiKMLGNjW+hdeEU0ryzr9/gI89ukFkLZ4xnOoN+YM77+fM2V7JSMdRnEbNmhp3dTCktE62P0SHo6kh9HYHnHz0LHFkp4RJVXnwgUc59siZJABGbswr4xa+9vtQ1fuBlw7vfkcXy2MKy5Jjx1KKBwpYubaOcCq36/rpycI7dZYFVTC8vNpDSqhlEa7KVJgUh3F6jdb6/qMg4p4vPpbETr7Ha3/3FYRByL99+av48BdO8p0XreL5Xn6rKIxptiTVAu6CwPS5t3bdavvEHsSWf/jgZ+kPxlx77VFuePKVBaSuQr835DMPnsT3DBcdXaPZ8J8kIu+d3s1ECYCHKeHyVSSkri6/vlZfK6pHp5IvhctZslda6VuzTzYz0uhWhTi2BZhWBcsQtvdG9CdJZq/ZbHDNNVdx6aVH8T2PE9t9xJhy/GHLxQSFuhQ0irHD8VRIGUYxvXEAIjzyyAbBpFxFYhUsShhbdrYHANcEv/tbZSwoYYKx1tpPIHKjW8WmM8GCakyrUyWB+fVOHnhmZKzT1f8lqEFlKp1TpEi1Fi+K4yJ67w/G/MiLf54wignCiFa7gRGDVXcnRuP0VwFGJgFEtqZOA46NQ65oeIyDiO2tPS666IJiGS+wG1lWfI/hYAJwad8PmIJIO8/4fgt8bCqZUYd/6ax1i+LkCpy8QQ1IUQ7KpJatZcdcS7NLAc/3SwGTavmz2G3TahR1yCceO8uZjXMocOWh1cIlTe/leV4BRzu5BlXQUVAOUtN2nufRt8ooTp5vc7NX9vEUdsMYBfb2xqiaxdh41O93Ax+sLiVyK+AKHFwqmHtF3agT1WrNmq8sp5C2EZ0NOZdyAdmMygbveyU1QiUOWOy0eNrjjjoFWMn1C60mN11zSaG60o2ePN8vp3qdqDvsj4gjSxxZXBzZiOAbw046O3q9sVM5l1w7SiPi8TjAqjbjIMLU7veDeQh4pJxyTML1cye3GO4MnTRl2TNxV7KUi7WqQVmR7sv/OYrYxWNUqfjSZZzHGJPi+/Wemyq84Jk38tVXXoxJibLSbvIjz76JC5cXShhVq9PCOAW+RdFecq9gOCIMI4IgJIrjivct9KJEykfjoBSRh3FMYJNa1cha+oNRbzCcULtMde8D7wzU8l7gxZkR2ju3x+bxzZSweyyvL3Lh0bU8Snc9GKnJmgr1iZ0K4FxeJqrVkg+tBelVlYWlLuPhJEsFOvYiuaDTavDT3/50HjvXYzAOOHrhMoudFrbS98JSN63acNMWCYNsnLioxqbC4mlpXYOI0I8tMTAOY9RmY1F2BxOiNDZRBBtrr9FSarcpW7zt+yyqf+oq/v7OIFmrpmBV2Tm7x8axzcTA1RTdVmdP1S+iat61DF1MN9NyZXoF41lZW3LigGL2WNXEA0n7ufjCZR53yTrddhNbQWiN8VhY6abSTqVeNdt/SLEpA6wtZqxNIYqJVWIl8czSvqPI8siZHWzaWavdIFZ9+Mr1c9MZMQen/qhVPqro05JKZ5O6Z4Vv0tvqE4UxR646jDFSWXZarmhWnZUFoLTY4kBVUjptqP1mg5W1JbY3d8tVakqdW1WbXlo7tITve+ksLldCSCp4cWQxtoga3ftEsSWySqyKRdFYGQYBo0nAsbO7ieSjtNtNbGwf5MVvsjMXaC994/MjVF+ZRbsLK91EkmwKSKUYymB3xO7ZvTII5kC5ZbCunPwu6fkab6sOUEOnv88aHLp4jUazmUijnQb2bCq1+W9b/O03GlxweAWrNYiv+yzpNXFcngHZc1qS/mKrbPUG7A3GxLFyrj8GktnY6bQA7qdu10SXIZ7RO1C9QxW6Sx06C61UBSUAoLXJ8XgwKblKqi6MUONOOd9rpbRErbNHt86CO3SqD1UwnuHSqy/C871EvdgMAS2eOf84XQjCxZetp+7ntAOgWl7EnTM0Tju2SQFDnApSDMRWmQRRglNNIjb7I3xJPLZWqxGo1WOzSxPTz8LXvyCYBOEv2NgOReDwZevJFE2lJkMBW+0GmYHQHLspw7GZRFIZ1FRZIdVzOjtrVsKnkk+z1eDKay+h3W0lqqAq+ZWPGOHiKw6zsNSpYXChO5XERuB5+czJ9LxVJQjjfGZaVSe2EPYmAZPI0jDCwkIbEfm8jXUrr46etRemiJh+f3j/9k7vP41GY/ymxyWPu4juUjsvsl1a7bK8voS1ytmT2xx78BTbZ3rTeYScEdRKv5szqOizKVVQrDnTcqVeuuC60fC56vFHuejoGl4qMFWVlHg8Ha689ijLqwsz7luebSIUq2/SvMJwHLK5M2Bzd4CRTAWBdRDU4SQkRmkbw/raEtbqu5CEYX7ddlpuBdc1z/sJHviv//m3J0H0jM4kfMbSYpdLrrmIMIiSJVnNpItRf8zmqSTfPBxMaC+0aHeb5ZUrepCiHy0XgTENsGnpvJbhbCduWL9olQvWl+n3Rgz6I8IgThbIdJosLXdpd5qVDaTKFd3V9IYINFcX6Z/ZYhREjMMI4xk8z8MINNKIXAHPJHuaxgqr3TZrCy0OLXQ4cmhlqNa+5UmveR28+nXGn7WtYuXv/qfe/Nof7g9G7xuNJld32i0WFjr4TimHmCIPK6l+dP13qcAPMqtGosIvcfcBqq6rVK2UyJRjBtUkQl1e6bK80p26a9nbqdkvRAtUy2qSc9izlnPDCXG6ttj3PYwxWIVLL1jk2NYeFqXbbaX3V1q+xwuf/gQ8I/ieebNVTuRl69W3KM3bXOi+N/3OjQr/CzjiGUO322Z5qYuXLrTZ3dxj91yfhZUOFx5ZKdX7H3hrrGo5yIwN/MrHWtq0o3SuVLpSxbxrIHVnfbK1ShCEjMYBg+GIKIpQYHBik/jsDopgROi0G3ieYRLG/P3nT3Co6fMvrjxCK9UODhlOiPKUp97++s18t5T9pN/9fqXZv297vPC9qvru2NrDYW/AeDyh026xuNBheX2R5QsXncBKp7cs2L9Mp5R44QBgoJYzH440a3klzIxOcoKnuYDxZJLADWHEZBJiU3Jk7VqHVuhv9yGKia1NUFVp0PAMX3/9FcV6sXJtbSTCSxp4m6X3GVQHud9OiZ9786tNf8zTFP2zrIqaVOd1Oy2Wl7o0fB/jJFTEXWA1KxrSMvCs5eUtpY0+tCZA0xp01p0hOqP4MY5jhqMJkyAkCMLUdbRTK2qK3drTJM/WHpNjG6hVjAjddgM/W7QtlX0uklO/3hD5tZtuf115K0t341b2eV+je/6jr/utq0HfpcqN2fCMJK5as+nT7bRY6LTxfVPo/xn7Ls3WSOXGU7lWN3egtQtWS65hFMWEYUQUxwyGY6LYEkcxYbbDSnWDECmKK8WIAwIkdx2c2SY6s4ONYjzP0G030+3jJN+SIWXAGwy87Gte//vB1KYdVQbM27yjahs+cvtvfpeIvNtqeR0k+Yt2hGbDp9Nu0u208D2PRsObMsjMMso67fWos85Fy5onr4ZO/PKIOLYMhmPCMCSKLUEaGNWWUUp1D5B0DCnxp5ZRqQ6BuwcnN98Tnt15PsitnmdMt9XIbZ+IbAKvEuENX/eGPwhqwfbz2T5SpGNg3YdmF/BvfcKVi7/xwme+p+l7Nyq11jE3gNnmHc2mj+cZOq0WzaZPw/fxjMHzJN8MqbT9jHXWGKdRq7WW2Cbb3Y9GE8ZBQBRZJpOQyMY5XFBe/cKcrc7K6/2MTG/DkglUMInobQ/5/PFzf/irf3HP7eMwGn/V+mL0kqdedf1lq92bu83GBd1WcygiXxDh71T19K1vfKPdd+PW/XYQh0MGVtYEuRLMYRGaoOaFX3/Dk1/0zTf+exHxcTZZLaBkLTHGVSrZZts2LYdv+H6y8XY6aGMkjzAhIaoRSVRHbBPX16aQgLsl1IzacncFC6X1v4m6qXoMkrrXNlaG/Qm7u0PGwxCrGv3RXQ/82r2PbJxQiAQCVXqgpxXdgOEO9Mewu+8G37WBWHkfuQsMXLUI3uWCuRphLSF+8pRvu/MzJ5/6+KN3P/Hqw8/OqsacJSrlJErFY7GJFczjhzC0tTniup3+tM4uaNUjkiniu3QurX1Ip5xJ88thEDEaBAz6Y8bjMFFdqVo6vTv89CePbW4iNAVpgnZFZBXlCEofuhtK9xRcsCmy3odzM1+tKPPeqA1HfFg8KpjHIRwRSQu3KrnOKw4tdm9/8Tf+3Opi52j+RqNK7mQwCjY3dgfHLju0fIPnSVN1/hqLclQ7nYypva7KgBp/X2u0kFWNesPg1Lne6MRDj20/vGK8aw8ttJ/mZoect/YFf/IPn3v1Rx7eOD4d5+TPaEHHqrqpcAr0GJzqqQ7qd02sgyLgQmDxiBFzU7qBqXEgT5NgigYRzKNn+8M/fu8//slPfvvNP+N7pl31ZFTVvuuDn33n2+/6zKeecNmFa9/ylKuf+PRrj35bt91YFiNT+0mXa4/cgi0tdj/R0m7fpd21MiMiprAB+TIOdz+6VKI/8+jm3a94+13vmoTJmqyVTusjL33Wk5sXr3RuzNdBpzP6M49tvfdjX9o4LjIHVhEMSFfgcoFFq9oD22PWrol17z+BzrLAtaBriBqw6cfFlS0QI2L5q3sf+tJffeQL74xjG9nYYmObVBqrct8Xz/zd2+964AGrlk8d29h69V/cc9fv/fd7X3/6ZG+zd3bIYHvMZBgSBTE2w9mtg79nWShbeDmqtjC26bIgtbEdT8L+2Z3+w596ZOP977jzgTec3Ox9GnUr8sTB9RNbcs/nTtw/Ca1NzI+wO5oEr3//fX/86ZNbfz0J416WBTu+tffRt37owb/RdNwpTah8nHMaqeoGBJsldGtWIFYY3aMG2ocF7xYxchg0JbxkPokpjjGZOhfBvPC2Jz7xO2+59vs7LX8tsjb6+Bceu+NV777nb4ZBFJWCJIVrDq8s/+itN3z3lesrN4uIn9X4GE94ZLv34eO7/YfXVzprnYbfbfheMykcS2ZUbNVOwmg8CqLhxs5w6/i5vc2TW3vbJ7cG/Z3BOAjjxLI/64bLr3jZt9/8s0ZMswo1AOwOJid+7I1/+6q9cRjVqbRDS53utRetXjIMovEDp849NoniKFU9pvDR8uNUM+SrRE+rxh+F7Q3VbXtgNzSZAeu+sHyliFyPyHrSee6t1908Pz56wWL3ukvWDp3ZGew9eHJrxyYcz6adLTwvrGfE3HzFRRc/+7rLbj68tHC5EfwT2/3Pv+nu+9/XGwfBfPCiUrM3A+946Tff9HXfdONVL5BkRZAbDds/+9CDb3zb3Q/+o0hddFiFQ+ui6dp8p0XpWbX3wfbDsD17/1B337hp13PNh+WjgrlBkCPpkiaTGJlpgjrHZeYkzzWTaSl8XuwgPL0GzW1fv6RSZm0/AMZgfvpbn/LMZ95wxXMLJgifO3nug7/49rveHsXn+VK2mXllgnTr4tOKPQGT03AqmPVagJleULnxRQbahw3eDcClIH4x1YTKtMvOu9/PAKCl5rp5x1Suk8pMdP+ePhYRbnvCZZd905Ovevr6UufyL57Zuf91f/3x9w0mUTRjDHVxekWIJE1wylhhS9FTYE/DqJdsqd7bNw7YF4xLmLAEXLAmeNeDHIXc/zUFE6aqHazz/fkc19iZks2ZvYpyn+PsL88IsdUDYLRzAZpIoaewCfYU2A0I+7BjYXTwF0cf5C1Kye9FAytdaKyCLAosgyynx22gmbq1fqm6JZGSyrGY/RmRGX33WubaHr5iP7XuZdb/WGFH0dMQnwK7A7tjCIHx3DeO1OVYpt6gMQ+OTjpqppKz5EPTT962KV3QRZBlQRbTlxh0k5mCKQx4TtiSG3xwxe6qvFJ/FUaUmDyLylm7tNCGTJ1YIAICRSNSqCHZGZ2xQg/CzeSFl70IJnw5766f+xalgwF0YuACYJAyxU+ZYroJYxJVJYivCYF8AT8lkK+J7fVTY+wLmm68oCnjEpQ7uVZNakONQ3xTw6SMoFEquREQKRoJEmlOUK38Jkja2iC51th0UVgEsQXPwsDCCIgOrGbmHs9iwJfzIrfkmoZDEGtgNdVSoUmCGN+AB0QGmqZsT9RP3rig6cfzyWcSvuMI+IIYEF+xJmVqBDoGcX7HKWETiYbApsRNpb7nGPLFZLOA84DlZ71HjBmvAt43EJunjr6c2bJfkme69QqwBxwFzjhqOF+kYaADdB2YZGgTQc7UUitDo6YIOe89jxzgXZDn22Zm1smplTH7HR+kXSat/78en0/b/w2X4/nK2jG4QAAAAABJRU5ErkJggg=="
  },
  "releaseDate": 1364368440,
  "updateDate": 1515903349,
  "downloads": 13856,
  "reviews": [
    {
      "id": 343842
    },
    {
      "id": 342243
    },
    {
      "id": 243148
    },
    {
      "id": 175179
    },
    {
      "id": 175087
    },
    {
      "id": 118337
    },
    {
      "id": 76931
    },
    {
      "id": 109
    },
    {
      "id": 70
    },
    {
      "id": 62
    },
    {
      "id": 61
    }
  ],
  "premium": false,
  "price": 0,
  "existenceStatus": 1,
  "id": 2
}

 */