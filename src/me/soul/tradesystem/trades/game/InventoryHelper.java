package me.soul.tradesystem.trades.game;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.soul.tradesystem.trades.Trade;
import me.soul.tradesystem.trades.TradeItem;

public class InventoryHelper {
	
	public static boolean isInSlots(int[] slots, int slot) {
		for(int i = 0; i < slots.length; i++)
			if(slots[i] == slot)
				return true;
		return false;
	}
	
	// Check if both (sender's and receiver's) inventories are empty
	public static boolean isTradeInventoryEmpty(TradeInterface tradeInt) {
		// Receiver inventory
		for(int i : tradeInt.receiverSlots)
			if(tradeInt.getInv().getItem(i) != null && !tradeInt.getInv().getItem(i).getType().equals(Material.AIR))
				return false;
		// Sender inventory
		for(int i : tradeInt.senderSlots)
			if(tradeInt.getInv().getItem(i) != null && !tradeInt.getInv().getItem(i).getType().equals(Material.AIR))
				return false;
		return true;
	}
	
	// Try to find next good slot
	public static TradeItem getNextGoodSlot(Inventory inv, int[] slots, ItemStack item) {
		for(int i = 0; i < slots.length; i++) {
			if((isSimiliar(inv.getContents()[slots[i]], item) && inv.getContents()[slots[i]].getMaxStackSize() >= (item.getAmount() + inv.getContents()[slots[i]].getAmount())))
				return new TradeItem(slots[i], true);
			else if(inv.getContents()[slots[i]] == null)
				return new TradeItem(slots[i], false);
		}
		return null;
	}
	
	public static boolean isSimiliar(ItemStack n1, ItemStack n2) {
		return n1 != null && n2 != null && n1.isSimilar(n2);
	}
	
	// Return items in case of trade cancel
	public static void returnItems(Trade trade) {
		Player sender = trade.getSender().getPlayer();
		Player receiver = trade.getReceiver().getPlayer();
		
		for(int i = 0; i < trade.getTradeInterface().senderSlots.length; i++) {
			ItemStack item = trade.getTradeInterface().getInv().getContents()[trade.getTradeInterface().senderSlots[i]];
			if(item != null && !item.getType().equals(Material.AIR))
				sender.getInventory().addItem(item);
		}
		
		for(int i = 0; i < trade.getTradeInterface().receiverSlots.length; i++) {
			ItemStack item = trade.getTradeInterface().getInv().getContents()[trade.getTradeInterface().receiverSlots[i]];
			if(item != null && !item.getType().equals(Material.AIR))
				receiver.getInventory().addItem(item);
		}
	}
	
	// Used to transfer items
	public static void executeTrade(Trade trade) {
		Player sender = trade.getSender().getPlayer();
		Player receiver = trade.getReceiver().getPlayer();
		
		for(int i = 0; i < trade.getTradeInterface().senderSlots.length; i++) {
			ItemStack item = trade.getTradeInterface().getInv().getContents()[trade.getTradeInterface().senderSlots[i]];
			if(item != null && !item.getType().equals(Material.AIR))
				receiver.getInventory().addItem(item);
		}
		
		for(int i = 0; i < trade.getTradeInterface().receiverSlots.length; i++) {
			ItemStack item = trade.getTradeInterface().getInv().getContents()[trade.getTradeInterface().receiverSlots[i]];
			if(item != null && !item.getType().equals(Material.AIR))
				sender.getInventory().addItem(item);
		}
	}
	
	// Check if a player has enough space in his inventory
	public static boolean hasEnoughSpace(Player player, int number) {
		if(getNumberOfItemInInv(player) + number > player.getInventory().getSize())
			return false;
		return true;
	}
	
	// Get number of current items in a player inventory
	public static int getNumberOfItemInInv(Player player) {
		int n = 0;
		for(int i = 0; i < player.getInventory().getContents().length; i++) {
			ItemStack item = player.getInventory().getContents()[i];
			if(item != null && !item.getType().equals(Material.AIR))
				n++;
		}
		return n;
	}
	
	public static int getNumberOfItems(Inventory inv, int[] slots) {
		int n = 0;
		for(int i = 0; i < slots.length; i++) {
			ItemStack item = inv.getContents()[slots[i]];
			if(item != null && !item.getType().equals(Material.AIR))
				n++;
		}
		return n;
	}
}
