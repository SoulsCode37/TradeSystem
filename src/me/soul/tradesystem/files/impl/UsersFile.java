package me.soul.tradesystem.files.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import me.soul.tradesystem.api.customevents.CreateUserEvent;
import me.soul.tradesystem.files.BaseFile;

public class UsersFile extends BaseFile {

	public UsersFile(String name) throws Exception {
		super(name, "");
	}
	
	public boolean existUser(String name) {
		return getFile().getBoolean(name + ".created") == true;
	}
	
	public void addUser(String name) throws IOException {
		CreateUserEvent event = new CreateUserEvent(name);
		Bukkit.getPluginManager().callEvent(event);
		
		if(event.isCancelled())
			return;
		
		getFile().set(name + ".created", true);
		getFile().set(name + ".trades", true);
		getFile().set(name + ".blacklist", new ArrayList<>());
		saveFile();
	}
	
	public List<String> getBlackList(String name) {
		return getFile().getStringList(name + ".blacklist");
	}
	
	public void setBlacklist(String name, List<String> list) throws IOException {
		getFile().set(name + ".blacklist", list);
		saveFile();
	}
	
	public boolean hasTrades(String name) {
		return getFile().getBoolean(name + ".trades");
	}
	
	public void setTrades(String name, boolean value) throws IOException {
		getFile().set(name + ".trades", value);
		saveFile();
	}
	
	public void blacklist(String name, String target) throws IOException {
		List<String> list = getBlackList(name);
		list.add(target);
		getFile().set(name + ".blacklist", list);
		saveFile();
	}
	
	public void unblacklist(String name, String target) throws IOException {
		List<String> list = getBlackList(name);
		list.remove(target);
		getFile().set(name + ".blacklist", list);
		saveFile();
	}
	
	public boolean isBlacklisted(String name, String target) {
		return getBlackList(name).contains(target);
	}
}
