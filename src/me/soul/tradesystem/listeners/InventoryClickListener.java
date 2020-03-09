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
import me.soul.tradesystem.trades.game.ItemFilter;
import me.soul.tradesystem.trades.game.MoneyTradeInterface;
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
		
		Trade trade = user.getCurrentTrade();
		
		if(trade == null)
			return;
		
		// Added check to fix: players could not trade stained clay and barriers
		if(event.getCurrentItem() == null || InventoryHelper.isInSlots(trade.getTradeInterface().senderSlots, event.getSlot()) || InventoryHelper.isInSlots(trade.getTradeInterface().receiverSlots, event.getSlot()))
			return;
		
		ItemStack item = event.getCurrentItem();

		
		boolean isSender = trade.isSender(user);
		
		switch(item.getType()) {
		case STAINED_CLAY:
			switch(item.getDurability()) {
			case 5:
				if(isSender)
					trade.getTradeInterface().lockSender();
				else
					trade.getTradeInterface().lockReceiver();
				
				if(trade.getTradeInterface().isSenderLocked() && trade.getTradeInterface().isReceiverLocked())
					trade.getTradeInterface().startAntiScamTimer();
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
		case GOLD_INGOT:
			// Create a new GUI 
			if(trade.isSender(user)) {
				if(!trade.getTradeInterface().isSenderLocked())
					trade.getTradeInterface().getSenderMoneyInterface().openInterface();
			} else if(!trade.getTradeInterface().isReceiverLocked())
				trade.getTradeInterface().getReceiverMoneyInterface().openInterface();
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
		
		boolean isSender = trade.isSender(user);
		
		if((isSender && trade.getTradeInterface().isSenderLocked()) || (!isSender && trade.getTradeInterface().isReceiverLocked()))
			return;
		
		if(event.getCurrentItem() == null)
			return;
		
		ItemStack item = event.getCurrentItem();
		
		if(user == null || trade == null || trade.getTradeInterface() == null)
			return;
		
		// Check if item can be traded
		ItemFilter filteredItem = new ItemFilter(item);
		
		try {
			if(!filteredItem.canBeTraded()) {
				user.getPlayer().sendMessage(Messages.convert("trade_inventory.invalid_item", true));
				return;
			}
		} catch (Exception e) {
			Main.getInstance().debug("Wrong Material name in filters.yml (Source: " + e.getMessage() + ")");
			return;
		}
		
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
	
	@EventHandler
	public void moneyInvManage(InventoryClickEvent event) {
		User user = Main.getInstance().usersManager.getUser(event.getWhoClicked().getName());
		
		if((event.getView() != null && !event.getView().getTitle().equals(Messages.convert("money_trade_inventory.title", false)))  || (event.getClickedInventory() != null && event.getClickedInventory().equals(user.getPlayer().getInventory())))
			return;
	
		event.setCancelled(true);
		
		Trade trade = user.getCurrentTrade();
		
		if(trade == null || event.getCurrentItem() == null)
			return;
		
		boolean isSender = user.getCurrentTrade().isSender(user);
		
		MoneyTradeInterface mi = isSender ? trade.getTradeInterface().getSenderMoneyInterface() : trade.getTradeInterface().getReceiverMoneyInterface();
		
		ItemStack item = event.getCurrentItem();
		
		switch(item.getType()) {
		case STAINED_CLAY:
			String name = item.getItemMeta().getDisplayName();
			switch(item.getDurability()) {
			// Confirm
			case 4:	
				if(Settings.USE_VAULT && !mi.hasEnoughMoney()) {
					user.getPlayer().sendMessage(Messages.convert("money_trade_inventory.vault.not_enough_money", true).replace("%money%", mi.getMoney() + ""));
					return;
				}
				
				trade.getTradeInterface().openInterface(user);
				user.getPlayer().sendMessage(Messages.convert("money_trade_inventory.money_added", true).replace("%money%", mi.getMoney() + ""));
				break;
			// Add
			case 5:
				String toAdd = name.replace("§a+", "");
				int add = Integer.parseInt(toAdd);
				mi.setMoney(mi.getMoney() + add);
				mi.openInterface();
				break;
			// Remove
			case 14:
				String toRemove = name.replace("§c-", "");
				int remove = Integer.parseInt(toRemove);
				
				if(mi.canRemoveMoney(remove)) {
					mi.setMoney(mi.getMoney() - remove);
					mi.openInterface();
				} else 
					user.getPlayer().sendMessage(Messages.convert("money_trade_inventory.cannot_remove_money", true));
				break;
			default:
				break;
			}
			break;
		// Cancel
		case BARRIER:
			mi.setMoney(0);
			trade.getTradeInterface().openInterface(user);
			user.getPlayer().sendMessage(Messages.convert("money_trade_inventory.money_not_added", true));
			break;
		default:
			break;
		}
		
	}
	
}
