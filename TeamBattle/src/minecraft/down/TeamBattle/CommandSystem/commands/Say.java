package down.TeamBattle.CommandSystem.commands;

import down.TeamBattle.CommandSystem.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

public final class Say extends Command {
	public Say() {
		super("say", "<message>", new String[] {});
	}

	@Override
	public void run(String message) {
		Minecraft
				.getMinecraft()
				.getNetHandler()
				.addToSendQueue(
						new C01PacketChatMessage(message.substring(".say "
								.length())));
	}
}
