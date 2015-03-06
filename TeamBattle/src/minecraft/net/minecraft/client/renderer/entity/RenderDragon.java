package net.minecraft.client.renderer.entity;

import java.util.Random;

import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderDragon extends RenderLiving {
	private static final ResourceLocation enderDragonCrystalBeamTextures = new ResourceLocation(
			"textures/entity/endercrystal/endercrystal_beam.png");
	private static final ResourceLocation enderDragonExplodingTextures = new ResourceLocation(
			"textures/entity/enderdragon/dragon_exploding.png");
	private static final ResourceLocation enderDragonEyesTextures = new ResourceLocation(
			"textures/entity/enderdragon/dragon_eyes.png");
	private static final ResourceLocation enderDragonTextures = new ResourceLocation(
			"textures/entity/enderdragon/dragon.png");

	/** An instance of the dragon model in RenderDragon */
	protected ModelDragon modelDragon;

	public RenderDragon() {
		super(new ModelDragon(0.0F), 0.5F);
		modelDragon = (ModelDragon) mainModel;
		setRenderPassModel(mainModel);
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
		this.doRender((EntityDragon) p_76986_1_, p_76986_2_, p_76986_4_,
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
	public void doRender(EntityDragon p_76986_1_, double p_76986_2_,
			double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		BossStatus.setBossStatus(p_76986_1_, false);
		super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_,
				p_76986_8_, p_76986_9_);

		if (p_76986_1_.healingEnderCrystal != null) {
			final float var10 = p_76986_1_.healingEnderCrystal.innerRotation
					+ p_76986_9_;
			float var11 = MathHelper.sin(var10 * 0.2F) / 2.0F + 0.5F;
			var11 = (var11 * var11 + var11) * 0.2F;
			final float var12 = (float) (p_76986_1_.healingEnderCrystal.posX
					- p_76986_1_.posX - (p_76986_1_.prevPosX - p_76986_1_.posX)
					* (1.0F - p_76986_9_));
			final float var13 = (float) (var11
					+ p_76986_1_.healingEnderCrystal.posY - 1.0D
					- p_76986_1_.posY - (p_76986_1_.prevPosY - p_76986_1_.posY)
					* (1.0F - p_76986_9_));
			final float var14 = (float) (p_76986_1_.healingEnderCrystal.posZ
					- p_76986_1_.posZ - (p_76986_1_.prevPosZ - p_76986_1_.posZ)
					* (1.0F - p_76986_9_));
			final float var15 = MathHelper.sqrt_float(var12 * var12 + var14
					* var14);
			final float var16 = MathHelper.sqrt_float(var12 * var12 + var13
					* var13 + var14 * var14);
			GL11.glPushMatrix();
			GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_ + 2.0F,
					(float) p_76986_6_);
			GL11.glRotatef((float) -Math.atan2(var14, var12) * 180.0F
					/ (float) Math.PI - 90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef((float) -Math.atan2(var15, var13) * 180.0F
					/ (float) Math.PI - 90.0F, 1.0F, 0.0F, 0.0F);
			final Tessellator var17 = Tessellator.instance;
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_CULL_FACE);
			bindTexture(enderDragonCrystalBeamTextures);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			final float var18 = 0.0F - (p_76986_1_.ticksExisted + p_76986_9_) * 0.01F;
			final float var19 = MathHelper.sqrt_float(var12 * var12 + var13
					* var13 + var14 * var14)
					/ 32.0F - (p_76986_1_.ticksExisted + p_76986_9_) * 0.01F;
			var17.startDrawing(5);
			final byte var20 = 8;

			for (int var21 = 0; var21 <= var20; ++var21) {
				final float var22 = MathHelper.sin(var21 % var20
						* (float) Math.PI * 2.0F / var20) * 0.75F;
				final float var23 = MathHelper.cos(var21 % var20
						* (float) Math.PI * 2.0F / var20) * 0.75F;
				final float var24 = var21 % var20 * 1.0F / var20;
				var17.setColorOpaque_I(0);
				var17.addVertexWithUV(var22 * 0.2F, var23 * 0.2F, 0.0D, var24,
						var19);
				var17.setColorOpaque_I(16777215);
				var17.addVertexWithUV(var22, var23, var16, var24, var18);
			}

			var17.draw();
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glShadeModel(GL11.GL_FLAT);
			RenderHelper.enableStandardItemLighting();
			GL11.glPopMatrix();
		}
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
	public void doRender(EntityLiving p_76986_1_, double p_76986_2_,
			double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		this.doRender((EntityDragon) p_76986_1_, p_76986_2_, p_76986_4_,
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
	@Override
	public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_,
			double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		this.doRender((EntityDragon) p_76986_1_, p_76986_2_, p_76986_4_,
				p_76986_6_, p_76986_8_, p_76986_9_);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntityDragon) p_110775_1_);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityDragon p_110775_1_) {
		return enderDragonTextures;
	}

	protected void renderEquippedItems(EntityDragon p_77029_1_, float p_77029_2_) {
		super.renderEquippedItems(p_77029_1_, p_77029_2_);
		final Tessellator var3 = Tessellator.instance;

		if (p_77029_1_.deathTicks > 0) {
			RenderHelper.disableStandardItemLighting();
			final float var4 = (p_77029_1_.deathTicks + p_77029_2_) / 200.0F;
			float var5 = 0.0F;

			if (var4 > 0.8F) {
				var5 = (var4 - 0.8F) / 0.2F;
			}

			final Random var6 = new Random(432L);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDepthMask(false);
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, -1.0F, -2.0F);

			for (int var7 = 0; var7 < (var4 + var4 * var4) / 2.0F * 60.0F; ++var7) {
				GL11.glRotatef(var6.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(var6.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(var6.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(var6.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(var6.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(var6.nextFloat() * 360.0F + var4 * 90.0F, 0.0F,
						0.0F, 1.0F);
				var3.startDrawing(6);
				final float var8 = var6.nextFloat() * 20.0F + 5.0F + var5
						* 10.0F;
				final float var9 = var6.nextFloat() * 2.0F + 1.0F + var5 * 2.0F;
				var3.setColorRGBA_I(16777215, (int) (255.0F * (1.0F - var5)));
				var3.addVertex(0.0D, 0.0D, 0.0D);
				var3.setColorRGBA_I(16711935, 0);
				var3.addVertex(-0.866D * var9, var8, -0.5F * var9);
				var3.addVertex(0.866D * var9, var8, -0.5F * var9);
				var3.addVertex(0.0D, var8, 1.0F * var9);
				var3.addVertex(-0.866D * var9, var8, -0.5F * var9);
				var3.draw();
			}

			GL11.glPopMatrix();
			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glShadeModel(GL11.GL_FLAT);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			RenderHelper.enableStandardItemLighting();
		}
	}

	@Override
	protected void renderEquippedItems(EntityLivingBase p_77029_1_,
			float p_77029_2_) {
		this.renderEquippedItems((EntityDragon) p_77029_1_, p_77029_2_);
	}

	/**
	 * Renders the model in RenderLiving
	 */
	protected void renderModel(EntityDragon p_77036_1_, float p_77036_2_,
			float p_77036_3_, float p_77036_4_, float p_77036_5_,
			float p_77036_6_, float p_77036_7_) {
		if (p_77036_1_.deathTicks > 0) {
			final float var8 = p_77036_1_.deathTicks / 200.0F;
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(GL11.GL_GREATER, var8);
			bindTexture(enderDragonExplodingTextures);
			mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_,
					p_77036_5_, p_77036_6_, p_77036_7_);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			GL11.glDepthFunc(GL11.GL_EQUAL);
		}

		bindEntityTexture(p_77036_1_);
		mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_,
				p_77036_5_, p_77036_6_, p_77036_7_);

		if (p_77036_1_.hurtTime > 0) {
			GL11.glDepthFunc(GL11.GL_EQUAL);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.5F);
			mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_,
					p_77036_5_, p_77036_6_, p_77036_7_);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
		}
	}

	/**
	 * Renders the model in RenderLiving
	 */
	@Override
	protected void renderModel(EntityLivingBase p_77036_1_, float p_77036_2_,
			float p_77036_3_, float p_77036_4_, float p_77036_5_,
			float p_77036_6_, float p_77036_7_) {
		this.renderModel((EntityDragon) p_77036_1_, p_77036_2_, p_77036_3_,
				p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
	}

	protected void rotateCorpse(EntityDragon p_77043_1_, float p_77043_2_,
			float p_77043_3_, float p_77043_4_) {
		final float var5 = (float) p_77043_1_.getMovementOffsets(7, p_77043_4_)[0];
		final float var6 = (float) (p_77043_1_
				.getMovementOffsets(5, p_77043_4_)[1] - p_77043_1_
				.getMovementOffsets(10, p_77043_4_)[1]);
		GL11.glRotatef(-var5, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(var6 * 10.0F, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(0.0F, 0.0F, 1.0F);

		if (p_77043_1_.deathTime > 0) {
			float var7 = (p_77043_1_.deathTime + p_77043_4_ - 1.0F) / 20.0F * 1.6F;
			var7 = MathHelper.sqrt_float(var7);

			if (var7 > 1.0F) {
				var7 = 1.0F;
			}

			GL11.glRotatef(var7 * getDeathMaxRotation(p_77043_1_), 0.0F, 0.0F,
					1.0F);
		}
	}

	@Override
	protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_,
			float p_77043_3_, float p_77043_4_) {
		this.rotateCorpse((EntityDragon) p_77043_1_, p_77043_2_, p_77043_3_,
				p_77043_4_);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityDragon p_77032_1_, int p_77032_2_,
			float p_77032_3_) {
		if (p_77032_2_ == 1) {
			GL11.glDepthFunc(GL11.GL_LEQUAL);
		}

		if (p_77032_2_ != 0)
			return -1;
		else {
			bindTexture(enderDragonEyesTextures);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDepthFunc(GL11.GL_EQUAL);
			final char var4 = 61680;
			final int var5 = var4 % 65536;
			final int var6 = var4 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,
					var5 / 1.0F, var6 / 1.0F);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			return 1;
		}
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	@Override
	protected int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_,
			float p_77032_3_) {
		return this.shouldRenderPass((EntityDragon) p_77032_1_, p_77032_2_,
				p_77032_3_);
	}
}
