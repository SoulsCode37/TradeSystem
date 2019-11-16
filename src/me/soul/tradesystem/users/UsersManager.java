package me.soul.tradesystem.users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.soul.tradesystem.Main;

public class UsersManager {
	
	// Local user array
	private List<User> users = new ArrayList<>();
	
	public void addUser(User u) {
		if(getUser(u.getPlayer().getName()) == null)
			users.add(u);
		u.setup();
		
		Main.getInstance().debug("Loaded user profile '" + u.getPlayer().getName() + "'");
	}
	
	public void removeUser(User u) throws IOException {
		users.remove(u);
	}
	
	public User getUser(String name) {
		for(User u : users)
			if(u.getPlayer().getName().equals(name))
				return u;
		return null;
	}
}
