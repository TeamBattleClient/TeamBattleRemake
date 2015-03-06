package net.minecraft.pathfinding;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class PathNavigate {
	/** If water blocks are avoided (at least by the pathfinder) */
	private boolean avoidsWater;
	/** If door blocks are considered passable even when closed */
	private boolean canPassClosedWoodenDoors;

	/**
	 * Specifically, if a wooden door block is even considered to be passable by
	 * the pathfinder
	 */
	private boolean canPassOpenWoodenDoors = true;
	/**
	 * If the entity can swim. Swimming AI enables this and the pathfinder will
	 * also cause the entity to swim straight upwards when underwater
	 */
	private boolean canSwim;

	/** The PathEntity being followed. */
	private PathEntity currentPath;
	/**
	 * Coordinates of the entity's position last time a check was done (part of
	 * monitoring getting 'stuck')
	 */
	private final Vec3 lastPosCheck = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);

	private boolean noSunPathfind;

	/**
	 * The number of blocks (extra) +/- in each axis that get pulled out as
	 * cache for the pathfinder's search space
	 */
	private final IAttributeInstance pathSearchRange;

	private double speed;

	private final EntityLiving theEntity;

	/**
	 * The time when the last position check was done (to detect successful
	 * movement)
	 */
	private int ticksAtLastPos;

	/** Time, in number of ticks, following the current path */
	private int totalTicks;

	private final World worldObj;

	public PathNavigate(EntityLiving p_i1671_1_, World p_i1671_2_) {
		theEntity = p_i1671_1_;
		worldObj = p_i1671_2_;
		pathSearchRange = p_i1671_1_
				.getEntityAttribute(SharedMonsterAttributes.followRange);
	}

	/**
	 * If on ground or swimming and can swim
	 */
	private boolean canNavigate() {
		return theEntity.onGround || canSwim && isInFluid()
				|| theEntity.isRiding() && theEntity instanceof EntityZombie
				&& theEntity.ridingEntity instanceof EntityChicken;
	}

	/**
	 * sets active PathEntity to null
	 */
	public void clearPathEntity() {
		currentPath = null;
	}

	public float func_111269_d() {
		return (float) pathSearchRange.getAttributeValue();
	}

	public boolean getAvoidsWater() {
		return avoidsWater;
	}

	/**
	 * Returns true if the entity can break doors, false otherwise
	 */
	public boolean getCanBreakDoors() {
		return canPassClosedWoodenDoors;
	}

	private Vec3 getEntityPosition() {
		return Vec3.createVectorHelper(theEntity.posX, getPathableYPos(),
				theEntity.posZ);
	}

	/**
	 * gets the actively used PathEntity
	 */
	public PathEntity getPath() {
		return currentPath;
	}

	/**
	 * Gets the safe pathing Y position for the entity depending on if it can
	 * path swim or not
	 */
	private int getPathableYPos() {
		if (theEntity.isInWater() && canSwim) {
			int var1 = (int) theEntity.boundingBox.minY;
			Block var2 = worldObj.getBlock(
					MathHelper.floor_double(theEntity.posX), var1,
					MathHelper.floor_double(theEntity.posZ));
			int var3 = 0;

			do {
				if (var2 != Blocks.flowing_water && var2 != Blocks.water)
					return var1;

				++var1;
				var2 = worldObj.getBlock(
						MathHelper.floor_double(theEntity.posX), var1,
						MathHelper.floor_double(theEntity.posZ));
				++var3;
			} while (var3 <= 16);

			return (int) theEntity.boundingBox.minY;
		} else
			return (int) (theEntity.boundingBox.minY + 0.5D);
	}

	/**
	 * Returns the path to the given EntityLiving
	 */
	public PathEntity getPathToEntityLiving(Entity p_75494_1_) {
		return !canNavigate() ? null : worldObj.getPathEntityToEntity(
				theEntity, p_75494_1_, func_111269_d(), canPassOpenWoodenDoors,
				canPassClosedWoodenDoors, avoidsWater, canSwim);
	}

	/**
	 * Returns the path to the given coordinates
	 */
	public PathEntity getPathToXYZ(double p_75488_1_, double p_75488_3_,
			double p_75488_5_) {
		return !canNavigate() ? null : worldObj.getEntityPathToXYZ(theEntity,
				MathHelper.floor_double(p_75488_1_), (int) p_75488_3_,
				MathHelper.floor_double(p_75488_5_), func_111269_d(),
				canPassOpenWoodenDoors, canPassClosedWoodenDoors, avoidsWater,
				canSwim);
	}

	/**
	 * Returns true when an entity of specified size could safely walk in a
	 * straight line between the two points. Args: pos1, pos2, entityXSize,
	 * entityYSize, entityZSize
	 */
	private boolean isDirectPathBetweenPoints(Vec3 p_75493_1_, Vec3 p_75493_2_,
			int p_75493_3_, int p_75493_4_, int p_75493_5_) {
		int var6 = MathHelper.floor_double(p_75493_1_.xCoord);
		int var7 = MathHelper.floor_double(p_75493_1_.zCoord);
		double var8 = p_75493_2_.xCoord - p_75493_1_.xCoord;
		double var10 = p_75493_2_.zCoord - p_75493_1_.zCoord;
		final double var12 = var8 * var8 + var10 * var10;

		if (var12 < 1.0E-8D)
			return false;
		else {
			final double var14 = 1.0D / Math.sqrt(var12);
			var8 *= var14;
			var10 *= var14;
			p_75493_3_ += 2;
			p_75493_5_ += 2;

			if (!isSafeToStandAt(var6, (int) p_75493_1_.yCoord, var7,
					p_75493_3_, p_75493_4_, p_75493_5_, p_75493_1_, var8, var10))
				return false;
			else {
				p_75493_3_ -= 2;
				p_75493_5_ -= 2;
				final double var16 = 1.0D / Math.abs(var8);
				final double var18 = 1.0D / Math.abs(var10);
				double var20 = var6 * 1 - p_75493_1_.xCoord;
				double var22 = var7 * 1 - p_75493_1_.zCoord;

				if (var8 >= 0.0D) {
					++var20;
				}

				if (var10 >= 0.0D) {
					++var22;
				}

				var20 /= var8;
				var22 /= var10;
				final int var24 = var8 < 0.0D ? -1 : 1;
				final int var25 = var10 < 0.0D ? -1 : 1;
				final int var26 = MathHelper.floor_double(p_75493_2_.xCoord);
				final int var27 = MathHelper.floor_double(p_75493_2_.zCoord);
				int var28 = var26 - var6;
				int var29 = var27 - var7;

				do {
					if (var28 * var24 <= 0 && var29 * var25 <= 0)
						return true;

					if (var20 < var22) {
						var20 += var16;
						var6 += var24;
						var28 = var26 - var6;
					} else {
						var22 += var18;
						var7 += var25;
						var29 = var27 - var7;
					}
				} while (isSafeToStandAt(var6, (int) p_75493_1_.yCoord, var7,
						p_75493_3_, p_75493_4_, p_75493_5_, p_75493_1_, var8,
						var10));

				return false;
			}
		}
	}

	/**
	 * Returns true if the entity is in water or lava, false otherwise
	 */
	private boolean isInFluid() {
		return theEntity.isInWater() || theEntity.handleLavaMovement();
	}

	/**
	 * Returns true if an entity does not collide with any solid blocks at the
	 * position. Args: xOffset, yOffset, zOffset, entityXSize, entityYSize,
	 * entityZSize, originPosition, vecX, vecZ
	 */
	private boolean isPositionClear(int p_75496_1_, int p_75496_2_,
			int p_75496_3_, int p_75496_4_, int p_75496_5_, int p_75496_6_,
			Vec3 p_75496_7_, double p_75496_8_, double p_75496_10_) {
		for (int var12 = p_75496_1_; var12 < p_75496_1_ + p_75496_4_; ++var12) {
			for (int var13 = p_75496_2_; var13 < p_75496_2_ + p_75496_5_; ++var13) {
				for (int var14 = p_75496_3_; var14 < p_75496_3_ + p_75496_6_; ++var14) {
					final double var15 = var12 + 0.5D - p_75496_7_.xCoord;
					final double var17 = var14 + 0.5D - p_75496_7_.zCoord;

					if (var15 * p_75496_8_ + var17 * p_75496_10_ >= 0.0D) {
						final Block var19 = worldObj.getBlock(var12, var13,
								var14);

						if (!var19.getBlocksMovement(worldObj, var12, var13,
								var14))
							return false;
					}
				}
			}
		}

		return true;
	}

	/**
	 * Returns true when an entity could stand at a position, including solid
	 * blocks under the entire entity. Args: xOffset, yOffset, zOffset,
	 * entityXSize, entityYSize, entityZSize, originPosition, vecX, vecZ
	 */
	private boolean isSafeToStandAt(int p_75483_1_, int p_75483_2_,
			int p_75483_3_, int p_75483_4_, int p_75483_5_, int p_75483_6_,
			Vec3 p_75483_7_, double p_75483_8_, double p_75483_10_) {
		final int var12 = p_75483_1_ - p_75483_4_ / 2;
		final int var13 = p_75483_3_ - p_75483_6_ / 2;

		if (!isPositionClear(var12, p_75483_2_, var13, p_75483_4_, p_75483_5_,
				p_75483_6_, p_75483_7_, p_75483_8_, p_75483_10_))
			return false;
		else {
			for (int var14 = var12; var14 < var12 + p_75483_4_; ++var14) {
				for (int var15 = var13; var15 < var13 + p_75483_6_; ++var15) {
					final double var16 = var14 + 0.5D - p_75483_7_.xCoord;
					final double var18 = var15 + 0.5D - p_75483_7_.zCoord;

					if (var16 * p_75483_8_ + var18 * p_75483_10_ >= 0.0D) {
						final Block var20 = worldObj.getBlock(var14,
								p_75483_2_ - 1, var15);
						final Material var21 = var20.getMaterial();

						if (var21 == Material.air)
							return false;

						if (var21 == Material.water && !theEntity.isInWater())
							return false;

						if (var21 == Material.lava)
							return false;
					}
				}
			}

			return true;
		}
	}

	/**
	 * If null path or reached the end
	 */
	public boolean noPath() {
		return currentPath == null || currentPath.isFinished();
	}

	public void onUpdateNavigation() {
		++totalTicks;

		if (!noPath()) {
			if (canNavigate()) {
				pathFollow();
			}

			if (!noPath()) {
				final Vec3 var1 = currentPath.getPosition(theEntity);

				if (var1 != null) {
					theEntity.getMoveHelper().setMoveTo(var1.xCoord,
							var1.yCoord, var1.zCoord, speed);
				}
			}
		}
	}

	private void pathFollow() {
		final Vec3 var1 = getEntityPosition();
		int var2 = currentPath.getCurrentPathLength();

		for (int var3 = currentPath.getCurrentPathIndex(); var3 < currentPath
				.getCurrentPathLength(); ++var3) {
			if (currentPath.getPathPointFromIndex(var3).yCoord != (int) var1.yCoord) {
				var2 = var3;
				break;
			}
		}

		final float var8 = theEntity.width * theEntity.width;
		int var4;

		for (var4 = currentPath.getCurrentPathIndex(); var4 < var2; ++var4) {
			if (var1.squareDistanceTo(currentPath.getVectorFromIndex(theEntity,
					var4)) < var8) {
				currentPath.setCurrentPathIndex(var4 + 1);
			}
		}

		var4 = MathHelper.ceiling_float_int(theEntity.width);
		final int var5 = (int) theEntity.height + 1;
		final int var6 = var4;

		for (int var7 = var2 - 1; var7 >= currentPath.getCurrentPathIndex(); --var7) {
			if (isDirectPathBetweenPoints(var1,
					currentPath.getVectorFromIndex(theEntity, var7), var4,
					var5, var6)) {
				currentPath.setCurrentPathIndex(var7);
				break;
			}
		}

		if (totalTicks - ticksAtLastPos > 100) {
			if (var1.squareDistanceTo(lastPosCheck) < 2.25D) {
				clearPathEntity();
			}

			ticksAtLastPos = totalTicks;
			lastPosCheck.xCoord = var1.xCoord;
			lastPosCheck.yCoord = var1.yCoord;
			lastPosCheck.zCoord = var1.zCoord;
		}
	}

	/**
	 * Trims path data from the end to the first sun covered block
	 */
	private void removeSunnyPath() {
		if (!worldObj.canBlockSeeTheSky(
				MathHelper.floor_double(theEntity.posX),
				(int) (theEntity.boundingBox.minY + 0.5D),
				MathHelper.floor_double(theEntity.posZ))) {
			for (int var1 = 0; var1 < currentPath.getCurrentPathLength(); ++var1) {
				final PathPoint var2 = currentPath.getPathPointFromIndex(var1);

				if (worldObj.canBlockSeeTheSky(var2.xCoord, var2.yCoord,
						var2.zCoord)) {
					currentPath.setCurrentPathLength(var1 - 1);
					return;
				}
			}
		}
	}

	/**
	 * Sets if the path should avoid sunlight
	 */
	public void setAvoidSun(boolean p_75504_1_) {
		noSunPathfind = p_75504_1_;
	}

	public void setAvoidsWater(boolean p_75491_1_) {
		avoidsWater = p_75491_1_;
	}

	public void setBreakDoors(boolean p_75498_1_) {
		canPassClosedWoodenDoors = p_75498_1_;
	}

	/**
	 * Sets if the entity can swim
	 */
	public void setCanSwim(boolean p_75495_1_) {
		canSwim = p_75495_1_;
	}

	/**
	 * Sets if the entity can enter open doors
	 */
	public void setEnterDoors(boolean p_75490_1_) {
		canPassOpenWoodenDoors = p_75490_1_;
	}

	/**
	 * sets the active path data if path is 100% unique compared to old path,
	 * checks to adjust path for sun avoiding ents and stores end coords
	 */
	public boolean setPath(PathEntity p_75484_1_, double p_75484_2_) {
		if (p_75484_1_ == null) {
			currentPath = null;
			return false;
		} else {
			if (!p_75484_1_.isSamePath(currentPath)) {
				currentPath = p_75484_1_;
			}

			if (noSunPathfind) {
				removeSunnyPath();
			}

			if (currentPath.getCurrentPathLength() == 0)
				return false;
			else {
				speed = p_75484_2_;
				final Vec3 var4 = getEntityPosition();
				ticksAtLastPos = totalTicks;
				lastPosCheck.xCoord = var4.xCoord;
				lastPosCheck.yCoord = var4.yCoord;
				lastPosCheck.zCoord = var4.zCoord;
				return true;
			}
		}
	}

	/**
	 * Sets the speed
	 */
	public void setSpeed(double p_75489_1_) {
		speed = p_75489_1_;
	}

	/**
	 * Try to find and set a path to EntityLiving. Returns true if successful.
	 */
	public boolean tryMoveToEntityLiving(Entity p_75497_1_, double p_75497_2_) {
		final PathEntity var4 = getPathToEntityLiving(p_75497_1_);
		return var4 != null ? setPath(var4, p_75497_2_) : false;
	}

	/**
	 * Try to find and set a path to XYZ. Returns true if successful.
	 */
	public boolean tryMoveToXYZ(double p_75492_1_, double p_75492_3_,
			double p_75492_5_, double p_75492_7_) {
		final PathEntity var9 = getPathToXYZ(
				MathHelper.floor_double(p_75492_1_), (int) p_75492_3_,
				MathHelper.floor_double(p_75492_5_));
		return setPath(var9, p_75492_7_);
	}
}
