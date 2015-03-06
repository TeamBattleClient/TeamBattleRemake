package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityFireworkSparkFX extends EntityFX {
	private final int baseTextureIndex = 160;
	private float fadeColourBlue;
	private float fadeColourGreen;
	private float fadeColourRed;
	private final EffectRenderer field_92047_az;
	private boolean field_92048_ay;
	private boolean field_92054_ax;
	private boolean hasFadeColour;

	public EntityFireworkSparkFX(World p_i1207_1_, double p_i1207_2_,
			double p_i1207_4_, double p_i1207_6_, double p_i1207_8_,
			double p_i1207_10_, double p_i1207_12_, EffectRenderer p_i1207_14_) {
		super(p_i1207_1_, p_i1207_2_, p_i1207_4_, p_i1207_6_);
		motionX = p_i1207_8_;
		motionY = p_i1207_10_;
		motionZ = p_i1207_12_;
		field_92047_az = p_i1207_14_;
		particleScale *= 0.75F;
		particleMaxAge = 48 + rand.nextInt(12);
		noClip = false;
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities
	 * when colliding.
	 */
	@Override
	public boolean canBePushed() {
		return false;
	}

	/**
	 * returns the bounding box for this entity
	 */
	@Override
	public AxisAlignedBB getBoundingBox() {
		return null;
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
		return 15728880;
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

		if (particleAge > particleMaxAge / 2) {
			setAlphaF(1.0F
					- ((float) particleAge - (float) (particleMaxAge / 2))
					/ particleMaxAge);

			if (hasFadeColour) {
				particleRed += (fadeColourRed - particleRed) * 0.2F;
				particleGreen += (fadeColourGreen - particleGreen) * 0.2F;
				particleBlue += (fadeColourBlue - particleBlue) * 0.2F;
			}
		}

		setParticleTextureIndex(baseTextureIndex + 7 - particleAge * 8
				/ particleMaxAge);
		motionY -= 0.004D;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.9100000262260437D;
		motionY *= 0.9100000262260437D;
		motionZ *= 0.9100000262260437D;

		if (onGround) {
			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
		}

		if (field_92054_ax && particleAge < particleMaxAge / 2
				&& (particleAge + particleMaxAge) % 2 == 0) {
			final EntityFireworkSparkFX var1 = new EntityFireworkSparkFX(
					worldObj, posX, posY, posZ, 0.0D, 0.0D, 0.0D,
					field_92047_az);
			var1.setRBGColorF(particleRed, particleGreen, particleBlue);
			var1.particleAge = var1.particleMaxAge / 2;

			if (hasFadeColour) {
				var1.hasFadeColour = true;
				var1.fadeColourRed = fadeColourRed;
				var1.fadeColourGreen = fadeColourGreen;
				var1.fadeColourBlue = fadeColourBlue;
			}

			var1.field_92048_ay = field_92048_ay;
			field_92047_az.addEffect(var1);
		}
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_,
			float p_70539_3_, float p_70539_4_, float p_70539_5_,
			float p_70539_6_, float p_70539_7_) {
		if (!field_92048_ay || particleAge < particleMaxAge / 3
				|| (particleAge + particleMaxAge) / 3 % 2 == 0) {
			super.renderParticle(p_70539_1_, p_70539_2_, p_70539_3_,
					p_70539_4_, p_70539_5_, p_70539_6_, p_70539_7_);
		}
	}

	public void setColour(int p_92044_1_) {
		final float var2 = ((p_92044_1_ & 16711680) >> 16) / 255.0F;
		final float var3 = ((p_92044_1_ & 65280) >> 8) / 255.0F;
		final float var4 = ((p_92044_1_ & 255) >> 0) / 255.0F;
		final float var5 = 1.0F;
		setRBGColorF(var2 * var5, var3 * var5, var4 * var5);
	}

	public void setFadeColour(int p_92046_1_) {
		fadeColourRed = ((p_92046_1_ & 16711680) >> 16) / 255.0F;
		fadeColourGreen = ((p_92046_1_ & 65280) >> 8) / 255.0F;
		fadeColourBlue = ((p_92046_1_ & 255) >> 0) / 255.0F;
		hasFadeColour = true;
	}

	public void setTrail(boolean p_92045_1_) {
		field_92054_ax = p_92045_1_;
	}

	public void setTwinkle(boolean p_92043_1_) {
		field_92048_ay = p_92043_1_;
	}
}
