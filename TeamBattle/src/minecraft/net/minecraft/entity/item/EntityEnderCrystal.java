package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;

public class EntityEnderCrystal extends Entity {
	public int health;
	/** Used to create the rotation animation when rendering the crystal. */
	public int innerRotation;

	public EntityEnderCrystal(World p_i1698_1_) {
		super(p_i1698_1_);
		preventEntitySpawning = true;
		setSize(2.0F, 2.0F);
		yOffset = height / 2.0F;
		health = 5;
		innerRotation = rand.nextInt(100000);
	}

	public EntityEnderCrystal(World p_i1699_1_, double p_i1699_2_,
			double p_i1699_4_, double p_i1699_6_) {
		this(p_i1699_1_);
		setPosition(p_i1699_2_, p_i1699_4_, p_i1699_6_);
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
				health = 0;

				if (health <= 0) {
					setDead();

					if (!worldObj.isClient) {
						worldObj.createExplosion((Entity) null, posX, posY,
								posZ, 6.0F, true);
					}
				}
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
		dataWatcher.addObject(8, Integer.valueOf(health));
	}

	@Override
	public float getShadowSize() {
		return 0.0F;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		++innerRotation;
		dataWatcher.updateObject(8, Integer.valueOf(health));
		final int var1 = MathHelper.floor_double(posX);
		final int var2 = MathHelper.floor_double(posY);
		final int var3 = MathHelper.floor_double(posZ);

		if (worldObj.provider instanceof WorldProviderEnd
				&& worldObj.getBlock(var1, var2, var3) != Blocks.fire) {
			worldObj.setBlock(var1, var2, var3, Blocks.fire);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
	}
}
