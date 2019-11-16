package me.soul.tradesystem.users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.trades.Trade;
import me.soul.tradesystem.trades.enums.TradeStatus;
import me.soul.tradesystem.trades.enums.TradeType;

public class User {
	
	private Player in;
	private List<Trade> tradesIn = new ArrayList<>();
	private List<Trade> tradesOut = new ArrayList<>();
	private List<ItemStack> tradingItems = new ArrayList<>();
	private List<String> blacklist;
	private boolean trades;
	
	public User(String in) {
		this.in = Bukkit.getPlayer(in);
	}

	// Retrive user settings
	public void setup() {
		this.trades = Main.getInstance().filesManager.getUsers().hasTrades(in.getName());
		this.blacklist = Main.getInstance().filesManager.getUsers().getBlackList(in.getName());
	}
	
	// Save user settings to files
	public void end() throws IOException {
		// This format of for is used to prevent concurrent modification exception
		for(int i = 0; i < getTradesOut().size(); i++)
			getTradesOut().get(i).expireTrade();
		for(int i = 0; i < getTradesIn().size(); i++)
			getTradesIn().get(i).expireTrade();
		
		Main.getInstance().filesManager.getUsers().setTrades(in.getName(), this.trades);
		Main.getInstance().filesManager.getUsers().setBlacklist(in.getName(), this.blacklist);
	}
	
	public boolean canSendRequestTo(String to) {
		for(Trade t : getTradesOut())
			if(t.getReceiver().getPlayer().getName().equals(to) && t.getStatus().equals(TradeStatus.SENT))
				return false;
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
		return in;
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
