package net.minecraft.util;

public class Vec3 {
	/**
	 * Static method for creating a new Vec3D given the three x,y,z values. This
	 * is only called from the other static method which creates and places it
	 * in the list.
	 */
	public static Vec3 createVectorHelper(double p_72443_0_, double p_72443_2_,
			double p_72443_4_) {
		return new Vec3(p_72443_0_, p_72443_2_, p_72443_4_);
	}

	/** X coordinate of Vec3D */
	public double xCoord;

	/** Y coordinate of Vec3D */
	public double yCoord;

	/** Z coordinate of Vec3D */
	public double zCoord;

	protected Vec3(double p_i1108_1_, double p_i1108_3_, double p_i1108_5_) {
		if (p_i1108_1_ == -0.0D) {
			p_i1108_1_ = 0.0D;
		}

		if (p_i1108_3_ == -0.0D) {
			p_i1108_3_ = 0.0D;
		}

		if (p_i1108_5_ == -0.0D) {
			p_i1108_5_ = 0.0D;
		}

		xCoord = p_i1108_1_;
		yCoord = p_i1108_3_;
		zCoord = p_i1108_5_;
	}

	/**
	 * Adds the specified x,y,z vector components to this vector and returns the
	 * resulting vector. Does not change this vector.
	 */
	public Vec3 addVector(double p_72441_1_, double p_72441_3_,
			double p_72441_5_) {
		return createVectorHelper(xCoord + p_72441_1_, yCoord + p_72441_3_,
				zCoord + p_72441_5_);
	}

	/**
	 * Returns a new vector with the result of this vector x the specified
	 * vector.
	 */
	public Vec3 crossProduct(Vec3 p_72431_1_) {
		return createVectorHelper(yCoord * p_72431_1_.zCoord - zCoord
				* p_72431_1_.yCoord, zCoord * p_72431_1_.xCoord - xCoord
				* p_72431_1_.zCoord, xCoord * p_72431_1_.yCoord - yCoord
				* p_72431_1_.xCoord);
	}

	/**
	 * Euclidean distance between this and the specified vector, returned as
	 * double.
	 */
	public double distanceTo(Vec3 p_72438_1_) {
		final double var2 = p_72438_1_.xCoord - xCoord;
		final double var4 = p_72438_1_.yCoord - yCoord;
		final double var6 = p_72438_1_.zCoord - zCoord;
		return MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
	}

	public double dotProduct(Vec3 p_72430_1_) {
		return xCoord * p_72430_1_.xCoord + yCoord * p_72430_1_.yCoord + zCoord
				* p_72430_1_.zCoord;
	}

	/**
	 * Returns a new vector with x value equal to the second parameter, along
	 * the line between this vector and the passed in vector, or null if not
	 * possible.
	 */
	public Vec3 getIntermediateWithXValue(Vec3 p_72429_1_, double p_72429_2_) {
		final double var4 = p_72429_1_.xCoord - xCoord;
		final double var6 = p_72429_1_.yCoord - yCoord;
		final double var8 = p_72429_1_.zCoord - zCoord;

		if (var4 * var4 < 1.0000000116860974E-7D)
			return null;
		else {
			final double var10 = (p_72429_2_ - xCoord) / var4;
			return var10 >= 0.0D && var10 <= 1.0D ? createVectorHelper(xCoord
					+ var4 * var10, yCoord + var6 * var10, zCoord + var8
					* var10) : null;
		}
	}

	/**
	 * Returns a new vector with y value equal to the second parameter, along
	 * the line between this vector and the passed in vector, or null if not
	 * possible.
	 */
	public Vec3 getIntermediateWithYValue(Vec3 p_72435_1_, double p_72435_2_) {
		final double var4 = p_72435_1_.xCoord - xCoord;
		final double var6 = p_72435_1_.yCoord - yCoord;
		final double var8 = p_72435_1_.zCoord - zCoord;

		if (var6 * var6 < 1.0000000116860974E-7D)
			return null;
		else {
			final double var10 = (p_72435_2_ - yCoord) / var6;
			return var10 >= 0.0D && var10 <= 1.0D ? createVectorHelper(xCoord
					+ var4 * var10, yCoord + var6 * var10, zCoord + var8
					* var10) : null;
		}
	}

