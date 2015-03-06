package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTTagIntArray;

public class StructureBoundingBox {
	/**
	 * used to project a possible new component Bounding Box - to check if it
	 * would cut anything already spawned
	 */
	public static StructureBoundingBox getComponentToAddBoundingBox(
			int p_78889_0_, int p_78889_1_, int p_78889_2_, int p_78889_3_,
			int p_78889_4_, int p_78889_5_, int p_78889_6_, int p_78889_7_,
			int p_78889_8_, int p_78889_9_) {
		switch (p_78889_9_) {
		case 0:
			return new StructureBoundingBox(p_78889_0_ + p_78889_3_, p_78889_1_
					+ p_78889_4_, p_78889_2_ + p_78889_5_, p_78889_0_
					+ p_78889_6_ - 1 + p_78889_3_, p_78889_1_ + p_78889_7_ - 1
					+ p_78889_4_, p_78889_2_ + p_78889_8_ - 1 + p_78889_5_);

		case 1:
			return new StructureBoundingBox(p_78889_0_ - p_78889_8_ + 1
					+ p_78889_5_, p_78889_1_ + p_78889_4_, p_78889_2_
					+ p_78889_3_, p_78889_0_ + p_78889_5_, p_78889_1_
					+ p_78889_7_ - 1 + p_78889_4_, p_78889_2_ + p_78889_6_ - 1
					+ p_78889_3_);

		case 2:
			return new StructureBoundingBox(p_78889_0_ + p_78889_3_, p_78889_1_
					+ p_78889_4_, p_78889_2_ - p_78889_8_ + 1 + p_78889_5_,
					p_78889_0_ + p_78889_6_ - 1 + p_78889_3_, p_78889_1_
							+ p_78889_7_ - 1 + p_78889_4_, p_78889_2_
							+ p_78889_5_);

		case 3:
			return new StructureBoundingBox(p_78889_0_ + p_78889_5_, p_78889_1_
					+ p_78889_4_, p_78889_2_ + p_78889_3_, p_78889_0_
					+ p_78889_8_ - 1 + p_78889_5_, p_78889_1_ + p_78889_7_ - 1
					+ p_78889_4_, p_78889_2_ + p_78889_6_ - 1 + p_78889_3_);

		default:
			return new StructureBoundingBox(p_78889_0_ + p_78889_3_, p_78889_1_
					+ p_78889_4_, p_78889_2_ + p_78889_5_, p_78889_0_
					+ p_78889_6_ - 1 + p_78889_3_, p_78889_1_ + p_78889_7_ - 1
					+ p_78889_4_, p_78889_2_ + p_78889_8_ - 1 + p_78889_5_);
		}
	}

	/**
	 * returns a new StructureBoundingBox with MAX values
	 */
	public static StructureBoundingBox getNewBoundingBox() {
		return new StructureBoundingBox(Integer.MAX_VALUE, Integer.MAX_VALUE,
				Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE,
				Integer.MIN_VALUE);
	}

	/** The second x coordinate of a bounding box. */
	public int maxX;

	/** The second y coordinate of a bounding box. */
	public int maxY;

	/** The second z coordinate of a bounding box. */
	public int maxZ;

	/** The first x coordinate of a bounding box. */
	public int minX;

	/** The first y coordinate of a bounding box. */
	public int minY;

	/** The first z coordinate of a bounding box. */
	public int minZ;

	public StructureBoundingBox() {
	}

	public StructureBoundingBox(int p_i2033_1_, int p_i2033_2_, int p_i2033_3_,
			int p_i2033_4_) {
		minX = p_i2033_1_;
		minZ = p_i2033_2_;
		maxX = p_i2033_3_;
		maxZ = p_i2033_4_;
		minY = 1;
		maxY = 512;
	}

	public StructureBoundingBox(int p_i2032_1_, int p_i2032_2_, int p_i2032_3_,
			int p_i2032_4_, int p_i2032_5_, int p_i2032_6_) {
		minX = p_i2032_1_;
		minY = p_i2032_2_;
		minZ = p_i2032_3_;
		maxX = p_i2032_4_;
		maxY = p_i2032_5_;
		maxZ = p_i2032_6_;
	}

