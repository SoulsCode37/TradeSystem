package me.soul.tradesystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.users.User;
import me.soul.tradesystem.utils.Messages;
import me.soul.tradesystem.utils.Permissions;

public class CToggleTrades implements CommandExecutor {

	// Toggle trades
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(lbl.equalsIgnoreCase("toggletrades")) {
			if(!(sender instanceof Player))
				return false;
			
			if(!sender.hasPermission(Permissions.TOGGLE_TRADES_COMMAND)) {
				sender.sendMessage(Messages.convert("no_permission", true));
				return false;
			}
			
			User user = Main.getInstance().usersManager.getUser(sender.getName());
			user.setTrades(!user.hasTrades());
			sender.sendMessage(Messages.convert("toggletrades_command.toggled", true).replace("%state%", user.hasTrades() ? "on" : "off"));
		}
		return false;
	}

}
