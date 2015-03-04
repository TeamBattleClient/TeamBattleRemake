package me.client.modules.movement;

import me.client.events.EventUpdate;
import me.client.helpers.BlockHelper;
import me.client.modules.Module;
import me.client.modules.ModuleCategory;
import net.minecraft.client.entity.EntityClientPlayerMP;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;

public class Dolphin extends Module{

	public Dolphin() {
		super("Dolphin", "Dolphin", ModuleCategory.MOVEMENT);
		setKeybind(Keyboard.KEY_O);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(BlockHelper.isInLiquid()) {
			event.player.motionY += 0.05;
		}
	}
}
