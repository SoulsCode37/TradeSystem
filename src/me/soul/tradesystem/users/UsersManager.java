package me.soul.tradesystem.users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.soul.tradesystem.Main;

public class UsersManager {
	
	// Local user array
	private List<User> users = new ArrayList<>();
	
	public User addUser(User u) {
		String name = u.getPlayer().getUniqueId().toString();
		
		if(!Main.getInstance().filesManager.getUsers().existUser(name)) {
			try {
				// Add user in file data
				Main.getInstance().filesManager.getUsers().addUser(name);
				Main.getInstance().debug("Created new user profile '" + name + "'");
			} catch (IOException e) {
				Main.getInstance().debug("Could not create new user profile '" + name + "'");
			}
		}
		
		users.add(u);
		u.setup();
		Main.getInstance().debug("Loaded user profile '" + u.getPlayer().getName() + "'");
		
		return u;
	}
	
	public void removeUser(User u) throws IOException {
		users.remove(u);
	}
	
	public User getUser(String name) {		
		for(User u : users)
			if(u.getPlayer().getName().equals(name))
				return u;
		return addUser(new User(name));
	}
}
