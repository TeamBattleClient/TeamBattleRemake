package net.minecraft.client.audio;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class MovingSoundMinecart extends MovingSound {
	private float field_147669_l = 0.0F;
	private final EntityMinecart field_147670_k;

	public MovingSoundMinecart(EntityMinecart p_i45105_1_) {
		super(new ResourceLocation("minecraft:minecart.base"));
		field_147670_k = p_i45105_1_;
		field_147659_g = true;
		field_147665_h = 0;
	}

	/**
	 * Updates the JList with a new model.
	 */
	@Override
	public void update() {
		if (field_147670_k.isDead) {
			field_147668_j = true;
		} else {
			field_147660_d = (float) field_147670_k.posX;
			field_147661_e = (float) field_147670_k.posY;
			field_147658_f = (float) field_147670_k.posZ;
			final float var1 = MathHelper.sqrt_double(field_147670_k.motionX
					* field_147670_k.motionX + field_147670_k.motionZ
					* field_147670_k.motionZ);

			if (var1 >= 0.01D) {
				field_147669_l = MathHelper.clamp_float(
						field_147669_l + 0.0025F, 0.0F, 1.0F);
				field_147662_b = 0.0F + MathHelper
						.clamp_float(var1, 0.0F, 0.5F) * 0.7F;
			} else {
				field_147669_l = 0.0F;
				field_147662_b = 0.0F;
			}
		}
	}
}
