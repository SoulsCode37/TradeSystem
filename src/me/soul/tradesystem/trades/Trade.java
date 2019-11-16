package me.soul.tradesystem.trades;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.trades.enums.TradeStatus;
import me.soul.tradesystem.trades.enums.TradeType;
import me.soul.tradesystem.trades.game.InventoryHelper;
import me.soul.tradesystem.trades.game.TradeInterface;
import me.soul.tradesystem.users.User;
import me.soul.tradesystem.utils.ClickableMessages;
import me.soul.tradesystem.utils.Messages;
import me.soul.tradesystem.utils.Settings;
import net.md_5.bungee.api.chat.TextComponent;

public class Trade {
	
	private User sender;
	private User receiver;
	private TradeStatus status;
	private BukkitTask countdown;
	private TradeInterface tradeInterface;
	private String inName;
	private String outName;
	
	// Init trade class
	public Trade(Player s, Player r) {
		this.sender = Main.getInstance().usersManager.getUser(s.getName());
		this.receiver = Main.getInstance().usersManager.getUser(r.getName());
		this.outName = sender.getPlayer().getName();
		this.inName = receiver.getPlayer().getName();
		Main.getInstance().tradesQueue.addToQueue(this);
		sender.getTradesOut().add(this);
		receiver.getTradesIn().add(this);
	}
	
	// Send trade request
	public void sendRequest() {
		setStatus(TradeStatus.SENT);
		sender.getPlayer().sendMessage(Messages.convert("trade_request.sent", true).replace("%to%", receiver.getPlayer().getName()));
		receiver.getPlayer().sendMessage(Messages.convert("trade_request.received", false).replace("%from%", sender.getPlayer().getName()));
		
		TextComponent msg = ClickableMessages.create(Messages.convert("trade_request.accept.text", false).replace("%from%", sender.getPlayer().getName()), Messages.convert("trade_request.accept.hover", false).replace("%from%", sender.getPlayer().getName()), "/tradeaccept " + sender.getPlayer().getName());
		msg = ClickableMessages.add(Messages.convert("trade_request.deny.text", false).replace("%from%", sender.getPlayer().getName()), Messages.convert("trade_request.deny.hover", false).replace("%from%", sender.getPlayer().getName()), "/tradedeny " + sender.getPlayer().getName(), msg);
		ClickableMessages.sendClickable(receiver.getPlayer(), msg);
		
		Main.getInstance().debug("Status: " + getStatus());
		// Start the count down for the expire of the request
		startCountdown();
	}
	
	// Local cooldown
	private void startCountdown() {
		countdown = Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				setStatus(TradeStatus.EXPIRED);
				if(receiver != null) {
					User in = Main.getInstance().usersManager.getUser(inName);
					in.getTradesIn().remove(in.getTrade(TradeType.IN, outName));
					receiver.getPlayer().sendMessage(Messages.convert("trade_request_expired.receiver", true).replace("%from%", sender.getPlayer().getName()));
				}
				if(sender != null) {
					sender.getPlayer().sendMessage(Messages.convert("trade_request_expired.sender", true).replace("%to%", sender.getPlayer().getName()));
					User out = Main.getInstance().usersManager.getUser(outName);
					out.getTradesOut().remove(out.getTrade(TradeType.OUT, inName));
				}	
			}
		}, Settings.EXPIRE_TIME * 20);
	}
	
	public void stopCountdown() {
		countdown.cancel();
		Bukkit.getScheduler().cancelTask(countdown.getTaskId());
	}
	
	// Start trade
	public void startTrading() {
		setStatus(TradeStatus.ACCEPTED);
		Main.getInstance().debug("Status: " + getStatus());
		
		this.stopCountdown();
		
		if(Main.isPremium && Settings.COOLDOWN_PLAYER) {
			if(TradesCooldowns.isOnCooldown(outName, inName))
				TradesCooldowns.removeCooldown(outName, inName);
			if(TradesCooldowns.isOnCooldown(inName, outName))
				TradesCooldowns.removeCooldown(inName, outName);
		}
		
		sender.getPlayer().sendMessage(Messages.convert("trade_request_accepted.sender", true).replace("%to%", receiver.getPlayer().getName()));
		receiver.getPlayer().sendMessage(Messages.convert("trade_request_accepted.receiver", true).replace("%from%", sender.getPlayer().getName()));
		
		this.tradeInterface = new TradeInterface(this);
		getTradeInterface().createInventory();
	}
	
	// Finish trade
	public void endTrading() {
		if(!InventoryHelper.hasEnoughSpace(sender.getPlayer(), InventoryHelper.getNumberOfItems(getTradeInterface().getInv(), getTradeInterface().receiverSlots))) {
			sender.getPlayer().sendMessage(Messages.convert("not_enough_space", true).replace("%name%", sender.getPlayer().getName()));
			receiver.getPlayer().sendMessage(Messages.convert("not_enough_space", true).replace("%name%", sender.getPlayer().getName()));
			return;
		}
		
		if(!InventoryHelper.hasEnoughSpace(receiver.getPlayer(), InventoryHelper.getNumberOfItems(getTradeInterface().getInv(), getTradeInterface().senderSlots))) {
			sender.getPlayer().sendMessage(Messages.convert("not_enough_space", true).replace("%name%", receiver.getPlayer().getName()));
			receiver.getPlayer().sendMessage(Messages.convert("not_enough_space", true).replace("%name%", receiver.getPlayer().getName()));
			return;
		}
		
		setStatus(TradeStatus.FINISHED);
		Main.getInstance().debug("Status: " + getStatus());
		
		sender.getPlayer().getOpenInventory().close();
		receiver.getPlayer().getOpenInventory().close();
		
		InventoryHelper.executeTrade(this);
		
		receiver.getTradesIn().remove(this);
		sender.getTradesOut().remove(this);
		
		sender.getPlayer().sendMessage(Messages.convert("trade_completed", true));
		receiver.getPlayer().sendMessage(Messages.convert("trade_completed", true));
	}
	
	// Deny trade request
	public void denyTrading() {
		setStatus(TradeStatus.DENIED);
		Main.getInstance().debug("Status: " + getStatus());
		this.stopCountdown();
		sender.getPlayer().sendMessage(Messages.convert("trade_request_denied.sender", true).replace("%to%", sender.getPlayer().getName()));
		receiver.getPlayer().sendMessage(Messages.convert("trade_request_denied.receiver", true).replace("%from%", sender.getPlayer().getName()));
		receiver.getTradesIn().remove(this);
		sender.getTradesOut().remove(this);
	}
	
	// Cancel trade request
	public void cancelTrading(String who) {
		setStatus(TradeStatus.CANCELLED);
		Main.getInstance().debug("Status: " + getStatus());
		
		sender.getPlayer().getOpenInventory().close();
		receiver.getPlayer().getOpenInventory().close();
		
		InventoryHelper.returnItems(this);
		
		sender.getTradesOut().remove(this);
		receiver.getTradesIn().remove(this);
		
		sender.getPlayer().sendMessage(Messages.convert("trade_cancelled", true).replace("%name%", who));
		receiver.getPlayer().sendMessage(Messages.convert("trade_cancelled", true).replace("%name%", who));
	}
	
	public User getSender() {
		return sender;
	}
	
	public User getReceiver() {
		return receiver;
	}

	public TradeStatus getStatus() {
		return status;
	}

	public void setStatus(TradeStatus status) {
		this.status = status;
	}

	public TradeInterface getTradeInterface() {
		return tradeInterface;
	}
}
