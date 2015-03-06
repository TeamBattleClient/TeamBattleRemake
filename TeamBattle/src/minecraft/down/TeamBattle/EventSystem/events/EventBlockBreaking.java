package down.TeamBattle.EventSystem.events;

import down.TeamBattle.EventSystem.Event;

public final class EventBlockBreaking extends Event {
	private float damage;
	private int delay;
	private final int x, y, z, side;

	public EventBlockBreaking(int x, int y, int z, int side, int delay,
			float damage) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.side = side;
		this.delay = delay;
		this.damage = damage;
	}

	public float getDamage() {
		return damage;
	}

	public int getDelay() {
		return delay;
	}

	public int getSide() {
		return side;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}
}
