package me.soul.tradesystem.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.trades.Trade;
import me.soul.tradesystem.trades.TradeItem;
import me.soul.tradesystem.trades.game.InventoryHelper;
import me.soul.tradesystem.users.User;
import me.soul.tradesystem.utils.Messages;
import me.soul.tradesystem.utils.Settings;

public class InventoryClickListener implements Listener {
	
	@EventHandler
	public void invManagement(InventoryClickEvent event) {
		User user = Main.getInstance().usersManager.getUser(event.getWhoClicked().getName());
		
		// Event getView() for Bugs-Free 1.14
		// Added check for null pointer in 1.15
		if((event.getView() != null && !event.getView().getTitle().equals(Messages.convert("trade_inventory.title", false)))  || (event.getClickedInventory() != null && event.getClickedInventory().equals(user.getPlayer().getInventory())))
			return;
		
		if(event.getCurrentItem() == null)
			return;
		
		ItemStack item = event.getCurrentItem();
		Trade trade = user.getCurrentTrade();
		
		boolean isSender = user == trade.getSender();
		
		switch(item.getType()) {
		case STAINED_CLAY:
			switch(item.getDurability()) {
			case 5:
				if(isSender)
					trade.getTradeInterface().lockSender();
				else
					trade.getTradeInterface().lockReceiver();
				
				if(trade.getTradeInterface().isSenderLocked() && trade.getTradeInterface().isReceiverLocked())
					trade.endTrading();
				break;
			case 14:
				trade.cancelTrading(event.getWhoClicked().getName());
				break;
			default:
				break;
			}
			break;
		case BARRIER:
			if((isSender && !trade.getTradeInterface().isSenderLocked()) || (!isSender && !trade.getTradeInterface().isReceiverLocked()))
				break;
			
			if(Settings.UNLOCK_WAIT) {
				if(isSender)
					trade.getTradeInterface().startUnlockingSender();
				else
					trade.getTradeInterface().startUnlockingReceiver();
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

					@Override
					public void run() {
						if (isSender)
							trade.getTradeInterface().unlockSender();
						else
							trade.getTradeInterface().unlockReceiver();
					}
				}, Settings.UNLOCK_WAIT_TIME * 20);
			} else {
				if(isSender)
					trade.getTradeInterface().unlockSender();
				else
					trade.getTradeInterface().unlockReceiver();
			}
			break;
		default:
			break;
		}
	}
	
	@EventHandler
	public void tradingItemsManagemnt(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		User user = Main.getInstance().usersManager.getUser(event.getWhoClicked().getName());
		
		if(!event.getView().getTitle().equals(Messages.convert("trade_inventory.title", false)))
			return;
	
		event.setCancelled(true);
		
		Trade trade = user.getCurrentTrade();
		
		// Bugs-Free
		if(trade == null)
			return;
		
		boolean isSender = user == trade.getSender();
		
		if((isSender && trade.getTradeInterface().isSenderLocked()) || (!isSender && trade.getTradeInterface().isReceiverLocked()))
			return;
		
		if(event.getCurrentItem() == null)
			return;
		
		ItemStack item = event.getCurrentItem();
		
		if(user == null || trade == null || trade.getTradeInterface() == null)
			return;
		
		int[] usedSlots = user == trade.getSender() ? trade.getTradeInterface().senderSlots : trade.getTradeInterface().receiverSlots;
		
		if (!event.getClickedInventory().equals(user.getPlayer().getInventory())) {
			if (!InventoryHelper.isInSlots(usedSlots, event.getSlot()))
				return;

			user.getPlayer().getInventory().addItem(item);
			user.getTradingItems().remove(item);
			event.setCurrentItem(null);
			return;
		}

		TradeItem tradeItem = InventoryHelper.getNextGoodSlot(inv, usedSlots, item);

		if (tradeItem != null) {
			if (tradeItem.canBeStacked)
				inv.getContents()[tradeItem.slot].setAmount(inv.getContents()[tradeItem.slot].getAmount() + item.getAmount());
			else
				inv.setItem(tradeItem.slot, item);
			user.getTradingItems().add(item);
			event.setCurrentItem(null);
		} else
			user.getPlayer().sendMessage(Messages.convert("trade_inventory.slots_full", true));
	}
}
