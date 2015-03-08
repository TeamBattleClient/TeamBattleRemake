package down.TeamBattle.Modules.Modules;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.MathHelper;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventAttack;
import down.TeamBattle.EventSystem.events.EventBlockBreaking;
import down.TeamBattle.EventSystem.events.EventBlockClicked;
import down.TeamBattle.EventSystem.events.EventPreSendMotionUpdates;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.BlockHelper;

public class CivDig extends ModuleBase {

	public CivDig() {
		super("Dig", 0x55ff55ff);
		
	}

	@Override
	public void onEvent(Event event) {
		if(event instanceof EventBlockClicked)
		{
			final EventBlockClicked clicked = (EventBlockClicked)event;
			Block block;
			
			mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(0, clicked.getX(), clicked.getY(), clicked.getZ(),clicked.getSide()));
			mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(2, clicked.getX(), clicked.getY(), clicked.getZ(),clicked.getSide()));
			this.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(clicked.getX(), clicked.getY(), clicked.getZ(), -1, this.mc.thePlayer.getCurrentEquippedItem(), 0.0F, 0.0F, 0.0F));
		}else if(event instanceof EventBlockBreaking)
		{
			final EventBlockBreaking dmg = (EventBlockBreaking)event;
			dmg.setDamage(999.0F);
			dmg.setDelay(0);
		}
		
	}
	public static void faceBlock(Block block)
	  {
	    double x = block.getBlockBoundsMinX() + 0.5D - mc.thePlayer.posX;
	    double z = block.getBlockBoundsMinY() + 0.5D - mc.thePlayer.posZ;
	    double y = block.getBlockBoundsMinZ() + 0.5D - mc.thePlayer.posY;
	    double helper = MathHelper.sqrt_double(x * x + z * z);

	    float newYaw = (float)Math.toDegrees(-Math.atan(x / z));
	    float newPitch = (float)(-Math.toDegrees(Math.atan(y / helper)));

	    if ((z < 0.0D) && (x < 0.0D)) newYaw = (float)(90.0D + Math.toDegrees(Math.atan(z / x)));
	    else if ((z < 0.0D) && (x > 0.0D)) newYaw = (float)(-90.0D + Math.toDegrees(Math.atan(z / x)));

	    mc.thePlayer.rotationYaw = newYaw;
	    mc.thePlayer.rotationPitch = newPitch;
	    mc.thePlayer.rotationYawHead = newPitch;
	  }
	

}
