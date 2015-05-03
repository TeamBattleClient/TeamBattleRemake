package down.TeamBattle.EventSystem.events;

import down.TeamBattle.EventSystem.Event;
import net.minecraft.entity.Entity;

public class EventEntityStepPost extends Event {

	private final Entity entity;

	public EventEntityStepPost(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}

}
