package event.events;

import net.minecraft.entity.Entity;
import event.Event;
public final class EventPostEntityRender extends Event {
	private final Entity entity;

	public EventPostEntityRender(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}
}
