package net.minecraft.entity.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBoat extends Entity {
	private double boatPitch;
	private int boatPosRotationIncrements;
	private double boatX;
	private double boatY;
	private double boatYaw;
	private double boatZ;
	/** true if no player in boat */
	private boolean isBoatEmpty;
	private double speedMultiplier;
	private double velocityX;
	private double velocityY;
	private double velocityZ;

	public EntityBoat(World p_i1704_1_) {
		super(p_i1704_1_);
		isBoatEmpty = true;
		speedMultiplier = 0.07D;
		preventEntitySpawning = true;
		setSize(1.5F, 0.6F);
		yOffset = height / 2.0F;
	}

	public EntityBoat(World p_i1705_1_, double p_i1705_2_, double p_i1705_4_,
			double p_i1705_6_) {
		this(p_i1705_1_);
		setPosition(p_i1705_2_, p_i1705_4_ + yOffset, p_i1705_6_);
		motionX = 0.0D;
		motionY = 0.0D;
		motionZ = 0.0D;
		prevPosX = p_i1705_2_;
		prevPosY = p_i1705_4_;
		prevPosZ = p_i1705_6_;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else if (!worldObj.isClient && !isDead) {
			setForwardDirection(-getForwardDirection());
			setTimeSinceHit(10);
			setDamageTaken(getDamageTaken() + p_70097_2_ * 10.0F);
			setBeenAttacked();
			final boolean var3 = p_70097_1_.getEntity() instanceof EntityPlayer
					&& ((EntityPlayer) p_70097_1_.getEntity()).capabilities.isCreativeMode;

			if (var3 || getDamageTaken() > 40.0F) {
				if (riddenByEntity != null) {
					riddenByEntity.mountEntity(this);
					System.out.println(this);
				}

				if (!var3) {
					func_145778_a(Items.boat, 1, 0.0F);
				}

				setDead();
			}

			return true;
		} else
			return true;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through
	 * this Entity.
	 */
	@Override
	public boolean canBeCollidedWith() {
		return !isDead;
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities
	 * when colliding.
	 */
	@Override
	public boolean canBePushed() {
		return true;
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
		dataWatcher.addObject(17, new Integer(0));
		dataWatcher.addObject(18, new Integer(1));
		dataWatcher.addObject(19, new Float(0.0F));
	}

	/**
	 * returns the bounding box for this entity
	 */
	@Override
	public AxisAlignedBB getBoundingBox() {
		return boundingBox;
	}

	/**
	 * Returns a boundingBox used to collide the entity with other entities and
	 * blocks. This enables the entity to be pushable on contact, like boats or
	 * minecarts.
	 */
	@Override
	public AxisAlignedBB getCollisionBox(Entity p_70114_1_) {
		return p_70114_1_.boundingBox;
	}

	/**
	 * Gets the damage taken from the last hit.
	 */
	public float getDamageTaken() {
		return dataWatcher.getWatchableObjectFloat(19);
	}

	/**
	 * Gets the forward direction of the entity.
	 */
	public int getForwardDirection() {
		return dataWatcher.getWatchableObjectInt(18);
	}

	/**
	 * Returns the Y offset from the entity's position for any entity riding
	 * this one.
	 */
	@Override
	public double getMountedYOffset() {
		return height * 0.0D - 0.30000001192092896D;
	}

	@Override
	public float getShadowSize() {
		return 0.0F;
	}

	/**
	 * Gets the time since the last hit.
	 */
	public int getTimeSinceHit() {
		return dataWatcher.getWatchableObjectInt(17);
	}

	/**
	 * First layer of player interaction
	 */
	@Override
	public boolean interactFirst(EntityPlayer p_130002_1_) {
		if (riddenByEntity != null && riddenByEntity instanceof EntityPlayer
				&& riddenByEntity != p_130002_1_)
			return true;
		else {
			if (!worldObj.isClient) {
				p_130002_1_.mountEntity(this);
			}

			return true;
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		super.onUpdate();

		if (getTimeSinceHit() > 0) {
			setTimeSinceHit(getTimeSinceHit() - 1);
		}

		if (getDamageTaken() > 0.0F) {
			setDamageTaken(getDamageTaken() - 1.0F);
		}

		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		final byte var1 = 5;
		double var2 = 0.0D;

		for (int var4 = 0; var4 < var1; ++var4) {
			final double var5 = boundingBox.minY
					+ (boundingBox.maxY - boundingBox.minY) * (var4 + 0) / var1
					- 0.125D;
			final double var7 = boundingBox.minY
					+ (boundingBox.maxY - boundingBox.minY) * (var4 + 1) / var1
					- 0.125D;
			final AxisAlignedBB var9 = AxisAlignedBB.getBoundingBox(
					boundingBox.minX, var5, boundingBox.minZ, boundingBox.maxX,
					var7, boundingBox.maxZ);

			if (worldObj.isAABBInMaterial(var9, Material.water)) {
				var2 += 1.0D / var1;
			}
		}

		final double var19 = Math.sqrt(motionX * motionX + motionZ * motionZ);
		double var6;
		double var8;
		int var10;

		if (var19 > 0.26249999999999996D) {
			var6 = Math.cos(rotationYaw * Math.PI / 180.0D);
			var8 = Math.sin(rotationYaw * Math.PI / 180.0D);

			for (var10 = 0; var10 < 1.0D + var19 * 60.0D; ++var10) {
				final double var11 = rand.nextFloat() * 2.0F - 1.0F;
				final double var13 = (rand.nextInt(2) * 2 - 1) * 0.7D;
				double var15;
				double var17;

				if (rand.nextBoolean()) {
					var15 = posX - var6 * var11 * 0.8D + var8 * var13;
					var17 = posZ - var8 * var11 * 0.8D - var6 * var13;
					worldObj.spawnParticle("splash", var15, posY - 0.125D,
							var17, motionX, motionY, motionZ);
				} else {
					var15 = posX + var6 + var8 * var11 * 0.7D;
					var17 = posZ + var8 - var6 * var11 * 0.7D;
					worldObj.spawnParticle("splash", var15, posY - 0.125D,
							var17, motionX, motionY, motionZ);
				}
			}
		}

		double var24;
		double var26;

		if (worldObj.isClient && isBoatEmpty) {
			if (boatPosRotationIncrements > 0) {
				var6 = posX + (boatX - posX) / boatPosRotationIncrements;
				var8 = posY + (boatY - posY) / boatPosRotationIncrements;
				var24 = posZ + (boatZ - posZ) / boatPosRotationIncrements;
				var26 = MathHelper.wrapAngleTo180_double(boatYaw - rotationYaw);
				rotationYaw = (float) (rotationYaw + var26
						/ boatPosRotationIncrements);
				rotationPitch = (float) (rotationPitch + (boatPitch - rotationPitch)
						/ boatPosRotationIncrements);
				--boatPosRotationIncrements;
				setPosition(var6, var8, var24);
				setRotation(rotationYaw, rotationPitch);
			} else {
				var6 = posX + motionX;
				var8 = posY + motionY;
				var24 = posZ + motionZ;
				setPosition(var6, var8, var24);

				if (onGround) {
					motionX *= 0.5D;
					motionY *= 0.5D;
					motionZ *= 0.5D;
				}

				motionX *= 0.9900000095367432D;
				motionY *= 0.949999988079071D;
				motionZ *= 0.9900000095367432D;
			}
		} else {
			if (var2 < 1.0D) {
				var6 = var2 * 2.0D - 1.0D;
				motionY += 0.03999999910593033D * var6;
			} else {
				if (motionY < 0.0D) {
					motionY /= 2.0D;
				}

				motionY += 0.007000000216066837D;
			}

			if (riddenByEntity != null
					&& riddenByEntity instanceof EntityLivingBase) {
				final EntityLivingBase var20 = (EntityLivingBase) riddenByEntity;
				final float var21 = riddenByEntity.rotationYaw
						+ -var20.moveStrafing * 90.0F;
				motionX += -Math.sin(var21 * (float) Math.PI / 180.0F)
						* speedMultiplier * var20.moveForward
						* 0.05000000074505806D;
				motionZ += Math.cos(var21 * (float) Math.PI / 180.0F)
						* speedMultiplier * var20.moveForward
						* 0.05000000074505806D;
			}

			var6 = Math.sqrt(motionX * motionX + motionZ * motionZ);

			if (var6 > 0.35D) {
				var8 = 0.35D / var6;
				motionX *= var8;
				motionZ *= var8;
				var6 = 0.35D;
			}

			if (var6 > var19 && speedMultiplier < 0.35D) {
				speedMultiplier += (0.35D - speedMultiplier) / 35.0D;

				if (speedMultiplier > 0.35D) {
					speedMultiplier = 0.35D;
				}
			} else {
				speedMultiplier -= (speedMultiplier - 0.07D) / 35.0D;

				if (speedMultiplier < 0.07D) {
					speedMultiplier = 0.07D;
				}
			}

			int var22;

			for (var22 = 0; var22 < 4; ++var22) {
				final int var23 = MathHelper.floor_double(posX
						+ (var22 % 2 - 0.5D) * 0.8D);
				var10 = MathHelper.floor_double(posZ + (var22 / 2 - 0.5D)
						* 0.8D);

				for (int var25 = 0; var25 < 2; ++var25) {
					final int var12 = MathHelper.floor_double(posY) + var25;
					final Block var27 = worldObj.getBlock(var23, var12, var10);

					if (var27 == Blocks.snow_layer) {
						worldObj.setBlockToAir(var23, var12, var10);
						isCollidedHorizontally = false;
					} else if (var27 == Blocks.waterlily) {
						worldObj.func_147480_a(var23, var12, var10, true);
						isCollidedHorizontally = false;
					}
				}
			}

			if (onGround) {
				motionX *= 0.5D;
				motionY *= 0.5D;
				motionZ *= 0.5D;
			}

			moveEntity(motionX, motionY, motionZ);

			if (isCollidedHorizontally && var19 > 0.2D) {
				if (!worldObj.isClient && !isDead) {
					setDead();

					for (var22 = 0; var22 < 3; ++var22) {
						func_145778_a(Item.getItemFromBlock(Blocks.planks), 1,
								0.0F);
					}

					for (var22 = 0; var22 < 2; ++var22) {
						func_145778_a(Items.stick, 1, 0.0F);
					}
				}
			} else {
				motionX *= 0.9900000095367432D;
				motionY *= 0.949999988079071D;
				motionZ *= 0.9900000095367432D;
			}

			rotationPitch = 0.0F;
			var8 = rotationYaw;
			var24 = prevPosX - posX;
			var26 = prevPosZ - posZ;

			if (var24 * var24 + var26 * var26 > 0.001D) {
				var8 = (float) (Math.atan2(var26, var24) * 180.0D / Math.PI);
			}

			double var14 = MathHelper.wrapAngleTo180_double(var8 - rotationYaw);

			if (var14 > 20.0D) {
				var14 = 20.0D;
			}

			if (var14 < -20.0D) {
				var14 = -20.0D;
			}

			rotationYaw = (float) (rotationYaw + var14);
			setRotation(rotationYaw, rotationPitch);

			if (!worldObj.isClient) {
				final List var16 = worldObj
						.getEntitiesWithinAABBExcludingEntity(this, boundingBox
								.expand(0.20000000298023224D, 0.0D,
										0.20000000298023224D));

				if (var16 != null && !var16.isEmpty()) {
					for (int var28 = 0; var28 < var16.size(); ++var28) {
						final Entity var18 = (Entity) var16.get(var28);

						if (var18 != riddenByEntity && var18.canBePushed()
								&& var18 instanceof EntityBoat) {
							var18.applyEntityCollision(this);
						}
					}
				}

				if (riddenByEntity != null && riddenByEntity.isDead) {
					riddenByEntity = null;
				}
			}
		}
	}

	/**
	 * Setups the entity to do the hurt animation. Only used by packets in
	 * multiplayer.
	 */
	@Override
	public void performHurtAnimation() {
		setForwardDirection(-getForwardDirection());
		setTimeSinceHit(10);
		setDamageTaken(getDamageTaken() * 11.0F);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
	}

	/**
	 * Sets the damage taken from the last hit.
	 */
	public void setDamageTaken(float p_70266_1_) {
		dataWatcher.updateObject(19, Float.valueOf(p_70266_1_));
	}

	/**
	 * Sets the forward direction of the entity.
	 */
	public void setForwardDirection(int p_70269_1_) {
		dataWatcher.updateObject(18, Integer.valueOf(p_70269_1_));
	}

	/**
	 * true if no player in boat
	 */
	public void setIsBoatEmpty(boolean p_70270_1_) {
		isBoatEmpty = p_70270_1_;
	}

	/**
	 * Sets the position and rotation. Only difference from the other one is no
	 * bounding on the rotation. Args: posX, posY, posZ, yaw, pitch
	 */
	@Override
	public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_,
			double p_70056_5_, float p_70056_7_, float p_70056_8_,
			int p_70056_9_) {
		if (isBoatEmpty) {
			boatPosRotationIncrements = p_70056_9_ + 5;
		} else {
			final double var10 = p_70056_1_ - posX;
			final double var12 = p_70056_3_ - posY;
			final double var14 = p_70056_5_ - posZ;
			final double var16 = var10 * var10 + var12 * var12 + var14 * var14;

			if (var16 <= 1.0D)
				return;

			boatPosRotationIncrements = 3;
		}

		boatX = p_70056_1_;
		boatY = p_70056_3_;
		boatZ = p_70056_5_;
		boatYaw = p_70056_7_;
		boatPitch = p_70056_8_;
		motionX = velocityX;
		motionY = velocityY;
		motionZ = velocityZ;
	}

	/**
	 * Sets the time to count down from since the last time entity was hit.
	 */
	public void setTimeSinceHit(int p_70265_1_) {
		dataWatcher.updateObject(17, Integer.valueOf(p_70265_1_));
	}

	/**
	 * Sets the velocity to the args. Args: x, y, z
	 */
	@Override
	public void setVelocity(double p_70016_1_, double p_70016_3_,
			double p_70016_5_) {
		velocityX = motionX = p_70016_1_;
		velocityY = motionY = p_70016_3_;
		velocityZ = motionZ = p_70016_5_;
	}

	/**
	 * Takes in the distance the entity has fallen this tick and whether its on
	 * the ground to update the fall distance and deal fall damage if landing on
	 * the ground. Args: distanceFallenThisTick, onGround
	 */
	@Override
	protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {
		final int var4 = MathHelper.floor_double(posX);
		final int var5 = MathHelper.floor_double(posY);
		final int var6 = MathHelper.floor_double(posZ);

		if (p_70064_3_) {
			if (fallDistance > 3.0F) {
				fall(fallDistance);

				if (!worldObj.isClient && !isDead) {
					setDead();
					int var7;

					for (var7 = 0; var7 < 3; ++var7) {
						func_145778_a(Item.getItemFromBlock(Blocks.planks), 1,
								0.0F);
					}

					for (var7 = 0; var7 < 2; ++var7) {
						func_145778_a(Items.stick, 1, 0.0F);
					}
				}

				fallDistance = 0.0F;
			}
		} else if (worldObj.getBlock(var4, var5 - 1, var6).getMaterial() != Material.water
				&& p_70064_1_ < 0.0D) {
			fallDistance = (float) (fallDistance - p_70064_1_);
		}
	}

	@Override
	public void updateRiderPosition() {
		if (riddenByEntity != null) {
			final double var1 = Math.cos(rotationYaw * Math.PI / 180.0D) * 0.4D;
			final double var3 = Math.sin(rotationYaw * Math.PI / 180.0D) * 0.4D;
			riddenByEntity.setPosition(posX + var1, posY + getMountedYOffset()
					+ riddenByEntity.getYOffset(), posZ + var3);
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
	}
}
