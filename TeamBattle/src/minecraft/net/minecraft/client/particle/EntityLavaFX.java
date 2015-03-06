package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class EntityLavaFX extends EntityFX {
	private final float lavaParticleScale;

	public EntityLavaFX(World p_i1215_1_, double p_i1215_2_, double p_i1215_4_,
			double p_i1215_6_) {
		super(p_i1215_1_, p_i1215_2_, p_i1215_4_, p_i1215_6_, 0.0D, 0.0D, 0.0D);
		motionX *= 0.800000011920929D;
		motionY *= 0.800000011920929D;
		motionZ *= 0.800000011920929D;
		motionY = rand.nextFloat() * 0.4F + 0.05F;
		particleRed = particleGreen = particleBlue = 1.0F;
		particleScale *= rand.nextFloat() * 2.0F + 0.2F;
		lavaParticleScale = particleScale;
		particleMaxAge = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
		noClip = false;
		setParticleTextureIndex(49);
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float p_70013_1_) {
		return 1.0F;
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
		final short var4 = 240;
		final int var5 = var3 >> 16 & 255;
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

		final float var1 = (float) particleAge / (float) particleMaxAge;

		if (rand.nextFloat() > var1) {
			worldObj.spawnParticle("smoke", posX, posY, posZ, motionX, motionY,
					motionZ);
		}

		motionY -= 0.03D;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.9990000128746033D;
		motionY *= 0.9990000128746033D;
		motionZ *= 0.9990000128746033D;

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
		particleScale = lavaParticleScale * (1.0F - var8 * var8);
		super.renderParticle(p_70539_1_, p_70539_2_, p_70539_3_, p_70539_4_,
				p_70539_5_, p_70539_6_, p_70539_7_);
	}
}
