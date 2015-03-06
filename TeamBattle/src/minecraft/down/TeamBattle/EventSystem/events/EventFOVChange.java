package down.TeamBattle.EventSystem.events;

import down.TeamBattle.EventSystem.Event;

public final class EventFOVChange extends Event {
	private float fov;

	public EventFOVChange(float fov) {
		this.fov = fov;
	}

	public float getFOV() {
		return fov;
	}

	public void setFOV(float fov) {
		this.fov = fov;
	}
}
