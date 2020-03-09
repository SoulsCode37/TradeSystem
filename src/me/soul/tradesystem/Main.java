package me.soul.tradesystem;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
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
import me.soul.tradesystem.listeners.KickListener;
import me.soul.tradesystem.listeners.LeftListener;
import me.soul.tradesystem.listeners.RightClickListener;
import me.soul.tradesystem.listeners.SoundsListener;
import me.soul.tradesystem.trades.TradesQueue;
import me.soul.tradesystem.users.UsersManager;
import me.soul.tradesystem.utils.Settings;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	/** 
	 * B4-SNAPSHOT update:
	 * Fixed InventoryClose null pointer
	 * Fixed PlayerQuit null pointer (Need more tests, please let me know) - Suggested by: @Ask3r and @Luiz_Wenner
	 * Fixed wrong Sound name TRADE_SEND was TRADE_SENT
	 * Added AntiScam timer, with 'animation' - Suggested by: @blaukat and @xdDirti
	 * Increased 1.15 silent cooldown
	 * Switch from Names to UUID for blacklist command - Suggested by: @haeiven (Github)
	 * Added automatic languages file update
	 * Optimized code - Suggested by: @haeiven (Github)
	 * Vault compability - Suggested by: @haeiven (Github)
	 * Trade Items filter - Suggested by: @Choubatsu
	 *  New file: filters.yml
	 *  3 way to filter:
	 *   name: Displayname check // Need test
	 *   lore: Per-line lore check // Need test
	 *   type: Material name check // Working
	 * 
	 * Warning! Need config update!
	**/
	
	//TODO Trades History
	
	
	private static Main instance;
	public FilesManager filesManager;
	public TradesQueue tradesQueue;
	public UsersManager usersManager;
	public Economy vaultEconomy;
	
	// Number of build
	public final int build = 4;
	
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
		
		saveDefaultConfig();
		
		send.sendMessage("§a> Loading listeners...");
		this.registerListeners();
		send.sendMessage("§a> Loading commands...");
		this.registerCommands();
	
		// Setup Vault as in the example on the GitHub page
		if(Settings.USE_VAULT) {
			if(!setupVaultEconomy()) {
				send.sendMessage("§cVault dependency not found, plugin disabled");
				getServer().getPluginManager().disablePlugin(this);
				return;
			} else
				send.sendMessage("§a> Vault hooked");
		}
		
		send.sendMessage("§aTradeSystem has been enabled B" + this.build);
	}
	
	public void onDisable() {
		for(Player p : Bukkit.getOnlinePlayers())
			try {
				debug("Saving account profile for '" + p.getName() + "'");
				usersManager.getUser(p.getName()).end();
			} catch (IOException e) {
				debug("Could not end profile for '" + p.getName() + "'");
			}

		send.sendMessage("§eTradeSystem has been disabled");
	}
	
	public void debug(String msg) {
		if(Settings.DEBUG)
			System.out.println("[TradeSystem - Debug] " + msg);
	}
	
	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new RightClickListener(), this);
		// Not used anymore
		// getServer().getPluginManager().registerEvents(new JoinListener(), this);
		getServer().getPluginManager().registerEvents(new LeftListener(), this);
		getServer().getPluginManager().registerEvents(new KickListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
		getServer().getPluginManager().registerEvents(new SoundsListener(), this);
	}
	
	private void registerCommands() {
		getCommand("tradeaccept").setExecutor(new CTradeAccept());
		getCommand("trade").setExecutor(new CTrade());
		getCommand("tradedeny").setExecutor(new CTradeDeny());
		getCommand("tradesystem").setExecutor(new CTradeSystem());
		getCommand("toggletrades").setExecutor(new CToggleTrades());
		getCommand("tblacklist").setExecutor(new CTradesBlacklist());
	}
	
    private boolean setupVaultEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        
        if (rsp == null)
            return false;
        
        vaultEconomy = rsp.getProvider();
        
        return vaultEconomy != null;
    }
    
	public static Main getInstance() {
		return instance;
	}
}
