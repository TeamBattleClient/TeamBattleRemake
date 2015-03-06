package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityEgg extends EntityThrowable {

	public EntityEgg(World p_i1779_1_) {
		super(p_i1779_1_);
	}

	public EntityEgg(World p_i1781_1_, double p_i1781_2_, double p_i1781_4_,
			double p_i1781_6_) {
		super(p_i1781_1_, p_i1781_2_, p_i1781_4_, p_i1781_6_);
	}

	public EntityEgg(World p_i1780_1_, EntityLivingBase p_i1780_2_) {
		super(p_i1780_1_, p_i1780_2_);
	}

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	@Override
	protected void onImpact(MovingObjectPosition p_70184_1_) {
		if (p_70184_1_.entityHit != null) {
			p_70184_1_.entityHit.attackEntityFrom(
					DamageSource.causeThrownDamage(this, getThrower()), 0.0F);
		}

		if (!worldObj.isClient && rand.nextInt(8) == 0) {
			byte var2 = 1;

			if (rand.nextInt(32) == 0) {
				var2 = 4;
			}

			for (int var3 = 0; var3 < var2; ++var3) {
				final EntityChicken var4 = new EntityChicken(worldObj);
				var4.setGrowingAge(-24000);
				var4.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
				worldObj.spawnEntityInWorld(var4);
			}
		}

		for (int var5 = 0; var5 < 8; ++var5) {
			worldObj.spawnParticle("snowballpoof", posX, posY, posZ, 0.0D,
					0.0D, 0.0D);
		}

		if (!worldObj.isClient) {
			setDead();
		}
	}
}
