package me.soul.tradesystem.trades;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import me.soul.tradesystem.Main;
import me.soul.tradesystem.api.customevents.TradeAcceptEvent;
import me.soul.tradesystem.api.customevents.TradeCancelEvent;
import me.soul.tradesystem.api.customevents.TradeDenyEvent;
import me.soul.tradesystem.api.customevents.TradeEndEvent;
import me.soul.tradesystem.api.customevents.TradeExpireEvent;
import me.soul.tradesystem.api.customevents.TradeSendEvent;
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
		TradeSendEvent event = new TradeSendEvent(this);
		Bukkit.getPluginManager().callEvent(event);
		
		if(event.isCancelled())
			return;
		
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
				expireTrade();
			}
		}, Settings.EXPIRE_TIME * 20);
	}
	
	public void stopCountdown() {
		countdown.cancel();
		Bukkit.getScheduler().cancelTask(countdown.getTaskId());
	}
	
	// Expire trade
	public void expireTrade() {
		TradeExpireEvent event = new TradeExpireEvent(this);
	    Bukkit.getPluginManager().callEvent(event);
		
		if(event.isCancelled())
			return;
		
		setStatus(TradeStatus.EXPIRED);
		stopCountdown();
		User in = Main.getInstance().usersManager.getUser(inName);
		User out = Main.getInstance().usersManager.getUser(outName);
		// Checks if a player has left
		if(receiver != null && in != null && !in.getTradesIn().isEmpty()) {
			in.getTradesIn().remove(in.getTrade(TradeType.IN, outName));
			receiver.getPlayer().sendMessage(Messages.convert("trade_request_expired.receiver", true).replace("%from%", sender.getPlayer().getName()));
		}
		if(sender != null && out != null && !out.getTradesOut().isEmpty()) {
			sender.getPlayer().sendMessage(Messages.convert("trade_request_expired.sender", true).replace("%to%", receiver.getPlayer().getName()));
			out.getTradesOut().remove(out.getTrade(TradeType.OUT, inName));
		}	
	}
	
	// Start trade
	public void startTrading() {
		TradeAcceptEvent event = new TradeAcceptEvent(this);
	    Bukkit.getPluginManager().callEvent(event);
		
		if(event.isCancelled())
			return;
		
		setStatus(TradeStatus.ACCEPTED);
		
		this.stopCountdown();
		
		if(Settings.COOLDOWN_PLAYER) {
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
	
	// Anti Scam timer of 3 seconds
	public boolean antiscamTimer() {
		if(!InventoryHelper.hasEnoughSpace(sender.getPlayer(), InventoryHelper.getNumberOfItems(getTradeInterface().getInv(), getTradeInterface().receiverSlots))) {
			sender.getPlayer().sendMessage(Messages.convert("not_enough_space", true).replace("%name%", sender.getPlayer().getName()));
			receiver.getPlayer().sendMessage(Messages.convert("not_enough_space", true).replace("%name%", sender.getPlayer().getName()));
			getTradeInterface().resetTrade();
			return false;
		}
		
		if(!InventoryHelper.hasEnoughSpace(receiver.getPlayer(), InventoryHelper.getNumberOfItems(getTradeInterface().getInv(), getTradeInterface().senderSlots))) {
			sender.getPlayer().sendMessage(Messages.convert("not_enough_space", true).replace("%name%", receiver.getPlayer().getName()));
			receiver.getPlayer().sendMessage(Messages.convert("not_enough_space", true).replace("%name%", receiver.getPlayer().getName()));
			getTradeInterface().resetTrade();
			return false;
		}
		
		if(InventoryHelper.isTradeInventoryEmpty(getTradeInterface())) {
			sender.getPlayer().sendMessage(Messages.convert("inventories_empty", true));
			receiver.getPlayer().sendMessage(Messages.convert("inventories_empty", true));
			getTradeInterface().resetTrade();
			return false;
		}
		
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if(getStatus().equals(TradeStatus.ACCEPTED))
					endTrading();
			}
		}, 20 * 3);
		return true;
	}
	
	// Finish trade
	public void endTrading() {
		TradeEndEvent event = new TradeEndEvent(this);
	    Bukkit.getPluginManager().callEvent(event);
		
		if(event.isCancelled())
			return;
		
		// Pay money
		int senderMoney = getTradeInterface().getSenderMoneyInterface().getMoney();
		int receiverMoney = getTradeInterface().getReceiverMoneyInterface().getMoney();
		
		if(Settings.USE_VAULT) {
			if(!getTradeInterface().getSenderMoneyInterface().hasEnoughMoney()) {
				sender.getPlayer().sendMessage(Messages.convert("money_trade_inventory.vault.not_enough_money", true).replace("%money%", senderMoney + ""));
				getTradeInterface().resetTrade();
				return;
			}
			
			if(!getTradeInterface().getReceiverMoneyInterface().hasEnoughMoney()) {
				receiver.getPlayer().sendMessage(Messages.convert("money_trade_inventory.vault.not_enough_money", true).replace("%money%", receiverMoney + ""));
				getTradeInterface().resetTrade();
				return;
			}
		}
		
		setStatus(TradeStatus.FINISHED);
		
		sender.getPlayer().getOpenInventory().close();
		receiver.getPlayer().getOpenInventory().close();
		
		InventoryHelper.executeTrade(this);

		
		String rName = getReceiver().getPlayer().getName();
		String sName = getSender().getPlayer().getName();
		
		if (senderMoney > 0)
			Bukkit.dispatchCommand(getSender().getPlayer(),
					Settings.PAY_COMMAND.replace("%money%", senderMoney + "").replace("%name%", rName));
		if (receiverMoney > 0)
			Bukkit.dispatchCommand(getReceiver().getPlayer(),
					Settings.PAY_COMMAND.replace("%money%", receiverMoney + "").replace("%name%", sName));
		
		receiver.getTradesIn().remove(this);
		sender.getTradesOut().remove(this);
		
		sender.getPlayer().sendMessage(Messages.convert("trade_completed", true));
		receiver.getPlayer().sendMessage(Messages.convert("trade_completed", true));
	}
	
	// Deny trade request
	public void denyTrading() {
		TradeDenyEvent event = new TradeDenyEvent(this);
	    Bukkit.getPluginManager().callEvent(event);
		
		if(event.isCancelled())
			return;
		
		setStatus(TradeStatus.DENIED);

		this.stopCountdown();
		sender.getPlayer().sendMessage(Messages.convert("trade_request_denied.sender", true).replace("%to%", sender.getPlayer().getName()));
		receiver.getPlayer().sendMessage(Messages.convert("trade_request_denied.receiver", true).replace("%from%", sender.getPlayer().getName()));
		receiver.getTradesIn().remove(this);
		sender.getTradesOut().remove(this);
	}
	
	// Cancel trade request
	public void cancelTrading(String who) {
		TradeCancelEvent event = new TradeCancelEvent(this);
	    Bukkit.getPluginManager().callEvent(event);
		
		if(event.isCancelled())
			return;
		
		setStatus(TradeStatus.CANCELLED);
		
		sender.getPlayer().getOpenInventory().close();
		receiver.getPlayer().getOpenInventory().close();
		
		InventoryHelper.returnItems(this);
		
		sender.getTradesOut().remove(this);
		receiver.getTradesIn().remove(this);
		
		sender.getPlayer().sendMessage(Messages.convert("trade_cancelled", true).replace("%name%", who));
		receiver.getPlayer().sendMessage(Messages.convert("trade_cancelled", true).replace("%name%", who));
	}
	
	public boolean isSender(User user) {
		return user.equals(getSender());
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
		Main.getInstance().debug(sender.getPlayer().getName() + "--> " + receiver.getPlayer().getName() + ": " + status);
	}

	public TradeInterface getTradeInterface() {
		return tradeInterface;
	}
}
