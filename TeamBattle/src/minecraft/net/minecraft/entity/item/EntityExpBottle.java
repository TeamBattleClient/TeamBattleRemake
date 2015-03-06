package net.minecraft.entity.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityExpBottle extends EntityThrowable {

	public EntityExpBottle(World p_i1785_1_) {
		super(p_i1785_1_);
	}

	public EntityExpBottle(World p_i1787_1_, double p_i1787_2_,
			double p_i1787_4_, double p_i1787_6_) {
		super(p_i1787_1_, p_i1787_2_, p_i1787_4_, p_i1787_6_);
	}

	public EntityExpBottle(World p_i1786_1_, EntityLivingBase p_i1786_2_) {
		super(p_i1786_1_, p_i1786_2_);
	}

	@Override
	protected float func_70182_d() {
		return 0.7F;
	}

	@Override
	protected float func_70183_g() {
		return -20.0F;
	}

	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	@Override
	protected float getGravityVelocity() {
		return 0.07F;
	}

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	@Override
	protected void onImpact(MovingObjectPosition p_70184_1_) {
		if (!worldObj.isClient) {
			worldObj.playAuxSFX(2002, (int) Math.round(posX),
					(int) Math.round(posY), (int) Math.round(posZ), 0);
			int var2 = 3 + worldObj.rand.nextInt(5) + worldObj.rand.nextInt(5);

			while (var2 > 0) {
				final int var3 = EntityXPOrb.getXPSplit(var2);
				var2 -= var3;
				worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX,
						posY, posZ, var3));
			}

			setDead();
		}
	}
}
