package down.TeamBattle.Modules.Modules;

import net.minecraft.network.play.client.C0BPacketEntityAction;

import org.lwjgl.input.Keyboard;

import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventPostSendMotionUpdates;
import down.TeamBattle.EventSystem.events.EventPreSendMotionUpdates;
import down.TeamBattle.Modules.ModuleBase;

public final class Sneak extends ModuleBase {
	public Sneak() {
		super("Sneak", Keyboard.KEY_Z, 0xFF049B69);
	}

	@Override
	public void onDisabled() {
		super.onDisabled();
		if (mc.thePlayer != null && !mc.thePlayer.isSneaking()) {
			mc.getNetHandler().addToSendQueue(
					new C0BPacketEntityAction(mc.thePlayer, 2));
		}
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPreSendMotionUpdates) {
			if (mc.thePlayer.motionX != 0 && mc.thePlayer.motionY != 0
					&& mc.thePlayer.motionZ != 0 && !mc.thePlayer.isSneaking()) {
				mc.getNetHandler().addToSendQueue(
						new C0BPacketEntityAction(mc.thePlayer, 1));
				mc.getNetHandler().addToSendQueue(
						new C0BPacketEntityAction(mc.thePlayer, 2));
			}
		} else if (event instanceof EventPostSendMotionUpdates) {
			if (!mc.thePlayer.isSneaking()) {
				mc.getNetHandler().addToSendQueue(
						new C0BPacketEntityAction(mc.thePlayer, 2));
				mc.getNetHandler().addToSendQueue(
						new C0BPacketEntityAction(mc.thePlayer, 1));
			}
		}
	}
}
