package me.client.modules.movement;

import me.client.events.EventPreMotion;
import me.client.modules.Module;
import me.client.modules.ModuleCategory;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

public class Flight extends Module{

	public Flight() {
		super("Flight", "Flight (Notch)", ModuleCategory.MOVEMENT);
		this.setKeybind(Keyboard.KEY_F);
	}
	
	@Override
	public void onEnable() {
		EventManager.register(this);
	}
	
	@Override
	public void onDisable() {
		EventManager.unregister(this);
		mc.thePlayer.capabilities.isFlying = false;
	}
	
	@EventTarget
	public void onPreMotionEvent(EventPreMotion event) {
		event.player.capabilities.isFlying = true;
	}

}
