package me.soul.tradesystem.files.impl;

import java.io.IOException;
import java.util.HashMap;

import me.soul.tradesystem.files.BaseFile;

public class LanguagesFile extends BaseFile {

	private HashMap<String, String> languages = new HashMap<>();
	
	public LanguagesFile(String name) throws Exception {
		super(name, "");
		this.setup();
	}
	
	// Setup languages file
	private void setup() throws IOException {
		languages.put("do:not:touch:this", "default");
		languages.put("prefix", "&9&lTrades ");
		languages.put("trade_inventory.title", "&8Trading...");
		languages.put("trade_inventory.accept_item.name", "&a&lAccept trade");
		languages.put("trade_inventory.cancel_item.name", "&c&lCancel trade");
		languages.put("trade_inventory.lock_item.name", "&c&lItems Locked");
		languages.put("trade_inventory.unlocking_item.name", "&6&lUnlocking items...");
		languages.put("trade_inventory.end_trading_item.name", "&9&lTrade will be completed in %seconds% seconds");
		languages.put("trade_inventory.money_item.name", "&6%name%'s money: &e%money%");
		languages.put("trade_inventory.slots_full", "&cSlots are full");
		languages.put("trade_inventory.invalid_item", "&cThis item cannot be traded");
		languages.put("money_trade_inventory.title", "&6Add money to the trade");
		languages.put("money_trade_inventory.confirm_item.name", "&eAdd &6%money% &eto the trade");
		languages.put("money_trade_inventory.cancel_item.name", "&cCancel");
		languages.put("money_trade_inventory.cannot_remove_money", "&cMoney can't go below 0");
		languages.put("money_trade_inventory.money_not_added", "&cNo money added to the trade");
		languages.put("money_trade_inventory.money_added", "&aAdded &2%money%&a to the trade");
		languages.put("money_trade_inventory.vault.not_enough_money", "&cYou don't have enough money");
		languages.put("trade_request.received",
				"&8&l&m+----------------------------+\n"
				+ "&r\n       &9&l%from% &bwants to trade");
		languages.put("trade_request.accept.text", "               &2&lAccept ");
		languages.put("trade_request.accept.hover", "&aAccept &2%from%&a's trade request");
		languages.put("trade_request.deny.text", "&4&lDeny            \n"
				+ "&r&8&l&m+----------------------------+");
		languages.put("trade_request.deny.hover", "&cDeny &4%from%&c's trade request");
		languages.put("trade_request.sent", "&bSent trade request to &9&l%to%");
		languages.put("trade_request_expired.receiver", "&cThe trade request from &4&l%from% &chas expired");
		languages.put("trade_request_expired.sender", "&cThe trade request to &4&l%to% &chas expired");
		languages.put("wait_expire_time", "&cWait till last request to this player expire");
		languages.put("trade_accept_command.syntax", "&cInvalid syntax, use /tradeaccept <player>");
		languages.put("trade_accept_command.not_requested", "&cSorry, you do not have any trade request from &4&l%name%&c");
		languages.put("trade_accept_command.invalid_player", "&cPlayer offline or does not exist");
		languages.put("trade_accept_command.creative_trade", "&cTrades between players in creative are disabled by the server");
		languages.put("trade_accept_command.inventory_open", "&4&l%name%&c has an inventory open, cannot start the trade");
		languages.put("trade_request_denied.receiver", "&cYou denied the trade request from &4&l%from%");
		languages.put("trade_request_denied.sender", "&cYour trade request to &4&l%to%&c has been denied");
		languages.put("trade_request_accepted.receiver", "&bAccepted &9&l%from%&b's trade request");
		languages.put("trade_request_accepted.sender", "&9&l%to%&b has accepted your trade request");
		languages.put("trade_cancelled", "&4&l%name%&c cancelled the trade");
		languages.put("not_enough_space", "&4&l%name%&c's inventory has not enough space");
		languages.put("trade_completed", "&aTrade successfully completed");
		languages.put("trade_command.syntax", "&cInvalid syntax, use /trade <player>");
		languages.put("trade_command.invalid_player", "&cPlayer offline or does not exist or is invalid");
		languages.put("trade_command.player_too_far", "&4&l%name%&c is too far away!");
		languages.put("trade_command.invalid_world", "&cTrades are disabled in your or in &4&l%name%&c's world");
		languages.put("trade_command.different_worlds", "&cYou and &4&l%name%&c are not on the same world");
		languages.put("trade_deny_command.syntax", "&cInvalid syntax, use /tradedeny <player>");
		languages.put("trade_deny_command.not_requested", "&cSorry, you do not have any trade request from &4&l%name%&c");
		languages.put("no_permission", "&cSorry, you do not have enough permissions to do that");
		languages.put("toggletrades_command.toggled", "&bTrades toggled &9&l%state%");
		languages.put("trade_command.trades_off", "&4&l%name%&c has deactivated trades");
		languages.put("inventories_empty", "&cBoth inventories are empty");
		languages.put("on_cooldown", "&cSorry, your trade requests with &4&l%name%&c are on cooldown");
		languages.put("blacklist_command.syntax", "&cInvalid syntax, use /tblacklist <add/remove> <player>");
		languages.put("blacklist_command.blacklisted", "&aYou wont receive any trade request from &4&l%name%");
		languages.put("blacklist_command.already_blacklisted", "&cYou have already blacklisted &4&l%name%");
		languages.put("blacklist_command.unblacklisted", "&aYou will receive trade requests from &2&l%name%");
		languages.put("blacklist_command.not_blacklisted", "&4&l%name% is not blacklisted");
		languages.put("spectate_trade_command.syntax", "&cInvalid syntax, use /spectatetrade <player>");
		languages.put("spectate_trade_command.not_trading", "&4%name%&c is not trading");
		languages.put("spectate_trade_command.spectating", "&aSpectating &2&l%sender%&a -> &2&l%receiver%&a trade");
		languages.put("spectate_trade_command.stop_spectating", "&cNo longer spectating the trade");
		
		if(getFile().contains("do:not:touch:this"))
			return;
		
		getFile().set("do:not:touch:this", "default");
		
		for(String source : languages.keySet())
			getFile().set(source, languages.get(source));
		saveFile();
	}
	
	public String addLanguagesText(String source) throws IOException {
		String text = languages.get(source);
		getFile().set(source, text);
		saveFile();
		return text;
	}
}
