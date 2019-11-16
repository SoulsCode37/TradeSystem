package me.soul.tradesystem.listeners;

import java.io.IOException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.users.User;

public class KickListener implements Listener {

	@EventHandler
	public void onKick(PlayerKickEvent event) {
		String player = event.getPlayer().getName();
		User user = Main.getInstance().usersManager.getUser(player);
		try {
			user.end();
			Main.getInstance().debug("Saved user profile '" + player + "'");
			
			// Prevent memory leaks
			Main.getInstance().usersManager.removeUser(user);
		} catch (IOException e) {
			Main.getInstance().debug("Could not end profile for user '" + player + "'");
		}
	}
}
