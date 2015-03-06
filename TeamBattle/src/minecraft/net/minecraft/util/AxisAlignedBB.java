package net.minecraft.util;

public class AxisAlignedBB {
	/**
	 * Returns a bounding box with the specified bounds. Args: minX, minY, minZ,
	 * maxX, maxY, maxZ
	 */
	public static AxisAlignedBB getBoundingBox(double p_72330_0_,
			double p_72330_2_, double p_72330_4_, double p_72330_6_,
			double p_72330_8_, double p_72330_10_) {
		return new AxisAlignedBB(p_72330_0_, p_72330_2_, p_72330_4_,
				p_72330_6_, p_72330_8_, p_72330_10_);
	}

	public double maxX;
	public double maxY;
	public double maxZ;
	public double minX;
	public double minY;

	public double minZ;

	protected AxisAlignedBB(double p_i2300_1_, double p_i2300_3_,
			double p_i2300_5_, double p_i2300_7_, double p_i2300_9_,
			double p_i2300_11_) {
		minX = p_i2300_1_;
		minY = p_i2300_3_;
		minZ = p_i2300_5_;
		maxX = p_i2300_7_;
		maxY = p_i2300_9_;
		maxZ = p_i2300_11_;
	}

	/**
	 * Adds the coordinates to the bounding box extending it if the point lies
	 * outside the current ranges. Args: x, y, z
	 */
	public AxisAlignedBB addCoord(double p_72321_1_, double p_72321_3_,
			double p_72321_5_) {
		double var7 = minX;
		double var9 = minY;
		double var11 = minZ;
		double var13 = maxX;
		double var15 = maxY;
		double var17 = maxZ;

		if (p_72321_1_ < 0.0D) {
			var7 += p_72321_1_;
		}

		if (p_72321_1_ > 0.0D) {
			var13 += p_72321_1_;
		}

		if (p_72321_3_ < 0.0D) {
			var9 += p_72321_3_;
		}

		if (p_72321_3_ > 0.0D) {
			var15 += p_72321_3_;
		}

		if (p_72321_5_ < 0.0D) {
			var11 += p_72321_5_;
		}

		if (p_72321_5_ > 0.0D) {
			var17 += p_72321_5_;
		}

		return getBoundingBox(var7, var9, var11, var13, var15, var17);
	}

	public MovingObjectPosition calculateIntercept(Vec3 p_72327_1_,
			Vec3 p_72327_2_) {
		Vec3 var3 = p_72327_1_.getIntermediateWithXValue(p_72327_2_, minX);
		Vec3 var4 = p_72327_1_.getIntermediateWithXValue(p_72327_2_, maxX);
		Vec3 var5 = p_72327_1_.getIntermediateWithYValue(p_72327_2_, minY);
		Vec3 var6 = p_72327_1_.getIntermediateWithYValue(p_72327_2_, maxY);
		Vec3 var7 = p_72327_1_.getIntermediateWithZValue(p_72327_2_, minZ);
		Vec3 var8 = p_72327_1_.getIntermediateWithZValue(p_72327_2_, maxZ);

		if (!isVecInYZ(var3)) {
			var3 = null;
		}

		if (!isVecInYZ(var4)) {
			var4 = null;
		}

		if (!isVecInXZ(var5)) {
			var5 = null;
		}

		if (!isVecInXZ(var6)) {
			var6 = null;
		}

		if (!isVecInXY(var7)) {
			var7 = null;
		}

		if (!isVecInXY(var8)) {
			var8 = null;
		}

		Vec3 var9 = null;

		if (var3 != null
				&& (var9 == null || p_72327_1_.squareDistanceTo(var3) < p_72327_1_
						.squareDistanceTo(var9))) {
			var9 = var3;
		}

		if (var4 != null
				&& (var9 == null || p_72327_1_.squareDistanceTo(var4) < p_72327_1_
						.squareDistanceTo(var9))) {
			var9 = var4;
		}

		if (var5 != null
				&& (var9 == null || p_72327_1_.squareDistanceTo(var5) < p_72327_1_
						.squareDistanceTo(var9))) {
			var9 = var5;
		}

		if (var6 != null
				&& (var9 == null || p_72327_1_.squareDistanceTo(var6) < p_72327_1_
						.squareDistanceTo(var9))) {
			var9 = var6;
		}

		if (var7 != null
				&& (var9 == null || p_72327_1_.squareDistanceTo(var7) < p_72327_1_
						.squareDistanceTo(var9))) {
			var9 = var7;
		}

		if (var8 != null
				&& (var9 == null || p_72327_1_.squareDistanceTo(var8) < p_72327_1_
						.squareDistanceTo(var9))) {
			var9 = var8;
		}

		if (var9 == null)
			return null;
		else {
			byte var10 = -1;

			if (var9 == var3) {
				var10 = 4;
			}

			if (var9 == var4) {
				var10 = 5;
			}

			if (var9 == var5) {
				var10 = 0;
			}

			if (var9 == var6) {
				var10 = 1;
			}

			if (var9 == var7) {
				var10 = 2;
			}

			if (var9 == var8) {
				var10 = 3;
			}

			return new MovingObjectPosition(0, 0, 0, var10, var9);
		}
	}

