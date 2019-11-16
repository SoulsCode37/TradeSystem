package me.soul.tradesystem.utils;

import org.bukkit.configuration.file.FileConfiguration;

import me.soul.tradesystem.Main;

public class Settings {
	
	private final static FileConfiguration c = Main.getInstance().getConfig();
	
	// Lite settings
	public final static boolean DEBUG = c.getBoolean("Settings.debug");
	public final static boolean PREFIX = c.getBoolean("Settings.prefix");
	public final static boolean CREATIVE_REQUEST = c.getBoolean("Settings.creative_request");
	public final static int EXPIRE_TIME = c.getInt("Settings.expire_time");
	public final static boolean WORLDS_TRADES = c.getBoolean("Settings.worlds_trades");
	public final static boolean UNLOCK_WAIT = c.getBoolean("Settings.unlock_wait.enabled");
	public final static int UNLOCK_WAIT_TIME = c.getInt("Settings.unlock_wait.time");
	// Premium settings (Ignore)
	public final static boolean COOLDOWN_PLAYER = c.getBoolean("Premium_Settings.cooldown_player.enabled");
	public final static int COOLDOWN_TIME = c.getInt("Premium_Settings.cooldown_player.time");
	public final static boolean RIGHT_CLICK = c.getBoolean("Premium_Settings.right_click_request");
}
