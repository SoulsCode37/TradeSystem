package me.soul.tradesystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.trades.Trade;
import me.soul.tradesystem.trades.enums.TradeType;
import me.soul.tradesystem.users.User;
import me.soul.tradesystem.utils.Messages;

public class CTradeDeny implements CommandExecutor {

	// Trade deny
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(lbl.equalsIgnoreCase("tradedeny")) {
			if(args.length < 1) {
				sender.sendMessage(Messages.convert("trade_deny_command.syntax", true));
				return false;
			}
			
			if(!(sender instanceof Player))
				return false;
			
			Player player = (Player)sender;
			User user = Main.getInstance().usersManager.getUser(player.getName());
			
			if(user.hasRequestFrom(args[0])) {
				Trade trade = user.getTrade(TradeType.IN, args[0]);
				
				// Deny trading actions
				trade.denyTrading();
			} else
				sender.sendMessage(Messages.convert("trade_deny_command.not_requested", true).replace("%name%", args[0]));
		}
		return false;
	}

}
