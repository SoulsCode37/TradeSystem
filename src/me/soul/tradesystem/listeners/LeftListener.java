package me.soul.tradesystem.listeners;

import java.io.IOException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.users.User;

public class LeftListener implements Listener {

	@EventHandler
	public void onLeft(PlayerQuitEvent event) {
		User user = Main.getInstance().usersManager.getUser(event.getPlayer().getName());
		try {
			user.end();
			Main.getInstance().debug("Saved user profile '" + event.getPlayer().getName() + "'");
			
			// Prevent memory leaks
			Main.getInstance().usersManager.removeUser(user);
		} catch (IOException e) {
			Main.getInstance().debug("Could not end profile for user '" + event.getPlayer().getName() + "'");
		}
	}
}
