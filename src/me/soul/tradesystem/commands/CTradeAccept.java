package me.soul.tradesystem.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.trades.Trade;
import me.soul.tradesystem.trades.enums.TradeType;
import me.soul.tradesystem.users.User;
import me.soul.tradesystem.utils.Messages;
import me.soul.tradesystem.utils.Settings;

public class CTradeAccept implements CommandExecutor {

	// Trade accept
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(lbl.equalsIgnoreCase("tradeaccept")) {
			if(args.length < 1) {
				sender.sendMessage(Messages.convert("trade_accept_command.syntax", true));
				return false;
			}
			
			if(!(sender instanceof Player))
				return false;
			
			Player player = (Player)sender;
			User user = Main.getInstance().usersManager.getUser(player.getName());
			
			if(!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
				sender.sendMessage(Messages.convert("trade_accept_command.invalid_player", true).replace("%name%", args[0]));
				return false;
			}
			
			Player from = Bukkit.getPlayer(args[0]);
			
			if(!Settings.CREATIVE_REQUEST) {
				if((from.getOpenInventory() != null && from.getOpenInventory().getType().equals(InventoryType.CREATIVE)) || (player.getOpenInventory() != null && player.getOpenInventory().getType().equals(InventoryType.CREATIVE))) {
						sender.sendMessage(Messages.convert("trade_accept_command.creative_trade", true));
						return false;
				}
			}
			
			if(!user.hasRequestFrom(from.getName())) {
				sender.sendMessage(Messages.convert("trade_accept_command.not_requested", true).replace("%name%", args[0]));
				return false;
			}
			
			Trade t = user.getTrade(TradeType.IN, from.getName());
			
			if(t.getSender().getPlayer().getOpenInventory() != null && !t.getSender().getPlayer().getOpenInventory().getType().equals(InventoryType.CRAFTING) && !t.getSender().getPlayer().getOpenInventory().getType().equals(InventoryType.CREATIVE)) {
				sender.sendMessage(Messages.convert("trade_accept_command.inventory_open", true).replace("%name%", args[0]));
				return false;
			}
			
			// Start the trading actions
			t.startTrading();
		}
		return false;
	}

}
