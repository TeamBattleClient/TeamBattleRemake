package net.minecraft.client.particle;

import net.minecraft.world.World;

public class EntityEnchantmentTableParticleFX extends EntityFX {
	private final float field_70565_a;
	private final double field_70566_as;
	private final double field_70567_ar;
	private final double field_70568_aq;

	public EntityEnchantmentTableParticleFX(World p_i1204_1_,
			double p_i1204_2_, double p_i1204_4_, double p_i1204_6_,
			double p_i1204_8_, double p_i1204_10_, double p_i1204_12_) {
		super(p_i1204_1_, p_i1204_2_, p_i1204_4_, p_i1204_6_, p_i1204_8_,
				p_i1204_10_, p_i1204_12_);
		motionX = p_i1204_8_;
		motionY = p_i1204_10_;
		motionZ = p_i1204_12_;
		field_70568_aq = posX = p_i1204_2_;
		field_70567_ar = posY = p_i1204_4_;
		field_70566_as = posZ = p_i1204_6_;
		final float var14 = rand.nextFloat() * 0.6F + 0.4F;
		field_70565_a = particleScale = rand.nextFloat() * 0.5F + 0.2F;
		particleRed = particleGreen = particleBlue = 1.0F * var14;
		particleGreen *= 0.9F;
		particleRed *= 0.9F;
		particleMaxAge = (int) (Math.random() * 10.0D) + 30;
		noClip = true;
		setParticleTextureIndex((int) (Math.random() * 26.0D + 1.0D + 224.0D));
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float p_70013_1_) {
		final float var2 = super.getBrightness(p_70013_1_);
		float var3 = (float) particleAge / (float) particleMaxAge;
		var3 *= var3;
		var3 *= var3;
		return var2 * (1.0F - var3) + var3;
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_) {
		final int var2 = super.getBrightnessForRender(p_70070_1_);
		float var3 = (float) particleAge / (float) particleMaxAge;
		var3 *= var3;
		var3 *= var3;
		final int var4 = var2 & 255;
		int var5 = var2 >> 16 & 255;
		var5 += (int) (var3 * 15.0F * 16.0F);

		if (var5 > 240) {
			var5 = 240;
		}

		return var4 | var5 << 16;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		float var1 = (float) particleAge / (float) particleMaxAge;
		var1 = 1.0F - var1;
		float var2 = 1.0F - var1;
		var2 *= var2;
		var2 *= var2;
		posX = field_70568_aq + motionX * var1;
		posY = field_70567_ar + motionY * var1 - var2 * 1.2F;
		posZ = field_70566_as + motionZ * var1;

		if (particleAge++ >= particleMaxAge) {
			setDead();
		}
	}
}
