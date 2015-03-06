package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

import org.lwjgl.opengl.GL11;

public abstract class RenderLiving extends RendererLivingEntity {

	public RenderLiving(ModelBase p_i1262_1_, float p_i1262_2_) {
		super(p_i1262_1_, p_i1262_2_);
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
		this.doRender((EntityLiving) p_76986_1_, p_76986_2_, p_76986_4_,
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
	public void doRender(EntityLiving p_76986_1_, double p_76986_2_,
			double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_,
				p_76986_8_, p_76986_9_);
		func_110827_b(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_,
				p_76986_8_, p_76986_9_);
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
	public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_,
			double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		this.doRender((EntityLiving) p_76986_1_, p_76986_2_, p_76986_4_,
				p_76986_6_, p_76986_8_, p_76986_9_);
	}

	protected boolean func_110813_b(EntityLiving p_110813_1_) {
		return super.func_110813_b(p_110813_1_)
				&& (p_110813_1_.getAlwaysRenderNameTagForRender() || p_110813_1_
						.hasCustomNameTag()
						&& p_110813_1_ == renderManager.field_147941_i);
	}

	@Override
	protected boolean func_110813_b(EntityLivingBase p_110813_1_) {
		return this.func_110813_b((EntityLiving) p_110813_1_);
	}

	protected void func_110827_b(EntityLiving p_110827_1_, double p_110827_2_,
			double p_110827_4_, double p_110827_6_, float p_110827_8_,
			float p_110827_9_) {
		final Entity var10 = p_110827_1_.getLeashedToEntity();

		if (var10 != null) {
			p_110827_4_ -= (1.6D - p_110827_1_.height) * 0.5D;
			final Tessellator var11 = Tessellator.instance;
			final double var12 = func_110828_a(var10.prevRotationYaw,
					var10.rotationYaw, p_110827_9_ * 0.5F) * 0.01745329238474369D;
			final double var14 = func_110828_a(var10.prevRotationPitch,
					var10.rotationPitch, p_110827_9_ * 0.5F) * 0.01745329238474369D;
			double var16 = Math.cos(var12);
			double var18 = Math.sin(var12);
			double var20 = Math.sin(var14);

			if (var10 instanceof EntityHanging) {
				var16 = 0.0D;
				var18 = 0.0D;
				var20 = -1.0D;
			}

			final double var22 = Math.cos(var14);
			final double var24 = func_110828_a(var10.prevPosX, var10.posX,
					p_110827_9_) - var16 * 0.7D - var18 * 0.5D * var22;
			final double var26 = func_110828_a(
					var10.prevPosY + var10.getEyeHeight() * 0.7D, var10.posY
							+ var10.getEyeHeight() * 0.7D, p_110827_9_)
					- var20 * 0.5D - 0.25D;
			final double var28 = func_110828_a(var10.prevPosZ, var10.posZ,
					p_110827_9_) - var18 * 0.7D + var16 * 0.5D * var22;
			final double var30 = func_110828_a(p_110827_1_.prevRenderYawOffset,
					p_110827_1_.renderYawOffset, p_110827_9_)
					* 0.01745329238474369D + Math.PI / 2D;
			var16 = Math.cos(var30) * p_110827_1_.width * 0.4D;
			var18 = Math.sin(var30) * p_110827_1_.width * 0.4D;
			final double var32 = func_110828_a(p_110827_1_.prevPosX,
					p_110827_1_.posX, p_110827_9_) + var16;
			final double var34 = func_110828_a(p_110827_1_.prevPosY,
					p_110827_1_.posY, p_110827_9_);
			final double var36 = func_110828_a(p_110827_1_.prevPosZ,
					p_110827_1_.posZ, p_110827_9_) + var18;
			p_110827_2_ += var16;
			p_110827_6_ += var18;
			final double var38 = (float) (var24 - var32);
			final double var40 = (float) (var26 - var34);
			final double var42 = (float) (var28 - var36);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_CULL_FACE);
			var11.startDrawing(5);
			int var47;
			float var48;

			for (var47 = 0; var47 <= 24; ++var47) {
				if (var47 % 2 == 0) {
					var11.setColorRGBA_F(0.5F, 0.4F, 0.3F, 1.0F);
				} else {
					var11.setColorRGBA_F(0.35F, 0.28F, 0.21000001F, 1.0F);
				}

				var48 = var47 / 24.0F;
				var11.addVertex(p_110827_2_ + var38 * var48 + 0.0D, p_110827_4_
						+ var40 * (var48 * var48 + var48) * 0.5D
						+ ((24.0F - var47) / 18.0F + 0.125F), p_110827_6_
						+ var42 * var48);
				var11.addVertex(p_110827_2_ + var38 * var48 + 0.025D,
						p_110827_4_ + var40 * (var48 * var48 + var48) * 0.5D
								+ ((24.0F - var47) / 18.0F + 0.125F) + 0.025D,
						p_110827_6_ + var42 * var48);
			}

			var11.draw();
			var11.startDrawing(5);

			for (var47 = 0; var47 <= 24; ++var47) {
				if (var47 % 2 == 0) {
					var11.setColorRGBA_F(0.5F, 0.4F, 0.3F, 1.0F);
				} else {
					var11.setColorRGBA_F(0.35F, 0.28F, 0.21000001F, 1.0F);
				}

				var48 = var47 / 24.0F;
				var11.addVertex(p_110827_2_ + var38 * var48 + 0.0D, p_110827_4_
						+ var40 * (var48 * var48 + var48) * 0.5D
						+ ((24.0F - var47) / 18.0F + 0.125F) + 0.025D,
						p_110827_6_ + var42 * var48);
				var11.addVertex(p_110827_2_ + var38 * var48 + 0.025D,
						p_110827_4_ + var40 * (var48 * var48 + var48) * 0.5D
								+ ((24.0F - var47) / 18.0F + 0.125F),
						p_110827_6_ + var42 * var48 + 0.025D);
			}

			var11.draw();
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_CULL_FACE);
		}
	}

	private double func_110828_a(double p_110828_1_, double p_110828_3_,
			double p_110828_5_) {
		return p_110828_1_ + (p_110828_3_ - p_110828_1_) * p_110828_5_;
	}
}
