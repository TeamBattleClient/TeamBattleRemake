package down.TeamBattle.CommandSystem.commands;

import java.util.Arrays;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.Utils.Logger;

public final class Help extends Command {
	public Help() {
		super("help", "none", "cmds");
	}

	@Override
	public void run(String message) {
		final String[] arguments = message.split(" ");
		if (arguments.length == 1) {
			final StringBuilder commands = new StringBuilder("Commands: ");
			for (final Command command : TeamBattleClient.getCommandManager()
					.getContents()) {
				commands.append(command.getCommand() + ", ");
			}
			Logger.logChat(commands.substring(0, commands.length() - 2)
					.toString());
			Logger.logChat("\247aNOTE:\247f Most commands have shorter versions, type .help <command> to see it's aliases.");
		} else {
			final Command command = TeamBattleClient.getCommandManager()
					.getCommandByName(arguments[1]);

			if (command == null) {
				Logger.logChat("Command \"." + arguments[1]
						+ "\" was not found!");
			} else {
				Logger.logChat("Command: " + command.getCommand());
				if (command.getAliases().length != 0) {
					Logger.logChat("Aliases: "
							+ Arrays.toString(command.getAliases()));
				}
				if (!command.getArguments().equals("none")) {
					Logger.logChat("Arguments: " + command.getArguments());
				}
			}
		}
	}

}
