package net.minecraft.world;

import net.minecraft.block.Block;

public class NextTickListEntry implements Comparable {
	/** The id number for the next tick entry */
	private static long nextTickEntryID;
	private final Block field_151352_g;

	public int priority;

	/** Time this tick is scheduled to occur at */
	public long scheduledTime;

	/** The id of the tick entry */
	private final long tickEntryID;

	/** X position this tick is occuring at */
	public int xCoord;
	/** Y position this tick is occuring at */
	public int yCoord;

	/** Z position this tick is occuring at */
	public int zCoord;

	public NextTickListEntry(int p_i45370_1_, int p_i45370_2_, int p_i45370_3_,
			Block p_i45370_4_) {
		tickEntryID = nextTickEntryID++;
		xCoord = p_i45370_1_;
		yCoord = p_i45370_2_;
		zCoord = p_i45370_3_;
		field_151352_g = p_i45370_4_;
	}

	public int compareTo(NextTickListEntry p_compareTo_1_) {
		return scheduledTime < p_compareTo_1_.scheduledTime ? -1
				: scheduledTime > p_compareTo_1_.scheduledTime ? 1
						: priority != p_compareTo_1_.priority ? priority
								- p_compareTo_1_.priority
								: tickEntryID < p_compareTo_1_.tickEntryID ? -1
										: tickEntryID > p_compareTo_1_.tickEntryID ? 1
												: 0;
	}

	@Override
	public int compareTo(Object p_compareTo_1_) {
		return this.compareTo((NextTickListEntry) p_compareTo_1_);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (!(p_equals_1_ instanceof NextTickListEntry))
			return false;
		else {
			final NextTickListEntry var2 = (NextTickListEntry) p_equals_1_;
			return xCoord == var2.xCoord && yCoord == var2.yCoord
					&& zCoord == var2.zCoord
					&& Block.isEqualTo(field_151352_g, var2.field_151352_g);
		}
	}

	public Block func_151351_a() {
		return field_151352_g;
	}

	@Override
	public int hashCode() {
		return (xCoord * 1024 * 1024 + zCoord * 1024 + yCoord) * 256;
	}

	public void setPriority(int p_82753_1_) {
		priority = p_82753_1_;
	}

	/**
	 * Sets the scheduled time for this tick entry
	 */
	public NextTickListEntry setScheduledTime(long p_77176_1_) {
		scheduledTime = p_77176_1_;
		return this;
	}

	@Override
	public String toString() {
		return Block.getIdFromBlock(field_151352_g) + ": (" + xCoord + ", "
				+ yCoord + ", " + zCoord + "), " + scheduledTime + ", "
				+ priority + ", " + tickEntryID;
	}
}
