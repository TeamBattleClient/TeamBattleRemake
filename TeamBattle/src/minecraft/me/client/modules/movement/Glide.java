package me.client.modules.movement;

import org.lwjgl.input.Keyboard;

import me.client.events.EventPreMotion;
import me.client.modules.Module;
import me.client.modules.ModuleCategory;

public class Glide extends Module {
	static String modName = "Glide";
	static String listName = "Glide (NCP+ Bypasses)";
	static ModuleCategory category = ModuleCategory.MOVEMENT;
	public Glide() {
		super(modName, listName, category);
		this.setKeybind(Keyboard.KEY_G);
	}
	public void onPreSkidMotion(EventPreMotion event) {
		
		 {
			 if(Keyboard.isKeyDown(57)){
				 mc.thePlayer.motionY =+ 5.0F;
	
			 
		 }else if (Keyboard.isKeyDown(56)){
			 
			 mc.thePlayer.motionY -= 5.0F;
		 }
			 
if(!mc.thePlayer.isInWater() && mc.thePlayer.isAirBorne){ 
	
	mc.thePlayer.motionY *= 0.08;
	
}}}}
