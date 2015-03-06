package down.TeamBattle.Modules.Modules;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventPreSendMotionUpdates;
import down.TeamBattle.ModuleValues.Value;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.Logger;

public final class AutoLog extends ModuleBase {
	private final Value<Float> health = new Value<Float>("autolog_health", 6.0F);

	public AutoLog() {
		super("AutoLog", 0xFF00D412);
		TeamBattleClient.getCommandManager()
				.getContents()
				.add(new Command("autologhealth", "<health>", "loghealth",
						"alh") {
					@Override
					public void run(String message) {
						if (message.split(" ")[1].equalsIgnoreCase("-d")) {
							health.setValue(health.getDefaultValue());
						} else {
							health.setValue(Float.parseFloat(message.split(" ")[1]));
						}

						if (health.getValue() < 1.0F) {
							health.setValue(1.0F);
						}
						Logger.logChat("AutoLog Health set to: "
								+ health.getValue());
					}
				});
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPreSendMotionUpdates) {
			if (mc.thePlayer.getHealth() <= health.getValue()) {
				mc.thePlayer.inventory.currentItem = -999;
				mc.playerController.updateController();
				setEnabled(false);
			}
		}
	}
}
