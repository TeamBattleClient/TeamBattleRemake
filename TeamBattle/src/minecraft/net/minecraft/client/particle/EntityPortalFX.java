package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class EntityPortalFX extends EntityFX {
	private final float portalParticleScale;
	private final double portalPosX;
	private final double portalPosY;
	private final double portalPosZ;

	public EntityPortalFX(World p_i1222_1_, double p_i1222_2_,
			double p_i1222_4_, double p_i1222_6_, double p_i1222_8_,
			double p_i1222_10_, double p_i1222_12_) {
		super(p_i1222_1_, p_i1222_2_, p_i1222_4_, p_i1222_6_, p_i1222_8_,
				p_i1222_10_, p_i1222_12_);
		motionX = p_i1222_8_;
		motionY = p_i1222_10_;
		motionZ = p_i1222_12_;
		portalPosX = posX = p_i1222_2_;
		portalPosY = posY = p_i1222_4_;
		portalPosZ = posZ = p_i1222_6_;
		final float var14 = rand.nextFloat() * 0.6F + 0.4F;
		portalParticleScale = particleScale = rand.nextFloat() * 0.2F + 0.5F;
		particleRed = particleGreen = particleBlue = 1.0F * var14;
		particleGreen *= 0.3F;
		particleRed *= 0.9F;
		particleMaxAge = (int) (Math.random() * 10.0D) + 40;
		noClip = true;
		setParticleTextureIndex((int) (Math.random() * 8.0D));
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float p_70013_1_) {
		final float var2 = super.getBrightness(p_70013_1_);
		float var3 = (float) particleAge / (float) particleMaxAge;
		var3 = var3 * var3 * var3 * var3;
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
		final float var2 = var1;
		var1 = -var1 + var1 * var1 * 2.0F;
		var1 = 1.0F - var1;
		posX = portalPosX + motionX * var1;
		posY = portalPosY + motionY * var1 + (1.0F - var2);
		posZ = portalPosZ + motionZ * var1;

		if (particleAge++ >= particleMaxAge) {
			setDead();
		}
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_,
			float p_70539_3_, float p_70539_4_, float p_70539_5_,
			float p_70539_6_, float p_70539_7_) {
		float var8 = (particleAge + p_70539_2_) / particleMaxAge;
		var8 = 1.0F - var8;
		var8 *= var8;
		var8 = 1.0F - var8;
		particleScale = portalParticleScale * var8;
		super.renderParticle(p_70539_1_, p_70539_2_, p_70539_3_, p_70539_4_,
				p_70539_5_, p_70539_6_, p_70539_7_);
	}
}
