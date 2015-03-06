package net.minecraft.client.particle;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class EntityLargeExplodeFX extends EntityFX {
	private static final ResourceLocation field_110127_a = new ResourceLocation(
			"textures/entity/explosion.png");
	private int field_70581_a;
	private final float field_70582_as;

	private final int field_70584_aq;
	/** The Rendering Engine. */
	private final TextureManager theRenderEngine;

	public EntityLargeExplodeFX(TextureManager p_i1213_1_, World p_i1213_2_,
			double p_i1213_3_, double p_i1213_5_, double p_i1213_7_,
			double p_i1213_9_, double p_i1213_11_, double p_i1213_13_) {
		super(p_i1213_2_, p_i1213_3_, p_i1213_5_, p_i1213_7_, 0.0D, 0.0D, 0.0D);
		theRenderEngine = p_i1213_1_;
		field_70584_aq = 6 + rand.nextInt(4);
		particleRed = particleGreen = particleBlue = rand.nextFloat() * 0.6F + 0.4F;
		field_70582_as = 1.0F - (float) p_i1213_9_ * 0.5F;
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_) {
		return 61680;
	}

	@Override
	public int getFXLayer() {
		return 3;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		++field_70581_a;

		if (field_70581_a == field_70584_aq) {
			setDead();
		}
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_,
			float p_70539_3_, float p_70539_4_, float p_70539_5_,
			float p_70539_6_, float p_70539_7_) {
		final int var8 = (int) ((field_70581_a + p_70539_2_) * 15.0F / field_70584_aq);

		if (var8 <= 15) {
			theRenderEngine.bindTexture(field_110127_a);
			final float var9 = var8 % 4 / 4.0F;
			final float var10 = var9 + 0.24975F;
			final float var11 = var8 / 4 / 4.0F;
			final float var12 = var11 + 0.24975F;
			final float var13 = 2.0F * field_70582_as;
			final float var14 = (float) (prevPosX + (posX - prevPosX)
					* p_70539_2_ - interpPosX);
			final float var15 = (float) (prevPosY + (posY - prevPosY)
					* p_70539_2_ - interpPosY);
			final float var16 = (float) (prevPosZ + (posZ - prevPosZ)
					* p_70539_2_ - interpPosZ);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			RenderHelper.disableStandardItemLighting();
			p_70539_1_.startDrawingQuads();
			p_70539_1_.setColorRGBA_F(particleRed, particleGreen, particleBlue,
					1.0F);
			p_70539_1_.setNormal(0.0F, 1.0F, 0.0F);
			p_70539_1_.setBrightness(240);
			p_70539_1_.addVertexWithUV(var14 - p_70539_3_ * var13 - p_70539_6_
					* var13, var15 - p_70539_4_ * var13, var16 - p_70539_5_
					* var13 - p_70539_7_ * var13, var10, var12);
			p_70539_1_.addVertexWithUV(var14 - p_70539_3_ * var13 + p_70539_6_
					* var13, var15 + p_70539_4_ * var13, var16 - p_70539_5_
					* var13 + p_70539_7_ * var13, var10, var11);
			p_70539_1_.addVertexWithUV(var14 + p_70539_3_ * var13 + p_70539_6_
					* var13, var15 + p_70539_4_ * var13, var16 + p_70539_5_
					* var13 + p_70539_7_ * var13, var9, var11);
			p_70539_1_.addVertexWithUV(var14 + p_70539_3_ * var13 - p_70539_6_
					* var13, var15 - p_70539_4_ * var13, var16 + p_70539_5_
					* var13 - p_70539_7_ * var13, var9, var12);
			p_70539_1_.draw();
			GL11.glPolygonOffset(0.0F, 0.0F);
			GL11.glEnable(GL11.GL_LIGHTING);
		}
	}
}
