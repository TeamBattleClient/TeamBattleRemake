package me.client.modules;

import java.util.ArrayList;

import me.client.modules.movement.*;
import me.client.modules.player.Sneak;

public class ModuleManager {

	public static ArrayList<Module> hacks = new ArrayList<Module>();
	
	public ModuleManager() {
		/*this.hacks.add(new Jesus());
		this.hacks.add(new FastIce());
		this.hacks.add(new Sprint());
		this.hacks.add(new Flight());
		this.hacks.add(new Glide());
		this.hacks.add(new Dolphin());
		this.hacks.add(new Speed());*/
		hacks.add(new Speed());
		hacks.add(new Flight());
		hacks.add(new Dolphin());
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
