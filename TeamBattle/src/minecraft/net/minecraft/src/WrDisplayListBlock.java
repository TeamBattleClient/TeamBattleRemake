package net.minecraft.src;

import net.minecraft.client.renderer.GLAllocation;

public class WrDisplayListBlock {
	public static final int BLOCK_LENGTH = 16384;
	private int end = -1;
	private int start = -1;
	private int used = -1;

	public WrDisplayListBlock() {
		start = GLAllocation.generateDisplayLists(16384);
		used = start;
		end = start + 16384;
	}

	public int allocate(int len) {
		if (!canAllocate(len))
			return -1;
		else {
			final int allocated = used;
			used += len;
			return allocated;
		}
	}

	public boolean canAllocate(int len) {
		return used + len < end;
	}

	public void deleteDisplayLists() {
		GLAllocation.deleteDisplayLists(start);
	}

	public int getEnd() {
		return end;
	}

	public int getStart() {
		return start;
	}

	public int getUsed() {
		return used;
	}

	public void reset() {
		used = start;
	}
}
