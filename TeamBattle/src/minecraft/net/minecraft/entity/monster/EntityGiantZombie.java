package net.minecraft.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class EntityGiantZombie extends EntityMob {

	public EntityGiantZombie(World p_i1736_1_) {
		super(p_i1736_1_);
		yOffset *= 6.0F;
		setSize(width * 6.0F, height * 6.0F);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(
				100.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(
				0.5D);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(
				50.0D);
	}

	/**
	 * Takes a coordinate in and returns a weight to determine how likely this
	 * creature will try to path to the block. Args: x, y, z
	 */
	@Override
	public float getBlockPathWeight(int p_70783_1_, int p_70783_2_,
			int p_70783_3_) {
		return worldObj.getLightBrightness(p_70783_1_, p_70783_2_, p_70783_3_) - 0.5F;
	}
}
