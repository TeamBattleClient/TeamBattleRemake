package down.TeamBattle.CommandSystem.commands;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.Logger;

public final class ListMods extends Command {
	public ListMods() {
		super("listmods", "none", "mods", "lm");
	}

	@Override
	public void run(String message) {
		final StringBuilder list = new StringBuilder("Mods ("
				+ TeamBattleClient.getModManager().getContents().size() + "): ");
		for (final ModuleBase mod : TeamBattleClient.getModManager().getContents()) {
			list.append(mod.isEnabled() ? "\247a" : "\247c")
					.append(mod.getName()).append("\247f, ");
		}
		Logger.logChat(list.toString().substring(0,
				list.toString().length() - 2));
		Logger.logChat("\247aNOTE:\247f This list is color coded based on if the mod is toggled on or off. Green = on, red = off.");
	}
}
