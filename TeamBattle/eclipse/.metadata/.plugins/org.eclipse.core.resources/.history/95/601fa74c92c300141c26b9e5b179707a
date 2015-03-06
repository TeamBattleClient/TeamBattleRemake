package Managers;

import java.util.concurrent.CopyOnWriteArrayList;

import event.Event;
import event.Listener;

public final class EventManager extends ListManager<Listener> {
	public void addListener(Listener listener) {
		if (!getContents().contains(listener)) {
			getContents().add(listener);
		}
	}

	public void call(Event event) {
		if (getContents() == null)
			return;
		for (final Listener listener : getContents()) {
			listener.onEvent(event);
		}
	}

	public void removeListener(Listener listener) {
		if (getContents().contains(listener)) {
			getContents().remove(listener);
		}
	}

	@Override
	public void setup() {
		contents = new CopyOnWriteArrayList<Listener>();
	}
}
