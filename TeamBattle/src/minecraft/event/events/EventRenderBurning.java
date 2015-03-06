package event.events;
import event.Cancellable;
import event.Event;

public class EventRenderBurning extends Event implements Cancellable {
	private boolean cancel;

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}
