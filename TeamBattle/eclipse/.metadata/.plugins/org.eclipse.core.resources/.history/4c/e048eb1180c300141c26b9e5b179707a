package me.client.events;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;

import com.darkmagician6.eventapi.types.EventType;

public class EventBoundingBox extends EventType {
	private Block block;
	private AxisAlignedBB boundingBox;
	private int x;
	private int y;
	private int z;

	public void EventBlockBoundingBox(AxisAlignedBB boundingBox, Block block, int x,int y, int z) {
		this.boundingBox = boundingBox;
		this.block = block;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Block getBlock() {
		return block;
	}

	public AxisAlignedBB getBoundingBox() {
		return boundingBox;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public void setBoundingBox(AxisAlignedBB boundingBox) {
		this.boundingBox = boundingBox;
	}
}

