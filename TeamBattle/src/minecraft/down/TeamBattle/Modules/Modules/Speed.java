package down.TeamBattle.Modules.Modules;

import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;

import org.lwjgl.input.Keyboard;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventPacketSent;
import down.TeamBattle.EventSystem.events.EventPlayerMovement;
import down.TeamBattle.EventSystem.events.EventPreSendMotionUpdates;
import down.TeamBattle.ModuleValues.Value;
import down.TeamBattle.Modules.Criticals;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.BlockHelper;
import down.TeamBattle.Utils.Logger;

public final class Speed extends ModuleBase {
	private final Value<Boolean> fastice = new Value<Boolean>("speed_fastice",
			true);
	private final Value<Boolean> fastladder = new Value<Boolean>(
			"speed_fastladder", true);
	private final Value<Double> ncSpeed = new Value<Double>(
			"speed_nocheat_speed", 2.60D);
	private boolean nextTick;
	private final Value<Boolean> nocheat = new Value<Boolean>("speed_nocheat",
			true);
	private final Value<Double> speed = new Value<Double>("speed_speed", 1.0D);
	private final Value<Boolean> sprint = new Value<Boolean>("speed_sprint",
			true);

	public Speed() {
		super("Speed", Keyboard.KEY_M, 0xFFE8552D);
		TeamBattleClient.getCommandManager().getContents()
				.add(new Command("speed", "<double>", "sp") {
					@Override
					public void run(String message) {
						if (message.split(" ")[1].equalsIgnoreCase("-d")) {
							speed.setValue(speed.getDefaultValue());
						} else {
							speed.setValue(Double.parseDouble(message
									.split(" ")[1]));
						}
						if (speed.getValue() > 10.0D) {
							speed.setValue(10.0D);
						} else if (speed.getValue() < 1.0D) {
							speed.setValue(1.0D);
						}
						Logger.logChat("Speed set to: " + speed.getValue());
					}
				});

		TeamBattleClient.getCommandManager()
				.getContents()
				.add(new Command("speednocheatspeed", "<double>", "sncspeed",
						"sncs") {
					@Override
					public void run(String message) {
						if (message.split(" ")[1].equalsIgnoreCase("-d")) {
							ncSpeed.setValue(ncSpeed.getDefaultValue());
						} else {
							ncSpeed.setValue(Double.parseDouble(message
									.split(" ")[1]));
						}

						if (ncSpeed.getValue() > 2.6D) {
							ncSpeed.setValue(2.6D);
						} else if (ncSpeed.getValue() < 1.0D) {
							ncSpeed.setValue(1.0D);
						}
						Logger.logChat("Speed NoCheat Speed set to: "
								+ ncSpeed.getValue());
					}
				});

		TeamBattleClient.getCommandManager().getContents()
				.add(new Command("speedsprint", "<double>", "ss") {
					@Override
					public void run(String message) {
						sprint.setValue(!sprint.getValue());
						Logger.logChat("Speed will "
								+ (sprint.getValue() ? "now" : "no longer")
								+ " sprint for you.");
					}
				});
		TeamBattleClient.getCommandManager().getContents()
				.add(new Command("speedfastice", "<double>", "sfi") {
					@Override
					public void run(String message) {
						fastice.setValue(!fastice.getValue());
						Logger.logChat("Speed will "
								+ (fastice.getValue() ? "now" : "no longer")
								+ " go fast on ice.");
					}
				});
		TeamBattleClient.getCommandManager().getContents()
				.add(new Command("speedfastladder", "<double>", "sfl") {
					@Override
					public void run(String message) {
						fastladder.setValue(!fastladder.getValue());
						Logger.logChat("Speed will "
								+ (fastladder.getValue() ? "now" : "no longer")
								+ " go fast on ladders.");
					}
				});
		TeamBattleClient.getCommandManager().getContents()
				.add(new Command("speednocheat", "<double>", "snc", "sn") {
					@Override
					public void run(String message) {
						nocheat.setValue(!nocheat.getValue());
						setColor(nocheat.getValue() ? 0xFFE8552D : 0xFF58DB8B);
						Logger.logChat("Speed will "
								+ (nocheat.getValue() ? "now" : "no longer")
								+ " go extra fast on NoCheat.");
					}
				});
	}

	@Override
	public void onDisabled() {
		super.onDisabled();
		if (mc.timer != null) {
			mc.timer.timerSpeed = 1.0F;
		}

		if (mc.theWorld != null) {
			Blocks.ice.slipperiness = 0.98F;
			Blocks.packed_ice.slipperiness = 0.98F;
		}
	}

