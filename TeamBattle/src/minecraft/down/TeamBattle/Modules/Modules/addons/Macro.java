package down.TeamBattle.Modules.Modules.addons;

public final class Macro {
	private final String command;
	private final int key;

	public Macro(String command, int key) {
		this.command = command;
		this.key = key;
	}

	public String getCommand() {
		return command;
	}

	public int getKey() {
		return key;
	}
}
