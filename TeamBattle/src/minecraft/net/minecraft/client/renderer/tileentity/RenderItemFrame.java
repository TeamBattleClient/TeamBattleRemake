package net.minecraft.client.renderer.tileentity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;

import org.lwjgl.opengl.GL11;

public class RenderItemFrame extends Render {
	private static final ResourceLocation mapBackgroundTextures = new ResourceLocation(
			"textures/map/map_background.png");
	private final RenderBlocks field_147916_f = new RenderBlocks();
	private final Minecraft field_147917_g = Minecraft.getMinecraft();
	private IIcon field_94147_f;

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
		this.doRender((EntityItemFrame) p_76986_1_, p_76986_2_, p_76986_4_,
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
	public void doRender(EntityItemFrame p_76986_1_, double p_76986_2_,
			double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		GL11.glPushMatrix();
		final double var10 = p_76986_1_.posX - p_76986_2_ - 0.5D;
		final double var12 = p_76986_1_.posY - p_76986_4_ - 0.5D;
		final double var14 = p_76986_1_.posZ - p_76986_6_ - 0.5D;
		final int var16 = p_76986_1_.field_146063_b
				+ Direction.offsetX[p_76986_1_.hangingDirection];
		final int var17 = p_76986_1_.field_146064_c;
		final int var18 = p_76986_1_.field_146062_d
				+ Direction.offsetZ[p_76986_1_.hangingDirection];
		GL11.glTranslated(var16 - var10, var17 - var12, var18 - var14);

		if (p_76986_1_.getDisplayedItem() != null
				&& p_76986_1_.getDisplayedItem().getItem() == Items.filled_map) {
			func_147915_b(p_76986_1_);
		} else {
			renderFrameItemAsBlock(p_76986_1_);
		}

		func_82402_b(p_76986_1_);
		GL11.glPopMatrix();
		func_147914_a(p_76986_1_, p_76986_2_
				+ Direction.offsetX[p_76986_1_.hangingDirection] * 0.3F,
				p_76986_4_ - 0.25D, p_76986_6_
						+ Direction.offsetZ[p_76986_1_.hangingDirection] * 0.3F);
	}

	protected void func_147914_a(EntityItemFrame p_147914_1_,
			double p_147914_2_, double p_147914_4_, double p_147914_6_) {
		if (Minecraft.isGuiEnabled() && p_147914_1_.getDisplayedItem() != null
				&& p_147914_1_.getDisplayedItem().hasDisplayName()
				&& renderManager.field_147941_i == p_147914_1_) {
			final float var8 = 1.6F;
			final float var9 = 0.016666668F * var8;
			final double var10 = p_147914_1_
					.getDistanceSqToEntity(renderManager.livingPlayer);
			final float var12 = p_147914_1_.isSneaking() ? 32.0F : 64.0F;

			if (var10 < var12 * var12) {
				final String var13 = p_147914_1_.getDisplayedItem()
						.getDisplayName();

				if (p_147914_1_.isSneaking()) {
					final FontRenderer var14 = getFontRendererFromRenderManager();
					GL11.glPushMatrix();
					GL11.glTranslatef((float) p_147914_2_ + 0.0F,
							(float) p_147914_4_ + p_147914_1_.height + 0.5F,
							(float) p_147914_6_);
					GL11.glNormal3f(0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
					GL11.glScalef(-var9, -var9, var9);
					GL11.glDisable(GL11.GL_LIGHTING);
					GL11.glTranslatef(0.0F, 0.25F / var9, 0.0F);
					GL11.glDepthMask(false);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA,
							GL11.GL_ONE_MINUS_SRC_ALPHA);
					final Tessellator var15 = Tessellator.instance;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					var15.startDrawingQuads();
					final int var16 = var14.getStringWidth(var13) / 2;
					var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
					var15.addVertex(-var16 - 1, -1.0D, 0.0D);
					var15.addVertex(-var16 - 1, 8.0D, 0.0D);
					var15.addVertex(var16 + 1, 8.0D, 0.0D);
					var15.addVertex(var16 + 1, -1.0D, 0.0D);
					var15.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					GL11.glDepthMask(true);
					var14.drawString(var13, -var14.getStringWidth(var13) / 2,
							0, 553648127);
					GL11.glEnable(GL11.GL_LIGHTING);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glPopMatrix();
				} else {
					func_147906_a(p_147914_1_, var13, p_147914_2_, p_147914_4_,
							p_147914_6_, 64);
				}
			}
		}
	}

	private void func_147915_b(EntityItemFrame p_147915_1_) {
		GL11.glPushMatrix();
		GL11.glRotatef(p_147915_1_.rotationYaw, 0.0F, 1.0F, 0.0F);
		renderManager.renderEngine
				.bindTexture(TextureMap.locationBlocksTexture);
		final Block var2 = Blocks.planks;
		final float var3 = 0.0625F;
		final float var4 = 1.0F;
		final float var5 = var4 / 2.0F;
		GL11.glPushMatrix();
		field_147916_f.overrideBlockBounds(0.0D, 0.5F - var5 + 0.0625F,
				0.5F - var5 + 0.0625F, var3, 0.5F + var5 - 0.0625F,
				0.5F + var5 - 0.0625F);
		field_147916_f.setOverrideBlockTexture(field_94147_f);
		field_147916_f.renderBlockAsItem(var2, 0, 1.0F);
		field_147916_f.clearOverrideBlockTexture();
		field_147916_f.unlockBlockBounds();
		GL11.glPopMatrix();
		field_147916_f.setOverrideBlockTexture(Blocks.planks.getIcon(1, 2));
		GL11.glPushMatrix();
		field_147916_f.overrideBlockBounds(0.0D, 0.5F - var5, 0.5F - var5,
				var3 + 1.0E-4F, var3 + 0.5F - var5, 0.5F + var5);
		field_147916_f.renderBlockAsItem(var2, 0, 1.0F);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		field_147916_f.overrideBlockBounds(0.0D, 0.5F + var5 - var3,
				0.5F - var5, var3 + 1.0E-4F, 0.5F + var5, 0.5F + var5);
		field_147916_f.renderBlockAsItem(var2, 0, 1.0F);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		field_147916_f.overrideBlockBounds(0.0D, 0.5F - var5, 0.5F - var5,
				var3, 0.5F + var5, var3 + 0.5F - var5);
		field_147916_f.renderBlockAsItem(var2, 0, 1.0F);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		field_147916_f.overrideBlockBounds(0.0D, 0.5F - var5, 0.5F + var5
				- var3, var3, 0.5F + var5, 0.5F + var5);
		field_147916_f.renderBlockAsItem(var2, 0, 1.0F);
		GL11.glPopMatrix();
		field_147916_f.unlockBlockBounds();
		field_147916_f.clearOverrideBlockTexture();
		GL11.glPopMatrix();
	}

	private void func_82402_b(EntityItemFrame p_82402_1_) {
		final ItemStack var2 = p_82402_1_.getDisplayedItem();

		if (var2 != null) {
			final EntityItem var3 = new EntityItem(p_82402_1_.worldObj, 0.0D,
					0.0D, 0.0D, var2);
			final Item var4 = var3.getEntityItem().getItem();
			var3.getEntityItem().stackSize = 1;
			var3.hoverStart = 0.0F;
			GL11.glPushMatrix();
			GL11.glTranslatef(-0.453125F
					* Direction.offsetX[p_82402_1_.hangingDirection], -0.18F,
					-0.453125F * Direction.offsetZ[p_82402_1_.hangingDirection]);
			GL11.glRotatef(180.0F + p_82402_1_.rotationYaw, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-90 * p_82402_1_.getRotation(), 0.0F, 0.0F, 1.0F);

			switch (p_82402_1_.getRotation()) {
			case 1:
				GL11.glTranslatef(-0.16F, -0.16F, 0.0F);
				break;

			case 2:
				GL11.glTranslatef(0.0F, -0.32F, 0.0F);
				break;

			case 3:
				GL11.glTranslatef(0.16F, -0.16F, 0.0F);
			}

			if (var4 == Items.filled_map) {
				renderManager.renderEngine.bindTexture(mapBackgroundTextures);
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
				final float var6 = 0.0078125F;
				GL11.glScalef(var6, var6, var6);

				switch (p_82402_1_.getRotation()) {
				case 0:
					GL11.glTranslatef(-64.0F, -87.0F, -1.5F);
					break;

				case 1:
					GL11.glTranslatef(-66.5F, -84.5F, -1.5F);
					break;

				case 2:
					GL11.glTranslatef(-64.0F, -82.0F, -1.5F);
					break;

				case 3:
					GL11.glTranslatef(-61.5F, -84.5F, -1.5F);
				}

				GL11.glNormal3f(0.0F, 0.0F, -1.0F);
				final MapData var7 = Items.filled_map.getMapData(
						var3.getEntityItem(), p_82402_1_.worldObj);
				GL11.glTranslatef(0.0F, 0.0F, -1.0F);

				if (var7 != null) {
					field_147917_g.entityRenderer.getMapItemRenderer()
							.func_148250_a(var7, true);
				}
			} else {
				if (var4 == Items.compass) {
					final TextureManager var12 = Minecraft.getMinecraft()
							.getTextureManager();
					var12.bindTexture(TextureMap.locationItemsTexture);
					final TextureAtlasSprite var14 = ((TextureMap) var12
							.getTexture(TextureMap.locationItemsTexture))
							.getAtlasSprite(Items.compass.getIconIndex(
									var3.getEntityItem()).getIconName());

					if (var14 instanceof TextureCompass) {
						final TextureCompass var15 = (TextureCompass) var14;
						final double var8 = var15.currentAngle;
						final double var10 = var15.angleDelta;
						var15.currentAngle = 0.0D;
						var15.angleDelta = 0.0D;
						var15.updateCompass(
								p_82402_1_.worldObj,
								p_82402_1_.posX,
								p_82402_1_.posZ,
								MathHelper
										.wrapAngleTo180_float(180 + p_82402_1_.hangingDirection * 90),
								false, true);
						var15.currentAngle = var8;
						var15.angleDelta = var10;
					}
				}

				RenderItem.renderInFrame = true;
				RenderManager.instance.func_147940_a(var3, 0.0D, 0.0D, 0.0D,
						0.0F, 0.0F);
				RenderItem.renderInFrame = false;

				if (var4 == Items.compass) {
					final TextureAtlasSprite var13 = ((TextureMap) Minecraft
							.getMinecraft().getTextureManager()
							.getTexture(TextureMap.locationItemsTexture))
							.getAtlasSprite(Items.compass.getIconIndex(
									var3.getEntityItem()).getIconName());

					if (var13.getFrameCount() > 0) {
						var13.updateAnimation();
					}
				}
			}

			GL11.glPopMatrix();
		}
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntityItemFrame) p_110775_1_);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityItemFrame p_110775_1_) {
		return null;
	}

	/**
	 * Render the item frame's item as a block.
	 */
	private void renderFrameItemAsBlock(EntityItemFrame p_82403_1_) {
		GL11.glPushMatrix();
		GL11.glRotatef(p_82403_1_.rotationYaw, 0.0F, 1.0F, 0.0F);
		renderManager.renderEngine
				.bindTexture(TextureMap.locationBlocksTexture);
		final Block var2 = Blocks.planks;
		final float var3 = 0.0625F;
		final float var4 = 0.75F;
		final float var5 = var4 / 2.0F;
		GL11.glPushMatrix();
		field_147916_f.overrideBlockBounds(0.0D, 0.5F - var5 + 0.0625F,
				0.5F - var5 + 0.0625F, var3 * 0.5F, 0.5F + var5 - 0.0625F,
				0.5F + var5 - 0.0625F);
		field_147916_f.setOverrideBlockTexture(field_94147_f);
		field_147916_f.renderBlockAsItem(var2, 0, 1.0F);
		field_147916_f.clearOverrideBlockTexture();
		field_147916_f.unlockBlockBounds();
		GL11.glPopMatrix();
		field_147916_f.setOverrideBlockTexture(Blocks.planks.getIcon(1, 2));
		GL11.glPushMatrix();
		field_147916_f.overrideBlockBounds(0.0D, 0.5F - var5, 0.5F - var5,
				var3 + 1.0E-4F, var3 + 0.5F - var5, 0.5F + var5);
		field_147916_f.renderBlockAsItem(var2, 0, 1.0F);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		field_147916_f.overrideBlockBounds(0.0D, 0.5F + var5 - var3,
				0.5F - var5, var3 + 1.0E-4F, 0.5F + var5, 0.5F + var5);
		field_147916_f.renderBlockAsItem(var2, 0, 1.0F);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		field_147916_f.overrideBlockBounds(0.0D, 0.5F - var5, 0.5F - var5,
				var3, 0.5F + var5, var3 + 0.5F - var5);
		field_147916_f.renderBlockAsItem(var2, 0, 1.0F);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		field_147916_f.overrideBlockBounds(0.0D, 0.5F - var5, 0.5F + var5
				- var3, var3, 0.5F + var5, 0.5F + var5);
		field_147916_f.renderBlockAsItem(var2, 0, 1.0F);
		GL11.glPopMatrix();
		field_147916_f.unlockBlockBounds();
		field_147916_f.clearOverrideBlockTexture();
		GL11.glPopMatrix();
	}

	@Override
	public void updateIcons(IIconRegister p_94143_1_) {
		field_94147_f = p_94143_1_.registerIcon("itemframe_background");
	}
}
