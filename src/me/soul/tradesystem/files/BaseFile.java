package me.soul.tradesystem.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.soul.tradesystem.Main;

public class BaseFile {
	
	private FileConfiguration s;
	private File sf;

	// Base file
	public BaseFile(String name, String subpath) throws Exception {
		Path fullPath = Paths.get(Main.getInstance().getDataFolder() + "/" + subpath);
		
		if(!Files.exists(fullPath))
			Files.createDirectory(fullPath);
		
		this.sf = new File(fullPath.toString() + "/" + name);

		if (!this.sf.exists()) {
			try {
				this.sf.createNewFile();
			} catch (Exception e) {
				throw new Exception("ERROR - Base file creation", e);
			}
		}

		this.s = YamlConfiguration.loadConfiguration(sf);
	}

	public FileConfiguration getFile() {
		return this.s;
	}
	
	protected void saveFile() throws IOException {
		this.s.save(sf);
	}
}
