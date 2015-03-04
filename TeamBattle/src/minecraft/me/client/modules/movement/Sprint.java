package me.client.modules.movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import me.client.modules.Module;
import me.client.modules.ModuleCategory;

public class Sprint extends Module {
	static String modName = "Sprint";
	static String listName = "Sprint (NCP+ Bypasses)";
	static ModuleCategory category = ModuleCategory.MOVEMENT;
	public Sprint() {
		super(modName, listName, category);	
		this.setKeybind(Keyboard.KEY_C);
	}
	public void onPreMotion()
	{
		if(SprintChecks()){
			mc.thePlayer.setSprinting(true);
		}else
		{
			mc.thePlayer.setSprinting(false);
		}
	}
	boolean check = mc.thePlayer.getFoodStats().getFoodLevel() > 6F && !mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isDead;
	public boolean SprintChecks(){
		return check;
		
	}

}
