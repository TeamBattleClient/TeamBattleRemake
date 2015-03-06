package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class EntityLookHelper {
	/**
	 * The amount of change that is made each update for an entity facing a
	 * direction.
	 */
	private float deltaLookPitch;

	/**
	 * The amount of change that is made each update for an entity facing a
	 * direction.
	 */
	private float deltaLookYaw;

	private final EntityLiving entity;

	/** Whether or not the entity is trying to look at something. */
	private boolean isLooking;
	private double posX;
	private double posY;
	private double posZ;

	public EntityLookHelper(EntityLiving p_i1613_1_) {
		entity = p_i1613_1_;
	}

	/**
	 * Updates look
	 */
	public void onUpdateLook() {
		entity.rotationPitch = 0.0F;

		if (isLooking) {
			isLooking = false;
			final double var1 = posX - entity.posX;
			final double var3 = posY - (entity.posY + entity.getEyeHeight());
			final double var5 = posZ - entity.posZ;
			final double var7 = MathHelper.sqrt_double(var1 * var1 + var5
					* var5);
			final float var9 = (float) (Math.atan2(var5, var1) * 180.0D / Math.PI) - 90.0F;
			final float var10 = (float) -(Math.atan2(var3, var7) * 180.0D / Math.PI);
			entity.rotationPitch = updateRotation(entity.rotationPitch, var10,
					deltaLookPitch);
			entity.rotationYawHead = updateRotation(entity.rotationYawHead,
					var9, deltaLookYaw);
		} else {
			entity.rotationYawHead = updateRotation(entity.rotationYawHead,
					entity.renderYawOffset, 10.0F);
		}

		final float var11 = MathHelper
				.wrapAngleTo180_float(entity.rotationYawHead
						- entity.renderYawOffset);

		if (!entity.getNavigator().noPath()) {
			if (var11 < -75.0F) {
				entity.rotationYawHead = entity.renderYawOffset - 75.0F;
			}

			if (var11 > 75.0F) {
				entity.rotationYawHead = entity.renderYawOffset + 75.0F;
			}
		}
	}

	/**
	 * Sets position to look at
	 */
	public void setLookPosition(double p_75650_1_, double p_75650_3_,
			double p_75650_5_, float p_75650_7_, float p_75650_8_) {
		posX = p_75650_1_;
		posY = p_75650_3_;
		posZ = p_75650_5_;
		deltaLookYaw = p_75650_7_;
		deltaLookPitch = p_75650_8_;
		isLooking = true;
	}

	/**
	 * Sets position to look at using entity
	 */
	public void setLookPositionWithEntity(Entity p_75651_1_, float p_75651_2_,
			float p_75651_3_) {
		posX = p_75651_1_.posX;

		if (p_75651_1_ instanceof EntityLivingBase) {
			posY = p_75651_1_.posY + p_75651_1_.getEyeHeight();
		} else {
			posY = (p_75651_1_.boundingBox.minY + p_75651_1_.boundingBox.maxY) / 2.0D;
		}

		posZ = p_75651_1_.posZ;
		deltaLookYaw = p_75651_2_;
		deltaLookPitch = p_75651_3_;
		isLooking = true;
	}

	private float updateRotation(float p_75652_1_, float p_75652_2_,
			float p_75652_3_) {
		float var4 = MathHelper.wrapAngleTo180_float(p_75652_2_ - p_75652_1_);

		if (var4 > p_75652_3_) {
			var4 = p_75652_3_;
		}

		if (var4 < -p_75652_3_) {
			var4 = -p_75652_3_;
		}

		return p_75652_1_ + var4;
	}
}
