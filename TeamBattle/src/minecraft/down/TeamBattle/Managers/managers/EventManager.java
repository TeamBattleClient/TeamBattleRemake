package down.TeamBattle.Managers.managers;

import java.util.concurrent.CopyOnWriteArrayList;

import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.Listener;
import down.TeamBattle.Managers.ListManager;

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
