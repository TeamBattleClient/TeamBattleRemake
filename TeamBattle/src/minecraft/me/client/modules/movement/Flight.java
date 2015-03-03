package me.client.modules.movement;

import me.client.events.EventPreMotion;
import me.client.modules.Module;
import me.client.modules.ModuleCategory;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;

public class Flight extends Module{

	public Flight() {
		super("Flight", "Flight (Notch)", ModuleCategory.MOVEMENT);
		this.setKeybind(Keyboard.KEY_F);
	}
	
	@EventTarget
	public void onPreMotionEvent(EventPreMotion event) {
		event.player.capabilities.isFlying = true;
	}
	
	@Override
	public void onDisable() {
		mc.thePlayer.capabilities.isFlying = false;
	}

}
