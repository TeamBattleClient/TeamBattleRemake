package net.minecraft.entity.ai;

import java.util.Random;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityAIFleeSun extends EntityAIBase {
	private final double movementSpeed;
	private double shelterX;
	private double shelterY;
	private double shelterZ;
	private final EntityCreature theCreature;
	private final World theWorld;

	public EntityAIFleeSun(EntityCreature p_i1623_1_, double p_i1623_2_) {
		theCreature = p_i1623_1_;
		movementSpeed = p_i1623_2_;
		theWorld = p_i1623_1_.worldObj;
		setMutexBits(1);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return !theCreature.getNavigator().noPath();
	}

	private Vec3 findPossibleShelter() {
		final Random var1 = theCreature.getRNG();

		for (int var2 = 0; var2 < 10; ++var2) {
			final int var3 = MathHelper.floor_double(theCreature.posX
					+ var1.nextInt(20) - 10.0D);
			final int var4 = MathHelper
					.floor_double(theCreature.boundingBox.minY
							+ var1.nextInt(6) - 3.0D);
			final int var5 = MathHelper.floor_double(theCreature.posZ
					+ var1.nextInt(20) - 10.0D);

			if (!theWorld.canBlockSeeTheSky(var3, var4, var5)
					&& theCreature.getBlockPathWeight(var3, var4, var5) < 0.0F)
				return Vec3.createVectorHelper(var3, var4, var5);
		}

		return null;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (!theWorld.isDaytime())
			return false;
		else if (!theCreature.isBurning())
			return false;
		else if (!theWorld.canBlockSeeTheSky(
				MathHelper.floor_double(theCreature.posX),
				(int) theCreature.boundingBox.minY,
				MathHelper.floor_double(theCreature.posZ)))
			return false;
		else {
			final Vec3 var1 = findPossibleShelter();

			if (var1 == null)
				return false;
			else {
				shelterX = var1.xCoord;
				shelterY = var1.yCoord;
				shelterZ = var1.zCoord;
				return true;
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		theCreature.getNavigator().tryMoveToXYZ(shelterX, shelterY, shelterZ,
				movementSpeed);
	}
}
