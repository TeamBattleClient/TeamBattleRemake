package down.TeamBattle.EventSystem.events;

import down.TeamBattle.EventSystem.Cancellable;
import down.TeamBattle.EventSystem.Event;
import net.minecraft.entity.Entity;

public final class EventVelocity extends Event implements Cancellable {
	public enum Type {
		KNOCKBACK, WATER;
	}

	private boolean cancel;
	private final Entity entity;

	private final Type type;

	public EventVelocity(Type type, Entity entity) {
		this.type = type;
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}

	public Type getType() {
		return type;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}
