package me.soul.tradesystem.utils;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ClickableMessages {
	
	// Create clickable message
	public static TextComponent create(String message, String hover, String command) {
		TextComponent s = new TextComponent(message);
		s.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		s.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
		return s;
	}
	
	// Add a custom component to a message
	public static TextComponent add(String message, String hover, String command, TextComponent toAdd) {
		TextComponent s = new TextComponent(message);
		s.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		s.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
		toAdd.addExtra(s);
		return toAdd;
	}
	
	// Send clickable message
	public static void sendClickable(Player to, TextComponent text) {
		to.spigot().sendMessage(text);
	}
}
