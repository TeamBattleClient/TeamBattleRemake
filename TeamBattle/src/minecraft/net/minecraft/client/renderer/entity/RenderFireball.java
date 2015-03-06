package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Items;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderFireball extends Render {
	private final float field_77002_a;

	public RenderFireball(float p_i1254_1_) {
		field_77002_a = p_i1254_1_;
	}

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
		this.doRender((EntityFireball) p_76986_1_, p_76986_2_, p_76986_4_,
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
	public void doRender(EntityFireball p_76986_1_, double p_76986_2_,
			double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		GL11.glPushMatrix();
		bindEntityTexture(p_76986_1_);
		GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_,
				(float) p_76986_6_);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		final float var10 = field_77002_a;
		GL11.glScalef(var10 / 1.0F, var10 / 1.0F, var10 / 1.0F);
		final IIcon var11 = Items.fire_charge.getIconFromDamage(0);
		final Tessellator var12 = Tessellator.instance;
		final float var13 = var11.getMinU();
		final float var14 = var11.getMaxU();
		final float var15 = var11.getMinV();
		final float var16 = var11.getMaxV();
		final float var17 = 1.0F;
		final float var18 = 0.5F;
		final float var19 = 0.25F;
		GL11.glRotatef(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		var12.startDrawingQuads();
		var12.setNormal(0.0F, 1.0F, 0.0F);
		var12.addVertexWithUV(0.0F - var18, 0.0F - var19, 0.0D, var13, var16);
		var12.addVertexWithUV(var17 - var18, 0.0F - var19, 0.0D, var14, var16);
		var12.addVertexWithUV(var17 - var18, 1.0F - var19, 0.0D, var14, var15);
		var12.addVertexWithUV(0.0F - var18, 1.0F - var19, 0.0D, var13, var15);
		var12.draw();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntityFireball) p_110775_1_);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityFireball p_110775_1_) {
		return TextureMap.locationItemsTexture;
	}
}
