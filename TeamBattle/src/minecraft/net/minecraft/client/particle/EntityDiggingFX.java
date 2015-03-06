package net.minecraft.client.particle;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class EntityDiggingFX extends EntityFX {
	private final Block field_145784_a;

	public EntityDiggingFX(World p_i1234_1_, double p_i1234_2_,
			double p_i1234_4_, double p_i1234_6_, double p_i1234_8_,
			double p_i1234_10_, double p_i1234_12_, Block p_i1234_14_,
			int p_i1234_15_) {
		super(p_i1234_1_, p_i1234_2_, p_i1234_4_, p_i1234_6_, p_i1234_8_,
				p_i1234_10_, p_i1234_12_);
		field_145784_a = p_i1234_14_;
		setParticleIcon(p_i1234_14_.getIcon(0, p_i1234_15_));
		particleGravity = p_i1234_14_.blockParticleGravity;
		particleRed = particleGreen = particleBlue = 0.6F;
		particleScale /= 2.0F;
	}

	/**
	 * If the block has a colour multiplier, copies it to this particle and
	 * returns this particle.
	 */
	public EntityDiggingFX applyColourMultiplier(int p_70596_1_,
			int p_70596_2_, int p_70596_3_) {
		if (field_145784_a == Blocks.grass)
			return this;
		else {
			final int var4 = field_145784_a.colorMultiplier(worldObj,
					p_70596_1_, p_70596_2_, p_70596_3_);
			particleRed *= (var4 >> 16 & 255) / 255.0F;
			particleGreen *= (var4 >> 8 & 255) / 255.0F;
			particleBlue *= (var4 & 255) / 255.0F;
			return this;
		}
	}

	/**
	 * Creates a new EntityDiggingFX with the block render color applied to the
	 * base particle color
	 */
	public EntityDiggingFX applyRenderColor(int p_90019_1_) {
		if (field_145784_a == Blocks.grass)
			return this;
		else {
			final int var2 = field_145784_a.getRenderColor(p_90019_1_);
			particleRed *= (var2 >> 16 & 255) / 255.0F;
			particleGreen *= (var2 >> 8 & 255) / 255.0F;
			particleBlue *= (var2 & 255) / 255.0F;
			return this;
		}
	}

	@Override
	public int getFXLayer() {
		return 1;
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_,
			float p_70539_3_, float p_70539_4_, float p_70539_5_,
			float p_70539_6_, float p_70539_7_) {
		float var8 = (particleTextureIndexX + particleTextureJitterX / 4.0F) / 16.0F;
		float var9 = var8 + 0.015609375F;
		float var10 = (particleTextureIndexY + particleTextureJitterY / 4.0F) / 16.0F;
		float var11 = var10 + 0.015609375F;
		final float var12 = 0.1F * particleScale;

		if (particleIcon != null) {
			var8 = particleIcon
					.getInterpolatedU(particleTextureJitterX / 4.0F * 16.0F);
			var9 = particleIcon
					.getInterpolatedU((particleTextureJitterX + 1.0F) / 4.0F * 16.0F);
			var10 = particleIcon
					.getInterpolatedV(particleTextureJitterY / 4.0F * 16.0F);
			var11 = particleIcon
					.getInterpolatedV((particleTextureJitterY + 1.0F) / 4.0F * 16.0F);
		}

		final float var13 = (float) (prevPosX + (posX - prevPosX) * p_70539_2_ - interpPosX);
		final float var14 = (float) (prevPosY + (posY - prevPosY) * p_70539_2_ - interpPosY);
		final float var15 = (float) (prevPosZ + (posZ - prevPosZ) * p_70539_2_ - interpPosZ);
		p_70539_1_.setColorOpaque_F(particleRed, particleGreen, particleBlue);
		p_70539_1_.addVertexWithUV(var13 - p_70539_3_ * var12 - p_70539_6_
				* var12, var14 - p_70539_4_ * var12, var15 - p_70539_5_ * var12
				- p_70539_7_ * var12, var8, var11);
		p_70539_1_.addVertexWithUV(var13 - p_70539_3_ * var12 + p_70539_6_
				* var12, var14 + p_70539_4_ * var12, var15 - p_70539_5_ * var12
				+ p_70539_7_ * var12, var8, var10);
		p_70539_1_.addVertexWithUV(var13 + p_70539_3_ * var12 + p_70539_6_
				* var12, var14 + p_70539_4_ * var12, var15 + p_70539_5_ * var12
				+ p_70539_7_ * var12, var9, var10);
		p_70539_1_.addVertexWithUV(var13 + p_70539_3_ * var12 - p_70539_6_
				* var12, var14 - p_70539_4_ * var12, var15 + p_70539_5_ * var12
				- p_70539_7_ * var12, var9, var11);
	}
}
