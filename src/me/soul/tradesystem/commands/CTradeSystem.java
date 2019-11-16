package me.soul.tradesystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.soul.tradesystem.Main;

public class CTradeSystem implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		// Ignore premium thing
		if(lbl.equalsIgnoreCase("tradesystem")) {
			if(Main.isPremium)
				sender.sendMessage("§bSoulsCode's §9§lTradeSystem §bB1");
			else
				sender.sendMessage("§bSoulsCode's §9§lTradeSystem §bB1");
		}
		return false;
	}

}
