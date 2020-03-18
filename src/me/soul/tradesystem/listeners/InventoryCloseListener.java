package me.soul.tradesystem.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.trades.Trade;
import me.soul.tradesystem.trades.enums.TradeStatus;
import me.soul.tradesystem.users.User;
import me.soul.tradesystem.utils.Messages;

public class InventoryCloseListener implements Listener {

	// Handle inventory close while trading
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if(!event.getView().getTitle().equals(Messages.convert("trade_inventory.title", false)) && !event.getView().getTitle().equals(Messages.convert("money_trade_inventory.title", false)))
			return;
		
		User user = Main.getInstance().usersManager.getUser(event.getPlayer().getName());
		
		if(user.getSpectatedTrade() != null) {
			user.stopSpectating();
			return;
		}
		
		Trade trade = user.getCurrentTrade();
		
		if(trade != null && trade.getStatus().equals(TradeStatus.ACCEPTED)) {
			// Added a delay to check if the player was adding money
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@SuppressWarnings("unused")
				@Override
				public void run() {
					if(user == null) {
						trade.cancelTrading(event.getPlayer().getName());
						return;
					}
					
					if(!isAddingMoney(user.getPlayer()))
						trade.cancelTrading(event.getPlayer().getName());
				}
			}, 1);
		}
	}
	
	// I'm not sure how this will be with other GUI plugins
	private boolean isAddingMoney(Player who) {
		return (who.getOpenInventory() != null && who.getOpenInventory().getType().equals(InventoryType.CHEST) && (who.getOpenInventory().getTitle().equals(Messages.convert("trade_inventory.title", false)) || who.getOpenInventory().getTitle().equals(Messages.convert("money_trade_inventory.title", false))));
	}
}
