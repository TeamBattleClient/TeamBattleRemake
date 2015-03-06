package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderArrow extends Render {
	private static final ResourceLocation arrowTextures = new ResourceLocation(
			"textures/entity/arrow.png");

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker
	 * function which does the actual work. In all probabilty, the class Render
	 * is generic (Render<T extends Entity) and this method has signature public
	 * void doRender(T entity, double d, double d1, double d2, float f, float
	 * f1). But JAD is pre 1.5 so doesn't do that.
	 */
	@Override
	public void doRender(Entity p_76986_1_, double p_76986_2_,
			double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		this.doRender((EntityArrow) p_76986_1_, p_76986_2_, p_76986_4_,
				p_76986_6_, p_76986_8_, p_76986_9_);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker
	 * function which does the actual work. In all probabilty, the class Render
	 * is generic (Render<T extends Entity) and this method has signature public
	 * void doRender(T entity, double d, double d1, double d2, float f, float
	 * f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(EntityArrow p_76986_1_, double p_76986_2_,
			double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		bindEntityTexture(p_76986_1_);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_,
				(float) p_76986_6_);
		GL11.glRotatef(p_76986_1_.prevRotationYaw
				+ (p_76986_1_.rotationYaw - p_76986_1_.prevRotationYaw)
				* p_76986_9_ - 90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(p_76986_1_.prevRotationPitch
				+ (p_76986_1_.rotationPitch - p_76986_1_.prevRotationPitch)
				* p_76986_9_, 0.0F, 0.0F, 1.0F);
		final Tessellator var10 = Tessellator.instance;
		final byte var11 = 0;
		final float var12 = 0.0F;
		final float var13 = 0.5F;
		final float var14 = (0 + var11 * 10) / 32.0F;
		final float var15 = (5 + var11 * 10) / 32.0F;
		final float var16 = 0.0F;
		final float var17 = 0.15625F;
		final float var18 = (5 + var11 * 10) / 32.0F;
		final float var19 = (10 + var11 * 10) / 32.0F;
		final float var20 = 0.05625F;
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		final float var21 = p_76986_1_.arrowShake - p_76986_9_;

		if (var21 > 0.0F) {
			final float var22 = -MathHelper.sin(var21 * 3.0F) * var21;
			GL11.glRotatef(var22, 0.0F, 0.0F, 1.0F);
		}

		GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(var20, var20, var20);
		GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
		GL11.glNormal3f(var20, 0.0F, 0.0F);
		var10.startDrawingQuads();
		var10.addVertexWithUV(-7.0D, -2.0D, -2.0D, var16, var18);
		var10.addVertexWithUV(-7.0D, -2.0D, 2.0D, var17, var18);
		var10.addVertexWithUV(-7.0D, 2.0D, 2.0D, var17, var19);
		var10.addVertexWithUV(-7.0D, 2.0D, -2.0D, var16, var19);
		var10.draw();
		GL11.glNormal3f(-var20, 0.0F, 0.0F);
		var10.startDrawingQuads();
		var10.addVertexWithUV(-7.0D, 2.0D, -2.0D, var16, var18);
		var10.addVertexWithUV(-7.0D, 2.0D, 2.0D, var17, var18);
		var10.addVertexWithUV(-7.0D, -2.0D, 2.0D, var17, var19);
		var10.addVertexWithUV(-7.0D, -2.0D, -2.0D, var16, var19);
		var10.draw();

		for (int var23 = 0; var23 < 4; ++var23) {
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, 0.0F, var20);
			var10.startDrawingQuads();
			var10.addVertexWithUV(-8.0D, -2.0D, 0.0D, var12, var14);
			var10.addVertexWithUV(8.0D, -2.0D, 0.0D, var13, var14);
			var10.addVertexWithUV(8.0D, 2.0D, 0.0D, var13, var15);
			var10.addVertexWithUV(-8.0D, 2.0D, 0.0D, var12, var15);
			var10.draw();
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntityArrow) p_110775_1_);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityArrow p_110775_1_) {
		return arrowTextures;
	}
}
