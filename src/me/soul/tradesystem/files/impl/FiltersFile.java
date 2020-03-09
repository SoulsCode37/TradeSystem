package me.soul.tradesystem.files.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import me.soul.tradesystem.files.BaseFile;

public class FiltersFile extends BaseFile {

	public FiltersFile(String name) throws Exception {
		super(name, "");
		this.setup();
	}
	
	private void setup() throws IOException {
		if(getFile().contains("do:not:touch:this"))
			return;
		
		getFile().set("do:not:touch:this", "default");
		getFile().set("lore_filter", Arrays.asList("Non-tradeable"));
		getFile().set("name_filter", Arrays.asList("Too overpowered item"));
		getFile().set("type_filter", Arrays.asList("AIR"));
		saveFile();
	}
	
	public List<String> getLoreFilter() {
		return getFile().getStringList("lore_filter");
	}
	
	public List<String> getTypeFilter() {
		return getFile().getStringList("type_filter");
	}
	
	public List<String> getNameFilter() {
		return getFile().getStringList("name_filter");
	}
}
