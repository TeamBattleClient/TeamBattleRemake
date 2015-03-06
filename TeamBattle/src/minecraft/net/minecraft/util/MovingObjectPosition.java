package net.minecraft.util;

import net.minecraft.entity.Entity;

public class MovingObjectPosition {
	public static enum MovingObjectType {
		BLOCK("BLOCK", 1), ENTITY("ENTITY", 2), MISS("MISS", 0);

		private MovingObjectType(String p_i2302_1_, int p_i2302_2_) {
		}
	}

	/** x coordinate of the block ray traced against */
	public int blockX;

	/** y coordinate of the block ray traced against */
	public int blockY;

	/** z coordinate of the block ray traced against */
	public int blockZ;

	/** The hit entity */
	public Entity entityHit;

	/** The vector position of the hit */
	public Vec3 hitVec;

	/**
	 * Which side was hit. If its -1 then it went the full length of the ray
	 * trace. Bottom = 0, Top = 1, East = 2, West = 3, North = 4, South = 5.
	 */
	public int sideHit;

	/** What type of ray trace hit was this? 0 = block, 1 = entity */
	public MovingObjectPosition.MovingObjectType typeOfHit;

	public MovingObjectPosition(Entity p_i2304_1_) {
		this(p_i2304_1_, Vec3.createVectorHelper(p_i2304_1_.posX,
				p_i2304_1_.posY, p_i2304_1_.posZ));
	}

	public MovingObjectPosition(Entity p_i45482_1_, Vec3 p_i45482_2_) {
		typeOfHit = MovingObjectPosition.MovingObjectType.ENTITY;
		entityHit = p_i45482_1_;
		hitVec = p_i45482_2_;
	}

	public MovingObjectPosition(int p_i2303_1_, int p_i2303_2_, int p_i2303_3_,
			int p_i2303_4_, Vec3 p_i2303_5_) {
		this(p_i2303_1_, p_i2303_2_, p_i2303_3_, p_i2303_4_, p_i2303_5_, true);
	}

	public MovingObjectPosition(int p_i45481_1_, int p_i45481_2_,
			int p_i45481_3_, int p_i45481_4_, Vec3 p_i45481_5_,
			boolean p_i45481_6_) {
		typeOfHit = p_i45481_6_ ? MovingObjectPosition.MovingObjectType.BLOCK
				: MovingObjectPosition.MovingObjectType.MISS;
		blockX = p_i45481_1_;
		blockY = p_i45481_2_;
		blockZ = p_i45481_3_;
		sideHit = p_i45481_4_;
		hitVec = Vec3.createVectorHelper(p_i45481_5_.xCoord,
				p_i45481_5_.yCoord, p_i45481_5_.zCoord);
	}

	@Override
	public String toString() {
		return "HitResult{type=" + typeOfHit + ", x=" + blockX + ", y="
				+ blockY + ", z=" + blockZ + ", f=" + sideHit + ", pos="
				+ hitVec + ", entity=" + entityHit + '}';
	}
}
