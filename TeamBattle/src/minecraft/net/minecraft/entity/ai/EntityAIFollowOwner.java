package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIFollowOwner extends EntityAIBase {
	private final double field_75336_f;
	private int field_75343_h;
	private boolean field_75344_i;
	float maxDist;
	float minDist;
	private final PathNavigate petPathfinder;
	private EntityLivingBase theOwner;
	private final EntityTameable thePet;
	World theWorld;

	public EntityAIFollowOwner(EntityTameable p_i1625_1_, double p_i1625_2_,
			float p_i1625_4_, float p_i1625_5_) {
		thePet = p_i1625_1_;
		theWorld = p_i1625_1_.worldObj;
		field_75336_f = p_i1625_2_;
		petPathfinder = p_i1625_1_.getNavigator();
		minDist = p_i1625_4_;
		maxDist = p_i1625_5_;
		setMutexBits(3);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return !petPathfinder.noPath()
				&& thePet.getDistanceSqToEntity(theOwner) > maxDist * maxDist
				&& !thePet.isSitting();
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		theOwner = null;
		petPathfinder.clearPathEntity();
		thePet.getNavigator().setAvoidsWater(field_75344_i);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		final EntityLivingBase var1 = thePet.getOwner();

		if (var1 == null)
			return false;
		else if (thePet.isSitting())
			return false;
		else if (thePet.getDistanceSqToEntity(var1) < minDist * minDist)
			return false;
		else {
			theOwner = var1;
			return true;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		field_75343_h = 0;
		field_75344_i = thePet.getNavigator().getAvoidsWater();
		thePet.getNavigator().setAvoidsWater(false);
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		thePet.getLookHelper().setLookPositionWithEntity(theOwner, 10.0F,
				thePet.getVerticalFaceSpeed());

		if (!thePet.isSitting()) {
			if (--field_75343_h <= 0) {
				field_75343_h = 10;

				if (!petPathfinder.tryMoveToEntityLiving(theOwner,
						field_75336_f)) {
					if (!thePet.getLeashed()) {
						if (thePet.getDistanceSqToEntity(theOwner) >= 144.0D) {
							final int var1 = MathHelper
									.floor_double(theOwner.posX) - 2;
							final int var2 = MathHelper
									.floor_double(theOwner.posZ) - 2;
							final int var3 = MathHelper
									.floor_double(theOwner.boundingBox.minY);

							for (int var4 = 0; var4 <= 4; ++var4) {
								for (int var5 = 0; var5 <= 4; ++var5) {
									if ((var4 < 1 || var5 < 1 || var4 > 3 || var5 > 3)
											&& World.doesBlockHaveSolidTopSurface(
													theWorld, var1 + var4,
													var3 - 1, var2 + var5)
											&& !theWorld.getBlock(var1 + var4,
													var3, var2 + var5)
													.isNormalCube()
											&& !theWorld.getBlock(var1 + var4,
													var3 + 1, var2 + var5)
													.isNormalCube()) {
										thePet.setLocationAndAngles(var1 + var4
												+ 0.5F, var3, var2 + var5
												+ 0.5F, thePet.rotationYaw,
												thePet.rotationPitch);
										petPathfinder.clearPathEntity();
										return;
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
