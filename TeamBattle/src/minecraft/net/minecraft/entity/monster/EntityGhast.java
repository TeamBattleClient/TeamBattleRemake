package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityGhast extends EntityFlying implements IMob {
	/** Cooldown time between target loss and new target aquirement. */
	private int aggroCooldown;
	public int attackCounter;
	public int courseChangeCooldown;
	/** The explosion radius of spawned fireballs. */
	private int explosionStrength = 1;
	public int prevAttackCounter;

	private Entity targetedEntity;
	public double waypointX;
	public double waypointY;

	public double waypointZ;

	public EntityGhast(World p_i1735_1_) {
		super(p_i1735_1_);
		setSize(4.0F, 4.0F);
		isImmuneToFire = true;
		experienceValue = 5;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(
				10.0D);
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else if ("fireball".equals(p_70097_1_.getDamageType())
				&& p_70097_1_.getEntity() instanceof EntityPlayer) {
			super.attackEntityFrom(p_70097_1_, 1000.0F);
			((EntityPlayer) p_70097_1_.getEntity())
					.triggerAchievement(AchievementList.ghast);
			return true;
		} else
			return super.attackEntityFrom(p_70097_1_, p_70097_2_);
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int var3 = rand.nextInt(2) + rand.nextInt(1 + p_70628_2_);
		int var4;

		for (var4 = 0; var4 < var3; ++var4) {
			func_145779_a(Items.ghast_tear, 1);
		}

		var3 = rand.nextInt(3) + rand.nextInt(1 + p_70628_2_);

		for (var4 = 0; var4 < var3; ++var4) {
			func_145779_a(Items.gunpowder, 1);
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}

	public boolean func_110182_bF() {
		return dataWatcher.getWatchableObjectByte(16) != 0;
	}

	@Override
	protected Item func_146068_u() {
		return Items.gunpowder;
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		return rand.nextInt(20) == 0 && super.getCanSpawnHere()
				&& worldObj.difficultySetting != EnumDifficulty.PEACEFUL;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "mob.ghast.death";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.ghast.scream";
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return "mob.ghast.moan";
	}

	/**
	 * Will return how many at most can spawn in a chunk at once.
	 */
	@Override
	public int getMaxSpawnedInChunk() {
		return 1;
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume() {
		return 10.0F;
	}

	/**
	 * True if the ghast has an unobstructed line of travel to the waypoint.
	 */
	private boolean isCourseTraversable(double p_70790_1_, double p_70790_3_,
			double p_70790_5_, double p_70790_7_) {
		final double var9 = (waypointX - posX) / p_70790_7_;
		final double var11 = (waypointY - posY) / p_70790_7_;
		final double var13 = (waypointZ - posZ) / p_70790_7_;
		final AxisAlignedBB var15 = boundingBox.copy();

		for (int var16 = 1; var16 < p_70790_7_; ++var16) {
			var15.offset(var9, var11, var13);

			if (!worldObj.getCollidingBoundingBoxes(this, var15).isEmpty())
				return false;
		}

		return true;
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);

		if (p_70037_1_.func_150297_b("ExplosionPower", 99)) {
			explosionStrength = p_70037_1_.getInteger("ExplosionPower");
		}
	}

	@Override
	protected void updateEntityActionState() {
		if (!worldObj.isClient
				&& worldObj.difficultySetting == EnumDifficulty.PEACEFUL) {
			setDead();
		}

		despawnEntity();
		prevAttackCounter = attackCounter;
		final double var1 = waypointX - posX;
		final double var3 = waypointY - posY;
		final double var5 = waypointZ - posZ;
		double var7 = var1 * var1 + var3 * var3 + var5 * var5;

		if (var7 < 1.0D || var7 > 3600.0D) {
			waypointX = posX + (rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
			waypointY = posY + (rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
			waypointZ = posZ + (rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
		}

		if (courseChangeCooldown-- <= 0) {
			courseChangeCooldown += rand.nextInt(5) + 2;
			var7 = MathHelper.sqrt_double(var7);

			if (isCourseTraversable(waypointX, waypointY, waypointZ, var7)) {
				motionX += var1 / var7 * 0.1D;
				motionY += var3 / var7 * 0.1D;
				motionZ += var5 / var7 * 0.1D;
			} else {
				waypointX = posX;
				waypointY = posY;
				waypointZ = posZ;
			}
		}

		if (targetedEntity != null && targetedEntity.isDead) {
			targetedEntity = null;
		}

		if (targetedEntity == null || aggroCooldown-- <= 0) {
			targetedEntity = worldObj.getClosestVulnerablePlayerToEntity(this,
					100.0D);

			if (targetedEntity != null) {
				aggroCooldown = 20;
			}
		}

		final double var9 = 64.0D;

		if (targetedEntity != null
				&& targetedEntity.getDistanceSqToEntity(this) < var9 * var9) {
			final double var11 = targetedEntity.posX - posX;
			final double var13 = targetedEntity.boundingBox.minY
					+ targetedEntity.height / 2.0F - (posY + height / 2.0F);
			final double var15 = targetedEntity.posZ - posZ;
			renderYawOffset = rotationYaw = -((float) Math.atan2(var11, var15))
					* 180.0F / (float) Math.PI;

			if (canEntityBeSeen(targetedEntity)) {
				if (attackCounter == 10) {
					worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1007,
							(int) posX, (int) posY, (int) posZ, 0);
				}

				++attackCounter;

				if (attackCounter == 20) {
					worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1008,
							(int) posX, (int) posY, (int) posZ, 0);
					final EntityLargeFireball var17 = new EntityLargeFireball(
							worldObj, this, var11, var13, var15);
					var17.field_92057_e = explosionStrength;
					final double var18 = 4.0D;
					final Vec3 var20 = getLook(1.0F);
					var17.posX = posX + var20.xCoord * var18;
					var17.posY = posY + height / 2.0F + 0.5D;
					var17.posZ = posZ + var20.zCoord * var18;
					worldObj.spawnEntityInWorld(var17);
					attackCounter = -40;
				}
			} else if (attackCounter > 0) {
				--attackCounter;
			}
		} else {
			renderYawOffset = rotationYaw = -((float) Math.atan2(motionX,
					motionZ)) * 180.0F / (float) Math.PI;

			if (attackCounter > 0) {
				--attackCounter;
			}
		}

		if (!worldObj.isClient) {
			final byte var21 = dataWatcher.getWatchableObjectByte(16);
			final byte var12 = (byte) (attackCounter > 10 ? 1 : 0);

			if (var21 != var12) {
				dataWatcher.updateObject(16, Byte.valueOf(var12));
			}
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("ExplosionPower", explosionStrength);
	}
}
