package me.soul.tradesystem.utils;

import me.soul.tradesystem.Main;

public class Messages {
	
	// Convert with/without prefix messages from a base string
	public static String convert(String base, boolean prefix) {
		return ((Settings.PREFIX && prefix) ? (Main.getInstance().filesManager.getLanguages().getFile().getString("prefix").replace("&", "§")) : "") + Main.getInstance().filesManager.getLanguages().getFile().getString(base).replace("&", "§");
	}
}
