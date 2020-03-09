package me.soul.tradesystem.users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.trades.Trade;
import me.soul.tradesystem.trades.TradesCooldowns;
import me.soul.tradesystem.trades.enums.TradeStatus;
import me.soul.tradesystem.trades.enums.TradeType;
import me.soul.tradesystem.utils.Messages;
import me.soul.tradesystem.utils.Settings;

public class User {
	
	private Player player;
	private List<Trade> tradesIn = new ArrayList<>();
	private List<Trade> tradesOut = new ArrayList<>();
	private List<ItemStack> tradingItems = new ArrayList<>();
	private List<String> blacklist = new ArrayList<>();
	private boolean trades;
	
	public User(String in) {
		this.player = Bukkit.getPlayer(in);
	}

	// Retrive user settings
	public void setup() {
		this.trades = Main.getInstance().filesManager.getUsers().hasTrades(getPlayer().getName());
		this.blacklist = Main.getInstance().filesManager.getUsers().getBlackList(getPlayer().getName());
	}
	
	// Save user settings to files
	public void end() throws IOException {
		// This format of for is used to prevent concurrent modification exception
		for(int i = 0; i < getTradesOut().size(); i++)
			getTradesOut().get(i).expireTrade();
		for(int i = 0; i < getTradesIn().size(); i++)
			getTradesIn().get(i).expireTrade();
		
		Main.getInstance().filesManager.getUsers().setTrades(getPlayer().getName(), this.trades);
		Main.getInstance().filesManager.getUsers().setBlacklist(getPlayer().getName(), this.blacklist);
	}
	
	// Initialize a new trade
	public void initializeTrade(Player in) {
		if(Settings.COOLDOWN_PLAYER) {
			if(!TradesCooldowns.isOnCooldown(getPlayer().getName(), in.getName())) {
				// Send a request which is stored in TradesQueue.class
				new Trade(getPlayer(), in).sendRequest();
				TradesCooldowns.cooldown(getPlayer().getName(), in.getName());
			} else
				getPlayer().sendMessage(Messages.convert("on_cooldown", true).replace("%name%", in.getName()));
		} else {
			// Send a request which is stored in TradesQueue.class
			new Trade(getPlayer(), in).sendRequest();
		}
	}
	
	// Check all the requirements for a trade and send messages if them are not respected
	public boolean canSendRequestTo(String to) {
		User inUser = Main.getInstance().usersManager.getUser(to);
		
		if(Settings.BLACKLISTED_WORLDS.contains(getPlayer().getWorld().getName()) || Settings.BLACKLISTED_WORLDS.contains(inUser.getPlayer().getWorld().getName()) ) {
			getPlayer().sendMessage(Messages.convert("trade_command.invalid_world", true).replace("%name%", to));
			return false;
		}
		
		if(!Settings.WORLDS_TRADES && !player.getWorld().getName().equals(inUser.getPlayer().getWorld().getName())) {
			getPlayer().sendMessage(Messages.convert("trade_command.different_worlds", true).replace("%name%", to));
			return false;
		}
		
		if(!Settings.CREATIVE_REQUEST && getPlayer().getOpenInventory() != null && getPlayer().getOpenInventory().getType().equals(InventoryType.CREATIVE)) {
			getPlayer().sendMessage(Messages.convert("trade_accept_command.creative_trade", true));
			return false;
		}
		
		if(!inUser.hasTrades()) {
			getPlayer().sendMessage(Messages.convert("trade_command.trades_off", true).replace("%name%", to));
			return false;
		}
		
		// Added check for incoming trades
		if(inUser.getBlacklist().contains(getPlayer().getUniqueId().toString()) && getTrade(TradeType.IN, to) == null) {
			getPlayer().sendMessage(Messages.convert("trade_request_denied.sender", true).replace("%to%", to));
			return false;
		}
	
		for(Trade t : getTradesOut()) {
			if(t.getReceiver().getPlayer().getName().equals(to) && t.getStatus().equals(TradeStatus.SENT)) {
				getPlayer().sendMessage(Messages.convert("wait_expire_time", true).replace("%to%", to));
				return false;
			}
		}
		return true;
	}
	
	public boolean hasRequestFrom(String from) {
		for(Trade t : getTradesIn())
			if(t.getSender().getPlayer().getName().equals(from))
				return true;
		return false;
	}
	
	// Return trade
	public Trade getTrade(TradeType type, String who) {
		switch(type) {
		case IN:
			for(Trade t : getTradesIn())
				if(t.getSender().getPlayer().getName().equals(who)) 
					return t;
			Main.getInstance().debug("Broke with null TYPE IN ");
			return null;
		case OUT:
			for(Trade t : getTradesOut())
				if(t.getReceiver().getPlayer().getName().equals(who))
					return t;
			Main.getInstance().debug("Broke with null TYPE OUT ");
			return null;
		default:
			Main.getInstance().debug("Broke with null TYPE DEF ");
			return null;
		}
	}
	
	public Trade getCurrentTrade() {
		for(Trade t : getTradesIn())
			if(t.getStatus().equals(TradeStatus.ACCEPTED))
				return t;
		for(Trade t : getTradesOut())
			if(t.getStatus().equals(TradeStatus.ACCEPTED))
				return t;
		return null;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(this.player.getName());
	}

	public List<Trade> getTradesIn() {
		return tradesIn;
	}

	public List<Trade> getTradesOut() {
		return tradesOut;
	}

	public List<ItemStack> getTradingItems() {
		return tradingItems;
	}

	public List<String> getBlacklist() {
		return blacklist;
	}

	public void setTrades(boolean trade) {
		this.trades = trade;
	}
	
	public boolean hasTrades() {
		return trades;
	}
}
