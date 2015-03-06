package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class EntityCritFX extends EntityFX {
	float initialParticleScale;

	public EntityCritFX(World p_i1201_1_, double p_i1201_2_, double p_i1201_4_,
			double p_i1201_6_, double p_i1201_8_, double p_i1201_10_,
			double p_i1201_12_) {
		this(p_i1201_1_, p_i1201_2_, p_i1201_4_, p_i1201_6_, p_i1201_8_,
				p_i1201_10_, p_i1201_12_, 1.0F);
	}

	public EntityCritFX(World p_i1202_1_, double p_i1202_2_, double p_i1202_4_,
			double p_i1202_6_, double p_i1202_8_, double p_i1202_10_,
			double p_i1202_12_, float p_i1202_14_) {
		super(p_i1202_1_, p_i1202_2_, p_i1202_4_, p_i1202_6_, 0.0D, 0.0D, 0.0D);
		motionX *= 0.10000000149011612D;
		motionY *= 0.10000000149011612D;
		motionZ *= 0.10000000149011612D;
		motionX += p_i1202_8_ * 0.4D;
		motionY += p_i1202_10_ * 0.4D;
		motionZ += p_i1202_12_ * 0.4D;
		particleRed = particleGreen = particleBlue = (float) (Math.random() * 0.30000001192092896D + 0.6000000238418579D);
		particleScale *= 0.75F;
		particleScale *= p_i1202_14_;
		initialParticleScale = particleScale;
		particleMaxAge = (int) (6.0D / (Math.random() * 0.8D + 0.6D));
		particleMaxAge = (int) (particleMaxAge * p_i1202_14_);
		noClip = false;
		setParticleTextureIndex(65);
		onUpdate();
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
		particleGreen = (float) (particleGreen * 0.96D);
		particleBlue = (float) (particleBlue * 0.9D);
		motionX *= 0.699999988079071D;
		motionY *= 0.699999988079071D;
		motionZ *= 0.699999988079071D;
		motionY -= 0.019999999552965164D;

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

		particleScale = initialParticleScale * var8;
		super.renderParticle(p_70539_1_, p_70539_2_, p_70539_3_, p_70539_4_,
				p_70539_5_, p_70539_6_, p_70539_7_);
	}
}
