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

public class CTrade implements CommandExecutor {

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
			
			if(!isReallyValid(args[0]) || args[0].equalsIgnoreCase(sender.getName())) {
				sender.sendMessage(Messages.convert("trade_command.invalid_player", true).replace("%name%", args[0]));
				return false;
			}
			
			Player in = Bukkit.getPlayer(args[0]);
			
			if(user.canSendRequestTo(args[0]))
				user.initializeTrade(in);
		}
		return false;
	}
	
	// To avoid a bug: Many users with similiar username caused a nullpointer
	private boolean isReallyValid(String name) {
		for(Player p : Bukkit.getOnlinePlayers())
			if(p.getName().equals(name))
				return true;
		return false;
	}

}
