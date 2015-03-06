package net.minecraft.entity.projectile;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityArrow extends Entity implements IProjectile {
	/** Seems to be some sort of timer for animating an arrow. */
	public int arrowShake;
	/** 1 if the player can pick up the arrow */
	public int canBePickedUp;
	private double damage = 2.0D;
	private int field_145789_f = -1;
	private Block field_145790_g;
	private int field_145791_d = -1;

	private int field_145792_e = -1;

	private int inData;

	private boolean inGround;
	/** The amount of knockback an arrow applies when it hits a mob. */
	private int knockbackStrength;
	/** The owner of this arrow. */
	public Entity shootingEntity;
	private int ticksInAir;

	private int ticksInGround;

	public EntityArrow(World p_i1753_1_) {
		super(p_i1753_1_);
		renderDistanceWeight = 10.0D;
		setSize(0.5F, 0.5F);
	}

	public EntityArrow(World p_i1754_1_, double p_i1754_2_, double p_i1754_4_,
			double p_i1754_6_) {
		super(p_i1754_1_);
		renderDistanceWeight = 10.0D;
		setSize(0.5F, 0.5F);
		setPosition(p_i1754_2_, p_i1754_4_, p_i1754_6_);
		yOffset = 0.0F;
	}

	public EntityArrow(World p_i1755_1_, EntityLivingBase p_i1755_2_,
			EntityLivingBase p_i1755_3_, float p_i1755_4_, float p_i1755_5_) {
		super(p_i1755_1_);
		renderDistanceWeight = 10.0D;
		shootingEntity = p_i1755_2_;

		if (p_i1755_2_ instanceof EntityPlayer) {
			canBePickedUp = 1;
		}

		posY = p_i1755_2_.posY + p_i1755_2_.getEyeHeight()
				- 0.10000000149011612D;
		final double var6 = p_i1755_3_.posX - p_i1755_2_.posX;
		final double var8 = p_i1755_3_.boundingBox.minY + p_i1755_3_.height
				/ 3.0F - posY;
		final double var10 = p_i1755_3_.posZ - p_i1755_2_.posZ;
		final double var12 = MathHelper
				.sqrt_double(var6 * var6 + var10 * var10);

		if (var12 >= 1.0E-7D) {
			final float var14 = (float) (Math.atan2(var10, var6) * 180.0D / Math.PI) - 90.0F;
			final float var15 = (float) -(Math.atan2(var8, var12) * 180.0D / Math.PI);
			final double var16 = var6 / var12;
			final double var18 = var10 / var12;
			setLocationAndAngles(p_i1755_2_.posX + var16, posY, p_i1755_2_.posZ
					+ var18, var14, var15);
			yOffset = 0.0F;
			final float var20 = (float) var12 * 0.2F;
			setThrowableHeading(var6, var8 + var20, var10, p_i1755_4_,
					p_i1755_5_);
		}
	}

	public EntityArrow(World p_i1756_1_, EntityLivingBase p_i1756_2_,
			float p_i1756_3_) {
		super(p_i1756_1_);
		renderDistanceWeight = 10.0D;
		shootingEntity = p_i1756_2_;

		if (p_i1756_2_ instanceof EntityPlayer) {
			canBePickedUp = 1;
		}

		setSize(0.5F, 0.5F);
		setLocationAndAngles(p_i1756_2_.posX,
				p_i1756_2_.posY + p_i1756_2_.getEyeHeight(), p_i1756_2_.posZ,
				p_i1756_2_.rotationYaw, p_i1756_2_.rotationPitch);
		posX -= MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
		posY -= 0.10000000149011612D;
		posZ -= MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
		setPosition(posX, posY, posZ);
		yOffset = 0.0F;
		motionX = -MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI)
				* MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
		motionZ = MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI)
				* MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
		motionY = -MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI);
		setThrowableHeading(motionX, motionY, motionZ, p_i1756_3_ * 1.5F, 1.0F);
	}

	/**
	 * If returns false, the item will not inflict any damage against entities.
	 */
	@Override
	public boolean canAttackWithItem() {
		return false;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they
	 * walk on. used for spiders and wolves to prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}

	public double getDamage() {
		return damage;
	}

	/**
	 * Whether the arrow has a stream of critical hit particles flying behind
	 * it.
	 */
	public boolean getIsCritical() {
		final byte var1 = dataWatcher.getWatchableObjectByte(16);
		return (var1 & 1) != 0;
	}

	@Override
	public float getShadowSize() {
		return 0.0F;
	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	@Override
	public void onCollideWithPlayer(EntityPlayer p_70100_1_) {
		if (!worldObj.isClient && inGround && arrowShake <= 0) {
			boolean var2 = canBePickedUp == 1 || canBePickedUp == 2
					&& p_70100_1_.capabilities.isCreativeMode;

			if (canBePickedUp == 1
					&& !p_70100_1_.inventory
							.addItemStackToInventory(new ItemStack(Items.arrow,
									1))) {
				var2 = false;
			}

			if (var2) {
				playSound(
						"random.pop",
						0.2F,
						((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				p_70100_1_.onItemPickup(this, 1);
				setDead();
			}
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		super.onUpdate();

		if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
			final float var1 = MathHelper.sqrt_double(motionX * motionX
					+ motionZ * motionZ);
			prevRotationYaw = rotationYaw = (float) (Math.atan2(motionX,
					motionZ) * 180.0D / Math.PI);
			prevRotationPitch = rotationPitch = (float) (Math.atan2(motionY,
					var1) * 180.0D / Math.PI);
		}

		final Block var16 = worldObj.getBlock(field_145791_d, field_145792_e,
				field_145789_f);

		if (var16.getMaterial() != Material.air) {
			var16.setBlockBoundsBasedOnState(worldObj, field_145791_d,
					field_145792_e, field_145789_f);
			final AxisAlignedBB var2 = var16.getCollisionBoundingBoxFromPool(
					worldObj, field_145791_d, field_145792_e, field_145789_f);

			if (var2 != null
					&& var2.isVecInside(Vec3.createVectorHelper(posX, posY,
							posZ))) {
				inGround = true;
			}
		}

		if (arrowShake > 0) {
			--arrowShake;
		}

		if (inGround) {
			final int var18 = worldObj.getBlockMetadata(field_145791_d,
					field_145792_e, field_145789_f);

			if (var16 == field_145790_g && var18 == inData) {
				++ticksInGround;

				if (ticksInGround == 1200) {
					setDead();
				}
			} else {
				inGround = false;
				motionX *= rand.nextFloat() * 0.2F;
				motionY *= rand.nextFloat() * 0.2F;
				motionZ *= rand.nextFloat() * 0.2F;
				ticksInGround = 0;
				ticksInAir = 0;
			}
		} else {
			++ticksInAir;
			Vec3 var17 = Vec3.createVectorHelper(posX, posY, posZ);
			Vec3 var3 = Vec3.createVectorHelper(posX + motionX, posY + motionY,
					posZ + motionZ);
			MovingObjectPosition var4 = worldObj.func_147447_a(var17, var3,
					false, true, false);
			var17 = Vec3.createVectorHelper(posX, posY, posZ);
			var3 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ
					+ motionZ);

			if (var4 != null) {
				var3 = Vec3.createVectorHelper(var4.hitVec.xCoord,
						var4.hitVec.yCoord, var4.hitVec.zCoord);
			}

			Entity var5 = null;
			final List var6 = worldObj.getEntitiesWithinAABBExcludingEntity(
					this, boundingBox.addCoord(motionX, motionY, motionZ)
							.expand(1.0D, 1.0D, 1.0D));
			double var7 = 0.0D;
			int var9;
			float var11;

			for (var9 = 0; var9 < var6.size(); ++var9) {
				final Entity var10 = (Entity) var6.get(var9);

				if (var10.canBeCollidedWith()
						&& (var10 != shootingEntity || ticksInAir >= 5)) {
					var11 = 0.3F;
					final AxisAlignedBB var12 = var10.boundingBox.expand(var11,
							var11, var11);
					final MovingObjectPosition var13 = var12
							.calculateIntercept(var17, var3);

					if (var13 != null) {
						final double var14 = var17.distanceTo(var13.hitVec);

						if (var14 < var7 || var7 == 0.0D) {
							var5 = var10;
							var7 = var14;
						}
					}
				}
			}

			if (var5 != null) {
				var4 = new MovingObjectPosition(var5);
			}

			if (var4 != null && var4.entityHit != null
					&& var4.entityHit instanceof EntityPlayer) {
				final EntityPlayer var19 = (EntityPlayer) var4.entityHit;

				if (var19.capabilities.disableDamage
						|| shootingEntity instanceof EntityPlayer
						&& !((EntityPlayer) shootingEntity)
								.canAttackPlayer(var19)) {
					var4 = null;
				}
			}

			float var20;
			float var26;

			if (var4 != null) {
				if (var4.entityHit != null) {
					var20 = MathHelper.sqrt_double(motionX * motionX + motionY
							* motionY + motionZ * motionZ);
					int var21 = MathHelper.ceiling_double_int(var20 * damage);

					if (getIsCritical()) {
						var21 += rand.nextInt(var21 / 2 + 2);
					}

					DamageSource var23 = null;

					if (shootingEntity == null) {
						var23 = DamageSource.causeArrowDamage(this, this);
					} else {
						var23 = DamageSource.causeArrowDamage(this,
								shootingEntity);
					}

					if (isBurning()
							&& !(var4.entityHit instanceof EntityEnderman)) {
						var4.entityHit.setFire(5);
					}

					if (var4.entityHit.attackEntityFrom(var23, var21)) {
						if (var4.entityHit instanceof EntityLivingBase) {
							final EntityLivingBase var24 = (EntityLivingBase) var4.entityHit;

							if (!worldObj.isClient) {
								var24.setArrowCountInEntity(var24
										.getArrowCountInEntity() + 1);
							}

							if (knockbackStrength > 0) {
								var26 = MathHelper.sqrt_double(motionX
										* motionX + motionZ * motionZ);

								if (var26 > 0.0F) {
									var4.entityHit.addVelocity(motionX
											* knockbackStrength
											* 0.6000000238418579D / var26,
											0.1D, motionZ * knockbackStrength
													* 0.6000000238418579D
													/ var26);
								}
							}

							if (shootingEntity != null
									&& shootingEntity instanceof EntityLivingBase) {
								EnchantmentHelper.func_151384_a(var24,
										shootingEntity);
								EnchantmentHelper.func_151385_b(
										(EntityLivingBase) shootingEntity,
										var24);
							}

							if (shootingEntity != null
									&& var4.entityHit != shootingEntity
									&& var4.entityHit instanceof EntityPlayer
									&& shootingEntity instanceof EntityPlayerMP) {
								((EntityPlayerMP) shootingEntity).playerNetServerHandler
										.sendPacket(new S2BPacketChangeGameState(
												6, 0.0F));
							}
						}

						playSound("random.bowhit", 1.0F,
								1.2F / (rand.nextFloat() * 0.2F + 0.9F));

						if (!(var4.entityHit instanceof EntityEnderman)) {
							setDead();
						}
					} else {
						motionX *= -0.10000000149011612D;
						motionY *= -0.10000000149011612D;
						motionZ *= -0.10000000149011612D;
						rotationYaw += 180.0F;
						prevRotationYaw += 180.0F;
						ticksInAir = 0;
					}
				} else {
					field_145791_d = var4.blockX;
					field_145792_e = var4.blockY;
					field_145789_f = var4.blockZ;
					field_145790_g = worldObj.getBlock(field_145791_d,
							field_145792_e, field_145789_f);
					inData = worldObj.getBlockMetadata(field_145791_d,
							field_145792_e, field_145789_f);
					motionX = (float) (var4.hitVec.xCoord - posX);
					motionY = (float) (var4.hitVec.yCoord - posY);
					motionZ = (float) (var4.hitVec.zCoord - posZ);
					var20 = MathHelper.sqrt_double(motionX * motionX + motionY
							* motionY + motionZ * motionZ);
					posX -= motionX / var20 * 0.05000000074505806D;
					posY -= motionY / var20 * 0.05000000074505806D;
					posZ -= motionZ / var20 * 0.05000000074505806D;
					playSound("random.bowhit", 1.0F,
							1.2F / (rand.nextFloat() * 0.2F + 0.9F));
					inGround = true;
					arrowShake = 7;
					setIsCritical(false);

					if (field_145790_g.getMaterial() != Material.air) {
						field_145790_g.onEntityCollidedWithBlock(worldObj,
								field_145791_d, field_145792_e, field_145789_f,
								this);
					}
				}
			}

			if (getIsCritical()) {
				for (var9 = 0; var9 < 4; ++var9) {
					worldObj.spawnParticle("crit",
							posX + motionX * var9 / 4.0D, posY + motionY * var9
									/ 4.0D, posZ + motionZ * var9 / 4.0D,
							-motionX, -motionY + 0.2D, -motionZ);
				}
			}

			posX += motionX;
			posY += motionY;
			posZ += motionZ;
			var20 = MathHelper.sqrt_double(motionX * motionX + motionZ
					* motionZ);
			rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);

			for (rotationPitch = (float) (Math.atan2(motionY, var20) * 180.0D / Math.PI); rotationPitch
					- prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F) {
				;
			}

			while (rotationPitch - prevRotationPitch >= 180.0F) {
				prevRotationPitch += 360.0F;
			}

			while (rotationYaw - prevRotationYaw < -180.0F) {
				prevRotationYaw -= 360.0F;
			}

			while (rotationYaw - prevRotationYaw >= 180.0F) {
				prevRotationYaw += 360.0F;
			}

			rotationPitch = prevRotationPitch
					+ (rotationPitch - prevRotationPitch) * 0.2F;
			rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw)
					* 0.2F;
			float var22 = 0.99F;
			var11 = 0.05F;

			if (isInWater()) {
				for (int var25 = 0; var25 < 4; ++var25) {
					var26 = 0.25F;
					worldObj.spawnParticle("bubble", posX - motionX * var26,
							posY - motionY * var26, posZ - motionZ * var26,
							motionX, motionY, motionZ);
				}

				var22 = 0.8F;
			}

			if (isWet()) {
				extinguish();
			}

			motionX *= var22;
			motionY *= var22;
			motionZ *= var22;
			motionY -= var11;
			setPosition(posX, posY, posZ);
			func_145775_I();
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		field_145791_d = p_70037_1_.getShort("xTile");
		field_145792_e = p_70037_1_.getShort("yTile");
		field_145789_f = p_70037_1_.getShort("zTile");
		ticksInGround = p_70037_1_.getShort("life");
		field_145790_g = Block.getBlockById(p_70037_1_.getByte("inTile") & 255);
		inData = p_70037_1_.getByte("inData") & 255;
		arrowShake = p_70037_1_.getByte("shake") & 255;
		inGround = p_70037_1_.getByte("inGround") == 1;

		if (p_70037_1_.func_150297_b("damage", 99)) {
			damage = p_70037_1_.getDouble("damage");
		}

		if (p_70037_1_.func_150297_b("pickup", 99)) {
			canBePickedUp = p_70037_1_.getByte("pickup");
		} else if (p_70037_1_.func_150297_b("player", 99)) {
			canBePickedUp = p_70037_1_.getBoolean("player") ? 1 : 0;
		}
	}

	public void setDamage(double p_70239_1_) {
		damage = p_70239_1_;
	}

	/**
	 * Whether the arrow has a stream of critical hit particles flying behind
	 * it.
	 */
	public void setIsCritical(boolean p_70243_1_) {
		final byte var2 = dataWatcher.getWatchableObjectByte(16);

		if (p_70243_1_) {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 | 1)));
		} else {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 & -2)));
		}
	}

	/**
	 * Sets the amount of knockback the arrow applies when it hits a mob.
	 */
	public void setKnockbackStrength(int p_70240_1_) {
		knockbackStrength = p_70240_1_;
	}

	/**
	 * Sets the position and rotation. Only difference from the other one is no
	 * bounding on the rotation. Args: posX, posY, posZ, yaw, pitch
	 */
	@Override
	public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_,
			double p_70056_5_, float p_70056_7_, float p_70056_8_,
			int p_70056_9_) {
		setPosition(p_70056_1_, p_70056_3_, p_70056_5_);
		setRotation(p_70056_7_, p_70056_8_);
	}

	/**
	 * Similar to setArrowHeading, it's point the throwable entity to a x, y, z
	 * direction.
	 */
	@Override
	public void setThrowableHeading(double p_70186_1_, double p_70186_3_,
			double p_70186_5_, float p_70186_7_, float p_70186_8_) {
		final float var9 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_
				+ p_70186_3_ * p_70186_3_ + p_70186_5_ * p_70186_5_);
		p_70186_1_ /= var9;
		p_70186_3_ /= var9;
		p_70186_5_ /= var9;
		p_70186_1_ += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1)
				* 0.007499999832361937D * p_70186_8_;
		p_70186_3_ += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1)
				* 0.007499999832361937D * p_70186_8_;
		p_70186_5_ += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1)
				* 0.007499999832361937D * p_70186_8_;
		p_70186_1_ *= p_70186_7_;
		p_70186_3_ *= p_70186_7_;
		p_70186_5_ *= p_70186_7_;
		motionX = p_70186_1_;
		motionY = p_70186_3_;
		motionZ = p_70186_5_;
		final float var10 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_
				+ p_70186_5_ * p_70186_5_);
		prevRotationYaw = rotationYaw = (float) (Math.atan2(p_70186_1_,
				p_70186_5_) * 180.0D / Math.PI);
		prevRotationPitch = rotationPitch = (float) (Math.atan2(p_70186_3_,
				var10) * 180.0D / Math.PI);
		ticksInGround = 0;
	}

	/**
	 * Sets the velocity to the args. Args: x, y, z
	 */
	@Override
	public void setVelocity(double p_70016_1_, double p_70016_3_,
			double p_70016_5_) {
		motionX = p_70016_1_;
		motionY = p_70016_3_;
		motionZ = p_70016_5_;

		if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
			final float var7 = MathHelper.sqrt_double(p_70016_1_ * p_70016_1_
					+ p_70016_5_ * p_70016_5_);
			prevRotationYaw = rotationYaw = (float) (Math.atan2(p_70016_1_,
					p_70016_5_) * 180.0D / Math.PI);
			prevRotationPitch = rotationPitch = (float) (Math.atan2(p_70016_3_,
					var7) * 180.0D / Math.PI);
			prevRotationPitch = rotationPitch;
			prevRotationYaw = rotationYaw;
			setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
			ticksInGround = 0;
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setShort("xTile", (short) field_145791_d);
		p_70014_1_.setShort("yTile", (short) field_145792_e);
		p_70014_1_.setShort("zTile", (short) field_145789_f);
		p_70014_1_.setShort("life", (short) ticksInGround);
		p_70014_1_.setByte("inTile",
				(byte) Block.getIdFromBlock(field_145790_g));
		p_70014_1_.setByte("inData", (byte) inData);
		p_70014_1_.setByte("shake", (byte) arrowShake);
		p_70014_1_.setByte("inGround", (byte) (inGround ? 1 : 0));
		p_70014_1_.setByte("pickup", (byte) canBePickedUp);
		p_70014_1_.setDouble("damage", damage);
	}
}
