package down.TeamBattle.Modules.Modules;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.input.Keyboard;

import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventBlockBoundingBox;
import down.TeamBattle.EventSystem.events.EventPacketSent;
import down.TeamBattle.EventSystem.events.EventPreSendMotionUpdates;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.BlockHelper;

public final class Jesus extends ModuleBase {
	private boolean nextTick;

	public Jesus() {
		super("Jesus", Keyboard.KEY_J, 0xFF89BAD3);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventBlockBoundingBox) {
			if (mc.thePlayer == null)
				return;
			final EventBlockBoundingBox block = (EventBlockBoundingBox) event;
			if (block.getBlock() instanceof BlockLiquid
					&& !BlockHelper.isInLiquid()
					&& mc.thePlayer.fallDistance < 3.0F
					&& !mc.thePlayer.isSneaking()) {
				block.setBoundingBox(AxisAlignedBB.getBoundingBox(block.getX(),
						block.getY(), block.getZ(), block.getX() + 1,
						block.getY() + 1, block.getZ() + 1));
			}
		} else if (event instanceof EventPreSendMotionUpdates) {
			if (BlockHelper.isInLiquid()
					&& mc.thePlayer.isInsideOfMaterial(Material.air)
					&& !mc.thePlayer.isSneaking()) {
				mc.thePlayer.motionY = 0.08;
			}
		} else if (event instanceof EventPacketSent) {
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

}