	public StructureBoundingBox(int[] p_i43000_1_) {
		if (p_i43000_1_.length == 6) {
			minX = p_i43000_1_[0];
			minY = p_i43000_1_[1];
			minZ = p_i43000_1_[2];
			maxX = p_i43000_1_[3];
			maxY = p_i43000_1_[4];
			maxZ = p_i43000_1_[5];
		}
	}

	public StructureBoundingBox(StructureBoundingBox p_i2031_1_) {
		minX = p_i2031_1_.minX;
		minY = p_i2031_1_.minY;
		minZ = p_i2031_1_.minZ;
		maxX = p_i2031_1_.maxX;
		maxY = p_i2031_1_.maxY;
		maxZ = p_i2031_1_.maxZ;
	}

	/**
	 * Expands a bounding box's dimensions to include the supplied bounding box.
	 */
	public void expandTo(StructureBoundingBox p_78888_1_) {
		minX = Math.min(minX, p_78888_1_.minX);
		minY = Math.min(minY, p_78888_1_.minY);
		minZ = Math.min(minZ, p_78888_1_.minZ);
		maxX = Math.max(maxX, p_78888_1_.maxX);
		maxY = Math.max(maxY, p_78888_1_.maxY);
		maxZ = Math.max(maxZ, p_78888_1_.maxZ);
	}

	public NBTTagIntArray func_151535_h() {
		return new NBTTagIntArray(new int[] { minX, minY, minZ, maxX, maxY,
				maxZ });
	}

	public int getCenterX() {
		return minX + (maxX - minX + 1) / 2;
	}

	public int getCenterY() {
		return minY + (maxY - minY + 1) / 2;
	}

	public int getCenterZ() {
		return minZ + (maxZ - minZ + 1) / 2;
	}

	/**
	 * Returns width of a bounding box
	 */
	public int getXSize() {
		return maxX - minX + 1;
	}

	/**
	 * Returns height of a bounding box
	 */
	public int getYSize() {
		return maxY - minY + 1;
	}

	/**
	 * Returns length of a bounding box
	 */
	public int getZSize() {
		return maxZ - minZ + 1;
	}

	/**
	 * Discover if a coordinate is inside the bounding box area.
	 */
	public boolean intersectsWith(int p_78885_1_, int p_78885_2_,
			int p_78885_3_, int p_78885_4_) {
		return maxX >= p_78885_1_ && minX <= p_78885_3_ && maxZ >= p_78885_2_
				&& minZ <= p_78885_4_;
	}

	/**
	 * Returns whether the given bounding box intersects with this one. Args:
	 * structureboundingbox
	 */
	public boolean intersectsWith(StructureBoundingBox p_78884_1_) {
		return maxX >= p_78884_1_.minX && minX <= p_78884_1_.maxX
				&& maxZ >= p_78884_1_.minZ && minZ <= p_78884_1_.maxZ
				&& maxY >= p_78884_1_.minY && minY <= p_78884_1_.maxY;
	}

	/**
	 * Returns true if block is inside bounding box
	 */
	public boolean isVecInside(int p_78890_1_, int p_78890_2_, int p_78890_3_) {
		return p_78890_1_ >= minX && p_78890_1_ <= maxX && p_78890_3_ >= minZ
				&& p_78890_3_ <= maxZ && p_78890_2_ >= minY
				&& p_78890_2_ <= maxY;
	}

	/**
	 * Offsets the current bounding box by the specified coordinates. Args: x,
	 * y, z
	 */
	public void offset(int p_78886_1_, int p_78886_2_, int p_78886_3_) {
		minX += p_78886_1_;
		minY += p_78886_2_;
		minZ += p_78886_3_;
		maxX += p_78886_1_;
		maxY += p_78886_2_;
		maxZ += p_78886_3_;
	}

	@Override
	public String toString() {
		return "(" + minX + ", " + minY + ", " + minZ + "; " + maxX + ", "
				+ maxY + ", " + maxZ + ")";
	}
}
