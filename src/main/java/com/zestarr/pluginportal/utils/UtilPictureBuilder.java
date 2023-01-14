package com.zestarr.pluginportal.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class UtilPictureBuilder {

	//        UtilPictureBuilder upb = new UtilPictureBuilder(
	//                student,
	//                new String[]{
	//                        "fffffffff",
	//                        "ffffcffff",
	//                        "fffc8cfff",
	//                        "ffce8ecff",
	//                        "ffce8ecff",
	//                        "ffce8ecff",
	//                        "fceeeeecf",
	//                        "fcee8eecf",
	//                        "ccccccccc",
	//                        "fffffffff"},
	//                new String[]{
	//                        "",
	//                        "&3&lYOU ARE BEING INTERROGATED",
	//                        "",
	//                        "&7Staff need to have a talk with",
	//                        "&7you or collect some information.",
	//                        "",
	//                        "&7Please &acomply with all instructions",
	//                        "&7or you risk being punished. If you",
	//                        "&7disconnect, the process will begin again.",
	//                        ""});
	//        upb.sendToPlayer();

	private Player player;
	private final LinkedList<Pair> data = new LinkedList<>();

	public UtilPictureBuilder(Player player) {
		this.player = player;
	}

	public UtilPictureBuilder(Player player, String[] picture) {
		this.player = player;
		populatePicture(picture);
	}

	public UtilPictureBuilder(Player player, String [] picture, String[] text) {
		this.player = player;
		populatePictureAndText(picture, text);
	}

	public UtilPictureBuilder(String[] picture) {
		populatePicture(picture);
	}

	public UtilPictureBuilder(String[] picture, String[] text) {
		populatePictureAndText(picture, text);
	}


	////////////////////////////////////////////////////////////////////////

	private void populatePicture(String[] picture) {
		for (String line : picture) {
			this.data.add(new Pair(line, ""));
		}
	}

	private void populatePictureAndText(String [] picture, String[] text) {
		for (int i = 0; i < picture.length; i++) {
			if (text.length >= i) {
				this.data.add(new Pair(picture[i], text[i]));
			} else {
				this.data.add(new Pair(picture[i], ""));
			}
		}
	}

	private void send(Player player) {
		for (Pair pair : this.data) {
			StringBuilder sb = new StringBuilder();
			for (char c : pair.getLeft().toString().toCharArray()) {
				sb.append(ChatColor.getByChar(c).toString()).append(ChatColor.BOLD).append("▉");
			}
			if (!pair.getRight().equals("")) {
				sb.append(" ").append(ChatColor.translateAlternateColorCodes('&', pair.getRight().toString()));
			}
//			Message.playerBlank(player, sb.toString());
		}
	}


	//////////////////////////////////////////////////////////////////////////

	public void addPictureLine(String pictureLine) {
		this.data.add(new Pair(pictureLine, ""));
	}

	public void setPicture(String[] picture) {
		this.data.clear();
		for (String line : picture) {
			this.data.add(new Pair(line, ""));
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setText(int line, String text) {
		this.data.get(line).setRight(text);
	}

	public void sendToPlayer(Player player) {
		send(player);
	}

	public void sendToPlayer() {
		send(this.player);
	}

	public void sendToPlayers(Player[] players) {
		for (Player player : players) {
			send(player);
		}
	}

	public void broadcast() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			send(player);
		}
	}

}