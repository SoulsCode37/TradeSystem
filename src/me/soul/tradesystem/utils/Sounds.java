package me.soul.tradesystem.utils;

import org.bukkit.Sound;

import me.soul.tradesystem.Main;

public class Sounds {
	
	public static Sound convert(String base) {
		try {
			return Sound.valueOf(Main.getInstance().filesManager.getSounds().getFile().getString(base));
		} catch(Exception e) {
			Main.getInstance().debug("Sounds file gave an error, wrong sound name? (Source: " + base + ")");
		}
		return null;
	}

}
