package down.TeamBattle.Modules.Modules;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;

import org.lwjgl.input.Keyboard;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventPacketSent;
import down.TeamBattle.EventSystem.events.EventPlayerMovement;
import down.TeamBattle.EventSystem.events.EventPreSendMotionUpdates;
import down.TeamBattle.ModuleValues.Value;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.Logger;

public final class Fly extends ModuleBase {
	private final Value<Float> speed = new Value<Float>("fly_speed", 0.05F);
	private final Value<Boolean> nocheat = new Value<Boolean>("fly_nocheat",
			true);
	private boolean setLoc;
	private float height;

	public Fly() {
		super("Fly", Keyboard.KEY_F, 0xFFEDCABD);
		TeamBattleClient.getCommandManager().getContents()
				.add(new Command("flyspeed", "<speed>", "fs") {
					@Override
					public void run(String message) {
						if (message.split(" ")[1].equalsIgnoreCase("-d")) {
							speed.setValue(speed.getDefaultValue());
						} else {
							speed.setValue(Float.parseFloat(message.split(" ")[1]));
						}

						if (speed.getValue() > 1.0F) {
							speed.setValue(1.0F);
						} else if (speed.getValue() < 0.05F) {
							speed.setValue(0.05F);
						}
						Logger.logChat("Fly Speed set to: " + speed.getValue());
					}
				});
		TeamBattleClient.getCommandManager().getContents()
				.add(new Command("flynocheat", "none", "fnocheat", "fnc") {
					@Override
					public void run(String message) {
						nocheat.setValue(!nocheat.getValue());
						Logger.logChat("Fly will "
								+ (nocheat.getValue() ? "now" : "no longer")
								+ " bypass nocheat.");
					}
				});
	}

	@Override
	public void onDisabled() {
		super.onDisabled();
		if (mc.thePlayer != null) {
			if (mc.thePlayer.capabilities.isFlying) {
				mc.thePlayer.capabilities.isFlying = false;
			}
			mc.thePlayer.capabilities.setFlySpeed(0.05F);
		}
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPlayerMovement && !nocheat.getValue()) {
			if (!mc.thePlayer.capabilities.isFlying) {
				mc.thePlayer.capabilities.isFlying = true;
			}
			mc.thePlayer.capabilities.setFlySpeed(speed.getValue());
		} else if (event instanceof EventPreSendMotionUpdates
				&& nocheat.getValue()) {
			// credits to klitnose
			if (mc.gameSettings.keyBindJump.getIsKeyPressed())
				mc.thePlayer.motionY = 0.5;
			else if (mc.gameSettings.keyBindSneak.getIsKeyPressed())
				mc.thePlayer.motionY = -0.5;
			if (!mc.thePlayer.onGround
					&& !mc.gameSettings.keyBindJump.getIsKeyPressed()
					&& !mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
				mc.thePlayer.motionY = -0.03;
				Block highBlock = mc.theWorld.getBlock(
						(int) Math.round(mc.thePlayer.posX),
						(int) Math.round(mc.thePlayer.boundingBox.minY - 1.5),
						(int) Math.round(mc.thePlayer.posZ));
				if (!(highBlock instanceof BlockAir)) {
					setLoc = true;
				} else {
					setLoc = false;
					height = 0.6F;
				}
			} else if (setLoc && mc.thePlayer.onGround && height >= 0.11) {
				height -= 0.005;
			}
		} else if (event instanceof EventPacketSent) {
			if (nocheat.getValue()) {
				final EventPacketSent sent = (EventPacketSent) event;
				if (sent.getPacket() instanceof C03PacketPlayer) {
					final C03PacketPlayer player = (C03PacketPlayer) sent
							.getPacket();
					if (setLoc) {
						player.field_149477_b += height;
						player.field_149475_d += height;
						player.field_149474_g = false;
					}
				}
			}
		}
	}
}
