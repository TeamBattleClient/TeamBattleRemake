package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class EntityFlameFX extends EntityFX {
	/** the scale of the flame FX */
	private final float flameScale;

	public EntityFlameFX(World p_i1209_1_, double p_i1209_2_,
			double p_i1209_4_, double p_i1209_6_, double p_i1209_8_,
			double p_i1209_10_, double p_i1209_12_) {
		super(p_i1209_1_, p_i1209_2_, p_i1209_4_, p_i1209_6_, p_i1209_8_,
				p_i1209_10_, p_i1209_12_);
		motionX = motionX * 0.009999999776482582D + p_i1209_8_;
		motionY = motionY * 0.009999999776482582D + p_i1209_10_;
		motionZ = motionZ * 0.009999999776482582D + p_i1209_12_;
		rand.nextFloat();
		rand.nextFloat();
		p_i1209_4_ = (rand.nextFloat() - rand.nextFloat()) * 0.05F;
		p_i1209_6_ = (rand.nextFloat() - rand.nextFloat()) * 0.05F;
		flameScale = particleScale;
		particleRed = particleGreen = particleBlue = 1.0F;
		particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
		noClip = true;
		setParticleTextureIndex(48);
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float p_70013_1_) {
		float var2 = (particleAge + p_70013_1_) / particleMaxAge;

		if (var2 < 0.0F) {
			var2 = 0.0F;
		}

		if (var2 > 1.0F) {
			var2 = 1.0F;
		}

		final float var3 = super.getBrightness(p_70013_1_);
		return var3 * var2 + (1.0F - var2);
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_) {
		float var2 = (particleAge + p_70070_1_) / particleMaxAge;

		if (var2 < 0.0F) {
			var2 = 0.0F;
		}

		if (var2 > 1.0F) {
			var2 = 1.0F;
		}

		final int var3 = super.getBrightnessForRender(p_70070_1_);
		int var4 = var3 & 255;
		final int var5 = var3 >> 16 & 255;
		var4 += (int) (var2 * 15.0F * 16.0F);

		if (var4 > 240) {
			var4 = 240;
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

		if (particleAge++ >= particleMaxAge) {
			setDead();
		}

		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.9599999785423279D;
		motionY *= 0.9599999785423279D;
		motionZ *= 0.9599999785423279D;

		if (onGround) {
			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
		}
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_,
			float p_70539_3_, float p_70539_4_, float p_70539_5_,
			float p_70539_6_, float p_70539_7_) {
		final float var8 = (particleAge + p_70539_2_) / particleMaxAge;
		particleScale = flameScale * (1.0F - var8 * var8 * 0.5F);
		super.renderParticle(p_70539_1_, p_70539_2_, p_70539_3_, p_70539_4_,
				p_70539_5_, p_70539_6_, p_70539_7_);
	}
}
