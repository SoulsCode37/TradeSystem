package me.soul.tradesystem;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.soul.tradesystem.commands.CToggleTrades;
import me.soul.tradesystem.commands.CTrade;
import me.soul.tradesystem.commands.CTradeAccept;
import me.soul.tradesystem.commands.CTradeDeny;
import me.soul.tradesystem.commands.CTradeSystem;
import me.soul.tradesystem.commands.CTradesBlacklist;
import me.soul.tradesystem.files.FilesManager;
import me.soul.tradesystem.listeners.InventoryClickListener;
import me.soul.tradesystem.listeners.InventoryCloseListener;
import me.soul.tradesystem.listeners.JoinListener;
import me.soul.tradesystem.listeners.KickListener;
import me.soul.tradesystem.listeners.LeftListener;
import me.soul.tradesystem.listeners.RightClickListener;
import me.soul.tradesystem.trades.TradesQueue;
import me.soul.tradesystem.users.User;
import me.soul.tradesystem.users.UsersManager;
import me.soul.tradesystem.utils.Settings;

public class Main extends JavaPlugin {

	//TODO Trades GUI
	//TODO Trades History
	
	// Build 1.2 changes:
	// Fixed a bug where sender's expire message was wrong
	// Fixed inventory click listener null pointer
	// Fixed run task exception in Trade.class when a trade was expiring and a player was offline
	// Fixed run task exception in TradesCooldowns.class
	// Changed how status debug messages works
	// Removed premium things from the code
	// Added check and message for empty inventories
	
	private static Main instance;
	public FilesManager filesManager;
	public TradesQueue tradesQueue;
	public UsersManager usersManager;
	
	// Number of build
	public int build = 1;
	
	ConsoleCommandSender send = getServer().getConsoleSender();
	
	public void onEnable() {
		send.sendMessage("§a> Loading instances...");
		instance = this;
		try {
			filesManager = new FilesManager();
		} catch (Exception e) {
			send.sendMessage("§c> Error while reading files");
			debug(e.getMessage());
			e.printStackTrace();
			this.setEnabled(false);
			return;
		}
		tradesQueue = new TradesQueue();
		usersManager = new UsersManager();
		
		send.sendMessage("§a> Loading listeners...");
		this.registerListeners();
		send.sendMessage("§a> Loading commands...");
		this.registerCommands();
		
		saveDefaultConfig();
		
		reload();
		
		send.sendMessage("§aTradeSystem has been enabled B" + this.build);
	}
	
	public void onDisable() {
		for(Player p : Bukkit.getOnlinePlayers())
			try {
				usersManager.getUser(p.getName()).end();
			} catch (IOException e) {
				debug("Could not end profile for '" + p.getName() + "'");
			}

		send.sendMessage("§eTradeSystem has been disabled");
	}

	public void reload() {
		if(Bukkit.getOnlinePlayers().isEmpty())
			return;
		debug("Reloading users...");
		for(Player p : Bukkit.getOnlinePlayers())
			usersManager.addUser(new User(p.getName()));
	}
	
	public void debug(String msg) {
		if(Settings.DEBUG)
			System.out.println("[TradeSystem - Debug] " + msg);
	}
	
	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new RightClickListener(), this);
		getServer().getPluginManager().registerEvents(new JoinListener(), this);
		getServer().getPluginManager().registerEvents(new LeftListener(), this);
		getServer().getPluginManager().registerEvents(new KickListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
	}
	
	private void registerCommands() {
		getCommand("tradeaccept").setExecutor(new CTradeAccept());
		getCommand("trade").setExecutor(new CTrade());
		getCommand("tradedeny").setExecutor(new CTradeDeny());
		getCommand("tradesystem").setExecutor(new CTradeSystem());
		getCommand("toggletrades").setExecutor(new CToggleTrades());
		getCommand("tblacklist").setExecutor(new CTradesBlacklist());
	}
	
	public static Main getInstance() {
		return instance;
	}
}
