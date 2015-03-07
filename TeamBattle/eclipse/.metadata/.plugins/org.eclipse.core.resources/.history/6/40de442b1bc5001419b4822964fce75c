package down.TeamBattle.CommandSystem.commands;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.Utils.Logger;

public class Reset extends Command {

	public Reset() {
		super("reload", "<none>", "rl");
	}

	@Override
	public void run(String message) {
		TeamBattleClient.getCommandManager().setup();
		TeamBattleClient.getModManager().setup();
		TeamBattleClient.getFriendManager().setup();
		TeamBattleClient.getAdminManager().setup();
		TeamBattleClient.getEventManager().setup();
		TeamBattleClient.getFriendManager().setup();
		TeamBattleClient.getValueManager().setup();
		Logger.logChat("Latemod configurations have been reset.");
	}

}
