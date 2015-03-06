package event.events;

import net.minecraft.entity.Entity;
import event.Event;
public final class EventPreEntityRender extends Event {
	private final Entity entity;

	public EventPreEntityRender(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}
}
