package down.TeamBattle.Modules.Modules;

import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventTick;
import down.TeamBattle.Modules.ModuleBase;

public final class Respawn extends ModuleBase {
	public Respawn() {
		super("Respawn");
		setEnabled(true);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventTick) {
			if (mc.thePlayer == null || mc.theWorld == null)
				return;
			if (!mc.thePlayer.isEntityAlive()) {
				mc.thePlayer.respawnPlayer();
			}
		}
	}
}
