package me.soul.tradesystem.utils;

import java.io.IOException;

import me.soul.tradesystem.Main;

public class Messages {
	
	// Convert with/without prefix messages from a base string
	public static String convert(String base, boolean prefix) {
		String pref = ((Settings.PREFIX && prefix) ? (Main.getInstance().filesManager.getLanguages().getFile().getString("prefix").replace("&", "§")) : "");
		try {
			return pref + Main.getInstance().filesManager.getLanguages().getFile().getString(base).replace("&", "§");
		} catch(Exception e) {
			Main.getInstance().debug("Languages file gave an error, maybe its not up to date? (Source: " + base + ")");
		}
		try {
			Main.getInstance().debug("Error automatically fixed");
			return pref + fixLanguagesFile(base).replace("&", "§");
		} catch (IOException e) {
			Main.getInstance().debug("Cannot fix languages file (Source: " + base + "), please update manually.");
		}
		return "Cannot fix languages file (Source: " + base + "), please update manually.";
	}
	
	private static String fixLanguagesFile(String source) throws IOException {
		return Main.getInstance().filesManager.getLanguages().addLanguagesText(source);
	}
}
