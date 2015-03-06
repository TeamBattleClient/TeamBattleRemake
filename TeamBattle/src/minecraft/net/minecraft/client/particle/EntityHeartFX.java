package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class EntityHeartFX extends EntityFX {
	float particleScaleOverTime;

	public EntityHeartFX(World p_i1211_1_, double p_i1211_2_,
			double p_i1211_4_, double p_i1211_6_, double p_i1211_8_,
			double p_i1211_10_, double p_i1211_12_) {
		this(p_i1211_1_, p_i1211_2_, p_i1211_4_, p_i1211_6_, p_i1211_8_,
				p_i1211_10_, p_i1211_12_, 2.0F);
	}

	public EntityHeartFX(World p_i1212_1_, double p_i1212_2_,
			double p_i1212_4_, double p_i1212_6_, double p_i1212_8_,
			double p_i1212_10_, double p_i1212_12_, float p_i1212_14_) {
		super(p_i1212_1_, p_i1212_2_, p_i1212_4_, p_i1212_6_, 0.0D, 0.0D, 0.0D);
		motionX *= 0.009999999776482582D;
		motionY *= 0.009999999776482582D;
		motionZ *= 0.009999999776482582D;
		motionY += 0.1D;
		particleScale *= 0.75F;
		particleScale *= p_i1212_14_;
		particleScaleOverTime = particleScale;
		particleMaxAge = 16;
		noClip = false;
		setParticleTextureIndex(80);
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

		if (posY == prevPosY) {
			motionX *= 1.1D;
			motionZ *= 1.1D;
		}

		motionX *= 0.8600000143051147D;
		motionY *= 0.8600000143051147D;
		motionZ *= 0.8600000143051147D;

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

		particleScale = particleScaleOverTime * var8;
		super.renderParticle(p_70539_1_, p_70539_2_, p_70539_3_, p_70539_4_,
				p_70539_5_, p_70539_6_, p_70539_7_);
	}
}
