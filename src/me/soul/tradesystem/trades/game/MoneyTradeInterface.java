package me.soul.tradesystem.trades.game;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.users.User;
import me.soul.tradesystem.utils.Messages;

public class MoneyTradeInterface {
	
	private Inventory inv;
	private TradeInterface tradeInterface;
	private User who;
	private int money;
	
	public MoneyTradeInterface(TradeInterface tradeInterface, User who) {
		this.tradeInterface = tradeInterface;
		this.who = who;
		this.money = 0;
	}

	public void openInterface() {
		this.inv = Bukkit.createInventory(null, 27,  Messages.convert("money_trade_inventory.title", false));
		
		// Add money item
		ItemStack add = new ItemStack(Material.STAINED_CLAY);
		add.setDurability((short)5);
		ItemMeta addMeta = add.getItemMeta();
		
		addMeta.setDisplayName("§a+100000");
		add.setItemMeta(addMeta);
		this.inv.setItem(0, add);
		
		addMeta.setDisplayName("§a+10000");
		add.setItemMeta(addMeta);
		this.inv.setItem(1, add);
		
		addMeta.setDisplayName("§a+1000");
		add.setItemMeta(addMeta);
		this.inv.setItem(2, add);
		
		addMeta.setDisplayName("§a+100");
		add.setItemMeta(addMeta);
		this.inv.setItem(9, add);
		
		addMeta.setDisplayName("§a+10");
		add.setItemMeta(addMeta);
		this.inv.setItem(10, add);
		
		addMeta.setDisplayName("§a+1");
		add.setItemMeta(addMeta);
		this.inv.setItem(11, add);
		
		// Remove money item
		ItemStack remove = new ItemStack(Material.STAINED_CLAY);
		remove.setDurability((short)14);
		ItemMeta removeMeta = remove.getItemMeta();
		
		removeMeta.setDisplayName("§c-100000");
		remove.setItemMeta(removeMeta);
		this.inv.setItem(6, remove);
		
		removeMeta.setDisplayName("§c-10000");
		remove.setItemMeta(removeMeta);
		this.inv.setItem(7, remove);

		removeMeta.setDisplayName("§c-1000");
		remove.setItemMeta(removeMeta);
		this.inv.setItem(8, remove);
		
		removeMeta.setDisplayName("§c-100");
		remove.setItemMeta(removeMeta);
		this.inv.setItem(15, remove);
		
		removeMeta.setDisplayName("§c-10");
		remove.setItemMeta(removeMeta);
		this.inv.setItem(16, remove);
		
		removeMeta.setDisplayName("§c-1");
		remove.setItemMeta(removeMeta);
		this.inv.setItem(17, remove);
		
		// Confirm item
		ItemStack confirm = new ItemStack(Material.STAINED_CLAY);
		confirm.setDurability((short)4);
		ItemMeta confirmMeta = confirm.getItemMeta();
		confirmMeta.setDisplayName(Messages.convert("money_trade_inventory.confirm_item.name", false).replace("%money%", getMoney() + ""));
		confirm.setItemMeta(confirmMeta);
		
		this.inv.setItem(4, confirm);
		
		// Cancel item
		ItemStack cancel = new ItemStack(Material.BARRIER);
		ItemMeta cancelMeta = cancel.getItemMeta();
		cancelMeta.setDisplayName(Messages.convert("money_trade_inventory.cancel_item.name", false));
		cancel.setItemMeta(cancelMeta);
		
		this.inv.setItem(22, cancel);
		
		getWho().getPlayer().openInventory(this.inv);
	}
	
	@SuppressWarnings("deprecation")
	public boolean hasEnoughMoney() {
		return Main.getInstance().vaultEconomy.getBalance(who.getPlayer().getName()) >= getMoney();
	}
	
	public boolean canRemoveMoney(int amount) {
		return getMoney() - amount >= 0;
	}
	
	public Inventory getInv() {
		return inv;
	}

	public TradeInterface getTradeInterface() {
		return tradeInterface;
	}

	public User getWho() {
		return who;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
}
