package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderFish extends Render {
	private static final ResourceLocation field_110792_a = new ResourceLocation(
			"textures/particle/particles.png");

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
		this.doRender((EntityFishHook) p_76986_1_, p_76986_2_, p_76986_4_,
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
	public void doRender(EntityFishHook p_76986_1_, double p_76986_2_,
			double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_,
				(float) p_76986_6_);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		bindEntityTexture(p_76986_1_);
		final Tessellator var10 = Tessellator.instance;
		final byte var11 = 1;
		final byte var12 = 2;
		final float var13 = (var11 * 8 + 0) / 128.0F;
		final float var14 = (var11 * 8 + 8) / 128.0F;
		final float var15 = (var12 * 8 + 0) / 128.0F;
		final float var16 = (var12 * 8 + 8) / 128.0F;
		final float var17 = 1.0F;
		final float var18 = 0.5F;
		final float var19 = 0.5F;
		GL11.glRotatef(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		var10.startDrawingQuads();
		var10.setNormal(0.0F, 1.0F, 0.0F);
		var10.addVertexWithUV(0.0F - var18, 0.0F - var19, 0.0D, var13, var16);
		var10.addVertexWithUV(var17 - var18, 0.0F - var19, 0.0D, var14, var16);
		var10.addVertexWithUV(var17 - var18, 1.0F - var19, 0.0D, var14, var15);
		var10.addVertexWithUV(0.0F - var18, 1.0F - var19, 0.0D, var13, var15);
		var10.draw();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();

		if (p_76986_1_.field_146042_b != null) {
			final float var20 = p_76986_1_.field_146042_b
					.getSwingProgress(p_76986_9_);
			final float var21 = MathHelper.sin(MathHelper.sqrt_float(var20)
					* (float) Math.PI);
			final Vec3 var22 = Vec3.createVectorHelper(-0.5D, 0.03D, 0.8D);
			var22.rotateAroundX(-(p_76986_1_.field_146042_b.prevRotationPitch + (p_76986_1_.field_146042_b.rotationPitch - p_76986_1_.field_146042_b.prevRotationPitch)
					* p_76986_9_)
					* (float) Math.PI / 180.0F);
			var22.rotateAroundY(-(p_76986_1_.field_146042_b.prevRotationYaw + (p_76986_1_.field_146042_b.rotationYaw - p_76986_1_.field_146042_b.prevRotationYaw)
					* p_76986_9_)
					* (float) Math.PI / 180.0F);
			var22.rotateAroundY(var21 * 0.5F);
			var22.rotateAroundX(-var21 * 0.7F);
			double var23 = p_76986_1_.field_146042_b.prevPosX
					+ (p_76986_1_.field_146042_b.posX - p_76986_1_.field_146042_b.prevPosX)
					* p_76986_9_ + var22.xCoord;
			double var25 = p_76986_1_.field_146042_b.prevPosY
					+ (p_76986_1_.field_146042_b.posY - p_76986_1_.field_146042_b.prevPosY)
					* p_76986_9_ + var22.yCoord;
			double var27 = p_76986_1_.field_146042_b.prevPosZ
					+ (p_76986_1_.field_146042_b.posZ - p_76986_1_.field_146042_b.prevPosZ)
					* p_76986_9_ + var22.zCoord;
			final double var29 = p_76986_1_.field_146042_b == Minecraft
					.getMinecraft().thePlayer ? 0.0D
					: (double) p_76986_1_.field_146042_b.getEyeHeight();

			if (renderManager.options.thirdPersonView > 0
					|| p_76986_1_.field_146042_b != Minecraft.getMinecraft().thePlayer) {
				final float var31 = (p_76986_1_.field_146042_b.prevRenderYawOffset + (p_76986_1_.field_146042_b.renderYawOffset - p_76986_1_.field_146042_b.prevRenderYawOffset)
						* p_76986_9_)
						* (float) Math.PI / 180.0F;
				final double var32 = MathHelper.sin(var31);
				final double var34 = MathHelper.cos(var31);
				var23 = p_76986_1_.field_146042_b.prevPosX
						+ (p_76986_1_.field_146042_b.posX - p_76986_1_.field_146042_b.prevPosX)
						* p_76986_9_ - var34 * 0.35D - var32 * 0.85D;
				var25 = p_76986_1_.field_146042_b.prevPosY
						+ var29
						+ (p_76986_1_.field_146042_b.posY - p_76986_1_.field_146042_b.prevPosY)
						* p_76986_9_ - 0.45D;
				var27 = p_76986_1_.field_146042_b.prevPosZ
						+ (p_76986_1_.field_146042_b.posZ - p_76986_1_.field_146042_b.prevPosZ)
						* p_76986_9_ - var32 * 0.35D + var34 * 0.85D;
			}

			final double var46 = p_76986_1_.prevPosX
					+ (p_76986_1_.posX - p_76986_1_.prevPosX) * p_76986_9_;
			final double var33 = p_76986_1_.prevPosY
					+ (p_76986_1_.posY - p_76986_1_.prevPosY) * p_76986_9_
					+ 0.25D;
			final double var35 = p_76986_1_.prevPosZ
					+ (p_76986_1_.posZ - p_76986_1_.prevPosZ) * p_76986_9_;
			final double var37 = (float) (var23 - var46);
			final double var39 = (float) (var25 - var33);
			final double var41 = (float) (var27 - var35);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			var10.startDrawing(3);
			var10.setColorOpaque_I(0);
			final byte var43 = 16;

			for (int var44 = 0; var44 <= var43; ++var44) {
				final float var45 = (float) var44 / (float) var43;
				var10.addVertex(p_76986_2_ + var37 * var45, p_76986_4_ + var39
						* (var45 * var45 + var45) * 0.5D + 0.25D, p_76986_6_
						+ var41 * var45);
			}

			var10.draw();
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntityFishHook) p_110775_1_);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityFishHook p_110775_1_) {
		return field_110792_a;
	}
}
