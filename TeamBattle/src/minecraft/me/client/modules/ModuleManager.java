package me.client.modules;

import java.util.ArrayList;

import me.client.modules.movement.*;

public class ModuleManager {

	public static ArrayList<Module> hacks = new ArrayList<Module>();
	
	public ModuleManager() {
		hacks.clear();
		hacks.add(new Flight());
		hacks.add(new Jesus());
		hacks.add(new FastIce());
		hacks.add(new Sprint());
		hacks.add(new Glide());
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
