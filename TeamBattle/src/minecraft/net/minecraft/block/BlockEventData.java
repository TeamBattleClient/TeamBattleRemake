package net.minecraft.block;

public class BlockEventData {
	private final int coordX;
	private final int coordY;
	private final int coordZ;
	/** Different for each blockID */
	private final int eventID;

	private final int eventParameter;
	private final Block field_151344_d;

	public BlockEventData(int p_i45362_1_, int p_i45362_2_, int p_i45362_3_,
			Block p_i45362_4_, int p_i45362_5_, int p_i45362_6_) {
		coordX = p_i45362_1_;
		coordY = p_i45362_2_;
		coordZ = p_i45362_3_;
		eventID = p_i45362_5_;
		eventParameter = p_i45362_6_;
		field_151344_d = p_i45362_4_;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (!(p_equals_1_ instanceof BlockEventData))
			return false;
		else {
			final BlockEventData var2 = (BlockEventData) p_equals_1_;
			return coordX == var2.coordX && coordY == var2.coordY
					&& coordZ == var2.coordZ && eventID == var2.eventID
					&& eventParameter == var2.eventParameter
					&& field_151344_d == var2.field_151344_d;
		}
	}

	public int func_151340_a() {
		return coordX;
	}

	public int func_151341_c() {
		return coordZ;
	}

	public int func_151342_b() {
		return coordY;
	}

	public Block getBlock() {
		return field_151344_d;
	}

	/**
	 * Get the Event ID (different for each BlockID)
	 */
	public int getEventID() {
		return eventID;
	}

	public int getEventParameter() {
		return eventParameter;
	}

	@Override
	public String toString() {
		return "TE(" + coordX + "," + coordY + "," + coordZ + ")," + eventID
				+ "," + eventParameter + "," + field_151344_d;
	}
}
