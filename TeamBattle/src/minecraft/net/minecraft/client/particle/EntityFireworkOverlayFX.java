package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFireworkOverlayFX extends EntityFX {

	protected EntityFireworkOverlayFX(World p_i1206_1_, double p_i1206_2_,
			double p_i1206_4_, double p_i1206_6_) {
		super(p_i1206_1_, p_i1206_2_, p_i1206_4_, p_i1206_6_);
		particleMaxAge = 4;
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_,
			float p_70539_3_, float p_70539_4_, float p_70539_5_,
			float p_70539_6_, float p_70539_7_) {
		final float var8 = 0.25F;
		final float var9 = var8 + 0.25F;
		final float var10 = 0.125F;
		final float var11 = var10 + 0.25F;
		final float var12 = 7.1F * MathHelper
				.sin((particleAge + p_70539_2_ - 1.0F) * 0.25F
						* (float) Math.PI);
		particleAlpha = 0.6F - (particleAge + p_70539_2_ - 1.0F) * 0.25F * 0.5F;
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
}
