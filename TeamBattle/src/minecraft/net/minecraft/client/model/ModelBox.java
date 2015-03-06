package net.minecraft.client.model;

import net.minecraft.client.renderer.Tessellator;

public class ModelBox {
	public String field_78247_g;

	/** X vertex coordinate of lower box corner */
	public final float posX1;

	/** X vertex coordinate of upper box corner */
	public final float posX2;

	/** Y vertex coordinate of lower box corner */
	public final float posY1;

	/** Y vertex coordinate of upper box corner */
	public final float posY2;

	/** Z vertex coordinate of lower box corner */
	public final float posZ1;

	/** Z vertex coordinate of upper box corner */
	public final float posZ2;

	/** An array of 6 TexturedQuads, one for each face of a cube */
	private final TexturedQuad[] quadList;
	/**
	 * The (x,y,z) vertex positions and (u,v) texture coordinates for each of
	 * the 8 points on a cube
	 */
	private final PositionTextureVertex[] vertexPositions;

	public ModelBox(ModelRenderer p_i1171_1_, int p_i1171_2_, int p_i1171_3_,
			float p_i1171_4_, float p_i1171_5_, float p_i1171_6_,
			int p_i1171_7_, int p_i1171_8_, int p_i1171_9_, float p_i1171_10_) {
		posX1 = p_i1171_4_;
		posY1 = p_i1171_5_;
		posZ1 = p_i1171_6_;
		posX2 = p_i1171_4_ + p_i1171_7_;
		posY2 = p_i1171_5_ + p_i1171_8_;
		posZ2 = p_i1171_6_ + p_i1171_9_;
		vertexPositions = new PositionTextureVertex[8];
		quadList = new TexturedQuad[6];
		float var11 = p_i1171_4_ + p_i1171_7_;
		float var12 = p_i1171_5_ + p_i1171_8_;
		float var13 = p_i1171_6_ + p_i1171_9_;
		p_i1171_4_ -= p_i1171_10_;
		p_i1171_5_ -= p_i1171_10_;
		p_i1171_6_ -= p_i1171_10_;
		var11 += p_i1171_10_;
		var12 += p_i1171_10_;
		var13 += p_i1171_10_;

		if (p_i1171_1_.mirror) {
			final float var14 = var11;
			var11 = p_i1171_4_;
			p_i1171_4_ = var14;
		}

		final PositionTextureVertex var23 = new PositionTextureVertex(
				p_i1171_4_, p_i1171_5_, p_i1171_6_, 0.0F, 0.0F);
		final PositionTextureVertex var15 = new PositionTextureVertex(var11,
				p_i1171_5_, p_i1171_6_, 0.0F, 8.0F);
		final PositionTextureVertex var16 = new PositionTextureVertex(var11,
				var12, p_i1171_6_, 8.0F, 8.0F);
		final PositionTextureVertex var17 = new PositionTextureVertex(
				p_i1171_4_, var12, p_i1171_6_, 8.0F, 0.0F);
		final PositionTextureVertex var18 = new PositionTextureVertex(
				p_i1171_4_, p_i1171_5_, var13, 0.0F, 0.0F);
		final PositionTextureVertex var19 = new PositionTextureVertex(var11,
				p_i1171_5_, var13, 0.0F, 8.0F);
		final PositionTextureVertex var20 = new PositionTextureVertex(var11,
				var12, var13, 8.0F, 8.0F);
		final PositionTextureVertex var21 = new PositionTextureVertex(
				p_i1171_4_, var12, var13, 8.0F, 0.0F);
		vertexPositions[0] = var23;
		vertexPositions[1] = var15;
		vertexPositions[2] = var16;
		vertexPositions[3] = var17;
		vertexPositions[4] = var18;
		vertexPositions[5] = var19;
		vertexPositions[6] = var20;
		vertexPositions[7] = var21;
		quadList[0] = new TexturedQuad(new PositionTextureVertex[] { var19,
				var15, var16, var20 }, p_i1171_2_ + p_i1171_9_ + p_i1171_7_,
				p_i1171_3_ + p_i1171_9_, p_i1171_2_ + p_i1171_9_ + p_i1171_7_
						+ p_i1171_9_, p_i1171_3_ + p_i1171_9_ + p_i1171_8_,
				p_i1171_1_.textureWidth, p_i1171_1_.textureHeight);
		quadList[1] = new TexturedQuad(new PositionTextureVertex[] { var23,
				var18, var21, var17 }, p_i1171_2_, p_i1171_3_ + p_i1171_9_,
				p_i1171_2_ + p_i1171_9_, p_i1171_3_ + p_i1171_9_ + p_i1171_8_,
				p_i1171_1_.textureWidth, p_i1171_1_.textureHeight);
		quadList[2] = new TexturedQuad(new PositionTextureVertex[] { var19,
				var18, var23, var15 }, p_i1171_2_ + p_i1171_9_, p_i1171_3_,
				p_i1171_2_ + p_i1171_9_ + p_i1171_7_, p_i1171_3_ + p_i1171_9_,
				p_i1171_1_.textureWidth, p_i1171_1_.textureHeight);
		quadList[3] = new TexturedQuad(new PositionTextureVertex[] { var16,
				var17, var21, var20 }, p_i1171_2_ + p_i1171_9_ + p_i1171_7_,
				p_i1171_3_ + p_i1171_9_, p_i1171_2_ + p_i1171_9_ + p_i1171_7_
						+ p_i1171_7_, p_i1171_3_, p_i1171_1_.textureWidth,
				p_i1171_1_.textureHeight);
		quadList[4] = new TexturedQuad(new PositionTextureVertex[] { var15,
				var23, var17, var16 }, p_i1171_2_ + p_i1171_9_, p_i1171_3_
				+ p_i1171_9_, p_i1171_2_ + p_i1171_9_ + p_i1171_7_, p_i1171_3_
				+ p_i1171_9_ + p_i1171_8_, p_i1171_1_.textureWidth,
				p_i1171_1_.textureHeight);
		quadList[5] = new TexturedQuad(new PositionTextureVertex[] { var18,
				var19, var20, var21 }, p_i1171_2_ + p_i1171_9_ + p_i1171_7_
				+ p_i1171_9_, p_i1171_3_ + p_i1171_9_, p_i1171_2_ + p_i1171_9_
				+ p_i1171_7_ + p_i1171_9_ + p_i1171_7_, p_i1171_3_ + p_i1171_9_
				+ p_i1171_8_, p_i1171_1_.textureWidth, p_i1171_1_.textureHeight);

		if (p_i1171_1_.mirror) {
			for (final TexturedQuad element : quadList) {
				element.flipFace();
			}
		}
	}

	public ModelBox func_78244_a(String p_78244_1_) {
		field_78247_g = p_78244_1_;
		return this;
	}

	/**
	 * Draw the six sided box defined by this ModelBox
	 */
	public void render(Tessellator p_78245_1_, float p_78245_2_) {
		for (final TexturedQuad element : quadList) {
			element.draw(p_78245_1_, p_78245_2_);
		}
	}
}
