package down.TeamBattle.Modules.Modules;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventLocation;
import down.TeamBattle.EventSystem.events.EventPreSendMotionUpdates;
import down.TeamBattle.EventSystem.events.EventTick;
import down.TeamBattle.Modules.ModuleBase;

public class FastFall extends ModuleBase {

	public FastFall() {
		super("FastFall", 0xFFC63E);
	
	}

	@Override
	public void onEvent(Event event) {
		if(event instanceof EventPreSendMotionUpdates)
			
		{
			final GutZ lol= new GutZ();
			if(this.IsFalling() && !TeamBattleClient.getModManager().getContents().contains(lol))
			mc.thePlayer.motionY *=6F;
		}
		
	}
	public boolean IsFalling(){
		return !mc.thePlayer.onGround && mc.thePlayer.fallDistance > 0.001F;
	}

}
