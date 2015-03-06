package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFX extends Entity {
	public static double interpPosX;
	public static double interpPosY;
	public static double interpPosZ;
	protected int particleAge;
	/** Particle alpha */
	protected float particleAlpha;
	/**
	 * The blue amount of color. Used as a percentage, 1.0 = 255 and 0.0 = 0.
	 */
	protected float particleBlue;
	protected float particleGravity;
	/**
	 * The green amount of color. Used as a percentage, 1.0 = 255 and 0.0 = 0.
	 */
	protected float particleGreen;

	/** The icon field from which the given particle pulls its texture. */
	protected IIcon particleIcon;

	protected int particleMaxAge;

	/** The red amount of color. Used as a percentage, 1.0 = 255 and 0.0 = 0. */
	protected float particleRed;

	protected float particleScale;

	protected int particleTextureIndexX;
	protected int particleTextureIndexY;
	protected float particleTextureJitterX;
	protected float particleTextureJitterY;

	protected EntityFX(World p_i1218_1_, double p_i1218_2_, double p_i1218_4_,
			double p_i1218_6_) {
		super(p_i1218_1_);
		particleAlpha = 1.0F;
		setSize(0.2F, 0.2F);
		yOffset = height / 2.0F;
		setPosition(p_i1218_2_, p_i1218_4_, p_i1218_6_);
		lastTickPosX = p_i1218_2_;
		lastTickPosY = p_i1218_4_;
		lastTickPosZ = p_i1218_6_;
		particleRed = particleGreen = particleBlue = 1.0F;
		particleTextureJitterX = rand.nextFloat() * 3.0F;
		particleTextureJitterY = rand.nextFloat() * 3.0F;
		particleScale = (rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
		particleMaxAge = (int) (4.0F / (rand.nextFloat() * 0.9F + 0.1F));
		particleAge = 0;
	}

	public EntityFX(World p_i1219_1_, double p_i1219_2_, double p_i1219_4_,
			double p_i1219_6_, double p_i1219_8_, double p_i1219_10_,
			double p_i1219_12_) {
		this(p_i1219_1_, p_i1219_2_, p_i1219_4_, p_i1219_6_);
		motionX = p_i1219_8_ + (float) (Math.random() * 2.0D - 1.0D) * 0.4F;
		motionY = p_i1219_10_ + (float) (Math.random() * 2.0D - 1.0D) * 0.4F;
		motionZ = p_i1219_12_ + (float) (Math.random() * 2.0D - 1.0D) * 0.4F;
		final float var14 = (float) (Math.random() + Math.random() + 1.0D) * 0.15F;
		final float var15 = MathHelper.sqrt_double(motionX * motionX + motionY
				* motionY + motionZ * motionZ);
		motionX = motionX / var15 * var14 * 0.4000000059604645D;
		motionY = motionY / var15 * var14 * 0.4000000059604645D
				+ 0.10000000149011612D;
		motionZ = motionZ / var15 * var14 * 0.4000000059604645D;
	}

	/**
	 * If returns false, the item will not inflict any damage against entities.
	 */
	@Override
	public boolean canAttackWithItem() {
		return false;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they
	 * walk on. used for spiders and wolves to prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void entityInit() {
	}

	public float getBlueColorF() {
		return particleBlue;
	}

	public int getFXLayer() {
		return 0;
	}

	public float getGreenColorF() {
		return particleGreen;
	}

	public float getRedColorF() {
		return particleRed;
	}

	public EntityFX multipleParticleScaleBy(float p_70541_1_) {
		setSize(0.2F * p_70541_1_, 0.2F * p_70541_1_);
		particleScale *= p_70541_1_;
		return this;
	}

	public EntityFX multiplyVelocity(float p_70543_1_) {
		motionX *= p_70543_1_;
		motionY = (motionY - 0.10000000149011612D) * p_70543_1_
				+ 0.10000000149011612D;
		motionZ *= p_70543_1_;
		return this;
	}

	public void nextTextureIndexX() {
		++particleTextureIndexX;
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

		motionY -= 0.04D * particleGravity;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.9800000190734863D;
		motionY *= 0.9800000190734863D;
		motionZ *= 0.9800000190734863D;

		if (onGround) {
			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
	}

	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_,
			float p_70539_3_, float p_70539_4_, float p_70539_5_,
			float p_70539_6_, float p_70539_7_) {
		float var8 = particleTextureIndexX / 16.0F;
		float var9 = var8 + 0.0624375F;
		float var10 = particleTextureIndexY / 16.0F;
		float var11 = var10 + 0.0624375F;
		final float var12 = 0.1F * particleScale;

		if (particleIcon != null) {
			var8 = particleIcon.getMinU();
			var9 = particleIcon.getMaxU();
			var10 = particleIcon.getMinV();
			var11 = particleIcon.getMaxV();
		}

		final float var13 = (float) (prevPosX + (posX - prevPosX) * p_70539_2_ - interpPosX);
		final float var14 = (float) (prevPosY + (posY - prevPosY) * p_70539_2_ - interpPosY);
		final float var15 = (float) (prevPosZ + (posZ - prevPosZ) * p_70539_2_ - interpPosZ);
		p_70539_1_.setColorRGBA_F(particleRed, particleGreen, particleBlue,
				particleAlpha);
		p_70539_1_.addVertexWithUV(var13 - p_70539_3_ * var12 - p_70539_6_
				* var12, var14 - p_70539_4_ * var12, var15 - p_70539_5_ * var12
				- p_70539_7_ * var12, var9, var11);
		p_70539_1_.addVertexWithUV(var13 - p_70539_3_ * var12 + p_70539_6_
				* var12, var14 + p_70539_4_ * var12, var15 - p_70539_5_ * var12
				+ p_70539_7_ * var12, var9, var10);
		p_70539_1_.addVertexWithUV(var13 + p_70539_3_ * var12 + p_70539_6_
				* var12, var14 + p_70539_4_ * var12, var15 + p_70539_5_ * var12
				+ p_70539_7_ * var12, var8, var10);
		p_70539_1_.addVertexWithUV(var13 + p_70539_3_ * var12 - p_70539_6_
				* var12, var14 - p_70539_4_ * var12, var15 + p_70539_5_ * var12
				- p_70539_7_ * var12, var8, var11);
	}

	/**
	 * Sets the particle alpha (float)
	 */
	public void setAlphaF(float p_82338_1_) {
		particleAlpha = p_82338_1_;
	}

	public void setParticleIcon(IIcon p_110125_1_) {
		if (getFXLayer() == 1) {
			particleIcon = p_110125_1_;
		} else {
			if (getFXLayer() != 2)
				throw new RuntimeException(
						"Invalid call to Particle.setTex, use coordinate methods");

			particleIcon = p_110125_1_;
		}
	}

	/**
	 * Public method to set private field particleTextureIndex.
	 */
	public void setParticleTextureIndex(int p_70536_1_) {
		if (getFXLayer() != 0)
			throw new RuntimeException("Invalid call to Particle.setMiscTex");
		else {
			particleTextureIndexX = p_70536_1_ % 16;
			particleTextureIndexY = p_70536_1_ / 16;
		}
	}

	public void setRBGColorF(float p_70538_1_, float p_70538_2_,
			float p_70538_3_) {
		particleRed = p_70538_1_;
		particleGreen = p_70538_2_;
		particleBlue = p_70538_3_;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ", Pos (" + posX + "," + posY
				+ "," + posZ + "), RGBA (" + particleRed + "," + particleGreen
				+ "," + particleBlue + "," + particleAlpha + "), Age "
				+ particleAge;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
	}
}
