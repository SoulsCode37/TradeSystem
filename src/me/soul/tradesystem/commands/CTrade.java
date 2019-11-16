package me.soul.tradesystem.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.trades.Trade;
import me.soul.tradesystem.trades.TradesCooldowns;
import me.soul.tradesystem.users.User;
import me.soul.tradesystem.utils.Messages;
import me.soul.tradesystem.utils.Permissions;
import me.soul.tradesystem.utils.Settings;

public class CTrade implements CommandExecutor {

	//TODO Cannot trade urself
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(lbl.equalsIgnoreCase("trade")) {
			if(args.length < 1) {
				sender.sendMessage(Messages.convert("trade_command.syntax", true));
				return false;
			}
			
			if(!(sender instanceof Player))
				return false;
			
			Player player = (Player)sender;
			
			if(!player.hasPermission(Permissions.TRADE)) {
				sender.sendMessage(Messages.convert("no_permission", true));
				return false;
			}
			
			User user = Main.getInstance().usersManager.getUser(player.getName());
			
			if(!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0])) || args[0].equalsIgnoreCase(sender.getName())) {
				sender.sendMessage(Messages.convert("trade_command.invalid_player", true).replace("%name%", args[0]));
				return false;
			}
			
			Player in = Bukkit.getPlayer(args[0]);
			
			if(!Settings.WORLDS_TRADES && !player.getWorld().getName().equals(in.getWorld().getName())) {
				sender.sendMessage(Messages.convert("trade_command.invalid_world", true).replace("%name%", args[0]));
				return false;
			}
			
			if(user.canSendRequestTo(args[0])) {
				User inUser = Main.getInstance().usersManager.getUser(args[0]);
				
				if(!inUser.hasTrades()) {
					player.sendMessage(Messages.convert("trade_command.trades_off", true).replace("%name%", args[0]));
					return false;
				}
				
				if(inUser.getBlacklist().contains(player.getName())) {
					player.sendMessage(Messages.convert("trade_request_denied.sender", true).replace("%to%", args[0]));
					return false;
				}
				
				if(Main.isPremium && Settings.COOLDOWN_PLAYER) {
					if(!TradesCooldowns.isOnCooldown(player.getName(), args[0])) {
						// Send a request which is stored in the TradesQueue class
						new Trade(player, in).sendRequest();
						TradesCooldowns.cooldown(player.getName(), args[0]);
					} else 
						player.sendMessage(Messages.convert("premium.on_cooldown", true).replace("%name%", args[0]));
				} else {
					// Send a request which is stored in the TradesQueue class
					new Trade(player, in).sendRequest();
				}
			} else
				player.sendMessage(Messages.convert("wait_expire_time", true).replace("%to%", args[0]));
		}
		return false;
	}

}
