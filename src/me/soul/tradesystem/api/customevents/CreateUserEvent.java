package me.soul.tradesystem.api.customevents;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CreateUserEvent extends Event implements Cancellable {

	private boolean isCancelled;
	private static final HandlerList handlers = new HandlerList();
	public String name;
	
	public CreateUserEvent(String name) {
		this.name = name;
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
