package me.client.modules.movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.init.Blocks;
import me.client.events.EventPreMotion;
import me.client.modules.Module;
import me.client.modules.ModuleCategory;

public class FastIce extends Module {
	static String modName = "FastIce";
	static String listName = "FastIce (NCP+ Bypasses)";
	static ModuleCategory category = ModuleCategory.MOVEMENT;
	public FastIce() {
		super(modName, listName, category);
		this.setKeybind(Keyboard.KEY_O);
	}

public void onEnable(){
	
	Blocks.ice.slipperiness =0.400F;
	Blocks.packed_ice.slipperiness =0.400F;
}
public void onDisable(){
	
	Blocks.ice.slipperiness =0.98F;
	Blocks.packed_ice.slipperiness =0.98F;
}
}