	@Override
	public void onEnabled() {
		super.onEnabled();
		setColor(nocheat.getValue() ? 0xFFE8552D : 0xFF58DB8B);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPlayerMovement) {
			final EventPlayerMovement movement = (EventPlayerMovement) event;
			if (sprint.getValue()) {
				mc.thePlayer.setSprinting(mc.thePlayer.moveForward > 0
						&& !mc.thePlayer.isCollidedHorizontally
						&& mc.thePlayer.getFoodStats().getFoodLevel() > 6);
			}

			if (fastladder.getValue() && BlockHelper.isOnLadder()
					&& mc.thePlayer.isCollidedHorizontally) {
				movement.setMotionY(movement.getMotionY() * 2.25D);
				mc.thePlayer.motionY += 0.15D;
			}

			if (fastice.getValue() && BlockHelper.isOnIce()) {
				Blocks.ice.slipperiness = 0.60F;
				Blocks.packed_ice.slipperiness = 0.60F;
				movement.setMotionX(movement.getMotionX() * 2.50D);
				movement.setMotionZ(movement.getMotionZ() * 2.50D);
			} else {
				Blocks.ice.slipperiness = 0.98F;
				Blocks.packed_ice.slipperiness = 0.98F;
			}

			if (!nocheat.getValue()) {
				movement.setMotionX(movement.getMotionX() * speed.getValue());
				movement.setMotionZ(movement.getMotionZ() * speed.getValue());
			}
		} else if (event instanceof EventPreSendMotionUpdates) {
			if (nocheat.getValue()) {
				final KillAura aura = (KillAura) TeamBattleClient.getModManager()
						.getModByName("killaura");
				double speed = BlockHelper.isOnLiquid()
						|| !aura.getTargets().isEmpty() ? 1.50D : ncSpeed
						.getValue();
				final boolean strafe = mc.thePlayer.moveStrafing != 0.0F;
				speed = speed + (mc.thePlayer.isSprinting() ? 0.02D : 0.40D);
				if (!strafe) {
					speed += 0.04F;
				}

				if (shouldSpeedUp()) {
					nextTick = !nextTick;
					if (nextTick) {
						mc.thePlayer.motionX *= speed;
						mc.thePlayer.motionZ *= speed;
						mc.timer.timerSpeed = 1.15F;
					} else {
						mc.thePlayer.motionX /= 1.50D;
						mc.thePlayer.motionZ /= 1.50D;
						mc.timer.timerSpeed = 1.0F;
					}
				} else if (nextTick) {
					mc.thePlayer.motionX /= speed;
					mc.thePlayer.motionZ /= speed;
					mc.timer.timerSpeed = 1.0F;
					nextTick = false;
				}
			} else if (mc.timer.timerSpeed != 1.0F) {
				mc.timer.timerSpeed = 1.0F;
			}
		} else if (event instanceof EventPacketSent) {
			if (nocheat.getValue()) {
				final EventPacketSent sent = (EventPacketSent) event;
				if (sent.getPacket() instanceof C03PacketPlayer) {
					final C03PacketPlayer player = (C03PacketPlayer) sent
							.getPacket();
					if (shouldSpeedUp() && nextTick) {
						final Criticals crits = (Criticals) TeamBattleClient
								.getModManager().getModByName("criticals");
						player.field_149477_b += 0.0625D;
						player.field_149475_d += 0.0625D;
						
						
					}
				}
			}
		}
	}

	private boolean shouldSpeedUp() {
		final boolean moving = mc.thePlayer.movementInput.moveForward != 0.0F
				|| mc.thePlayer.movementInput.moveStrafe != 0.0F;
		final Step step = (Step) TeamBattleClient.getModManager().getModByName("step");
		boolean walkable = true;
		if (step != null && step.isEnabled()) {
			for (final Object bb : mc.theWorld.getCollidingBoundingBoxes(
					mc.thePlayer,
					mc.thePlayer.boundingBox.expand(0.50D, 0.0D, 0.50D))) {
				walkable = false;
			}

			if (step.isEditingPackets()) {
				walkable = false;
			}
		}

		return !mc.thePlayer.isInWater() && !BlockHelper.isInLiquid()
				&& !BlockHelper.isOnIce() && !BlockHelper.isOnLadder()
				&& !mc.thePlayer.isSneaking() && mc.thePlayer.onGround
				&& moving && walkable;
	}

}
