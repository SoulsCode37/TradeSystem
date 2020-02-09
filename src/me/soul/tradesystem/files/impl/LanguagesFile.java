package me.soul.tradesystem.files.impl;

import java.io.IOException;

import me.soul.tradesystem.files.BaseFile;

public class LanguagesFile extends BaseFile {

	public LanguagesFile(String name) throws Exception {
		super(name, "");
		this.setup();
	}
	
	// Setup languages file
	private void setup() throws IOException {
		if(getFile().contains("do:not:touch:this"))
			return;
		getFile().set("do:not:touch:this", "default");
		getFile().set("prefix", "&9&lTrades ");
		getFile().set("trade_inventory.title", "&8Trading...");
		getFile().set("trade_inventory.accept_item.name", "&a&lAccept trade");
		getFile().set("trade_inventory.cancel_item.name", "&c&lCancel trade");
		getFile().set("trade_inventory.lock_item.name", "&c&lItems Locked");
		getFile().set("trade_inventory.unlocking_item.name", "&6&lUnlocking items...");
		getFile().set("trade_inventory.slots_full", "&cSlots are full");
		getFile().set("trade_request.received",
				"&8&l&m+----------------------------+\n"
				+ "&r\n       &9&l%from% &bwants to trade");
		getFile().set("trade_request.accept.text", "               &2&lAccept ");
		getFile().set("trade_request.accept.hover", "&aAccept &2%from%&a's trade request");
		getFile().set("trade_request.deny.text", "&4&lDeny            \n"
				+ "&r&8&l&m+----------------------------+");
		getFile().set("trade_request.deny.hover", "&cDeny &4%from%&c's trade request");
		getFile().set("trade_request.sent", "&bSent trade request to &9&l%to%");
		getFile().set("trade_request_expired.receiver", "&cThe trade request from &4&l%from% &chas expired");
		getFile().set("trade_request_expired.sender", "&cThe trade request to &4&l%to% &chas expired");
		getFile().set("wait_expire_time", "&cWait till last request to this player expire");
		getFile().set("trade_accept_command.syntax", "&cInvalid syntax, use /tradeaccept <player>");
		getFile().set("trade_accept_command.not_requested", "&cSorry, you do not have any trade request from &4&l%name%&c");
		getFile().set("trade_accept_command.invalid_player", "&cPlayer offline or does not exist");
		getFile().set("trade_accept_command.creative_trade", "&cTrades between players in creative are disabled by the server");
		getFile().set("trade_accept_command.inventory_open", "&4&l%name%&c has an inventory open, cannot start the trade");
		getFile().set("trade_request_denied.receiver", "&cYou denied the trade request from &4&l%from%");
		getFile().set("trade_request_denied.sender", "&cYour trade request to &4&l%to%&c has been denied");
		getFile().set("trade_request_accepted.receiver", "&bAccepted &9&l%from%&b's trade request");
		getFile().set("trade_request_accepted.sender", "&9&l%to%&b has accepted your trade request");
		getFile().set("trade_cancelled", "&4&l%name%&c cancelled the trade");
		getFile().set("not_enough_space", "&4&l%name%&c's inventory has not enough space");
		getFile().set("trade_completed", "&aTrade successfully completed");
		getFile().set("trade_command.syntax", "&cInvalid syntax, use /trade <player>");
		getFile().set("trade_command.invalid_player", "&cPlayer offline or does not exist or is invalid");
		getFile().set("trade_command.invalid_world", "&cTrades are disabled in your or in &4&l%name%&c's world");
		getFile().set("trade_command.different_worlds", "&cYou and &4&l%name%&c are not on the same world");
		getFile().set("trade_deny_command.syntax", "&cInvalid syntax, use /tradedeny <player>");
		getFile().set("trade_deny_command.not_requested", "&cSorry, you do not have any trade request from &4&l%name%&c");
		getFile().set("no_permission", "&cSorry, you do not have enough permissions to do that");
		getFile().set("toggletrades_command.toggled", "&bTrades toggled &9&l%state%");
		getFile().set("trade_command.trades_off", "&4&l%name%&c has deactivated trades");
		getFile().set("inventories_empty", "&cBoth inventories are empty");
		getFile().set("on_cooldown", "&cSorry, your trade requests with &4&l%name%&c are on cooldown");
		getFile().set("blacklist_command.syntax", "&cInvalid syntax, use /tblacklist <add/remove> <player>");
		getFile().set("blacklist_command.blacklisted", "&cYou wont receive any trade request from &4&l%name%");
		getFile().set("blacklist_command.unblacklisted", "&aYou will receive trade requests from &2&l%name%");
		saveFile();
	}
}
