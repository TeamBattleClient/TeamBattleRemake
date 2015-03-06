package net.minecraft.world.chunk;

public class NibbleArray {
	/**
	 * Byte array of data stored in this holder. Possibly a light map or some
	 * chunk data. Data is accessed in 4-bit pieces.
	 */
	public final byte[] data;

	/**
	 * Log base 2 of the chunk height (128); applied as a shift on Z coordinate
	 */
	private final int depthBits;

	/**
	 * Log base 2 of the chunk height (128) * width (16); applied as a shift on
	 * X coordinate
	 */
	private final int depthBitsPlusFour;

	public NibbleArray(byte[] p_i1993_1_, int p_i1993_2_) {
		data = p_i1993_1_;
		depthBits = p_i1993_2_;
		depthBitsPlusFour = p_i1993_2_ + 4;
	}

	public NibbleArray(int p_i1992_1_, int p_i1992_2_) {
		data = new byte[p_i1992_1_ >> 1];
		depthBits = p_i1992_2_;
		depthBitsPlusFour = p_i1992_2_ + 4;
	}

	/**
	 * Returns the nibble of data corresponding to the passed in x, y, z. y is
	 * at most 6 bits, z is at most 4.
	 */
	public int get(int p_76582_1_, int p_76582_2_, int p_76582_3_) {
		final int var4 = p_76582_2_ << depthBitsPlusFour
				| p_76582_3_ << depthBits | p_76582_1_;
		final int var5 = var4 >> 1;
		final int var6 = var4 & 1;
		return var6 == 0 ? data[var5] & 15 : data[var5] >> 4 & 15;
	}

	/**
	 * Arguments are x, y, z, val. Sets the nibble of data at x << 11 | z << 7 |
	 * y to val.
	 */
	public void set(int p_76581_1_, int p_76581_2_, int p_76581_3_,
			int p_76581_4_) {
		final int var5 = p_76581_2_ << depthBitsPlusFour
				| p_76581_3_ << depthBits | p_76581_1_;
		final int var6 = var5 >> 1;
		final int var7 = var5 & 1;

		if (var7 == 0) {
			data[var6] = (byte) (data[var6] & 240 | p_76581_4_ & 15);
		} else {
			data[var6] = (byte) (data[var6] & 15 | (p_76581_4_ & 15) << 4);
		}
	}
}
