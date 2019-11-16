package me.soul.tradesystem.trades;

import java.util.HashMap;

import org.bukkit.Bukkit;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.utils.Permissions;
import me.soul.tradesystem.utils.Settings;

public class TradesCooldowns {

	// Cooldown things
	private static HashMap<String, String> cooldown = new HashMap<>();

	public static void cooldown(String owner, String with) {
		getCooldown().put(owner, with);
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if(isOnCooldown(owner, with))
					getCooldown().remove(owner, with);
			}
		}, Settings.COOLDOWN_TIME * 20);
	}
	
	public static boolean isOnCooldown(String owner, String with) {
		for(String s : getCooldown().keySet())
			if(s.equals(owner) && getCooldown().get(owner).equals(with))
				return !Bukkit.getPlayer(owner).hasPermission(Permissions.COOLDOWN_BYPASS);
		return false;
	}
	
	public static void removeCooldown(String owner, String with) {
		getCooldown().remove(owner, with);
	}
	
	public static HashMap<String, String> getCooldown() {
		return cooldown;
	}
}
