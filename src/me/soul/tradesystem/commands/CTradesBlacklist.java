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

public class CTradesBlacklist implements CommandExecutor {

	// Trade's blacklist things
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(lbl.equalsIgnoreCase("tblacklist")) {
			if(!(sender instanceof Player))
				return false;
			
			if(!sender.hasPermission(Permissions.BLACKLIST_COMMAND)) {
				sender.sendMessage(Messages.convert("no_permission", true));
				return false;
			}
			
			if(args.length < 2) {
				sender.sendMessage(Messages.convert("blacklist_command.syntax", true));
				return false;
			}
			
			User user = Main.getInstance().usersManager.getUser(sender.getName());
			String action = args[0];
			String target = args[1];
			String id = Bukkit.getPlayer(target).getUniqueId().toString();
			
			switch(action.toLowerCase()) {
			case "add":
				if(!user.getBlacklist().contains(id)) {
					user.getBlacklist().add(id);
					sender.sendMessage(Messages.convert("blacklist_command.blacklisted", true).replace("%name%", target));
				} else
					sender.sendMessage(Messages.convert("blacklist_command.already_blacklisted", true).replace("%name%", target));
				break;
			case "remove":
				if(user.getBlacklist().contains(id)) { 
					user.getBlacklist().remove(id);
					sender.sendMessage(Messages.convert("blacklist_command.unblacklisted", true).replace("%name%", target));
				} else
					sender.sendMessage(Messages.convert("blacklist_command.not_blacklisted", true).replace("%name%", target));
				break;
			}
		}
		return false;
	}
}
