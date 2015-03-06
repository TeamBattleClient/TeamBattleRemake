package me.client.modules;

import java.util.ArrayList;

import me.client.modules.movement.*;
import me.client.modules.player.Sneak;

public class ModuleManager {

	public static ArrayList<Module> hacks = new ArrayList<Module>();
	
	public ModuleManager() {
		hacks.clear();
		hacks.add(new Dolphin());
		hacks.add(new Flight());
		hacks.add(new Glide());
		hacks.add(new Jesus());
		hacks.add(new Sneak());
		hacks.add(new FastIce());
		hacks.add(new Speed());
		hacks.add(new Sprint());
	}
	
	public static Module getModName(String modName) {
		for(Module module : hacks) {
			if(module.getName() == modName) {
				return module;
			}
		}
		
		return null;
	}
	
	public static Module findMod(Class < ? extends Module > clazz) {
		for(Module module : hacks) {
			if(module.getClass() == clazz) {
				return module;
			}
		}
		
		return null;
	}
	
}
