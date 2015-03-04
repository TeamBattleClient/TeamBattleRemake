package me.client.modules.movement;

import org.lwjgl.input.Keyboard;

import me.client.modules.Module;
import me.client.modules.ModuleCategory;

public class Speed extends Module {
	static String modName = "FastIce";
	static String listName = "FastIce (NCP+ Bypasses)";
	static ModuleCategory category = ModuleCategory.MOVEMENT;
	public Speed() {
		super(modName, listName, category);
		this.setKeybind(Keyboard.KEY_SEMICOLON);
	}
	/*
	 * Comment : We gonna make this later as this is going to be a hard process.
	 */

}
