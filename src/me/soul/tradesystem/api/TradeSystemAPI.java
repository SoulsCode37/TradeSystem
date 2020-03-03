package me.soul.tradesystem.api;
import org.bukkit.entity.Player;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.files.impl.UsersFile;
import me.soul.tradesystem.trades.TradesQueue;
import me.soul.tradesystem.users.UsersManager;

public class TradeSystemAPI {

	public TradeSystemAPI() {
		Main.getInstance().debug("API Called");
	}
	
	public void cancelTrade(Player player, String whoCancelled) throws Exception {
		try {
			getUsersManager().getUser(player.getName()).getCurrentTrade().cancelTrading(whoCancelled);
		} catch(Exception e) {
			throw new Exception("Could not cancel trade");
		}
	}
	
	public void sendTradeRequest(Player from, Player to) throws Exception {
		try {
			getUsersManager().getUser(from.getName()).initializeTrade(to);
		} catch(Exception e) {
			throw new Exception("Could not send trade");
		}
	}
	
	
	public UsersManager getUsersManager() {
		return Main.getInstance().usersManager;
	}

	public TradesQueue getTradesQueue() {
		return Main.getInstance().tradesQueue;
	}

	public UsersFile getPlayersDataFile() {
		return Main.getInstance().filesManager.getUsers();
	}
}