	/**
	 * Returns a new vector with z value equal to the second parameter, along
	 * the line between this vector and the passed in vector, or null if not
	 * possible.
	 */
	public Vec3 getIntermediateWithZValue(Vec3 p_72434_1_, double p_72434_2_) {
		final double var4 = p_72434_1_.xCoord - xCoord;
		final double var6 = p_72434_1_.yCoord - yCoord;
		final double var8 = p_72434_1_.zCoord - zCoord;

		if (var8 * var8 < 1.0000000116860974E-7D)
			return null;
		else {
			final double var10 = (p_72434_2_ - zCoord) / var8;
			return var10 >= 0.0D && var10 <= 1.0D ? createVectorHelper(xCoord
					+ var4 * var10, yCoord + var6 * var10, zCoord + var8
					* var10) : null;
		}
	}

	/**
	 * Returns the length of the vector.
	 */
	public double lengthVector() {
		return MathHelper.sqrt_double(xCoord * xCoord + yCoord * yCoord
				+ zCoord * zCoord);
	}

	/**
	 * Normalizes the vector to a length of 1 (except if it is the zero vector)
	 */
	public Vec3 normalize() {
		final double var1 = MathHelper.sqrt_double(xCoord * xCoord + yCoord
				* yCoord + zCoord * zCoord);
		return var1 < 1.0E-4D ? createVectorHelper(0.0D, 0.0D, 0.0D)
				: createVectorHelper(xCoord / var1, yCoord / var1, zCoord
						/ var1);
	}

	/**
	 * Rotates the vector around the x axis by the specified angle.
	 */
	public void rotateAroundX(float p_72440_1_) {
		final float var2 = MathHelper.cos(p_72440_1_);
		final float var3 = MathHelper.sin(p_72440_1_);
		final double var4 = xCoord;
		final double var6 = yCoord * var2 + zCoord * var3;
		final double var8 = zCoord * var2 - yCoord * var3;
		setComponents(var4, var6, var8);
	}

	/**
	 * Rotates the vector around the y axis by the specified angle.
	 */
	public void rotateAroundY(float p_72442_1_) {
		final float var2 = MathHelper.cos(p_72442_1_);
		final float var3 = MathHelper.sin(p_72442_1_);
		final double var4 = xCoord * var2 + zCoord * var3;
		final double var6 = yCoord;
		final double var8 = zCoord * var2 - xCoord * var3;
		setComponents(var4, var6, var8);
	}

	/**
	 * Rotates the vector around the z axis by the specified angle.
	 */
	public void rotateAroundZ(float p_72446_1_) {
		final float var2 = MathHelper.cos(p_72446_1_);
		final float var3 = MathHelper.sin(p_72446_1_);
		final double var4 = xCoord * var2 + yCoord * var3;
		final double var6 = yCoord * var2 - xCoord * var3;
		final double var8 = zCoord;
		setComponents(var4, var6, var8);
	}

	/**
	 * Sets the x,y,z components of the vector as specified.
	 */
	protected Vec3 setComponents(double p_72439_1_, double p_72439_3_,
			double p_72439_5_) {
		xCoord = p_72439_1_;
		yCoord = p_72439_3_;
		zCoord = p_72439_5_;
		return this;
	}

	/**
	 * The square of the Euclidean distance between this and the vector of x,y,z
	 * components passed in.
	 */
	public double squareDistanceTo(double p_72445_1_, double p_72445_3_,
			double p_72445_5_) {
		final double var7 = p_72445_1_ - xCoord;
		final double var9 = p_72445_3_ - yCoord;
		final double var11 = p_72445_5_ - zCoord;
		return var7 * var7 + var9 * var9 + var11 * var11;
	}

	/**
	 * The square of the Euclidean distance between this and the specified
	 * vector.
	 */
	public double squareDistanceTo(Vec3 p_72436_1_) {
		final double var2 = p_72436_1_.xCoord - xCoord;
		final double var4 = p_72436_1_.yCoord - yCoord;
		final double var6 = p_72436_1_.zCoord - zCoord;
		return var2 * var2 + var4 * var4 + var6 * var6;
	}

	/**
	 * Returns a new vector with the result of the specified vector minus this.
	 */
	public Vec3 subtract(Vec3 p_72444_1_) {
		return createVectorHelper(p_72444_1_.xCoord - xCoord, p_72444_1_.yCoord
				- yCoord, p_72444_1_.zCoord - zCoord);
	}

	@Override
	public String toString() {
		return "(" + xCoord + ", " + yCoord + ", " + zCoord + ")";
	}
}
