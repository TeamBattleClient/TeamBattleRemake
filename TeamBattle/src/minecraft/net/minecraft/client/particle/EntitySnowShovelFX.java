package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class EntitySnowShovelFX extends EntityFX {
	float snowDigParticleScale;

	public EntitySnowShovelFX(World p_i1227_1_, double p_i1227_2_,
			double p_i1227_4_, double p_i1227_6_, double p_i1227_8_,
			double p_i1227_10_, double p_i1227_12_) {
		this(p_i1227_1_, p_i1227_2_, p_i1227_4_, p_i1227_6_, p_i1227_8_,
				p_i1227_10_, p_i1227_12_, 1.0F);
	}

	public EntitySnowShovelFX(World p_i1228_1_, double p_i1228_2_,
			double p_i1228_4_, double p_i1228_6_, double p_i1228_8_,
			double p_i1228_10_, double p_i1228_12_, float p_i1228_14_) {
		super(p_i1228_1_, p_i1228_2_, p_i1228_4_, p_i1228_6_, p_i1228_8_,
				p_i1228_10_, p_i1228_12_);
		motionX *= 0.10000000149011612D;
		motionY *= 0.10000000149011612D;
		motionZ *= 0.10000000149011612D;
		motionX += p_i1228_8_;
		motionY += p_i1228_10_;
		motionZ += p_i1228_12_;
		particleRed = particleGreen = particleBlue = 1.0F - (float) (Math
				.random() * 0.30000001192092896D);
		particleScale *= 0.75F;
		particleScale *= p_i1228_14_;
		snowDigParticleScale = particleScale;
		particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
		particleMaxAge = (int) (particleMaxAge * p_i1228_14_);
		noClip = false;
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

		setParticleTextureIndex(7 - particleAge * 8 / particleMaxAge);
		motionY -= 0.03D;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.9900000095367432D;
		motionY *= 0.9900000095367432D;
		motionZ *= 0.9900000095367432D;

		if (onGround) {
			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
		}
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_,
			float p_70539_3_, float p_70539_4_, float p_70539_5_,
			float p_70539_6_, float p_70539_7_) {
		float var8 = (particleAge + p_70539_2_) / particleMaxAge * 32.0F;

		if (var8 < 0.0F) {
			var8 = 0.0F;
		}

		if (var8 > 1.0F) {
			var8 = 1.0F;
		}

		particleScale = snowDigParticleScale * var8;
		super.renderParticle(p_70539_1_, p_70539_2_, p_70539_3_, p_70539_4_,
				p_70539_5_, p_70539_6_, p_70539_7_);
	}
}
