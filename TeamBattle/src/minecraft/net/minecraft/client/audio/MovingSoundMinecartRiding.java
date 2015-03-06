package net.minecraft.client.audio;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class MovingSoundMinecartRiding extends MovingSound {
	private final EntityMinecart field_147671_l;
	private final EntityPlayer field_147672_k;

	public MovingSoundMinecartRiding(EntityPlayer p_i45106_1_,
			EntityMinecart p_i45106_2_) {
		super(new ResourceLocation("minecraft:minecart.inside"));
		field_147672_k = p_i45106_1_;
		field_147671_l = p_i45106_2_;
		field_147666_i = ISound.AttenuationType.NONE;
		field_147659_g = true;
		field_147665_h = 0;
	}

	/**
	 * Updates the JList with a new model.
	 */
	@Override
	public void update() {
		if (!field_147671_l.isDead && field_147672_k.isRiding()
				&& field_147672_k.ridingEntity == field_147671_l) {
			final float var1 = MathHelper.sqrt_double(field_147671_l.motionX
					* field_147671_l.motionX + field_147671_l.motionZ
					* field_147671_l.motionZ);

			if (var1 >= 0.01D) {
				field_147662_b = 0.0F + MathHelper
						.clamp_float(var1, 0.0F, 1.0F) * 0.75F;
			} else {
				field_147662_b = 0.0F;
			}
		} else {
			field_147668_j = true;
		}
	}
}
