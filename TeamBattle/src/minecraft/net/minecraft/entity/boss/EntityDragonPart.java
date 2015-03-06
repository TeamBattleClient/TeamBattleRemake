package net.minecraft.entity.boss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class EntityDragonPart extends Entity {
	/** The dragon entity this dragon part belongs to */
	public final IEntityMultiPart entityDragonObj;
	public final String field_146032_b;

	public EntityDragonPart(IEntityMultiPart p_i1697_1_, String p_i1697_2_,
			float p_i1697_3_, float p_i1697_4_) {
		super(p_i1697_1_.func_82194_d());
		setSize(p_i1697_3_, p_i1697_4_);
		entityDragonObj = p_i1697_1_;
		field_146032_b = p_i1697_2_;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		return isEntityInvulnerable() ? false : entityDragonObj
				.attackEntityFromPart(this, p_70097_1_, p_70097_2_);
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

	/**
	 * Returns true if Entity argument is equal to this Entity
	 */
	@Override
	public boolean isEntityEqual(Entity p_70028_1_) {
		return this == p_70028_1_ || entityDragonObj == p_70028_1_;
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
