package me.soul.tradesystem.utils;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import me.soul.tradesystem.Main;

public class Settings {
	
	private final static FileConfiguration c = Main.getInstance().getConfig();
	
	// All settings
	public final static boolean DEBUG = c.getBoolean("Settings.debug");
	public final static boolean PREFIX = c.getBoolean("Settings.prefix");
	public final static boolean CREATIVE_REQUEST = c.getBoolean("Settings.creative_request");
	public final static int EXPIRE_TIME = c.getInt("Settings.expire_time");
	public final static boolean WORLDS_TRADES = c.getBoolean("Settings.worlds_trades");
	public final static boolean UNLOCK_WAIT = c.getBoolean("Settings.unlock_wait.enabled");
	public final static int UNLOCK_WAIT_TIME = c.getInt("Settings.unlock_wait.time");
	public final static boolean COOLDOWN_PLAYER = c.getBoolean("Settings.cooldown_player.enabled");
	public final static int COOLDOWN_TIME = c.getInt("Settings.cooldown_player.time");
	public final static boolean RIGHT_CLICK = c.getBoolean("Settings.right_click_request");
	public final static List<String> BLACKLISTED_WORLDS = c.getStringList("Settings.blacklisted_worlds");
	public final static boolean USE_VAULT = c.getBoolean("Settings.use_vault");
	public final static String PAY_COMMAND = c.getString("Settings.pay_command");
	public final static boolean MONEY_TRADE = c.getBoolean("Settings.money_trade");
	public final static int SOUNDS_INTENSITY = c.getInt("Settings.sounds_intensity");
	public final static int MAX_TRADE_DISTANCE = c.getInt("Settings.max_trade_distance");
}
