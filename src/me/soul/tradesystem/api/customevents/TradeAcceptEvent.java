package me.soul.tradesystem.api.customevents;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.soul.tradesystem.trades.Trade;

public class TradeAcceptEvent extends Event implements Cancellable {

	private boolean isCancelled;
	private static final HandlerList handlers = new HandlerList();
	public Trade trade;
	
	public TradeAcceptEvent(Trade trade) {
		this.trade = trade;
	}
	
	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		this.isCancelled = arg0;
	}
	
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}

}
