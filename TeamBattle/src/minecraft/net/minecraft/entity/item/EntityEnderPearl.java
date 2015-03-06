package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityEnderPearl extends EntityThrowable {

	public EntityEnderPearl(World p_i1782_1_) {
		super(p_i1782_1_);
	}

	public EntityEnderPearl(World p_i1784_1_, double p_i1784_2_,
			double p_i1784_4_, double p_i1784_6_) {
		super(p_i1784_1_, p_i1784_2_, p_i1784_4_, p_i1784_6_);
	}

	public EntityEnderPearl(World p_i1783_1_, EntityLivingBase p_i1783_2_) {
		super(p_i1783_1_, p_i1783_2_);
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

		for (int var2 = 0; var2 < 32; ++var2) {
			worldObj.spawnParticle("portal", posX, posY + rand.nextDouble()
					* 2.0D, posZ, rand.nextGaussian(), 0.0D,
					rand.nextGaussian());
		}

		if (!worldObj.isClient) {
			if (getThrower() != null && getThrower() instanceof EntityPlayerMP) {
				final EntityPlayerMP var3 = (EntityPlayerMP) getThrower();

				if (var3.playerNetServerHandler.func_147362_b().isChannelOpen()
						&& var3.worldObj == worldObj) {
					if (getThrower().isRiding()) {
						getThrower().mountEntity((Entity) null);
					}

					getThrower().setPositionAndUpdate(posX, posY, posZ);
					getThrower().fallDistance = 0.0F;
					getThrower().attackEntityFrom(DamageSource.fall, 5.0F);
				}
			}

			setDead();
		}
	}
}