	/**
	 * if instance and the argument bounding boxes overlap in the Y and Z
	 * dimensions, calculate the offset between them in the X dimension. return
	 * var2 if the bounding boxes do not overlap or if var2 is closer to 0 then
	 * the calculated offset. Otherwise return the calculated offset.
	 */
	public double calculateXOffset(AxisAlignedBB p_72316_1_, double p_72316_2_) {
		if (p_72316_1_.maxY > minY && p_72316_1_.minY < maxY) {
			if (p_72316_1_.maxZ > minZ && p_72316_1_.minZ < maxZ) {
				double var4;

				if (p_72316_2_ > 0.0D && p_72316_1_.maxX <= minX) {
					var4 = minX - p_72316_1_.maxX;

					if (var4 < p_72316_2_) {
						p_72316_2_ = var4;
					}
				}

				if (p_72316_2_ < 0.0D && p_72316_1_.minX >= maxX) {
					var4 = maxX - p_72316_1_.minX;

					if (var4 > p_72316_2_) {
						p_72316_2_ = var4;
					}
				}

				return p_72316_2_;
			} else
				return p_72316_2_;
		} else
			return p_72316_2_;
	}

	/**
	 * if instance and the argument bounding boxes overlap in the X and Z
	 * dimensions, calculate the offset between them in the Y dimension. return
	 * var2 if the bounding boxes do not overlap or if var2 is closer to 0 then
	 * the calculated offset. Otherwise return the calculated offset.
	 */
	public double calculateYOffset(AxisAlignedBB p_72323_1_, double p_72323_2_) {
		if (p_72323_1_.maxX > minX && p_72323_1_.minX < maxX) {
			if (p_72323_1_.maxZ > minZ && p_72323_1_.minZ < maxZ) {
				double var4;

				if (p_72323_2_ > 0.0D && p_72323_1_.maxY <= minY) {
					var4 = minY - p_72323_1_.maxY;

					if (var4 < p_72323_2_) {
						p_72323_2_ = var4;
					}
				}

				if (p_72323_2_ < 0.0D && p_72323_1_.minY >= maxY) {
					var4 = maxY - p_72323_1_.minY;

					if (var4 > p_72323_2_) {
						p_72323_2_ = var4;
					}
				}

				return p_72323_2_;
			} else
				return p_72323_2_;
		} else
			return p_72323_2_;
	}

	/**
	 * if instance and the argument bounding boxes overlap in the Y and X
	 * dimensions, calculate the offset between them in the Z dimension. return
	 * var2 if the bounding boxes do not overlap or if var2 is closer to 0 then
	 * the calculated offset. Otherwise return the calculated offset.
	 */
	public double calculateZOffset(AxisAlignedBB p_72322_1_, double p_72322_2_) {
		if (p_72322_1_.maxX > minX && p_72322_1_.minX < maxX) {
			if (p_72322_1_.maxY > minY && p_72322_1_.minY < maxY) {
				double var4;

				if (p_72322_2_ > 0.0D && p_72322_1_.maxZ <= minZ) {
					var4 = minZ - p_72322_1_.maxZ;

					if (var4 < p_72322_2_) {
						p_72322_2_ = var4;
					}
				}

				if (p_72322_2_ < 0.0D && p_72322_1_.minZ >= maxZ) {
					var4 = maxZ - p_72322_1_.minZ;

					if (var4 > p_72322_2_) {
						p_72322_2_ = var4;
					}
				}

				return p_72322_2_;
			} else
				return p_72322_2_;
		} else
			return p_72322_2_;
	}

	/**
	 * Returns a bounding box that is inset by the specified amounts
	 */
	public AxisAlignedBB contract(double p_72331_1_, double p_72331_3_,
			double p_72331_5_) {
		final double var7 = minX + p_72331_1_;
		final double var9 = minY + p_72331_3_;
		final double var11 = minZ + p_72331_5_;
		final double var13 = maxX - p_72331_1_;
		final double var15 = maxY - p_72331_3_;
		final double var17 = maxZ - p_72331_5_;
		return getBoundingBox(var7, var9, var11, var13, var15, var17);
	}

	/**
	 * Returns a copy of the bounding box.
	 */
	public AxisAlignedBB copy() {
		return getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}

	/**
	 * Returns a bounding box expanded by the specified vector (if negative
	 * numbers are given it will shrink). Args: x, y, z
	 */
	public AxisAlignedBB expand(double p_72314_1_, double p_72314_3_,
			double p_72314_5_) {
		final double var7 = minX - p_72314_1_;
		final double var9 = minY - p_72314_3_;
		final double var11 = minZ - p_72314_5_;
		final double var13 = maxX + p_72314_1_;
		final double var15 = maxY + p_72314_3_;
		final double var17 = maxZ + p_72314_5_;
		return getBoundingBox(var7, var9, var11, var13, var15, var17);
	}

