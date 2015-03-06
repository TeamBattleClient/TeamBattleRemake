package down.TeamBattle.Modules.Modules;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventRightClick;
import down.TeamBattle.ModuleValues.Value;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.Logger;

public final class FastPlace extends ModuleBase {
	private final Value<Integer> speed = new Value<Integer>("fastplace_speed",
			4);

	public FastPlace() {
		super("FastPlace", 0xFF90A7C5);

		TeamBattleClient.getCommandManager()
				.getContents()
				.add(new Command("fastplacespeed", "<speed>", "fpspeed", "fps") {
					@Override
					public void run(String message) {
						if (message.split(" ")[1].equalsIgnoreCase("-d")) {
							speed.setValue(speed.getDefaultValue());
						} else {
							speed.setValue(Integer.parseInt(message.split(" ")[1]));
						}
						if (speed.getValue() < 1) {
							speed.setValue(1);
						} else if (speed.getValue() > 4) {
							speed.setValue(4);
						}
						Logger.logChat("FastPlace Speed set to: "
								+ speed.getValue());
					}
				});
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRightClick) {
			final EventRightClick rightClick = (EventRightClick) event;
			rightClick.setDelay(rightClick.getDelay() - speed.getValue());
		}
	}
}
