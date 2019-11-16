package me.soul.tradesystem.utils;

import me.soul.tradesystem.Main;

public class Messages {
	
	// Convert with/without prefix messages from a base string
	public static String convert(String base, boolean prefix) {
		try {
			return ((Settings.PREFIX && prefix) ? (Main.getInstance().filesManager.getLanguages().getFile().getString("prefix").replace("&", "§")) : "") + Main.getInstance().filesManager.getLanguages().getFile().getString(base).replace("&", "§");
		} catch(Exception e) {
			Main.getInstance().debug("Languages file gave an error, maybe its not up to date?");
		}
		return "Languages file gave an error, maybe its not up to date?";
	}
}
