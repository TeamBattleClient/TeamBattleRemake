package down.TeamBattle.CommandSystem.commands;

import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.Utils.Logger;
import net.minecraft.client.Minecraft;

public final class VClip extends Command {
	private final Minecraft mc = Minecraft.getMinecraft();

	public VClip() {
		super("vclip", "<blocks>", "vc");
	}

	@Override
	public void run(String message) {
		final double blocks = Double.parseDouble(message.split(" ")[1]);
		mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + blocks,
				mc.thePlayer.posZ);
		Logger.logChat("Teleported \"" + blocks + "\" blocks.");
	}
}
