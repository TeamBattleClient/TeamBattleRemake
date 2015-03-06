package net.minecraft.entity;

import java.util.UUID;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class EntityCreature extends EntityLiving {
	public static final UUID field_110179_h = UUID
			.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
	public static final AttributeModifier field_110181_i = new AttributeModifier(
			field_110179_h, "Fleeing speed bonus", 2.0D, 2).setSaved(false);
	/** The Entity this EntityCreature is set to attack. */
	protected Entity entityToAttack;

	private final EntityAIBase field_110178_bs = new EntityAIMoveTowardsRestriction(
			this, 1.0D);

	private boolean field_110180_bt;

	/** Used to make a creature speed up and wander away when hit. */
	protected int fleeingTick;
	/**
	 * returns true if a creature has attacked recently only used for creepers
	 * and skeletons
	 */
	protected boolean hasAttacked;

	private final ChunkCoordinates homePosition = new ChunkCoordinates(0, 0, 0);
	/** If -1 there is no maximum distance */
	private float maximumHomeDistance = -1.0F;
	private PathEntity pathToEntity;

	public EntityCreature(World p_i1602_1_) {
		super(p_i1602_1_);
	}

	/**
	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden
	 * by each mob to define their attack.
	 */
	protected void attackEntity(Entity p_70785_1_, float p_70785_2_) {
	}

	public void detachHome() {
		maximumHomeDistance = -1.0F;
	}

	/**
	 * Finds the closest player within 16 blocks to attack, or null if this
	 * Entity isn't interested in attacking (Animals, Spiders at day, peaceful
	 * PigZombies).
	 */
	protected Entity findPlayerToAttack() {
		return null;
	}

	public float func_110174_bM() {
		return maximumHomeDistance;
	}

	protected void func_142017_o(float p_142017_1_) {
	}

	/**
	 * Takes a coordinate in and returns a weight to determine how likely this
	 * creature will try to path to the block. Args: x, y, z
	 */
	public float getBlockPathWeight(int p_70783_1_, int p_70783_2_,
			int p_70783_3_) {
		return 0.0F;
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		final int var1 = MathHelper.floor_double(posX);
		final int var2 = MathHelper.floor_double(boundingBox.minY);
		final int var3 = MathHelper.floor_double(posZ);
		return super.getCanSpawnHere()
				&& getBlockPathWeight(var1, var2, var3) >= 0.0F;
	}

	/**
	 * returns the target Entity
	 */
	public Entity getEntityToAttack() {
		return entityToAttack;
	}

	/**
	 * Returns the chunk coordinate object of the home position.
	 */
	public ChunkCoordinates getHomePosition() {
		return homePosition;
	}

	/**
	 * Returns whether a home area is defined for this entity.
	 */
	public boolean hasHome() {
		return maximumHomeDistance != -1.0F;
	}

	/**
	 * if the entity got a PathEntity it returns true, else false
	 */
	public boolean hasPath() {
		return pathToEntity != null;
	}

	/**
	 * Disables a mob's ability to move on its own while true.
	 */
	protected boolean isMovementCeased() {
		return false;
	}

	public boolean isWithinHomeDistance(int p_110176_1_, int p_110176_2_,
			int p_110176_3_) {
		return maximumHomeDistance == -1.0F ? true
				: homePosition.getDistanceSquared(p_110176_1_, p_110176_2_,
						p_110176_3_) < maximumHomeDistance
						* maximumHomeDistance;
	}

	public boolean isWithinHomeDistanceCurrentPosition() {
		return isWithinHomeDistance(MathHelper.floor_double(posX),
				MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
	}

	public void setHomeArea(int p_110171_1_, int p_110171_2_, int p_110171_3_,
			int p_110171_4_) {
		homePosition.set(p_110171_1_, p_110171_2_, p_110171_3_);
		maximumHomeDistance = p_110171_4_;
	}

	/**
	 * sets the pathToEntity
	 */
	public void setPathToEntity(PathEntity p_70778_1_) {
		pathToEntity = p_70778_1_;
	}

	/**
	 * Sets the entity which is to be attacked.
	 */
	public void setTarget(Entity p_70784_1_) {
		entityToAttack = p_70784_1_;
	}

	@Override
	protected void updateEntityActionState() {
		worldObj.theProfiler.startSection("ai");

		if (fleeingTick > 0 && --fleeingTick == 0) {
			final IAttributeInstance var1 = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			var1.removeModifier(field_110181_i);
		}

		hasAttacked = isMovementCeased();
		final float var21 = 16.0F;

		if (entityToAttack == null) {
			entityToAttack = findPlayerToAttack();

			if (entityToAttack != null) {
				pathToEntity = worldObj.getPathEntityToEntity(this,
						entityToAttack, var21, true, false, false, true);
			}
		} else if (entityToAttack.isEntityAlive()) {
			final float var2 = entityToAttack.getDistanceToEntity(this);

			if (canEntityBeSeen(entityToAttack)) {
				attackEntity(entityToAttack, var2);
			}
		} else {
			entityToAttack = null;
		}

		if (entityToAttack instanceof EntityPlayerMP
				&& ((EntityPlayerMP) entityToAttack).theItemInWorldManager
						.isCreative()) {
			entityToAttack = null;
		}

		worldObj.theProfiler.endSection();

		if (!hasAttacked && entityToAttack != null
				&& (pathToEntity == null || rand.nextInt(20) == 0)) {
			pathToEntity = worldObj.getPathEntityToEntity(this, entityToAttack,
					var21, true, false, false, true);
		} else if (!hasAttacked
				&& (pathToEntity == null && rand.nextInt(180) == 0
						|| rand.nextInt(120) == 0 || fleeingTick > 0)
				&& entityAge < 100) {
			updateWanderPath();
		}

		final int var22 = MathHelper.floor_double(boundingBox.minY + 0.5D);
		final boolean var3 = isInWater();
		final boolean var4 = handleLavaMovement();
		rotationPitch = 0.0F;

		if (pathToEntity != null && rand.nextInt(100) != 0) {
			worldObj.theProfiler.startSection("followpath");
			Vec3 var5 = pathToEntity.getPosition(this);
			final double var6 = width * 2.0F;

			while (var5 != null
					&& var5.squareDistanceTo(posX, var5.yCoord, posZ) < var6
							* var6) {
				pathToEntity.incrementPathIndex();

				if (pathToEntity.isFinished()) {
					var5 = null;
					pathToEntity = null;
				} else {
					var5 = pathToEntity.getPosition(this);
				}
			}

			isJumping = false;

			if (var5 != null) {
				final double var8 = var5.xCoord - posX;
				final double var10 = var5.zCoord - posZ;
				final double var12 = var5.yCoord - var22;
				final float var14 = (float) (Math.atan2(var10, var8) * 180.0D / Math.PI) - 90.0F;
				float var15 = MathHelper.wrapAngleTo180_float(var14
						- rotationYaw);
				moveForward = (float) getEntityAttribute(
						SharedMonsterAttributes.movementSpeed)
						.getAttributeValue();

				if (var15 > 30.0F) {
					var15 = 30.0F;
				}

				if (var15 < -30.0F) {
					var15 = -30.0F;
				}

				rotationYaw += var15;

				if (hasAttacked && entityToAttack != null) {
					final double var16 = entityToAttack.posX - posX;
					final double var18 = entityToAttack.posZ - posZ;
					final float var20 = rotationYaw;
					rotationYaw = (float) (Math.atan2(var18, var16) * 180.0D / Math.PI) - 90.0F;
					var15 = (var20 - rotationYaw + 90.0F) * (float) Math.PI
							/ 180.0F;
					moveStrafing = -MathHelper.sin(var15) * moveForward * 1.0F;
					moveForward = MathHelper.cos(var15) * moveForward * 1.0F;
				}

				if (var12 > 0.0D) {
					isJumping = true;
				}
			}

			if (entityToAttack != null) {
				faceEntity(entityToAttack, 30.0F, 30.0F);
			}

			if (isCollidedHorizontally && !hasPath()) {
				isJumping = true;
			}

			if (rand.nextFloat() < 0.8F && (var3 || var4)) {
				isJumping = true;
			}

			worldObj.theProfiler.endSection();
		} else {
			super.updateEntityActionState();
			pathToEntity = null;
		}
	}

	/**
	 * Applies logic related to leashes, for example dragging the entity or
	 * breaking the leash.
	 */
	@Override
	protected void updateLeashedState() {
		super.updateLeashedState();

		if (getLeashed() && getLeashedToEntity() != null
				&& getLeashedToEntity().worldObj == worldObj) {
			final Entity var1 = getLeashedToEntity();
			setHomeArea((int) var1.posX, (int) var1.posY, (int) var1.posZ, 5);
			final float var2 = getDistanceToEntity(var1);

			if (this instanceof EntityTameable
					&& ((EntityTameable) this).isSitting()) {
				if (var2 > 10.0F) {
					clearLeashed(true, true);
				}

				return;
			}

			if (!field_110180_bt) {
				tasks.addTask(2, field_110178_bs);
				getNavigator().setAvoidsWater(false);
				field_110180_bt = true;
			}

			func_142017_o(var2);

			if (var2 > 4.0F) {
				getNavigator().tryMoveToEntityLiving(var1, 1.0D);
			}

			if (var2 > 6.0F) {
				final double var3 = (var1.posX - posX) / var2;
				final double var5 = (var1.posY - posY) / var2;
				final double var7 = (var1.posZ - posZ) / var2;
				motionX += var3 * Math.abs(var3) * 0.4D;
				motionY += var5 * Math.abs(var5) * 0.4D;
				motionZ += var7 * Math.abs(var7) * 0.4D;
			}

			if (var2 > 10.0F) {
				clearLeashed(true, true);
			}
		} else if (!getLeashed() && field_110180_bt) {
			field_110180_bt = false;
			tasks.removeTask(field_110178_bs);
			getNavigator().setAvoidsWater(true);
			detachHome();
		}
	}

	/**
	 * Time remaining during which the Animal is sped up and flees.
	 */
	protected void updateWanderPath() {
		worldObj.theProfiler.startSection("stroll");
		boolean var1 = false;
		int var2 = -1;
		int var3 = -1;
		int var4 = -1;
		float var5 = -99999.0F;

		for (int var6 = 0; var6 < 10; ++var6) {
			final int var7 = MathHelper.floor_double(posX + rand.nextInt(13)
					- 6.0D);
			final int var8 = MathHelper.floor_double(posY + rand.nextInt(7)
					- 3.0D);
			final int var9 = MathHelper.floor_double(posZ + rand.nextInt(13)
					- 6.0D);
			final float var10 = getBlockPathWeight(var7, var8, var9);

			if (var10 > var5) {
				var5 = var10;
				var2 = var7;
				var3 = var8;
				var4 = var9;
				var1 = true;
			}
		}

		if (var1) {
			pathToEntity = worldObj.getEntityPathToXYZ(this, var2, var3, var4,
					10.0F, true, false, false, true);
		}

		worldObj.theProfiler.endSection();
	}
}
