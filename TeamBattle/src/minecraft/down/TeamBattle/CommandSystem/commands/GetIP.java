package down.TeamBattle.CommandSystem.commands;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.Utils.Logger;
import net.minecraft.client.Minecraft;

public final class GetIP extends Command {
	public GetIP() {
		super("getip", "none", "gip", "ip");
	}

	@Override
	public void run(String message) {
		final StringSelection ss = new StringSelection(Minecraft.getMinecraft()
				.func_147104_D().serverIP);
		final Clipboard clipboard = Toolkit.getDefaultToolkit()
				.getSystemClipboard();
		clipboard.setContents(ss, null);
		Logger.logChat("Current server IP copied to clipboard.");
	}
}
