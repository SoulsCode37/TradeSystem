package me.soul.tradesystem.trades.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.soul.tradesystem.Main;

public class ItemFilter {
	
	private ItemStack item;
	
	public ItemFilter(ItemStack item) {
		this.item = item;
	}
	
	public boolean canBeTraded() throws Exception {
		if (this.item.hasItemMeta()) {
			// Lore Filter
			if (this.item.getItemMeta().hasLore()) {
				for (String loreLine : this.item.getItemMeta().getLore())
					for (String loreFilter : getLoreFilter())
						if (normalizeString(loreLine).contains(loreFilter))
							return false;
			}
			
			// Name Filter
			if(this.item.getItemMeta().hasDisplayName()) {
				for (String nameFilter : getNameFilter())
					if (normalizeString(this.item.getItemMeta().getDisplayName()).contains(nameFilter))
						return false;
			}
		}
		try {
			// Type Filter
			return !getTypeFilter().contains(this.item.getType());
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	private List<Material> getTypeFilter() throws Exception {
		List<Material> mats = new ArrayList<>();
		
		for(String s : Main.getInstance().filesManager.getFilters().getTypeFilter()) {
			try {
				mats.add(Material.getMaterial(s));
			} catch(Exception e) {
				throw new Exception(s);
			}
		}
		return mats;
	}
	
	private List<String> getNameFilter() {
		List<String> names = new ArrayList<>();
		
		for(String s : Main.getInstance().filesManager.getFilters().getNameFilter())
			names.add(normalizeString(s));
		return names;
	}
	
	private List<String> getLoreFilter() {
		List<String> lores = new ArrayList<>();
		
		for(String s : Main.getInstance().filesManager.getFilters().getLoreFilter())
			lores.add(normalizeString(s));
		return lores;
	}
	
	private String normalizeString(String s) {
		return s
				.replace("&a", "")
				.replace("&b", "")
				.replace("&c", "")
				.replace("&d", "")
				.replace("&e", "")
				.replace("&f", "")
				.replace("&k", "")
				.replace("&n", "")
				.replace("&m", "")
				.replace("&l", "")
				.replace("&i", "")
				.replace("&u", "")
				.replace("&o", "")
				.replace("&r", "")
				.replace("&1", "")
				.replace("&2", "")
				.replace("&3", "")
				.replace("&4", "")
				.replace("&5", "")
				.replace("&6", "")
				.replace("&7", "")
				.replace("&8", "")
				.replace("&9", "")
				.replace("&0", "");
	}
	
}
