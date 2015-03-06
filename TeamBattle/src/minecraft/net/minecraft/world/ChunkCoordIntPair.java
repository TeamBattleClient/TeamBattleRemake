package net.minecraft.world;

public class ChunkCoordIntPair {
	/**
	 * converts a chunk coordinate pair to an integer (suitable for hashing)
	 */
	public static long chunkXZ2Int(int par0, int par1) {
		return par0 & 4294967295L | (par1 & 4294967295L) << 32;
	}

	private int cachedHashCode = 0;

	/** The X position of this Chunk Coordinate Pair */
	public final int chunkXPos;

	/** The Z position of this Chunk Coordinate Pair */
	public final int chunkZPos;

	public ChunkCoordIntPair(int par1, int par2) {
		chunkXPos = par1;
		chunkZPos = par2;
	}

	@Override
	public boolean equals(Object par1Obj) {
		if (this == par1Obj)
			return true;
		else if (!(par1Obj instanceof ChunkCoordIntPair))
			return false;
		else {
			final ChunkCoordIntPair var2 = (ChunkCoordIntPair) par1Obj;
			return chunkXPos == var2.chunkXPos && chunkZPos == var2.chunkZPos;
		}
	}

	public ChunkPosition func_151349_a(int p_151349_1_) {
		return new ChunkPosition(getCenterXPos(), p_151349_1_,
				getCenterZPosition());
	}

	public int getCenterXPos() {
		return (chunkXPos << 4) + 8;
	}

	public int getCenterZPosition() {
		return (chunkZPos << 4) + 8;
	}

	@Override
	public int hashCode() {
		if (cachedHashCode == 0) {
			final int var1 = 1664525 * chunkXPos + 1013904223;
			final int var2 = 1664525 * (chunkZPos ^ -559038737) + 1013904223;
			cachedHashCode = var1 ^ var2;
		}

		return cachedHashCode;
	}

	@Override
	public String toString() {
		return "[" + chunkXPos + ", " + chunkZPos + "]";
	}
}
