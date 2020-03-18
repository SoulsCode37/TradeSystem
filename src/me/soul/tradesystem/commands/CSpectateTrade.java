package me.soul.tradesystem.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.users.User;
import me.soul.tradesystem.utils.Messages;
import me.soul.tradesystem.utils.Permissions;
public class CSpectateTrade implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(lbl.equalsIgnoreCase("spectatetrade")) {
			if(!(sender instanceof Player))
				return false;
			
			Player p = (Player)sender;
			
			if(!p.hasPermission(Permissions.SPECTATE_TRADE_COMMAND)) {
				sender.sendMessage(Messages.convert("no_permission", true));
				return false;
			}
			
			if(args.length < 1) {
				sender.sendMessage(Messages.convert("spectate_trade_command.syntax", true));
				return false;
			}
			
			if(!isReallyValid(args[0]) || args[0].equalsIgnoreCase(sender.getName())) {
				sender.sendMessage(Messages.convert("trade_command.invalid_player", true).replace("%name%", args[0]));
				return false;
			}
			
			User target = Main.getInstance().usersManager.getUser(args[0]);
			User pl = Main.getInstance().usersManager.getUser(p.getName());
			
			if(target.getCurrentTrade() == null) {
				sender.sendMessage(Messages.convert("spectate_trade_command.not_trading", true).replace("%name%", args[0]));
				return false;
			}
			
			pl.spectateTrade(target.getCurrentTrade());
			
		}
		return false;
	}
	
	private boolean isReallyValid(String name) {
		for(Player p : Bukkit.getOnlinePlayers())
			if(p.getName().equals(name))
				return true;
		return false;
	}
}
