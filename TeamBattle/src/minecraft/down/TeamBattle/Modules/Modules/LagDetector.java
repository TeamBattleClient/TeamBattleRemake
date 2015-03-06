package down.TeamBattle.Modules.Modules;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventHUDDraw;
import down.TeamBattle.EventSystem.events.EventPacketReceive;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.TimeHelper;
import net.minecraft.network.play.server.S02PacketChat;

/**
 *  it does this by checking when the last packet was sent to you by the
 * server (excluding chat packets since there's some bug where you can still
 * chat but the server's dead)
 * 
 * @author Balen
 */
public final class LagDetector extends ModuleBase {
	private final TimeHelper time = new TimeHelper();

	public LagDetector() {
		super("LagDetector");
		setEnabled(true);
	}

	public TimeHelper getTime() {
		return time;
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventHUDDraw) {
			if (time.hasReached(1000L)) {
				mc.fontRenderer
						.drawStringWithShadow(
								"\2474Lag: \2477"
										+ (time.getCurrentMS() - time
												.getLastMS()) + " ms", 2,
								(Boolean) TeamBattleClient.getValueManager()
										.getValueByName("hud_watermark")
										.getValue()
										|| (Boolean) TeamBattleClient.getValueManager()
												.getValueByName("hud_time")
												.getValue() ? 12 : 2,
								0xFFFFFFFF);
			}
		} else if (event instanceof EventPacketReceive) {
			final EventPacketReceive receive = (EventPacketReceive) event;
			if (!(receive.getPacket() instanceof S02PacketChat)) {
				time.reset();
			}
		}
	}

}
