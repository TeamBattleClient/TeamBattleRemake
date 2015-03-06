package event.events;

import net.minecraft.entity.Entity;
import event.Event;
public class EventEntityStepPost extends Event {

	private final Entity entity;

	public EventEntityStepPost(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}

}
