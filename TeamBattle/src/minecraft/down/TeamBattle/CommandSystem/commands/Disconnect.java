package down.TeamBattle.CommandSystem.commands;

import down.TeamBattle.CommandSystem.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;

public class Disconnect extends Command {

	public Disconnect() {
		super("disconnect", "<none>", "discon");
	}

	@Override
	public void run(String message) {
		Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
	}

}
