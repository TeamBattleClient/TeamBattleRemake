package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;

import org.apache.commons.lang3.StringUtils;

public abstract class EntityAITarget extends EntityAIBase {
	private int field_75298_g;

	/**
	 * When true, only entities that can be reached with minimal effort will be
	 * targetted.
	 */
	private final boolean nearbyOnly;

	/**
	 * If true, EntityAI targets must be able to be seen (cannot be blocked by
	 * walls) to be suitable targets.
	 */
	protected boolean shouldCheckSight;

	/**
	 * When nearbyOnly is true, this throttles target searching to avoid
	 * excessive pathfinding.
	 */
	private int targetSearchDelay;

	/**
	 * When nearbyOnly is true: 0 -> No target, but OK to search; 1 -> Nearby
	 * target found; 2 -> Target too far.
	 */
	private int targetSearchStatus;
	/** The entity that this task belongs to */
	protected EntityCreature taskOwner;

	public EntityAITarget(EntityCreature p_i1669_1_, boolean p_i1669_2_) {
		this(p_i1669_1_, p_i1669_2_, false);
	}

	public EntityAITarget(EntityCreature p_i1670_1_, boolean p_i1670_2_,
			boolean p_i1670_3_) {
		taskOwner = p_i1670_1_;
		shouldCheckSight = p_i1670_2_;
		nearbyOnly = p_i1670_3_;
	}

	/**
	 * Checks to see if this entity can find a short path to the given target.
	 */
	private boolean canEasilyReach(EntityLivingBase p_75295_1_) {
		targetSearchDelay = 10 + taskOwner.getRNG().nextInt(5);
		final PathEntity var2 = taskOwner.getNavigator().getPathToEntityLiving(
				p_75295_1_);

		if (var2 == null)
			return false;
		else {
			final PathPoint var3 = var2.getFinalPathPoint();

			if (var3 == null)
				return false;
			else {
				final int var4 = var3.xCoord
						- MathHelper.floor_double(p_75295_1_.posX);
				final int var5 = var3.zCoord
						- MathHelper.floor_double(p_75295_1_.posZ);
				return var4 * var4 + var5 * var5 <= 2.25D;
			}
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		final EntityLivingBase var1 = taskOwner.getAttackTarget();

		if (var1 == null)
			return false;
		else if (!var1.isEntityAlive())
			return false;
		else {
			final double var2 = getTargetDistance();

			if (taskOwner.getDistanceSqToEntity(var1) > var2 * var2)
				return false;
			else {
				if (shouldCheckSight) {
					if (taskOwner.getEntitySenses().canSee(var1)) {
						field_75298_g = 0;
					} else if (++field_75298_g > 60)
						return false;
				}

				return !(var1 instanceof EntityPlayerMP)
						|| !((EntityPlayerMP) var1).theItemInWorldManager
								.isCreative();
			}
		}
	}

	protected double getTargetDistance() {
		final IAttributeInstance var1 = taskOwner
				.getEntityAttribute(SharedMonsterAttributes.followRange);
		return var1 == null ? 16.0D : var1.getAttributeValue();
	}

	/**
	 * A method used to see if an entity is a suitable target through a number
	 * of checks.
	 */
	protected boolean isSuitableTarget(EntityLivingBase p_75296_1_,
			boolean p_75296_2_) {
		if (p_75296_1_ == null)
			return false;
		else if (p_75296_1_ == taskOwner)
			return false;
		else if (!p_75296_1_.isEntityAlive())
			return false;
		else if (!taskOwner.canAttackClass(p_75296_1_.getClass()))
			return false;
		else {
			if (taskOwner instanceof IEntityOwnable
					&& StringUtils.isNotEmpty(((IEntityOwnable) taskOwner)
							.func_152113_b())) {
				if (p_75296_1_ instanceof IEntityOwnable
						&& ((IEntityOwnable) taskOwner).func_152113_b().equals(
								((IEntityOwnable) p_75296_1_).func_152113_b()))
					return false;

				if (p_75296_1_ == ((IEntityOwnable) taskOwner).getOwner())
					return false;
			} else if (p_75296_1_ instanceof EntityPlayer && !p_75296_2_
					&& ((EntityPlayer) p_75296_1_).capabilities.disableDamage)
				return false;

			if (!taskOwner.isWithinHomeDistance(
					MathHelper.floor_double(p_75296_1_.posX),
					MathHelper.floor_double(p_75296_1_.posY),
					MathHelper.floor_double(p_75296_1_.posZ)))
				return false;
			else if (shouldCheckSight
					&& !taskOwner.getEntitySenses().canSee(p_75296_1_))
				return false;
			else {
				if (nearbyOnly) {
					if (--targetSearchDelay <= 0) {
						targetSearchStatus = 0;
					}

					if (targetSearchStatus == 0) {
						targetSearchStatus = canEasilyReach(p_75296_1_) ? 1 : 2;
					}

					if (targetSearchStatus == 2)
						return false;
				}

				return true;
			}
		}
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		taskOwner.setAttackTarget((EntityLivingBase) null);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		targetSearchStatus = 0;
		targetSearchDelay = 0;
		field_75298_g = 0;
	}
}
