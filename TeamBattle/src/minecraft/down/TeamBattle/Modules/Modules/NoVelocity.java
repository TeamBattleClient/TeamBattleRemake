package down.TeamBattle.Modules.Modules;

import org.lwjgl.input.Keyboard;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventVelocity;
import down.TeamBattle.EventSystem.events.EventVelocity.Type;
import down.TeamBattle.ModuleValues.Value;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.Logger;

public final class NoVelocity extends ModuleBase {
	private final Value<Boolean> knockback = new Value<Boolean>(
			"novelocity_knockback", true);
	private final Value<Boolean> water = new Value<Boolean>("novelocity_water",
			true);

	public NoVelocity() {
		super("NoVelocity", Keyboard.KEY_BACKSLASH, 0xFF56748A);

		TeamBattleClient.getCommandManager()
				.getContents()
				.add(new Command("novelocityknockback", "none", "nvknockback",
						"nvk") {
					@Override
					public void run(String message) {
						knockback.setValue(!knockback.getValue());
						Logger.logChat("NoVelocity will "
								+ (knockback.getValue() ? "now" : "no longer")
								+ " ignore knockback.");
					}
				});

		TeamBattleClient.getCommandManager().getContents()
				.add(new Command("novelocitywater", "none", "nvwater", "nvw") {
					@Override
					public void run(String message) {
						water.setValue(!water.getValue());
						Logger.logChat("NoVelocity will "
								+ (water.getValue() ? "now" : "no longer")
								+ " ignore water velocity.");
					}
				});
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventVelocity) {
			final EventVelocity velocity = (EventVelocity) event;
			if (velocity.getType() == Type.KNOCKBACK) {
				velocity.setCancelled(velocity.getEntity() == mc.thePlayer
						&& knockback.getValue());
			} else if (velocity.getType() == Type.WATER) {
				velocity.setCancelled(velocity.getEntity() == mc.thePlayer
						&& water.getValue());
			}
		}
	}
}
