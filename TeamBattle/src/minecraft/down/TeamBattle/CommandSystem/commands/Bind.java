package down.TeamBattle.CommandSystem.commands;

import org.lwjgl.input.Keyboard;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.Logger;

public final class Bind extends Command {
	public Bind() {
		super("bind", "<mod name> <key>");
	}

	@Override
	public void run(String message) {
		final ModuleBase mod = TeamBattleClient.getModManager().getModByName(
				message.split(" ")[1]);
		if (mod == null) {
			Logger.logChat("Mod \"" + message.split(" ")[1]
					+ "\" was not found!");
		} else {
			mod.setKeybind(Keyboard.getKeyIndex(message.split(" ")[2]
					.toUpperCase()));
			Logger.logChat("Mod \"" + mod.getName() + "\" is now bound to: "
					+ Keyboard.getKeyName(mod.getKeybind()));
		}
	}

}
