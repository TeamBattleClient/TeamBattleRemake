package event.events;

import event.Event;


public class EventBlockOpacity extends Event {
	private int blockOpacity;

	public EventBlockOpacity(int blockOpacity) {
		this.blockOpacity = blockOpacity;
	}

	public int getBlockOpacity() {
		return blockOpacity;
	}

	public void setBlockOpacity(int blockOpacity) {
		this.blockOpacity = blockOpacity;
	}
}
