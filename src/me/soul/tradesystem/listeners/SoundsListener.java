package me.soul.tradesystem.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.soul.tradesystem.api.customevents.TradeAcceptEvent;
import me.soul.tradesystem.api.customevents.TradeCancelEvent;
import me.soul.tradesystem.api.customevents.TradeDenyEvent;
import me.soul.tradesystem.api.customevents.TradeEndEvent;
import me.soul.tradesystem.api.customevents.TradeExpireEvent;
import me.soul.tradesystem.api.customevents.TradeSendEvent;
import me.soul.tradesystem.trades.Trade;
import me.soul.tradesystem.utils.Settings;
import me.soul.tradesystem.utils.Sounds;

public class SoundsListener implements Listener {

	// Examples on how to use the API
	
	@EventHandler
	public void onTradeSend(TradeSendEvent event) {
		Trade trade = event.trade;
		
		// Sender
		Player sender = trade.getSender().getPlayer();
		// Receiver
		Player receiver = trade.getReceiver().getPlayer();
		
		sender.playSound(sender.getLocation(), Sounds.convert("TRADE_SEND"), Settings.SOUNDS_INTENSITY, 1);
		receiver.playSound(receiver.getLocation(), Sounds.convert("TRADE_ARRIVE"), Settings.SOUNDS_INTENSITY, 1);
	}
	
	@EventHandler
	public void onTradeAccept(TradeAcceptEvent event) {
		Trade trade = event.trade;
		
		// Sender
		Player sender = trade.getSender().getPlayer();
		// Receiver
		Player receiver = trade.getReceiver().getPlayer();
		
		sender.playSound(sender.getLocation(), Sounds.convert("TRADE_ACCEPT"), Settings.SOUNDS_INTENSITY, 1);
		receiver.playSound(receiver.getLocation(), Sounds.convert("TRADE_ACCEPT"), Settings.SOUNDS_INTENSITY, 1);
	}
	
	@EventHandler
	public void onTradeDeny(TradeDenyEvent event) {
		Trade trade = event.trade;
		
		// Sender
		Player sender = trade.getSender().getPlayer();
		// Receiver
		Player receiver = trade.getReceiver().getPlayer();
		
		sender.playSound(sender.getLocation(), Sounds.convert("TRADE_DENY"), Settings.SOUNDS_INTENSITY, 1);
		receiver.playSound(receiver.getLocation(), Sounds.convert("TRADE_DENY"), Settings.SOUNDS_INTENSITY, 1);
	}
	
	@EventHandler
	public void onTradeCancel(TradeCancelEvent event) {
		Trade trade = event.trade;
		
		// Sender
		Player sender = trade.getSender().getPlayer();
		// Receiver
		Player receiver = trade.getReceiver().getPlayer();
		
		sender.playSound(sender.getLocation(), Sounds.convert("TRADE_CANCEL"), Settings.SOUNDS_INTENSITY, 1);
		receiver.playSound(receiver.getLocation(), Sounds.convert("TRADE_CANCEL"), Settings.SOUNDS_INTENSITY, 1);
	}
	
	@EventHandler
	public void onTradeExpire(TradeExpireEvent event) {
		Trade trade = event.trade;
		
		// Sender
		Player sender = trade.getSender().getPlayer();
		// Receiver
		Player receiver = trade.getReceiver().getPlayer();
		
		sender.playSound(sender.getLocation(), Sounds.convert("TRADE_EXPIRE"), Settings.SOUNDS_INTENSITY, 1);
		receiver.playSound(receiver.getLocation(), Sounds.convert("TRADE_EXPIRE"), Settings.SOUNDS_INTENSITY, 1);
	}
	
	@EventHandler
	public void onTradeEnd(TradeEndEvent event) {
		Trade trade = event.trade;
		
		// Sender
		Player sender = trade.getSender().getPlayer();
		// Receiver
		Player receiver = trade.getReceiver().getPlayer();
		
		sender.playSound(sender.getLocation(), Sounds.convert("TRADE_END"), Settings.SOUNDS_INTENSITY, 1);
		receiver.playSound(receiver.getLocation(), Sounds.convert("TRADE_END"), Settings.SOUNDS_INTENSITY, 1);
	}
}
