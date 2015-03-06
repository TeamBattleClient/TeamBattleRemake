package me.client.modules.movement;

import me.client.events.EventPreMotion;
import me.client.modules.Module;
import me.client.modules.ModuleCategory;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;

public class Sprint extends Module {
	
	public Sprint() {
		super("Sprint", "Sprint", ModuleCategory.MOVEMENT);	
		this.setKeybind(Keyboard.KEY_C);
	}
	
	@EventTarget
	public void onPreMotion(EventPreMotion event) {
		if(SprintChecks()) {
			mc.thePlayer.setSprinting(true);
		}
		else{
			mc.thePlayer.setSprinting(false);
		}
	}
	
	boolean check = mc.thePlayer.getFoodStats().getFoodLevel() > 6F && !mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isDead;
	
	public boolean SprintChecks(){
		return check;
	}

}
