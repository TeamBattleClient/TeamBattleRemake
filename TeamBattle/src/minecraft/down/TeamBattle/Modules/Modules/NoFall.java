package down.TeamBattle.Modules.Modules;

import net.minecraft.block.Block;
import net.minecraft.network.play.client.C03PacketPlayer;

import org.lwjgl.input.Keyboard;

import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventPacketSent;
import down.TeamBattle.Modules.ModuleBase;

public final class NoFall extends ModuleBase {
	private Block block;

	public NoFall() {
		super("NoFall", Keyboard.KEY_N, 0xFF8599D9);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPacketSent) {
			final EventPacketSent sent = (EventPacketSent) event;
			if (sent.getPacket() instanceof C03PacketPlayer) {
				final C03PacketPlayer player = (C03PacketPlayer) sent
						.getPacket();
				if (mc.thePlayer.fallDistance >= 3) {
					player.setOnGround(true);
				}
			}
		}
	}

}
