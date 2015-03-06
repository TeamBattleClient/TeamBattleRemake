package down.TeamBattle.CommandSystem.commands;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.Utils.Logger;

public final class Delete extends Command {
	public Delete() {
		super("del", "<name>", "fdel", "delete");
	}

	@Override
	public void run(String message) {
		final String name = message.split(" ")[1];
		TeamBattleClient.getFriendManager().removeFriend(name);
		Logger.logChat("Friend \"" + name + "\" removed.");
		if (TeamBattleClient.getFileManager().getFileByName("friends") != null) {
			TeamBattleClient.getFileManager().getFileByName("friends").saveFile();
		}
	}
}
