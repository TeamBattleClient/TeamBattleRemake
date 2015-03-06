package event.events;

import event.Cancellable;
import event.Event;


public final class EventInsideBlock extends Event implements Cancellable {
	@Override
	public boolean isCancelled() {
		return true;
	}

	@Override
	public void setCancelled(boolean cancel) {
	}

}
