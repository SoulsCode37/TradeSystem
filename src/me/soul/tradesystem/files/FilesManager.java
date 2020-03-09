package me.soul.tradesystem.files;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.files.impl.FiltersFile;
import me.soul.tradesystem.files.impl.LanguagesFile;
import me.soul.tradesystem.files.impl.SoundsFile;
import me.soul.tradesystem.files.impl.UsersFile;

public class FilesManager {
	
	
	private LanguagesFile languages;
	private UsersFile users;
	private SoundsFile sounds;
	private FiltersFile filters;
	
	public FilesManager() throws Exception {
		this.setup();
	}
	
	// Initialize files
	private void setup() throws Exception {
		if (!Main.getInstance().getDataFolder().exists())
			Main.getInstance().getDataFolder().mkdir();
		
		try {
			languages = new LanguagesFile("languages.yml");
			users = new UsersFile("users.yml");
			sounds = new SoundsFile("sounds.yml");
			filters = new FiltersFile("filters.yml");
		} catch (Exception e) {
			throw new Exception("Could not setup files");
		}
	}

	public LanguagesFile getLanguages() {
		return languages;
	}

	public UsersFile getUsers() {
		return users;
	}

	public SoundsFile getSounds() {
		return sounds;
	}

	public FiltersFile getFilters() {
		return filters;
	}
}
