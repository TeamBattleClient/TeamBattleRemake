package event.events;
import event.Event;

public final class EventRender3D extends Event {
	private final float partialTicks;

	public EventRender3D(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
}
