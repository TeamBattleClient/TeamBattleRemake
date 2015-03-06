package net.minecraft.entity.item;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityXPOrb extends Entity {
	/**
	 * Get a fragment of the maximum experience points value for the supplied
	 * value of experience points value.
	 */
	public static int getXPSplit(int p_70527_0_) {
		return p_70527_0_ >= 2477 ? 2477 : p_70527_0_ >= 1237 ? 1237
				: p_70527_0_ >= 617 ? 617 : p_70527_0_ >= 307 ? 307
						: p_70527_0_ >= 149 ? 149 : p_70527_0_ >= 73 ? 73
								: p_70527_0_ >= 37 ? 37 : p_70527_0_ >= 17 ? 17
										: p_70527_0_ >= 7 ? 7
												: p_70527_0_ >= 3 ? 3 : 1;
	}

	/** The closest EntityPlayer to this orb. */
	private EntityPlayer closestPlayer;
	public int field_70532_c;

	/**
	 * A constantly increasing value that RenderXPOrb uses to control the colour
	 * shifting (Green / yellow)
	 */
	public int xpColor;

	/** The age of the XP orb in ticks. */
	public int xpOrbAge;

	/** The health of this XP orb. */
	private int xpOrbHealth = 5;

	/** Threshold color for tracking players */
	private int xpTargetColor;

	/** This is how much XP this orb has. */
	private int xpValue;

	public EntityXPOrb(World p_i1586_1_) {
		super(p_i1586_1_);
		setSize(0.25F, 0.25F);
		yOffset = height / 2.0F;
	}

	public EntityXPOrb(World p_i1585_1_, double p_i1585_2_, double p_i1585_4_,
			double p_i1585_6_, int p_i1585_8_) {
		super(p_i1585_1_);
		setSize(0.5F, 0.5F);
		yOffset = height / 2.0F;
		setPosition(p_i1585_2_, p_i1585_4_, p_i1585_6_);
		rotationYaw = (float) (Math.random() * 360.0D);
		motionX = (float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F;
		motionY = (float) (Math.random() * 0.2D) * 2.0F;
		motionZ = (float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F;
		xpValue = p_i1585_8_;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else {
			setBeenAttacked();
			xpOrbHealth = (int) (xpOrbHealth - p_70097_2_);

			if (xpOrbHealth <= 0) {
				setDead();
			}

			return false;
		}
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

	/**
	 * Will deal the specified amount of damage to the entity if the entity
	 * isn't immune to fire damage. Args: amountDamage
	 */
	@Override
	protected void dealFireDamage(int p_70081_1_) {
		attackEntityFrom(DamageSource.inFire, p_70081_1_);
	}

	@Override
	protected void entityInit() {
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_) {
		float var2 = 0.5F;

		if (var2 < 0.0F) {
			var2 = 0.0F;
		}

		if (var2 > 1.0F) {
			var2 = 1.0F;
		}

		final int var3 = super.getBrightnessForRender(p_70070_1_);
		int var4 = var3 & 255;
		final int var5 = var3 >> 16 & 255;
		var4 += (int) (var2 * 15.0F * 16.0F);

		if (var4 > 240) {
			var4 = 240;
		}

		return var4 | var5 << 16;
	}

	/**
	 * Returns a number from 1 to 10 based on how much XP this orb is worth.
	 * This is used by RenderXPOrb to determine what texture to use.
	 */
	public int getTextureByXP() {
		return xpValue >= 2477 ? 10 : xpValue >= 1237 ? 9 : xpValue >= 617 ? 8
				: xpValue >= 307 ? 7 : xpValue >= 149 ? 6 : xpValue >= 73 ? 5
						: xpValue >= 37 ? 4 : xpValue >= 17 ? 3
								: xpValue >= 7 ? 2 : xpValue >= 3 ? 1 : 0;
	}

	/**
	 * Returns the XP value of this XP orb.
	 */
	public int getXpValue() {
		return xpValue;
	}

	/**
	 * Returns if this entity is in water and will end up adding the waters
	 * velocity to the entity
	 */
	@Override
	public boolean handleWaterMovement() {
		return worldObj.handleMaterialAcceleration(boundingBox, Material.water,
				this);
	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	@Override
	public void onCollideWithPlayer(EntityPlayer p_70100_1_) {
		if (!worldObj.isClient) {
			if (field_70532_c == 0 && p_70100_1_.xpCooldown == 0) {
				p_70100_1_.xpCooldown = 2;
				worldObj.playSoundAtEntity(
						p_70100_1_,
						"random.orb",
						0.1F,
						0.5F * ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.8F));
				p_70100_1_.onItemPickup(this, 1);
				p_70100_1_.addExperience(xpValue);
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

		if (field_70532_c > 0) {
			--field_70532_c;
		}

		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		motionY -= 0.029999999329447746D;

		if (worldObj.getBlock(MathHelper.floor_double(posX),
				MathHelper.floor_double(posY), MathHelper.floor_double(posZ))
				.getMaterial() == Material.lava) {
			motionY = 0.20000000298023224D;
			motionX = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
			motionZ = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
			playSound("random.fizz", 0.4F, 2.0F + rand.nextFloat() * 0.4F);
		}

		func_145771_j(posX, (boundingBox.minY + boundingBox.maxY) / 2.0D, posZ);
		final double var1 = 8.0D;

		if (xpTargetColor < xpColor - 20 + getEntityId() % 100) {
			if (closestPlayer == null
					|| closestPlayer.getDistanceSqToEntity(this) > var1 * var1) {
				closestPlayer = worldObj.getClosestPlayerToEntity(this, var1);
			}

			xpTargetColor = xpColor;
		}

		if (closestPlayer != null) {
			final double var3 = (closestPlayer.posX - posX) / var1;
			final double var5 = (closestPlayer.posY
					+ closestPlayer.getEyeHeight() - posY)
					/ var1;
			final double var7 = (closestPlayer.posZ - posZ) / var1;
			final double var9 = Math.sqrt(var3 * var3 + var5 * var5 + var7
					* var7);
			double var11 = 1.0D - var9;

			if (var11 > 0.0D) {
				var11 *= var11;
				motionX += var3 / var9 * var11 * 0.1D;
				motionY += var5 / var9 * var11 * 0.1D;
				motionZ += var7 / var9 * var11 * 0.1D;
			}
		}

		moveEntity(motionX, motionY, motionZ);
		float var13 = 0.98F;

		if (onGround) {
			var13 = worldObj.getBlock(MathHelper.floor_double(posX),
					MathHelper.floor_double(boundingBox.minY) - 1,
					MathHelper.floor_double(posZ)).slipperiness * 0.98F;
		}

		motionX *= var13;
		motionY *= 0.9800000190734863D;
		motionZ *= var13;

		if (onGround) {
			motionY *= -0.8999999761581421D;
		}

		++xpColor;
		++xpOrbAge;

		if (xpOrbAge >= 6000) {
			setDead();
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		xpOrbHealth = p_70037_1_.getShort("Health") & 255;
		xpOrbAge = p_70037_1_.getShort("Age");
		xpValue = p_70037_1_.getShort("Value");
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setShort("Health", (byte) xpOrbHealth);
		p_70014_1_.setShort("Age", (short) xpOrbAge);
		p_70014_1_.setShort("Value", (short) xpValue);
	}
}
