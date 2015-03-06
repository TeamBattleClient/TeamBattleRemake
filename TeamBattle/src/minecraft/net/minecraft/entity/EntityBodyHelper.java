package net.minecraft.entity;

import net.minecraft.util.MathHelper;

public class EntityBodyHelper {
	private int field_75666_b;
	private float field_75667_c;
	/** Instance of EntityLiving. */
	private final EntityLivingBase theLiving;

	public EntityBodyHelper(EntityLivingBase p_i1611_1_) {
		theLiving = p_i1611_1_;
	}

	public void func_75664_a() {
		final double var1 = theLiving.posX - theLiving.prevPosX;
		final double var3 = theLiving.posZ - theLiving.prevPosZ;

		if (var1 * var1 + var3 * var3 > 2.500000277905201E-7D) {
			theLiving.renderYawOffset = theLiving.rotationYaw;
			theLiving.rotationYawHead = func_75665_a(theLiving.renderYawOffset,
					theLiving.rotationYawHead, 75.0F);
			field_75667_c = theLiving.rotationYawHead;
			field_75666_b = 0;
		} else {
			float var5 = 75.0F;

			if (Math.abs(theLiving.rotationYawHead - field_75667_c) > 15.0F) {
				field_75666_b = 0;
				field_75667_c = theLiving.rotationYawHead;
			} else {
				++field_75666_b;
				if (field_75666_b > 10) {
					var5 = Math.max(1.0F - (field_75666_b - 10) / 10.0F, 0.0F) * 75.0F;
				}
			}

			theLiving.renderYawOffset = func_75665_a(theLiving.rotationYawHead,
					theLiving.renderYawOffset, var5);
		}
	}

	private float func_75665_a(float p_75665_1_, float p_75665_2_,
			float p_75665_3_) {
		float var4 = MathHelper.wrapAngleTo180_float(p_75665_1_ - p_75665_2_);

		if (var4 < -p_75665_3_) {
			var4 = -p_75665_3_;
		}

		if (var4 >= p_75665_3_) {
			var4 = p_75665_3_;
		}

		return p_75665_1_ - var4;
	}
}
