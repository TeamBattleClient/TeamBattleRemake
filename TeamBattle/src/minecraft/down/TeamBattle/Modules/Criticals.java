package down.TeamBattle.Modules;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventAttack;
import down.TeamBattle.EventSystem.events.EventPacketSent;
import down.TeamBattle.EventSystem.events.EventPostSendMotionUpdates;
import down.TeamBattle.EventSystem.events.EventPreSendMotionUpdates;
import down.TeamBattle.EventSystem.events.EventTick;

public class Criticals extends ModuleBase{

	public Criticals() {
		super("Crits", 0x27123FF);
		
	}

	@Override
	public void onEvent(Event event) {
		if(event instanceof EventPacketSent)
		{
			final EventPacketSent sent = (EventPacketSent)event;
			if(sent.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition)
			{
				  final C03PacketPlayer.C04PacketPlayerPosition c04PacketPlayerPosition = (C03PacketPlayer.C04PacketPlayerPosition)sent.getPacket();
            return;
			}
			 if (sent.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
		            final C03PacketPlayer.C06PacketPlayerPosLook c06PacketPlayerPosLook = (C03PacketPlayer.C06PacketPlayerPosLook)sent.getPacket();
        }
		
       
        }else if(event instanceof EventAttack)
        {
        	final EventAttack jerk = (EventAttack)event;
        	if(mc.thePlayer.onGround)
        	{
    			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(X(), YY() + 1.0E-14, Y() + 1.0E-14, Z(), false));
    			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(X(), YY(), Y(), Z(), false));
    		}
        	jerk.setCancelled(true);
        }
			
		}
	
		
	}

	
