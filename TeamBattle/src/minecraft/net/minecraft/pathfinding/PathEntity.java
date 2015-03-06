package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public class PathEntity {
	/** PathEntity Array Index the Entity is currently targeting */
	private int currentPathIndex;

	/** The total length of the path */
	private int pathLength;

	/** The actual points in the path */
	private final PathPoint[] points;

	public PathEntity(PathPoint[] p_i2136_1_) {
		points = p_i2136_1_;
		pathLength = p_i2136_1_.length;
	}

	public int getCurrentPathIndex() {
		return currentPathIndex;
	}

	public int getCurrentPathLength() {
		return pathLength;
	}

	/**
	 * returns the last PathPoint of the Array
	 */
	public PathPoint getFinalPathPoint() {
		return pathLength > 0 ? points[pathLength - 1] : null;
	}

	/**
	 * return the PathPoint located at the specified PathIndex, usually the
	 * current one
	 */
	public PathPoint getPathPointFromIndex(int p_75877_1_) {
		return points[p_75877_1_];
	}

	/**
	 * returns the current PathEntity target node as Vec3D
	 */
	public Vec3 getPosition(Entity p_75878_1_) {
		return getVectorFromIndex(p_75878_1_, currentPathIndex);
	}

	/**
	 * Gets the vector of the PathPoint associated with the given index.
	 */
	public Vec3 getVectorFromIndex(Entity p_75881_1_, int p_75881_2_) {
		final double var3 = points[p_75881_2_].xCoord
				+ (int) (p_75881_1_.width + 1.0F) * 0.5D;
		final double var5 = points[p_75881_2_].yCoord;
		final double var7 = points[p_75881_2_].zCoord
				+ (int) (p_75881_1_.width + 1.0F) * 0.5D;
		return Vec3.createVectorHelper(var3, var5, var7);
	}

	/**
	 * Directs this path to the next point in its array
	 */
	public void incrementPathIndex() {
		++currentPathIndex;
	}

	/**
	 * Returns true if the final PathPoint in the PathEntity is equal to Vec3D
	 * coords.
	 */
	public boolean isDestinationSame(Vec3 p_75880_1_) {
		final PathPoint var2 = getFinalPathPoint();
		return var2 == null ? false : var2.xCoord == (int) p_75880_1_.xCoord
				&& var2.zCoord == (int) p_75880_1_.zCoord;
	}

	/**
	 * Returns true if this path has reached the end
	 */
	public boolean isFinished() {
		return currentPathIndex >= pathLength;
	}

	/**
	 * Returns true if the EntityPath are the same. Non instance related equals.
	 */
	public boolean isSamePath(PathEntity p_75876_1_) {
		if (p_75876_1_ == null)
			return false;
		else if (p_75876_1_.points.length != points.length)
			return false;
		else {
			for (int var2 = 0; var2 < points.length; ++var2) {
				if (points[var2].xCoord != p_75876_1_.points[var2].xCoord
						|| points[var2].yCoord != p_75876_1_.points[var2].yCoord
						|| points[var2].zCoord != p_75876_1_.points[var2].zCoord)
					return false;
			}

			return true;
		}
	}

	public void setCurrentPathIndex(int p_75872_1_) {
		currentPathIndex = p_75872_1_;
	}

	public void setCurrentPathLength(int p_75871_1_) {
		pathLength = p_75871_1_;
	}
}