	public AxisAlignedBB func_111270_a(AxisAlignedBB p_111270_1_) {
		final double var2 = Math.min(minX, p_111270_1_.minX);
		final double var4 = Math.min(minY, p_111270_1_.minY);
		final double var6 = Math.min(minZ, p_111270_1_.minZ);
		final double var8 = Math.max(maxX, p_111270_1_.maxX);
		final double var10 = Math.max(maxY, p_111270_1_.maxY);
		final double var12 = Math.max(maxZ, p_111270_1_.maxZ);
		return getBoundingBox(var2, var4, var6, var8, var10, var12);
	}

	/**
	 * Returns the average length of the edges of the bounding box.
	 */
	public double getAverageEdgeLength() {
		final double var1 = maxX - minX;
		final double var3 = maxY - minY;
		final double var5 = maxZ - minZ;
		return (var1 + var3 + var5) / 3.0D;
	}

	/**
	 * Returns a bounding box offseted by the specified vector (if negative
	 * numbers are given it will shrink). Args: x, y, z
	 */
	public AxisAlignedBB getOffsetBoundingBox(double p_72325_1_,
			double p_72325_3_, double p_72325_5_) {
		return getBoundingBox(minX + p_72325_1_, minY + p_72325_3_, minZ
				+ p_72325_5_, maxX + p_72325_1_, maxY + p_72325_3_, maxZ
				+ p_72325_5_);
	}

	/**
	 * Returns whether the given bounding box intersects with this one. Args:
	 * axisAlignedBB
	 */
	public boolean intersectsWith(AxisAlignedBB p_72326_1_) {
		return p_72326_1_.maxX > minX && p_72326_1_.minX < maxX ? p_72326_1_.maxY > minY
				&& p_72326_1_.minY < maxY ? p_72326_1_.maxZ > minZ
				&& p_72326_1_.minZ < maxZ : false
				: false;
	}

	/**
	 * Returns if the supplied Vec3D is completely inside the bounding box
	 */
	public boolean isVecInside(Vec3 p_72318_1_) {
		return p_72318_1_.xCoord > minX && p_72318_1_.xCoord < maxX ? p_72318_1_.yCoord > minY
				&& p_72318_1_.yCoord < maxY ? p_72318_1_.zCoord > minZ
				&& p_72318_1_.zCoord < maxZ : false
				: false;
	}

	/**
	 * Checks if the specified vector is within the XY dimensions of the
	 * bounding box. Args: Vec3D
	 */
	private boolean isVecInXY(Vec3 p_72319_1_) {
		return p_72319_1_ == null ? false : p_72319_1_.xCoord >= minX
				&& p_72319_1_.xCoord <= maxX && p_72319_1_.yCoord >= minY
				&& p_72319_1_.yCoord <= maxY;
	}

	/**
	 * Checks if the specified vector is within the XZ dimensions of the
	 * bounding box. Args: Vec3D
	 */
	private boolean isVecInXZ(Vec3 p_72315_1_) {
		return p_72315_1_ == null ? false : p_72315_1_.xCoord >= minX
				&& p_72315_1_.xCoord <= maxX && p_72315_1_.zCoord >= minZ
				&& p_72315_1_.zCoord <= maxZ;
	}

	/**
	 * Checks if the specified vector is within the YZ dimensions of the
	 * bounding box. Args: Vec3D
	 */
	private boolean isVecInYZ(Vec3 p_72333_1_) {
		return p_72333_1_ == null ? false : p_72333_1_.yCoord >= minY
				&& p_72333_1_.yCoord <= maxY && p_72333_1_.zCoord >= minZ
				&& p_72333_1_.zCoord <= maxZ;
	}

	/**
	 * Offsets the current bounding box by the specified coordinates. Args: x,
	 * y, z
	 */
	public AxisAlignedBB offset(double p_72317_1_, double p_72317_3_,
			double p_72317_5_) {
		minX += p_72317_1_;
		minY += p_72317_3_;
		minZ += p_72317_5_;
		maxX += p_72317_1_;
		maxY += p_72317_3_;
		maxZ += p_72317_5_;
		return this;
	}

	/**
	 * Sets the bounding box to the same bounds as the bounding box passed in.
	 * Args: axisAlignedBB
	 */
	public void setBB(AxisAlignedBB p_72328_1_) {
		minX = p_72328_1_.minX;
		minY = p_72328_1_.minY;
		minZ = p_72328_1_.minZ;
		maxX = p_72328_1_.maxX;
		maxY = p_72328_1_.maxY;
		maxZ = p_72328_1_.maxZ;
	}

	/**
	 * Sets the bounds of the bounding box. Args: minX, minY, minZ, maxX, maxY,
	 * maxZ
	 */
	public AxisAlignedBB setBounds(double p_72324_1_, double p_72324_3_,
			double p_72324_5_, double p_72324_7_, double p_72324_9_,
			double p_72324_11_) {
		minX = p_72324_1_;
		minY = p_72324_3_;
		minZ = p_72324_5_;
		maxX = p_72324_7_;
		maxY = p_72324_9_;
		maxZ = p_72324_11_;
		return this;
	}

	@Override
	public String toString() {
		return "box[" + minX + ", " + minY + ", " + minZ + " -> " + maxX + ", "
				+ maxY + ", " + maxZ + "]";
	}
}
