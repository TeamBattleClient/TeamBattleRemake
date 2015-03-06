package down.TeamBattle.CommandSystem.commands;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.Logger;

public final class Toggle extends Command {
	public Toggle() {
		super("toggle", "<mod name>", "t");
	}

	@Override
	public void run(String message) {
		final String[] arguments = message.split(" ");
		final ModuleBase mod = TeamBattleClient.getModManager().getModByName(arguments[1]);
		if (mod == null) {
			Logger.logChat("Mod \"" + arguments[1] + "\" was not found!");
		} else {
			mod.toggle();
			Logger.logChat("Mod \"" + mod.getName() + "\" was toggled "
					+ (mod.isEnabled() ? "\2472on" : "\2474off") + "\247f.");
		}
	}

}
