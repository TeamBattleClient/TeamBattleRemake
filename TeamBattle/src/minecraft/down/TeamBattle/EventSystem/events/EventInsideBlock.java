package down.TeamBattle.EventSystem.events;

import down.TeamBattle.EventSystem.Cancellable;
import down.TeamBattle.EventSystem.Event;

public final class EventInsideBlock extends Event implements Cancellable {
	@Override
	public boolean isCancelled() {
		return true;
	}

	@Override
	public void setCancelled(boolean cancel) {
	}

}
