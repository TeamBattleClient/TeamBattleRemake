package down.TeamBattle.Modules.Modules;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventPreSendMotionUpdates;
import down.TeamBattle.Modules.ModuleBase;

public class FlyBypass extends ModuleBase{

	public FlyBypass() {
		super("FLyBypass", 0x55FF99FF);
		
	}

	@Override
	public void onEvent(Event event) {
		if(event instanceof EventPreSendMotionUpdates)
		{
			

		}
		
	}
	 public boolean isairborne(){
		 
		 Block block = mc.theWorld.getBlock((int)mc.thePlayer.posX, (int)(mc.thePlayer.posY - 0.01), (int)mc.thePlayer.posZ);
		 return block instanceof BlockAir && !block.isCollidable();
	 }

}
