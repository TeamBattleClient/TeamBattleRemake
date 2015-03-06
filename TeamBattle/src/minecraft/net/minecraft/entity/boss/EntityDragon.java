package net.minecraft.entity.boss;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEndPortal;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityDragon extends EntityLiving implements IBossDisplayData,
		IEntityMultiPart, IMob {
	/**
	 * Animation time, used to control the speed of the animation cycles (wings
	 * flapping, jaw opening, etc.)
	 */
	public float animTime;
	public int deathTicks;
	/** An array containing all body parts of this dragon */
	public EntityDragonPart[] dragonPartArray;

	/** The body bounding box of a dragon */
	public EntityDragonPart dragonPartBody;

	/** The head bounding box of a dragon */
	public EntityDragonPart dragonPartHead;

	public EntityDragonPart dragonPartTail1;

	public EntityDragonPart dragonPartTail2;

	public EntityDragonPart dragonPartTail3;
	public EntityDragonPart dragonPartWing1;
	public EntityDragonPart dragonPartWing2;
	/** Force selecting a new flight target at next tick if set to true. */
	public boolean forceNewTarget;
	/** The current endercrystal that is healing this dragon */
	public EntityEnderCrystal healingEnderCrystal;
	/** Animation time at previous tick. */
	public float prevAnimTime;

	/**
	 * Ring buffer array for the last 64 Y-positions and yaw rotations. Used to
	 * calculate offsets for the animations.
	 */
	public double[][] ringBuffer = new double[64][3];

	/**
	 * Index into the ring buffer. Incremented once per tick and restarts at 0
	 * once it reaches the end of the buffer.
	 */
	public int ringBufferIndex = -1;

	/**
	 * Activated if the dragon is flying though obsidian, white stone or
	 * bedrock. Slows movement and animation speed.
	 */
	public boolean slowed;

	private Entity target;
	public double targetX;
	public double targetY;

	public double targetZ;

	public EntityDragon(World p_i1700_1_) {
		super(p_i1700_1_);
		dragonPartArray = new EntityDragonPart[] {
				dragonPartHead = new EntityDragonPart(this, "head", 6.0F, 6.0F),
				dragonPartBody = new EntityDragonPart(this, "body", 8.0F, 8.0F),
				dragonPartTail1 = new EntityDragonPart(this, "tail", 4.0F, 4.0F),
				dragonPartTail2 = new EntityDragonPart(this, "tail", 4.0F, 4.0F),
				dragonPartTail3 = new EntityDragonPart(this, "tail", 4.0F, 4.0F),
				dragonPartWing1 = new EntityDragonPart(this, "wing", 4.0F, 4.0F),
				dragonPartWing2 = new EntityDragonPart(this, "wing", 4.0F, 4.0F) };
		setHealth(getMaxHealth());
		setSize(16.0F, 8.0F);
		noClip = true;
		isImmuneToFire = true;
		targetY = 100.0D;
		ignoreFrustumCheck = true;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(
				200.0D);
	}

	/**
	 * Attacks all entities inside this list, dealing 5 hearts of damage.
	 */
	private void attackEntitiesInList(List p_70971_1_) {
		for (int var2 = 0; var2 < p_70971_1_.size(); ++var2) {
			final Entity var3 = (Entity) p_70971_1_.get(var2);

			if (var3 instanceof EntityLivingBase) {
				var3.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0F);
			}
		}
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		return false;
	}

	@Override
	public boolean attackEntityFromPart(EntityDragonPart p_70965_1_,
			DamageSource p_70965_2_, float p_70965_3_) {
		if (p_70965_1_ != dragonPartHead) {
			p_70965_3_ = p_70965_3_ / 4.0F + 1.0F;
		}

		final float var4 = rotationYaw * (float) Math.PI / 180.0F;
		final float var5 = MathHelper.sin(var4);
		final float var6 = MathHelper.cos(var4);
		targetX = posX + var5 * 5.0F + (rand.nextFloat() - 0.5F) * 2.0F;
		targetY = posY + rand.nextFloat() * 3.0F + 1.0D;
		targetZ = posZ - var6 * 5.0F + (rand.nextFloat() - 0.5F) * 2.0F;
		target = null;

		if (p_70965_2_.getEntity() instanceof EntityPlayer
				|| p_70965_2_.isExplosion()) {
			func_82195_e(p_70965_2_, p_70965_3_);
		}

		return true;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through
	 * this Entity.
	 */
	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	/**
	 * Pushes all entities inside the list away from the enderdragon.
	 */
	private void collideWithEntities(List p_70970_1_) {
		final double var2 = (dragonPartBody.boundingBox.minX + dragonPartBody.boundingBox.maxX) / 2.0D;
		final double var4 = (dragonPartBody.boundingBox.minZ + dragonPartBody.boundingBox.maxZ) / 2.0D;
		final Iterator var6 = p_70970_1_.iterator();

		while (var6.hasNext()) {
			final Entity var7 = (Entity) var6.next();

			if (var7 instanceof EntityLivingBase) {
				final double var8 = var7.posX - var2;
				final double var10 = var7.posZ - var4;
				final double var12 = var8 * var8 + var10 * var10;
				var7.addVelocity(var8 / var12 * 4.0D, 0.20000000298023224D,
						var10 / var12 * 4.0D);
			}
		}
	}

	/**
	 * Creates the ender portal leading back to the normal world after defeating
	 * the enderdragon.
	 */
	private void createEnderPortal(int p_70975_1_, int p_70975_2_) {
		final byte var3 = 64;
		BlockEndPortal.field_149948_a = true;
		final byte var4 = 4;

		for (int var5 = var3 - 1; var5 <= var3 + 32; ++var5) {
			for (int var6 = p_70975_1_ - var4; var6 <= p_70975_1_ + var4; ++var6) {
				for (int var7 = p_70975_2_ - var4; var7 <= p_70975_2_ + var4; ++var7) {
					final double var8 = var6 - p_70975_1_;
					final double var10 = var7 - p_70975_2_;
					final double var12 = var8 * var8 + var10 * var10;

					if (var12 <= (var4 - 0.5D) * (var4 - 0.5D)) {
						if (var5 < var3) {
							if (var12 <= (var4 - 1 - 0.5D) * (var4 - 1 - 0.5D)) {
								worldObj.setBlock(var6, var5, var7,
										Blocks.bedrock);
							}
						} else if (var5 > var3) {
							worldObj.setBlock(var6, var5, var7, Blocks.air);
						} else if (var12 > (var4 - 1 - 0.5D)
								* (var4 - 1 - 0.5D)) {
							worldObj.setBlock(var6, var5, var7, Blocks.bedrock);
						} else {
							worldObj.setBlock(var6, var5, var7,
									Blocks.end_portal);
						}
					}
				}
			}
		}

		worldObj.setBlock(p_70975_1_, var3 + 0, p_70975_2_, Blocks.bedrock);
		worldObj.setBlock(p_70975_1_, var3 + 1, p_70975_2_, Blocks.bedrock);
		worldObj.setBlock(p_70975_1_, var3 + 2, p_70975_2_, Blocks.bedrock);
		worldObj.setBlock(p_70975_1_ - 1, var3 + 2, p_70975_2_, Blocks.torch);
		worldObj.setBlock(p_70975_1_ + 1, var3 + 2, p_70975_2_, Blocks.torch);
		worldObj.setBlock(p_70975_1_, var3 + 2, p_70975_2_ - 1, Blocks.torch);
		worldObj.setBlock(p_70975_1_, var3 + 2, p_70975_2_ + 1, Blocks.torch);
		worldObj.setBlock(p_70975_1_, var3 + 3, p_70975_2_, Blocks.bedrock);
		worldObj.setBlock(p_70975_1_, var3 + 4, p_70975_2_, Blocks.dragon_egg);
		BlockEndPortal.field_149948_a = false;
	}

	/**
	 * Makes the entity despawn if requirements are reached
	 */
	@Override
	public void despawnEntity() {
	}

	/**
	 * Destroys all blocks that aren't associated with 'The End' inside the
	 * given bounding box.
	 */
	private boolean destroyBlocksInAABB(AxisAlignedBB p_70972_1_) {
		final int var2 = MathHelper.floor_double(p_70972_1_.minX);
		final int var3 = MathHelper.floor_double(p_70972_1_.minY);
		final int var4 = MathHelper.floor_double(p_70972_1_.minZ);
		final int var5 = MathHelper.floor_double(p_70972_1_.maxX);
		final int var6 = MathHelper.floor_double(p_70972_1_.maxY);
		final int var7 = MathHelper.floor_double(p_70972_1_.maxZ);
		boolean var8 = false;
		boolean var9 = false;

		for (int var10 = var2; var10 <= var5; ++var10) {
			for (int var11 = var3; var11 <= var6; ++var11) {
				for (int var12 = var4; var12 <= var7; ++var12) {
					final Block var13 = worldObj.getBlock(var10, var11, var12);

					if (var13.getMaterial() != Material.air) {
						if (var13 != Blocks.obsidian
								&& var13 != Blocks.end_stone
								&& var13 != Blocks.bedrock
								&& worldObj.getGameRules()
										.getGameRuleBooleanValue("mobGriefing")) {
							var9 = worldObj.setBlockToAir(var10, var11, var12)
									|| var9;
						} else {
							var8 = true;
						}
					}
				}
			}
		}

		if (var9) {
			final double var16 = p_70972_1_.minX
					+ (p_70972_1_.maxX - p_70972_1_.minX) * rand.nextFloat();
			final double var17 = p_70972_1_.minY
					+ (p_70972_1_.maxY - p_70972_1_.minY) * rand.nextFloat();
			final double var14 = p_70972_1_.minZ
					+ (p_70972_1_.maxZ - p_70972_1_.minZ) * rand.nextFloat();
			worldObj.spawnParticle("largeexplode", var16, var17, var14, 0.0D,
					0.0D, 0.0D);
		}

		return var8;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
	}

	@Override
	public World func_82194_d() {
		return worldObj;
	}

	protected boolean func_82195_e(DamageSource p_82195_1_, float p_82195_2_) {
		return super.attackEntityFrom(p_82195_1_, p_82195_2_);
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.enderdragon.hit";
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return "mob.enderdragon.growl";
	}

	/**
	 * Returns a double[3] array with movement offsets, used to calculate
	 * trailing tail/neck positions. [0] = yaw offset, [1] = y offset, [2] =
	 * unused, always 0. Parameters: buffer index offset, partial ticks.
	 */
	public double[] getMovementOffsets(int p_70974_1_, float p_70974_2_) {
		if (getHealth() <= 0.0F) {
			p_70974_2_ = 0.0F;
		}

		p_70974_2_ = 1.0F - p_70974_2_;
		final int var3 = ringBufferIndex - p_70974_1_ * 1 & 63;
		final int var4 = ringBufferIndex - p_70974_1_ * 1 - 1 & 63;
		final double[] var5 = new double[3];
		double var6 = ringBuffer[var3][0];
		double var8 = MathHelper.wrapAngleTo180_double(ringBuffer[var4][0]
				- var6);
		var5[0] = var6 + var8 * p_70974_2_;
		var6 = ringBuffer[var3][1];
		var8 = ringBuffer[var4][1] - var6;
		var5[1] = var6 + var8 * p_70974_2_;
		var5[2] = ringBuffer[var3][2]
				+ (ringBuffer[var4][2] - ringBuffer[var3][2]) * p_70974_2_;
		return var5;
	}

	/**
	 * Return the Entity parts making up this Entity (currently only for
	 * dragons)
	 */
	@Override
	public Entity[] getParts() {
		return dragonPartArray;
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume() {
		return 5.0F;
	}

	/**
	 * handles entity death timer, experience orb and particle creation
	 */
	@Override
	protected void onDeathUpdate() {
		++deathTicks;

		if (deathTicks >= 180 && deathTicks <= 200) {
			final float var1 = (rand.nextFloat() - 0.5F) * 8.0F;
			final float var2 = (rand.nextFloat() - 0.5F) * 4.0F;
			final float var3 = (rand.nextFloat() - 0.5F) * 8.0F;
			worldObj.spawnParticle("hugeexplosion", posX + var1, posY + 2.0D
					+ var2, posZ + var3, 0.0D, 0.0D, 0.0D);
		}

		int var4;
		int var5;

		if (!worldObj.isClient) {
			if (deathTicks > 150 && deathTicks % 5 == 0) {
				var4 = 1000;

				while (var4 > 0) {
					var5 = EntityXPOrb.getXPSplit(var4);
					var4 -= var5;
					worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX,
							posY, posZ, var5));
				}
			}

			if (deathTicks == 1) {
				worldObj.playBroadcastSound(1018, (int) posX, (int) posY,
						(int) posZ, 0);
			}
		}

		moveEntity(0.0D, 0.10000000149011612D, 0.0D);
		renderYawOffset = rotationYaw += 20.0F;

		if (deathTicks == 200 && !worldObj.isClient) {
			var4 = 2000;

			while (var4 > 0) {
				var5 = EntityXPOrb.getXPSplit(var4);
				var4 -= var5;
				worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX,
						posY, posZ, var5));
			}

			createEnderPortal(MathHelper.floor_double(posX),
					MathHelper.floor_double(posZ));
			setDead();
		}
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		float var1;
		float var2;

		if (worldObj.isClient) {
			var1 = MathHelper.cos(animTime * (float) Math.PI * 2.0F);
			var2 = MathHelper.cos(prevAnimTime * (float) Math.PI * 2.0F);

			if (var2 <= -0.3F && var1 >= -0.3F) {
				worldObj.playSound(posX, posY, posZ, "mob.enderdragon.wings",
						5.0F, 0.8F + rand.nextFloat() * 0.3F, false);
			}
		}

		prevAnimTime = animTime;
		float var3;

		if (getHealth() <= 0.0F) {
			var1 = (rand.nextFloat() - 0.5F) * 8.0F;
			var2 = (rand.nextFloat() - 0.5F) * 4.0F;
			var3 = (rand.nextFloat() - 0.5F) * 8.0F;
			worldObj.spawnParticle("largeexplode", posX + var1, posY + 2.0D
					+ var2, posZ + var3, 0.0D, 0.0D, 0.0D);
		} else {
			updateDragonEnderCrystal();
			var1 = 0.2F / (MathHelper.sqrt_double(motionX * motionX + motionZ
					* motionZ) * 10.0F + 1.0F);
			var1 *= (float) Math.pow(2.0D, motionY);

			if (slowed) {
				animTime += var1 * 0.5F;
			} else {
				animTime += var1;
			}

			rotationYaw = MathHelper.wrapAngleTo180_float(rotationYaw);

			if (ringBufferIndex < 0) {
				for (int var25 = 0; var25 < ringBuffer.length; ++var25) {
					ringBuffer[var25][0] = rotationYaw;
					ringBuffer[var25][1] = posY;
				}
			}

			if (++ringBufferIndex == ringBuffer.length) {
				ringBufferIndex = 0;
			}

			ringBuffer[ringBufferIndex][0] = rotationYaw;
			ringBuffer[ringBufferIndex][1] = posY;
			double var4;
			double var6;
			double var8;
			double var26;
			float var31;

			if (worldObj.isClient) {
				if (newPosRotationIncrements > 0) {
					var26 = posX + (newPosX - posX) / newPosRotationIncrements;
					var4 = posY + (newPosY - posY) / newPosRotationIncrements;
					var6 = posZ + (newPosZ - posZ) / newPosRotationIncrements;
					var8 = MathHelper.wrapAngleTo180_double(newRotationYaw
							- rotationYaw);
					rotationYaw = (float) (rotationYaw + var8
							/ newPosRotationIncrements);
					rotationPitch = (float) (rotationPitch + (newRotationPitch - rotationPitch)
							/ newPosRotationIncrements);
					--newPosRotationIncrements;
					setPosition(var26, var4, var6);
					setRotation(rotationYaw, rotationPitch);
				}
			} else {
				var26 = targetX - posX;
				var4 = targetY - posY;
				var6 = targetZ - posZ;
				var8 = var26 * var26 + var4 * var4 + var6 * var6;

				if (target != null) {
					targetX = target.posX;
					targetZ = target.posZ;
					final double var10 = targetX - posX;
					final double var12 = targetZ - posZ;
					final double var14 = Math.sqrt(var10 * var10 + var12
							* var12);
					double var16 = 0.4000000059604645D + var14 / 80.0D - 1.0D;

					if (var16 > 10.0D) {
						var16 = 10.0D;
					}

					targetY = target.boundingBox.minY + var16;
				} else {
					targetX += rand.nextGaussian() * 2.0D;
					targetZ += rand.nextGaussian() * 2.0D;
				}

				if (forceNewTarget || var8 < 100.0D || var8 > 22500.0D
						|| isCollidedHorizontally || isCollidedVertically) {
					setNewTarget();
				}

				var4 /= MathHelper.sqrt_double(var26 * var26 + var6 * var6);
				var31 = 0.6F;

				if (var4 < -var31) {
					var4 = -var31;
				}

				if (var4 > var31) {
					var4 = var31;
				}

				motionY += var4 * 0.10000000149011612D;
				rotationYaw = MathHelper.wrapAngleTo180_float(rotationYaw);
				final double var11 = 180.0D - Math.atan2(var26, var6) * 180.0D
						/ Math.PI;
				double var13 = MathHelper.wrapAngleTo180_double(var11
						- rotationYaw);

				if (var13 > 50.0D) {
					var13 = 50.0D;
				}

				if (var13 < -50.0D) {
					var13 = -50.0D;
				}

				final Vec3 var15 = Vec3.createVectorHelper(targetX - posX,
						targetY - posY, targetZ - posZ).normalize();
				final Vec3 var39 = Vec3
						.createVectorHelper(
								MathHelper.sin(rotationYaw * (float) Math.PI
										/ 180.0F),
								motionY,
								-MathHelper.cos(rotationYaw * (float) Math.PI
										/ 180.0F)).normalize();
				float var17 = (float) (var39.dotProduct(var15) + 0.5D) / 1.5F;

				if (var17 < 0.0F) {
					var17 = 0.0F;
				}

				randomYawVelocity *= 0.8F;
				final float var18 = MathHelper.sqrt_double(motionX * motionX
						+ motionZ * motionZ) * 1.0F + 1.0F;
				double var19 = Math.sqrt(motionX * motionX + motionZ * motionZ) * 1.0D + 1.0D;

				if (var19 > 40.0D) {
					var19 = 40.0D;
				}

				randomYawVelocity = (float) (randomYawVelocity + var13
						* (0.699999988079071D / var19 / var18));
				rotationYaw += randomYawVelocity * 0.1F;
				final float var21 = (float) (2.0D / (var19 + 1.0D));
				final float var22 = 0.06F;
				moveFlying(0.0F, -1.0F, var22
						* (var17 * var21 + (1.0F - var21)));

				if (slowed) {
					moveEntity(motionX * 0.800000011920929D,
							motionY * 0.800000011920929D,
							motionZ * 0.800000011920929D);
				} else {
					moveEntity(motionX, motionY, motionZ);
				}

				final Vec3 var23 = Vec3.createVectorHelper(motionX, motionY,
						motionZ).normalize();
				float var24 = (float) (var23.dotProduct(var39) + 1.0D) / 2.0F;
				var24 = 0.8F + 0.15F * var24;
				motionX *= var24;
				motionZ *= var24;
				motionY *= 0.9100000262260437D;
			}

			renderYawOffset = rotationYaw;
			dragonPartHead.width = dragonPartHead.height = 3.0F;
			dragonPartTail1.width = dragonPartTail1.height = 2.0F;
			dragonPartTail2.width = dragonPartTail2.height = 2.0F;
			dragonPartTail3.width = dragonPartTail3.height = 2.0F;
			dragonPartBody.height = 3.0F;
			dragonPartBody.width = 5.0F;
			dragonPartWing1.height = 2.0F;
			dragonPartWing1.width = 4.0F;
			dragonPartWing2.height = 3.0F;
			dragonPartWing2.width = 4.0F;
			var2 = (float) (getMovementOffsets(5, 1.0F)[1] - getMovementOffsets(
					10, 1.0F)[1]) * 10.0F / 180.0F * (float) Math.PI;
			var3 = MathHelper.cos(var2);
			final float var27 = -MathHelper.sin(var2);
			final float var5 = rotationYaw * (float) Math.PI / 180.0F;
			final float var28 = MathHelper.sin(var5);
			final float var7 = MathHelper.cos(var5);
			dragonPartBody.onUpdate();
			dragonPartBody.setLocationAndAngles(posX + var28 * 0.5F, posY, posZ
					- var7 * 0.5F, 0.0F, 0.0F);
			dragonPartWing1.onUpdate();
			dragonPartWing1.setLocationAndAngles(posX + var7 * 4.5F,
					posY + 2.0D, posZ + var28 * 4.5F, 0.0F, 0.0F);
			dragonPartWing2.onUpdate();
			dragonPartWing2.setLocationAndAngles(posX - var7 * 4.5F,
					posY + 2.0D, posZ - var28 * 4.5F, 0.0F, 0.0F);

			if (!worldObj.isClient && hurtTime == 0) {
				collideWithEntities(worldObj
						.getEntitiesWithinAABBExcludingEntity(
								this,
								dragonPartWing1.boundingBox.expand(4.0D, 2.0D,
										4.0D).offset(0.0D, -2.0D, 0.0D)));
				collideWithEntities(worldObj
						.getEntitiesWithinAABBExcludingEntity(
								this,
								dragonPartWing2.boundingBox.expand(4.0D, 2.0D,
										4.0D).offset(0.0D, -2.0D, 0.0D)));
				attackEntitiesInList(worldObj
						.getEntitiesWithinAABBExcludingEntity(this,
								dragonPartHead.boundingBox.expand(1.0D, 1.0D,
										1.0D)));
			}

			final double[] var29 = getMovementOffsets(5, 1.0F);
			final double[] var9 = getMovementOffsets(0, 1.0F);
			var31 = MathHelper.sin(rotationYaw * (float) Math.PI / 180.0F
					- randomYawVelocity * 0.01F);
			final float var33 = MathHelper.cos(rotationYaw * (float) Math.PI
					/ 180.0F - randomYawVelocity * 0.01F);
			dragonPartHead.onUpdate();
			dragonPartHead.setLocationAndAngles(posX + var31 * 5.5F * var3,
					posY + (var9[1] - var29[1]) * 1.0D + var27 * 5.5F, posZ
							- var33 * 5.5F * var3, 0.0F, 0.0F);

			for (int var30 = 0; var30 < 3; ++var30) {
				EntityDragonPart var32 = null;

				if (var30 == 0) {
					var32 = dragonPartTail1;
				}

				if (var30 == 1) {
					var32 = dragonPartTail2;
				}

				if (var30 == 2) {
					var32 = dragonPartTail3;
				}

				final double[] var34 = getMovementOffsets(12 + var30 * 2, 1.0F);
				final float var35 = rotationYaw * (float) Math.PI / 180.0F
						+ simplifyAngle(var34[0] - var29[0]) * (float) Math.PI
						/ 180.0F * 1.0F;
				final float var36 = MathHelper.sin(var35);
				final float var37 = MathHelper.cos(var35);
				final float var38 = 1.5F;
				final float var40 = (var30 + 1) * 2.0F;
				var32.onUpdate();
				var32.setLocationAndAngles(posX
						- (var28 * var38 + var36 * var40) * var3, posY
						+ (var34[1] - var29[1]) * 1.0D - (var40 + var38)
						* var27 + 1.5D, posZ + (var7 * var38 + var37 * var40)
						* var3, 0.0F, 0.0F);
			}

			if (!worldObj.isClient) {
				slowed = destroyBlocksInAABB(dragonPartHead.boundingBox)
						| destroyBlocksInAABB(dragonPartBody.boundingBox);
			}
		}
	}

	/**
	 * Sets a new target for the flight AI. It can be a random coordinate or a
	 * nearby player.
	 */
	private void setNewTarget() {
		forceNewTarget = false;

		if (rand.nextInt(2) == 0 && !worldObj.playerEntities.isEmpty()) {
			target = (Entity) worldObj.playerEntities.get(rand
					.nextInt(worldObj.playerEntities.size()));
		} else {
			boolean var1 = false;

			do {
				targetX = 0.0D;
				targetY = 70.0F + rand.nextFloat() * 50.0F;
				targetZ = 0.0D;
				targetX += rand.nextFloat() * 120.0F - 60.0F;
				targetZ += rand.nextFloat() * 120.0F - 60.0F;
				final double var2 = posX - targetX;
				final double var4 = posY - targetY;
				final double var6 = posZ - targetZ;
				var1 = var2 * var2 + var4 * var4 + var6 * var6 > 100.0D;
			} while (!var1);

			target = null;
		}
	}

	/**
	 * Simplifies the value of a number by adding/subtracting 180 to the point
	 * that the number is between -180 and 180.
	 */
	private float simplifyAngle(double p_70973_1_) {
		return (float) MathHelper.wrapAngleTo180_double(p_70973_1_);
	}

	/**
	 * Updates the state of the enderdragon's current endercrystal.
	 */
	private void updateDragonEnderCrystal() {
		if (healingEnderCrystal != null) {
			if (healingEnderCrystal.isDead) {
				if (!worldObj.isClient) {
					attackEntityFromPart(dragonPartHead,
							DamageSource.setExplosionSource((Explosion) null),
							10.0F);
				}

				healingEnderCrystal = null;
			} else if (ticksExisted % 10 == 0 && getHealth() < getMaxHealth()) {
				setHealth(getHealth() + 1.0F);
			}
		}

		if (rand.nextInt(10) == 0) {
			final float var1 = 32.0F;
			final List var2 = worldObj.getEntitiesWithinAABB(
					EntityEnderCrystal.class,
					boundingBox.expand(var1, var1, var1));
			EntityEnderCrystal var3 = null;
			double var4 = Double.MAX_VALUE;
			final Iterator var6 = var2.iterator();

			while (var6.hasNext()) {
				final EntityEnderCrystal var7 = (EntityEnderCrystal) var6
						.next();
				final double var8 = var7.getDistanceSqToEntity(this);

				if (var8 < var4) {
					var4 = var8;
					var3 = var7;
				}
			}

			healingEnderCrystal = var3;
		}
	}
}
