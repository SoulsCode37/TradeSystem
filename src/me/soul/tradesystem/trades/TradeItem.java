package me.soul.tradesystem.trades;

public class TradeItem {
	
	public int slot;
	public boolean canBeStacked;
	
	public TradeItem(int slot, boolean stacked) {
		this.slot = slot;
		this.canBeStacked = stacked;
	}
}
