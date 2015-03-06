package net.minecraft.entity;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityHanging extends Entity {
	public int field_146062_d;
	public int field_146063_b;
	public int field_146064_c;
	public int hangingDirection;
	private int tickCounter1;

	public EntityHanging(World p_i1588_1_) {
		super(p_i1588_1_);
		yOffset = 0.0F;
		setSize(0.5F, 0.5F);
	}

	public EntityHanging(World p_i1589_1_, int p_i1589_2_, int p_i1589_3_,
			int p_i1589_4_, int p_i1589_5_) {
		this(p_i1589_1_);
		field_146063_b = p_i1589_2_;
		field_146064_c = p_i1589_3_;
		field_146062_d = p_i1589_4_;
	}

	/**
	 * Adds to the current velocity of the entity. Args: x, y, z
	 */
	@Override
	public void addVelocity(double p_70024_1_, double p_70024_3_,
			double p_70024_5_) {
		if (!worldObj.isClient
				&& !isDead
				&& p_70024_1_ * p_70024_1_ + p_70024_3_ * p_70024_3_
						+ p_70024_5_ * p_70024_5_ > 0.0D) {
			setDead();
			onBroken((Entity) null);
		}
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else {
			if (!isDead && !worldObj.isClient) {
				setDead();
				setBeenAttacked();
				onBroken(p_70097_1_.getEntity());
			}

			return true;
		}
	}

	/**
	 * Returns true if other Entities should be prevented from moving through
	 * this Entity.
	 */
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	public void func_145781_i(int p_145781_1_) {
		worldObj.func_147450_X();
	}

	private float func_70517_b(int p_70517_1_) {
		return p_70517_1_ == 32 ? 0.5F : p_70517_1_ == 64 ? 0.5F : 0.0F;
	}

	public abstract int getHeightPixels();

	public abstract int getWidthPixels();

	/**
	 * Called when a player attacks an entity. If this returns true the attack
	 * will not happen.
	 */
	@Override
	public boolean hitByEntity(Entity p_85031_1_) {
		return p_85031_1_ instanceof EntityPlayer ? attackEntityFrom(
				DamageSource.causePlayerDamage((EntityPlayer) p_85031_1_), 0.0F)
				: false;
	}

	/**
	 * Tries to moves the entity by the passed in displacement. Args: x, y, z
	 */
	@Override
	public void moveEntity(double p_70091_1_, double p_70091_3_,
			double p_70091_5_) {
		if (!worldObj.isClient
				&& !isDead
				&& p_70091_1_ * p_70091_1_ + p_70091_3_ * p_70091_3_
						+ p_70091_5_ * p_70091_5_ > 0.0D) {
			setDead();
			onBroken((Entity) null);
		}
	}

	/**
	 * Called when this entity is broken. Entity parameter may be null.
	 */
	public abstract void onBroken(Entity p_110128_1_);

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (tickCounter1++ == 100 && !worldObj.isClient) {
			tickCounter1 = 0;

			if (!isDead && !onValidSurface()) {
				setDead();
				onBroken((Entity) null);
			}
		}
	}

	/**
	 * checks to make sure painting can be placed there
	 */
	public boolean onValidSurface() {
		if (!worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty())
			return false;
		else {
			final int var1 = Math.max(1, getWidthPixels() / 16);
			final int var2 = Math.max(1, getHeightPixels() / 16);
			int var3 = field_146063_b;
			int var4 = field_146064_c;
			int var5 = field_146062_d;

			if (hangingDirection == 2) {
				var3 = MathHelper.floor_double(posX - getWidthPixels() / 32.0F);
			}

			if (hangingDirection == 1) {
				var5 = MathHelper.floor_double(posZ - getWidthPixels() / 32.0F);
			}

			if (hangingDirection == 0) {
				var3 = MathHelper.floor_double(posX - getWidthPixels() / 32.0F);
			}

			if (hangingDirection == 3) {
				var5 = MathHelper.floor_double(posZ - getWidthPixels() / 32.0F);
			}

			var4 = MathHelper.floor_double(posY - getHeightPixels() / 32.0F);

			for (int var6 = 0; var6 < var1; ++var6) {
				for (int var7 = 0; var7 < var2; ++var7) {
					Material var8;

					if (hangingDirection != 2 && hangingDirection != 0) {
						var8 = worldObj.getBlock(field_146063_b, var4 + var7,
								var5 + var6).getMaterial();
					} else {
						var8 = worldObj.getBlock(var3 + var6, var4 + var7,
								field_146062_d).getMaterial();
					}

					if (!var8.isSolid())
						return false;
				}
			}

			final List var9 = worldObj.getEntitiesWithinAABBExcludingEntity(
					this, boundingBox);
			final Iterator var10 = var9.iterator();
			Entity var11;

			do {
				if (!var10.hasNext())
					return true;

				var11 = (Entity) var10.next();
			} while (!(var11 instanceof EntityHanging));

			return false;
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		if (p_70037_1_.func_150297_b("Direction", 99)) {
			hangingDirection = p_70037_1_.getByte("Direction");
		} else {
			switch (p_70037_1_.getByte("Dir")) {
			case 0:
				hangingDirection = 2;
				break;

			case 1:
				hangingDirection = 1;
				break;

			case 2:
				hangingDirection = 0;
				break;

			case 3:
				hangingDirection = 3;
			}
		}

		field_146063_b = p_70037_1_.getInteger("TileX");
		field_146064_c = p_70037_1_.getInteger("TileY");
		field_146062_d = p_70037_1_.getInteger("TileZ");
		setDirection(hangingDirection);
	}

	public void setDirection(int p_82328_1_) {
		hangingDirection = p_82328_1_;
		prevRotationYaw = rotationYaw = p_82328_1_ * 90;
		float var2 = getWidthPixels();
		float var3 = getHeightPixels();
		float var4 = getWidthPixels();

		if (p_82328_1_ != 2 && p_82328_1_ != 0) {
			var2 = 0.5F;
		} else {
			var4 = 0.5F;
			rotationYaw = prevRotationYaw = Direction.rotateOpposite[p_82328_1_] * 90;
		}

		var2 /= 32.0F;
		var3 /= 32.0F;
		var4 /= 32.0F;
		float var5 = field_146063_b + 0.5F;
		float var6 = field_146064_c + 0.5F;
		float var7 = field_146062_d + 0.5F;
		final float var8 = 0.5625F;

		if (p_82328_1_ == 2) {
			var7 -= var8;
		}

		if (p_82328_1_ == 1) {
			var5 -= var8;
		}

		if (p_82328_1_ == 0) {
			var7 += var8;
		}

		if (p_82328_1_ == 3) {
			var5 += var8;
		}

		if (p_82328_1_ == 2) {
			var5 -= func_70517_b(getWidthPixels());
		}

		if (p_82328_1_ == 1) {
			var7 += func_70517_b(getWidthPixels());
		}

		if (p_82328_1_ == 0) {
			var5 += func_70517_b(getWidthPixels());
		}

		if (p_82328_1_ == 3) {
			var7 -= func_70517_b(getWidthPixels());
		}

		var6 += func_70517_b(getHeightPixels());
		setPosition(var5, var6, var7);
		final float var9 = -0.03125F;
		boundingBox.setBounds(var5 - var2 - var9, var6 - var3 - var9, var7
				- var4 - var9, var5 + var2 + var9, var6 + var3 + var9, var7
				+ var4 + var9);
	}

	@Override
	protected boolean shouldSetPosAfterLoading() {
		return false;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setByte("Direction", (byte) hangingDirection);
		p_70014_1_.setInteger("TileX", field_146063_b);
		p_70014_1_.setInteger("TileY", field_146064_c);
		p_70014_1_.setInteger("TileZ", field_146062_d);

		switch (hangingDirection) {
		case 0:
			p_70014_1_.setByte("Dir", (byte) 2);
			break;

		case 1:
			p_70014_1_.setByte("Dir", (byte) 1);
			break;

		case 2:
			p_70014_1_.setByte("Dir", (byte) 0);
			break;

		case 3:
			p_70014_1_.setByte("Dir", (byte) 3);
		}
	}
}
