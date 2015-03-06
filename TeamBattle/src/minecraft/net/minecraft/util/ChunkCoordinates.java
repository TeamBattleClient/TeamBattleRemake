package net.minecraft.util;

public class ChunkCoordinates implements Comparable {
	public int posX;

	/** the y coordinate */
	public int posY;

	/** the z coordinate */
	public int posZ;

	public ChunkCoordinates() {
	}

	public ChunkCoordinates(ChunkCoordinates p_i1355_1_) {
		posX = p_i1355_1_.posX;
		posY = p_i1355_1_.posY;
		posZ = p_i1355_1_.posZ;
	}

	public ChunkCoordinates(int p_i1354_1_, int p_i1354_2_, int p_i1354_3_) {
		posX = p_i1354_1_;
		posY = p_i1354_2_;
		posZ = p_i1354_3_;
	}

	public int compareTo(ChunkCoordinates p_compareTo_1_) {
		return posY == p_compareTo_1_.posY ? posZ == p_compareTo_1_.posZ ? posX
				- p_compareTo_1_.posX : posZ - p_compareTo_1_.posZ : posY
				- p_compareTo_1_.posY;
	}

	@Override
	public int compareTo(Object p_compareTo_1_) {
		return this.compareTo((ChunkCoordinates) p_compareTo_1_);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (!(p_equals_1_ instanceof ChunkCoordinates))
			return false;
		else {
			final ChunkCoordinates var2 = (ChunkCoordinates) p_equals_1_;
			return posX == var2.posX && posY == var2.posY && posZ == var2.posZ;
		}
	}

	/**
	 * Returns the squared distance between this coordinates and the coordinates
	 * given as argument.
	 */
	public float getDistanceSquared(int p_71569_1_, int p_71569_2_,
			int p_71569_3_) {
		final float var4 = posX - p_71569_1_;
		final float var5 = posY - p_71569_2_;
		final float var6 = posZ - p_71569_3_;
		return var4 * var4 + var5 * var5 + var6 * var6;
	}

	/**
	 * Return the squared distance between this coordinates and the
	 * ChunkCoordinates given as argument.
	 */
	public float getDistanceSquaredToChunkCoordinates(
			ChunkCoordinates p_82371_1_) {
		return getDistanceSquared(p_82371_1_.posX, p_82371_1_.posY,
				p_82371_1_.posZ);
	}

	@Override
	public int hashCode() {
		return posX + posZ << 8 + posY << 16;
	}

	public void set(int p_71571_1_, int p_71571_2_, int p_71571_3_) {
		posX = p_71571_1_;
		posY = p_71571_2_;
		posZ = p_71571_3_;
	}

	@Override
	public String toString() {
		return "Pos{x=" + posX + ", y=" + posY + ", z=" + posZ + '}';
	}
}
