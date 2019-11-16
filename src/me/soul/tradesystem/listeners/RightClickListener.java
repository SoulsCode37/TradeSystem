package me.soul.tradesystem.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.trades.Trade;
import me.soul.tradesystem.trades.TradesCooldowns;
import me.soul.tradesystem.trades.enums.TradeType;
import me.soul.tradesystem.users.User;
import me.soul.tradesystem.utils.Messages;
import me.soul.tradesystem.utils.Permissions;
import me.soul.tradesystem.utils.Settings;

public class RightClickListener implements Listener {
	
	// Silent cooldown feature for 1.14
	private List<String> silentCooldown = new ArrayList<>();
	
	// Used to handle right click trade requests
	@EventHandler
	public void onRightClick(PlayerInteractAtEntityEvent event) {
		if(!Settings.RIGHT_CLICK)
			return;
		
		Player player = event.getPlayer();
		
		if(!player.hasPermission(Permissions.RIGHT_CLICK_TRADE) || isCooldown(player.getName()))
			return;
		
		if(!Settings.CREATIVE_REQUEST && player.getOpenInventory() != null && player.getOpenInventory().getType().equals(InventoryType.CREATIVE))
			return;
		
		Entity right = event.getRightClicked();

		if(right != null && (right instanceof Player)) {
			Player clicked = (Player)right;

			if(!player.getItemInHand().getType().equals(Material.AIR))
				return;

			User out = Main.getInstance().usersManager.getUser(player.getName());	
			
			silentCooldown(player.getName());
			
			if(out.canSendRequestTo(clicked.getName()) && !out.hasRequestFrom(clicked.getName())) {
				User inUser = Main.getInstance().usersManager.getUser(clicked.getName());
				
				if(!inUser.hasTrades()) {
					player.sendMessage(Messages.convert("trade_command.trades_off", true).replace("%name%", clicked.getName()));
					return;
				}
				
				if(inUser.getBlacklist().contains(player.getName())) {
					player.sendMessage(Messages.convert("trade_request_denied.sender", true).replace("%to%", clicked.getName()));
					return;
				}
				
				if(Settings.COOLDOWN_PLAYER) {
					if(!TradesCooldowns.isOnCooldown(player.getName(), clicked.getName())) {
						// Send a request which is stored in the TradesQueue class
						new Trade(player, clicked).sendRequest();
						TradesCooldowns.cooldown(player.getName(), clicked.getName());
					} else
						player.sendMessage(Messages.convert("premium.on_cooldown", true).replace("%name%", clicked.getName()));
				} else {
					// Send a request which is stored in the TradesQueue class
					new Trade(player, clicked).sendRequest();
				}
			} else if(out.hasRequestFrom(clicked.getName())) {
				Trade trade = out.getTrade(TradeType.IN, clicked.getName());
				trade.startTrading();
			} else if(!out.canSendRequestTo(clicked.getName()))
				player.sendMessage(Messages.convert("wait_expire_time", true).replace("%to%", clicked.getName()));
			
		}
	}
	
	// For 1.14 right-click bugs
	private void silentCooldown(String name) {
		silentCooldown.add(name);
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				silentCooldown.remove(name);
			}
		}, 1);
	}
	
	private boolean isCooldown(String name) {
		return silentCooldown.contains(name);
	}
}
