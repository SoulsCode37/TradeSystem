package me.soul.tradesystem.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.trades.Trade;
import me.soul.tradesystem.trades.enums.TradeStatus;
import me.soul.tradesystem.users.User;
import me.soul.tradesystem.utils.Messages;

public class InventoryCloseListener implements Listener {

	// Handle inventory close while trading
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		
		if(!event.getView().getTitle().equals(Messages.convert("trade_inventory.title", false)))
			return;
		
		User user = Main.getInstance().usersManager.getUser(event.getPlayer().getName());
		Trade trade = user.getCurrentTrade();
		
		if(trade != null && trade.getStatus().equals(TradeStatus.ACCEPTED))
			trade.cancelTrading(event.getPlayer().getName());
	}

}
