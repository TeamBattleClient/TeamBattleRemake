package event.events;

import net.minecraft.entity.Entity;
import event.Event;
public final class EventEntityStep extends Event {
	private final Entity entity;

	public EventEntityStep(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}
}
