package net.minecraft.client.particle;

import net.minecraft.world.World;

public class EntityExplodeFX extends EntityFX {

	public EntityExplodeFX(World p_i1205_1_, double p_i1205_2_,
			double p_i1205_4_, double p_i1205_6_, double p_i1205_8_,
			double p_i1205_10_, double p_i1205_12_) {
		super(p_i1205_1_, p_i1205_2_, p_i1205_4_, p_i1205_6_, p_i1205_8_,
				p_i1205_10_, p_i1205_12_);
		motionX = p_i1205_8_ + (float) (Math.random() * 2.0D - 1.0D) * 0.05F;
		motionY = p_i1205_10_ + (float) (Math.random() * 2.0D - 1.0D) * 0.05F;
		motionZ = p_i1205_12_ + (float) (Math.random() * 2.0D - 1.0D) * 0.05F;
		particleRed = particleGreen = particleBlue = rand.nextFloat() * 0.3F + 0.7F;
		particleScale = rand.nextFloat() * rand.nextFloat() * 6.0F + 1.0F;
		particleMaxAge = (int) (16.0D / (rand.nextFloat() * 0.8D + 0.2D)) + 2;
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
		motionY += 0.004D;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.8999999761581421D;
		motionY *= 0.8999999761581421D;
		motionZ *= 0.8999999761581421D;

		if (onGround) {
			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
		}
	}
}
