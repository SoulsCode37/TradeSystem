package me.soul.tradesystem.trades;

import java.util.ArrayList;
import java.util.List;

public class TradesQueue {
	
	// Trades Queue
	public List<Trade> queue = new ArrayList<>();
	
	public void addToQueue(Trade t) {
		queue.add(t);
	}
	
	public void removeFromQueue(Trade t) {
		queue.remove(t);
	}
}
