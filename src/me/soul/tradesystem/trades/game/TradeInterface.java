package me.soul.tradesystem.trades.game;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.soul.tradesystem.trades.Trade;
import me.soul.tradesystem.users.User;
import me.soul.tradesystem.utils.Messages;
import me.soul.tradesystem.utils.Settings;

public class TradeInterface {

	private Inventory inv;
	private Trade trade;
	private boolean senderLocked;
	private boolean receiverLocked;
	public int[] senderSlots = { 0, 1, 2, 3,
            9, 10, 11, 12,
            18, 19, 20, 21,
            27, 28, 29, 30
          };
	
	public int[] receiverSlots = { 5, 6, 7, 8,
              14, 15, 16, 17,
              23, 24, 25, 26,
              32, 33, 34, 35
            };
	private MoneyTradeInterface senderMoneyInterface;
	private MoneyTradeInterface receiverMoneyInterface;
	
	public TradeInterface(Trade trade) {
		this.trade = trade;
		this.senderMoneyInterface = new MoneyTradeInterface(this, getTrade().getSender());
		this.receiverMoneyInterface = new MoneyTradeInterface(this, getTrade().getReceiver());
	}

	public void createInventory() {
		this.inv = Bukkit.createInventory(null, 54, Messages.convert("trade_inventory.title", false));
		
		ItemStack panel = new ItemStack(Material.STAINED_GLASS_PANE);
		ItemMeta panelMeta = panel.getItemMeta();
		panelMeta.setLore(Arrays.asList("§r"));
	
		
		panel.setDurability((short)9);
		
		// Strange things to do a cool graphic ui
		for(int i = 45 -9; i < 45; i++) {
			if(i > 40)
				panelMeta.setDisplayName("§f" + getTrade().getReceiver().getPlayer().getName());
			else if ( i < 40)
				panelMeta.setDisplayName("§f" + getTrade().getSender().getPlayer().getName());
			panel.setItemMeta(panelMeta);
			this.inv.setItem(i, panel);
		}
		
		panelMeta.setDisplayName("§r");
		panel.setItemMeta(panelMeta);
		panel.setDurability((short)7);
		for(int j = 4; j < 50; j += 9)
			this.inv.setItem(j, panel);

		ItemStack accept = new ItemStack(Material.STAINED_CLAY);
		accept.setDurability((short)5);
		ItemMeta acceptMeta = accept.getItemMeta();
		acceptMeta.setDisplayName(Messages.convert("trade_inventory.accept_item.name", false));
		accept.setItemMeta(acceptMeta);
		
		this.inv.setItem(45, accept);
		this.inv.setItem(52, accept);
		
		ItemStack cancel = new ItemStack(Material.STAINED_CLAY);
		cancel.setDurability((short)14);
		ItemMeta cancelMeta = cancel.getItemMeta();
		cancelMeta.setDisplayName(Messages.convert("trade_inventory.cancel_item.name", false));
		cancel.setItemMeta(cancelMeta);
		
		this.inv.setItem(46, cancel);
		this.inv.setItem(53, cancel);
		
		if(Settings.MONEY_TRADE) {
			// New money item
			ItemStack money = new ItemStack(Material.GOLD_INGOT);
			ItemMeta moneyMeta = money.getItemMeta();

			moneyMeta.setDisplayName(Messages.convert("trade_inventory.money_item.name", false)
					.replace("%name%", getTrade().getSender().getPlayer().getName()).replace("%money%", 0.0 + ""));
			money.setItemMeta(moneyMeta);

			this.inv.setItem(47, money);

			moneyMeta.setDisplayName(Messages.convert("trade_inventory.money_item.name", false)
					.replace("%name%", getTrade().getReceiver().getPlayer().getName()).replace("%money%", 0.0 + ""));
			money.setItemMeta(moneyMeta);

			this.inv.setItem(51, money);
		}
		
		getTrade().getReceiver().getPlayer().openInventory(inv);
		getTrade().getSender().getPlayer().openInventory(inv);
	}
	
	// Update and open inventory
	public void openInterface(User user) {
		ItemStack money = new ItemStack(Material.GOLD_INGOT);
		ItemMeta moneyMeta = money.getItemMeta();
		
		moneyMeta.setDisplayName(Messages.convert("trade_inventory.money_item.name", false).replace("%name%", getTrade().getSender().getPlayer().getName()).replace("%money%", getSenderMoneyInterface().getMoney() + ""));
		money.setItemMeta(moneyMeta);
		
		this.inv.setItem(47, money);
		
		moneyMeta.setDisplayName(Messages.convert("trade_inventory.money_item.name", false).replace("%name%", getTrade().getReceiver().getPlayer().getName()).replace("%money%", getReceiverMoneyInterface().getMoney() + ""));
		money.setItemMeta(moneyMeta);
		
		this.inv.setItem(51, money);
		user.getPlayer().openInventory(this.inv);
	}
	
	// Lock sender's items
	public void lockSender() {
		this.setSenderLocked(true);
		ItemStack lock = new ItemStack(Material.BARRIER);
		ItemMeta meta = lock.getItemMeta();
		meta.setDisplayName(Messages.convert("trade_inventory.lock_item.name", false));
		lock.setItemMeta(meta);
		this.inv.setItem(48, lock);
	}
	
	// Lock receiver's items
	public void lockReceiver() {
		this.setReceiverLocked(true);
		ItemStack lock = new ItemStack(Material.BARRIER);
		ItemMeta meta = lock.getItemMeta();
		meta.setDisplayName(Messages.convert("trade_inventory.lock_item.name", false));
		lock.setItemMeta(meta);
		this.inv.setItem(50, lock);
	}
	
	public void unlockSender() {
		this.setSenderLocked(false);
		this.inv.setItem(48, null);
	}
	
	public void unlockReceiver() {
		this.setReceiverLocked(false);
		this.inv.setItem(50, null);
	}
	
	public void startUnlockingSender() {
		ItemStack lock = new ItemStack(Material.STAINED_CLAY);
		lock.setDurability((short)4);
		ItemMeta meta = lock.getItemMeta();
		meta.setDisplayName(Messages.convert("trade_inventory.unlocking_item.name", false));
		lock.setItemMeta(meta);
		this.inv.setItem(48, lock);
	}
	
	public void startUnlockingReceiver() {
		ItemStack lock = new ItemStack(Material.STAINED_CLAY);
		lock.setDurability((short)4);
		ItemMeta meta = lock.getItemMeta();
		meta.setDisplayName(Messages.convert("trade_inventory.unlocking_item.name", false));
		lock.setItemMeta(meta);
		this.inv.setItem(50, lock);
	}
	
	public Inventory getInv() {
		return inv;
	}

	public Trade getTrade() {
		return trade;
	}

	public boolean isSenderLocked() {
		return senderLocked;
	}

	public void setSenderLocked(boolean senderLocked) {
		this.senderLocked = senderLocked;
	}

	public boolean isReceiverLocked() {
		return receiverLocked;
	}

	public void setReceiverLocked(boolean receiverLocked) {
		this.receiverLocked = receiverLocked;
	}

	public void setReceiverMoneyInterface(MoneyTradeInterface r) {
		this.receiverMoneyInterface = r;
	}
	
	public MoneyTradeInterface getReceiverMoneyInterface() {
		return receiverMoneyInterface;
	}
	
	public void setSenderMoneyInterface(MoneyTradeInterface s) {
		this.senderMoneyInterface = s;
	}
	
	public MoneyTradeInterface getSenderMoneyInterface() {
		return senderMoneyInterface;
	}
}
