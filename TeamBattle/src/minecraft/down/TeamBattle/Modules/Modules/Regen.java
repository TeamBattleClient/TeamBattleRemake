package down.TeamBattle.Modules.Modules;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventPreSendMotionUpdates;
import down.TeamBattle.ModuleValues.Value;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.Logger;
import net.minecraft.network.play.client.C03PacketPlayer;

public final class Regen extends ModuleBase {
	private final Value<Float> health = new Value<Float>("regen_health", 18.0F);
	private final Value<Integer> packets = new Value<Integer>(
			"regen_packet_amount", 100);

	public Regen() {
		super("Regen", 0xFF0FA7D6);

		TeamBattleClient.getCommandManager().getContents()
				.add(new Command("regenhealth", "<health>", "rh") {
					@Override
					public void run(String message) {
						if (message.split(" ")[1].equalsIgnoreCase("-d")) {
							health.setValue(health.getDefaultValue());
						} else {
							health.setValue(Float.parseFloat(message.split(" ")[1]));
						}

						if (health.getValue() < 1.0F) {
							health.setValue(1.0F);
						}
						Logger.logChat("Regen Health set to: "
								+ health.getValue());
					}
				});

		TeamBattleClient.getCommandManager().getContents()
				.add(new Command("regenpackets", "<amount of packets>", "rp") {
					@Override
					public void run(String message) {
						if (message.split(" ")[1].equalsIgnoreCase("-d")) {
							packets.setValue(packets.getDefaultValue());
						} else {
							packets.setValue(Integer.parseInt(message
									.split(" ")[1]));
						}

						if (packets.getValue() > 1000) {
							packets.setValue(1000);
						} else if (packets.getValue() < 10) {
							packets.setValue(10);
						}
						Logger.logChat("Regen Packet Amount set to: "
								+ packets.getValue());
					}
				});
	}

	@Override
	public void onEnabled() {
		super.onEnabled();
		if (mc.theWorld != null) {
			Logger.logChat("\247cWARNING:\247f This mod is patched by newer NoCheat versions.");
		}
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPreSendMotionUpdates) {
			for (int index = 0; index < packets.getValue(); index++) {
				if (mc.thePlayer.onGround
						&& mc.thePlayer.getHealth() <= health.getValue()
						&& mc.thePlayer.getFoodStats().getFoodLevel() > 17) {
					mc.getNetHandler().addToSendQueue(
							new C03PacketPlayer(mc.thePlayer.onGround));
				}
			}
		}
	}
}
