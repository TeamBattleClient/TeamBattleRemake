package down.TeamBattle.Modules.Modules;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.C03PacketPlayer;

import org.lwjgl.input.Keyboard;

import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventInsideBlock;
import down.TeamBattle.EventSystem.events.EventPacketSent;
import down.TeamBattle.EventSystem.events.EventPlayerMovement;
import down.TeamBattle.EventSystem.events.EventPreSendMotionUpdates;
import down.TeamBattle.EventSystem.events.EventPushOutOfBlocks;
import down.TeamBattle.Modules.ModuleBase;

public final class Freecam extends ModuleBase {
	private double x, y, minY, z;
	private float yaw, pitch;

	public Freecam() {
		super("Freecam", "V");
	}

	@Override
	public void onDisabled() {
		super.onDisabled();
		if (mc.thePlayer != null) {
			mc.thePlayer.noClip = false;
			mc.theWorld.removeEntityFromWorld(-1);
			mc.thePlayer.setPositionAndRotation(x, y, z, yaw, pitch);
		}
	}

	@Override
	public void onEnabled() {
		super.onEnabled();
		if (mc.thePlayer != null) {
			x = mc.thePlayer.posX;
			y = mc.thePlayer.posY;
			minY = mc.thePlayer.boundingBox.minY;
			z = mc.thePlayer.posZ;
			yaw = mc.thePlayer.rotationYaw;
			pitch = mc.thePlayer.rotationPitch;
			final EntityOtherPlayerMP ent = new EntityOtherPlayerMP(
					mc.theWorld, mc.thePlayer.getGameProfile());
			ent.inventory = mc.thePlayer.inventory;
			ent.inventoryContainer = mc.thePlayer.inventoryContainer;
			ent.setPositionAndRotation(x, minY, z, yaw, pitch);
			ent.rotationYawHead = mc.thePlayer.rotationYawHead;
			mc.theWorld.addEntityToWorld(-1, ent);
			mc.thePlayer.noClip = true;
		}
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPlayerMovement) {
			final EventPlayerMovement movement = (EventPlayerMovement) event;
			double speed = 4.0;
			if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
				speed = speed / 2.0;
			}
			movement.setMotionY(0);
			mc.thePlayer.motionY = 0;
			if (!mc.inGameHasFocus)
				return;
			if (mc.thePlayer.movementInput.moveForward != 0
					|| mc.thePlayer.movementInput.moveStrafe != 0) {
				movement.setMotionX(movement.getMotionX() * speed);
				movement.setMotionZ(movement.getMotionZ() * speed);
			}

			if (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
				movement.setMotionY(speed / 4);
			} else if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak
					.getKeyCode())) {
				movement.setMotionY(-(speed / 4));
			}
		} else if (event instanceof EventPushOutOfBlocks) {
			((EventPushOutOfBlocks) event).setCancelled(true);
		} else if (event instanceof EventInsideBlock) {
			((EventInsideBlock) event).setCancelled(true);
		} else if (event instanceof EventPreSendMotionUpdates) {
			mc.thePlayer.renderArmPitch += 400;
		} else if (event instanceof EventPacketSent) {
			final EventPacketSent sent = (EventPacketSent) event;
			if (sent.getPacket() instanceof C03PacketPlayer) {
				final C03PacketPlayer player = (C03PacketPlayer) sent
						.getPacket();
				player.field_149479_a = x;
				player.field_149477_b = minY;
				player.field_149475_d = y;
				player.field_149478_c = z;
				player.field_149476_e = yaw;
				player.field_149473_f = pitch;
			}
		}
	}

}
