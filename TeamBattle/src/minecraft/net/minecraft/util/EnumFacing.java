package net.minecraft.util;

public enum EnumFacing {
	DOWN(0, 1, 0, -1, 0), EAST(4, 5, -1, 0, 0), NORTH(2, 3, 0, 0, -1), SOUTH(3,
			2, 0, 0, 1), UP(1, 0, 0, 1, 0), WEST(5, 4, 1, 0, 0);

	/** List of all values in EnumFacing. Order is D-U-N-S-E-W. */
	private static final EnumFacing[] faceList = new EnumFacing[6];

	static {
		final EnumFacing[] var0 = values();
		final int var1 = var0.length;

		for (int var2 = 0; var2 < var1; ++var2) {
			final EnumFacing var3 = var0[var2];
			faceList[var3.order_a] = var3;
		}
	}

	/**
	 * Returns the facing that represents the block in front of it.
	 */
	public static EnumFacing getFront(int p_82600_0_) {
		return faceList[p_82600_0_ % faceList.length];
	}

	private final int frontOffsetX;

	private final int frontOffsetY;

	private final int frontOffsetZ;

	/** Face order for D-U-N-S-E-W. */
	private final int order_a;

	private EnumFacing(int p_i1367_3_, int p_i1367_4_, int p_i1367_5_,
			int p_i1367_6_, int p_i1367_7_) {
		order_a = p_i1367_3_;
		frontOffsetX = p_i1367_5_;
		frontOffsetY = p_i1367_6_;
		frontOffsetZ = p_i1367_7_;
	}

	/**
	 * Returns a offset that addresses the block in front of this facing.
	 */
	public int getFrontOffsetX() {
		return frontOffsetX;
	}

	public int getFrontOffsetY() {
		return frontOffsetY;
	}

	/**
	 * Returns a offset that addresses the block in front of this facing.
	 */
	public int getFrontOffsetZ() {
		return frontOffsetZ;
	}
}
