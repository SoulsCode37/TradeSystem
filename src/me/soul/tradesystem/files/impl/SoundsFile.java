package me.soul.tradesystem.files.impl;

import java.io.IOException;

import me.soul.tradesystem.files.BaseFile;

public class SoundsFile extends BaseFile {

	public SoundsFile(String name) throws Exception {
		super(name, "");
		this.setup();
	}
	
	private void setup() throws IOException {
		if(getFile().contains("do:not:touch:this"))
			return;
		getFile().set("do:not:touch:this", "default");
		getFile().set("TRADE_ARRIVE", "ENTITY_VILLAGER_TRADE");
		getFile().set("TRADE_SENT", "ENTITY_VILLAGER_TRADE");
		getFile().set("TRADE_ACCEPT", "ENTITY_VILLAGER_YES");
		getFile().set("TRADE_DENY", "ENTITY_VILLAGER_NO");
		getFile().set("TRADE_CANCEL", "ENTITY_VILLAGER_NO");
		getFile().set("TRADE_END", "ENTITY_VILLAGER_CELEBRATE");
		getFile().set("TRADE_EXPIRE", "ENTITY_ITEM_BREAK");
		saveFile();
	}
}
