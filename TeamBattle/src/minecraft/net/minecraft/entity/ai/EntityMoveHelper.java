package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.MathHelper;

public class EntityMoveHelper {
	/** The EntityLiving that is being moved */
	private final EntityLiving entity;
	private double posX;
	private double posY;
	private double posZ;

	/** The speed at which the entity should move */
	private double speed;
	private boolean update;

	public EntityMoveHelper(EntityLiving p_i1614_1_) {
		entity = p_i1614_1_;
		posX = p_i1614_1_.posX;
		posY = p_i1614_1_.posY;
		posZ = p_i1614_1_.posZ;
	}

	public double getSpeed() {
		return speed;
	}

	public boolean isUpdating() {
		return update;
	}

	/**
	 * Limits the given angle to a upper and lower limit.
	 */
	private float limitAngle(float p_75639_1_, float p_75639_2_,
			float p_75639_3_) {
		float var4 = MathHelper.wrapAngleTo180_float(p_75639_2_ - p_75639_1_);

		if (var4 > p_75639_3_) {
			var4 = p_75639_3_;
		}

		if (var4 < -p_75639_3_) {
			var4 = -p_75639_3_;
		}

		return p_75639_1_ + var4;
	}

	public void onUpdateMoveHelper() {
		entity.setMoveForward(0.0F);

		if (update) {
			update = false;
			final int var1 = MathHelper
					.floor_double(entity.boundingBox.minY + 0.5D);
			final double var2 = posX - entity.posX;
			final double var4 = posZ - entity.posZ;
			final double var6 = posY - var1;
			final double var8 = var2 * var2 + var6 * var6 + var4 * var4;

			if (var8 >= 2.500000277905201E-7D) {
				final float var10 = (float) (Math.atan2(var4, var2) * 180.0D / Math.PI) - 90.0F;
				entity.rotationYaw = limitAngle(entity.rotationYaw, var10,
						30.0F);
				entity.setAIMoveSpeed((float) (speed * entity
						.getEntityAttribute(
								SharedMonsterAttributes.movementSpeed)
						.getAttributeValue()));

				if (var6 > 0.0D && var2 * var2 + var4 * var4 < 1.0D) {
					entity.getJumpHelper().setJumping();
				}
			}
		}
	}

	/**
	 * Sets the speed and location to move to
	 */
	public void setMoveTo(double p_75642_1_, double p_75642_3_,
			double p_75642_5_, double p_75642_7_) {
		posX = p_75642_1_;
		posY = p_75642_3_;
		posZ = p_75642_5_;
		speed = p_75642_7_;
		update = true;
	}
}
