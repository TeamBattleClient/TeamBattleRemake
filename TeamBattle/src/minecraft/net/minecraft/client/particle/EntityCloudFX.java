package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityCloudFX extends EntityFX {
	float field_70569_a;

	public EntityCloudFX(World p_i1221_1_, double p_i1221_2_,
			double p_i1221_4_, double p_i1221_6_, double p_i1221_8_,
			double p_i1221_10_, double p_i1221_12_) {
		super(p_i1221_1_, p_i1221_2_, p_i1221_4_, p_i1221_6_, 0.0D, 0.0D, 0.0D);
		final float var14 = 2.5F;
		motionX *= 0.10000000149011612D;
		motionY *= 0.10000000149011612D;
		motionZ *= 0.10000000149011612D;
		motionX += p_i1221_8_;
		motionY += p_i1221_10_;
		motionZ += p_i1221_12_;
		particleRed = particleGreen = particleBlue = 1.0F - (float) (Math
				.random() * 0.30000001192092896D);
		particleScale *= 0.75F;
		particleScale *= var14;
		field_70569_a = particleScale;
		particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.3D));
		particleMaxAge = (int) (particleMaxAge * var14);
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
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.9599999785423279D;
		motionY *= 0.9599999785423279D;
		motionZ *= 0.9599999785423279D;
		final EntityPlayer var1 = worldObj.getClosestPlayerToEntity(this, 2.0D);

		if (var1 != null && posY > var1.boundingBox.minY) {
			posY += (var1.boundingBox.minY - posY) * 0.2D;
			motionY += (var1.motionY - motionY) * 0.2D;
			setPosition(posX, posY, posZ);
		}

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

		particleScale = field_70569_a * var8;
		super.renderParticle(p_70539_1_, p_70539_2_, p_70539_3_, p_70539_4_,
				p_70539_5_, p_70539_6_, p_70539_7_);
	}
}
