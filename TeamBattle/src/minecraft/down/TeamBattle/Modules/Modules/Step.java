package down.TeamBattle.Modules.Modules;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventEntityStep;
import down.TeamBattle.EventSystem.events.EventPacketSent;
import down.TeamBattle.ModuleValues.Value;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.Logger;
import net.minecraft.network.play.client.C03PacketPlayer;

public final class Step extends ModuleBase {
	private boolean editPacket;
	private final Value<Float> height = new Value<Float>("step_height", 1.0F);

	public Step() {
		super("Step");
		setEnabled(true);
		setVisible(false);
		TeamBattleClient.getCommandManager().getContents()
				.add(new Command("stepheight", "<float>", "sh") {
					@Override
					public void run(String message) {
						if (message.split(" ")[1].equalsIgnoreCase("-d")) {
							height.setValue(height.getDefaultValue());
						} else {
							height.setValue(Float.parseFloat(message.split(" ")[1]));
						}

						if (height.getValue() < 1.0F) {
							height.setValue(1.0F);
						}
						Logger.logChat("Step Height set to: "
								+ height.getValue());
					}
				});
	}

	public boolean isEditingPackets() {
		return editPacket;
	}

	@Override
	public void onDisabled() {
		super.onDisabled();
		if (mc.thePlayer != null) {
			mc.thePlayer.stepHeight = 0.5F;
		}
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventEntityStep) {
			if (mc.thePlayer == null)
				return;
			final EventEntityStep step = (EventEntityStep) event;
			final double stance = mc.thePlayer.posY
					- mc.thePlayer.boundingBox.minY;
			final boolean shouldStep = step.getEntity() == mc.thePlayer
					&& mc.thePlayer.onGround && !mc.thePlayer.isInWater()
					&& !mc.thePlayer.isCollidedHorizontally && stance <= 1.65D
					&& stance >= 0.1D;
			mc.thePlayer.stepHeight = mc.thePlayer.isInWater() ? 0.50F : height
					.getValue();
			editPacket = shouldStep;
		} else if (event instanceof EventPacketSent) {
			final EventPacketSent sent = (EventPacketSent) event;
			if (sent.getPacket() instanceof C03PacketPlayer) {
				final C03PacketPlayer player = (C03PacketPlayer) sent
						.getPacket();

				if (editPacket) {
					player.field_149477_b += 0.0626;
					player.field_149475_d += 0.0626;
					editPacket = false;
				}

			}

		}

	}
}
