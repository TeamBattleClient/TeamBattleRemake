package me.client.modules.movement;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;

import me.client.events.EventBoundingBox;
import me.client.events.EventPacketSent;
import me.client.events.EventPreMotion;
import me.client.helpers.BlockHelper;
import me.client.modules.Module;
import me.client.modules.ModuleCategory;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public class Jesus extends Module {
static String modName = "Jesus";
static String listName = "Jesus (NCP+ Bypasses)";
static ModuleCategory category = ModuleCategory.MOVEMENT;
	public Jesus() {
		super(modName, listName, category);
		this.setKeybind(Keyboard.KEY_J);
	}
	private boolean nextTick;
	
	public void onBoundingBoxEvent(EventBoundingBox event) {
	
	 
			if (mc.thePlayer == null)
				return;
			final EventBoundingBox block = (EventBoundingBox) event;
			if (block.getBlock() instanceof BlockLiquid
					&& !BlockHelper.isInLiquid()
					&& mc.thePlayer.fallDistance < 3.0F
					&& !mc.thePlayer.isSneaking()) {
				block.setBoundingBox(AxisAlignedBB.getBoundingBox(block.getX(),
						block.getY(), block.getZ(), block.getX() + 1,
						block.getY() + 1, block.getZ() + 1));
			
		}

}
	
public void onEventPreMotion(EventPreMotion event)
{
	if (BlockHelper.isInLiquid()
			&& mc.thePlayer.isInsideOfMaterial(Material.air)
			&& !mc.thePlayer.isSneaking()) {
		mc.thePlayer.motionY = 0.08;
	}
}
	
public void onEventPacketSent(EventPacketSent event)
{
	final EventPacketSent sent = (EventPacketSent) event;
	if (sent.getPacket() instanceof C03PacketPlayer) {
		final C03PacketPlayer player = (C03PacketPlayer) sent
				.getPacket();
		if (BlockHelper.isOnLiquid()) {
			nextTick = !nextTick;
			if (nextTick) {
				player.field_149477_b -= 0.01;
				player.field_149475_d -= 0.01;
			}
		}
	}
	
}



}