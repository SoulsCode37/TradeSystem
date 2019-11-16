package me.soul.tradesystem.listeners;

import java.io.IOException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.users.User;

public class JoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		String name = event.getPlayer().getName();
		// Check if user exists in files data
		if(!Main.getInstance().filesManager.getUsers().existUser(name)) {
			try {
				// Add user in file data
				Main.getInstance().filesManager.getUsers().addUser(name);
				Main.getInstance().debug("Created new user profile '" + name + "'");
			} catch (IOException e) {
				Main.getInstance().debug("Could not create new user profile '" + name + "'");
			}
		}
		
		// Add to local array user
		Main.getInstance().usersManager.addUser(new User(name));
	}
}
