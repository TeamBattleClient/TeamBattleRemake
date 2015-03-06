package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.EventSystem.events.EventRenderBurning;

public class ItemRenderer {
	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation(
			"textures/misc/enchanted_item_glint.png");

	private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation(
			"textures/map/map_background.png");
	private static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation(
			"textures/misc/underwater.png");

	/**
	 * Renders an item held in hand as a 2D texture with thickness
	 */
	public static void renderItemIn2D(Tessellator p_78439_0_, float p_78439_1_,
			float p_78439_2_, float p_78439_3_, float p_78439_4_,
			int p_78439_5_, int p_78439_6_, float p_78439_7_) {
		p_78439_0_.startDrawingQuads();
		p_78439_0_.setNormal(0.0F, 0.0F, 1.0F);
		p_78439_0_.addVertexWithUV(0.0D, 0.0D, 0.0D, p_78439_1_, p_78439_4_);
		p_78439_0_.addVertexWithUV(1.0D, 0.0D, 0.0D, p_78439_3_, p_78439_4_);
		p_78439_0_.addVertexWithUV(1.0D, 1.0D, 0.0D, p_78439_3_, p_78439_2_);
		p_78439_0_.addVertexWithUV(0.0D, 1.0D, 0.0D, p_78439_1_, p_78439_2_);
		p_78439_0_.draw();
		p_78439_0_.startDrawingQuads();
		p_78439_0_.setNormal(0.0F, 0.0F, -1.0F);
		p_78439_0_.addVertexWithUV(0.0D, 1.0D, 0.0F - p_78439_7_, p_78439_1_,
				p_78439_2_);
		p_78439_0_.addVertexWithUV(1.0D, 1.0D, 0.0F - p_78439_7_, p_78439_3_,
				p_78439_2_);
		p_78439_0_.addVertexWithUV(1.0D, 0.0D, 0.0F - p_78439_7_, p_78439_3_,
				p_78439_4_);
		p_78439_0_.addVertexWithUV(0.0D, 0.0D, 0.0F - p_78439_7_, p_78439_1_,
				p_78439_4_);
		p_78439_0_.draw();
		final float var8 = 0.5F * (p_78439_1_ - p_78439_3_) / p_78439_5_;
		final float var9 = 0.5F * (p_78439_4_ - p_78439_2_) / p_78439_6_;
		p_78439_0_.startDrawingQuads();
		p_78439_0_.setNormal(-1.0F, 0.0F, 0.0F);
		int var10;
		float var11;
		float var12;

		for (var10 = 0; var10 < p_78439_5_; ++var10) {
			var11 = (float) var10 / (float) p_78439_5_;
			var12 = p_78439_1_ + (p_78439_3_ - p_78439_1_) * var11 - var8;
			p_78439_0_.addVertexWithUV(var11, 0.0D, 0.0F - p_78439_7_, var12,
					p_78439_4_);
			p_78439_0_.addVertexWithUV(var11, 0.0D, 0.0D, var12, p_78439_4_);
			p_78439_0_.addVertexWithUV(var11, 1.0D, 0.0D, var12, p_78439_2_);
			p_78439_0_.addVertexWithUV(var11, 1.0D, 0.0F - p_78439_7_, var12,
					p_78439_2_);
		}

		p_78439_0_.draw();
		p_78439_0_.startDrawingQuads();
		p_78439_0_.setNormal(1.0F, 0.0F, 0.0F);
		float var13;

		for (var10 = 0; var10 < p_78439_5_; ++var10) {
			var11 = (float) var10 / (float) p_78439_5_;
			var12 = p_78439_1_ + (p_78439_3_ - p_78439_1_) * var11 - var8;
			var13 = var11 + 1.0F / p_78439_5_;
			p_78439_0_.addVertexWithUV(var13, 1.0D, 0.0F - p_78439_7_, var12,
					p_78439_2_);
			p_78439_0_.addVertexWithUV(var13, 1.0D, 0.0D, var12, p_78439_2_);
			p_78439_0_.addVertexWithUV(var13, 0.0D, 0.0D, var12, p_78439_4_);
			p_78439_0_.addVertexWithUV(var13, 0.0D, 0.0F - p_78439_7_, var12,
					p_78439_4_);
		}

		p_78439_0_.draw();
		p_78439_0_.startDrawingQuads();
		p_78439_0_.setNormal(0.0F, 1.0F, 0.0F);

		for (var10 = 0; var10 < p_78439_6_; ++var10) {
			var11 = (float) var10 / (float) p_78439_6_;
			var12 = p_78439_4_ + (p_78439_2_ - p_78439_4_) * var11 - var9;
			var13 = var11 + 1.0F / p_78439_6_;
			p_78439_0_.addVertexWithUV(0.0D, var13, 0.0D, p_78439_1_, var12);
			p_78439_0_.addVertexWithUV(1.0D, var13, 0.0D, p_78439_3_, var12);
			p_78439_0_.addVertexWithUV(1.0D, var13, 0.0F - p_78439_7_,
					p_78439_3_, var12);
			p_78439_0_.addVertexWithUV(0.0D, var13, 0.0F - p_78439_7_,
					p_78439_1_, var12);
		}

		p_78439_0_.draw();
		p_78439_0_.startDrawingQuads();
		p_78439_0_.setNormal(0.0F, -1.0F, 0.0F);

		for (var10 = 0; var10 < p_78439_6_; ++var10) {
			var11 = (float) var10 / (float) p_78439_6_;
			var12 = p_78439_4_ + (p_78439_2_ - p_78439_4_) * var11 - var9;
			p_78439_0_.addVertexWithUV(1.0D, var11, 0.0D, p_78439_3_, var12);
			p_78439_0_.addVertexWithUV(0.0D, var11, 0.0D, p_78439_1_, var12);
			p_78439_0_.addVertexWithUV(0.0D, var11, 0.0F - p_78439_7_,
					p_78439_1_, var12);
			p_78439_0_.addVertexWithUV(1.0D, var11, 0.0F - p_78439_7_,
					p_78439_3_, var12);
		}

		p_78439_0_.draw();
	}

	/** The index of the currently held item (0-8, or -1 if not yet updated) */
	private int equippedItemSlot = -1;

	/**
	 * How far the current item has been equipped (0 disequipped and 1 fully up)
	 */
	private float equippedProgress;
	private ItemStack itemToRender;
	/** A reference to the Minecraft object. */
	private final Minecraft mc;

	private float prevEquippedProgress;

	private final RenderBlocks renderBlocksIr = new RenderBlocks();

	public ItemRenderer(Minecraft p_i1247_1_) {
		mc = p_i1247_1_;
	}

	/**
	 * Renders the fire on the screen for first person mode. Arg:
	 * partialTickTime
	 */
	private void renderFireInFirstPerson(float p_78442_1_) {
		final EventRenderBurning event = new EventRenderBurning();
		TeamBattleClient.getEventManager().call(event);
		if(event.isCancelled()) return;
		final Tessellator var2 = Tessellator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		final float var3 = 1.0F;

		for (int var4 = 0; var4 < 2; ++var4) {
			GL11.glPushMatrix();
			final IIcon var5 = Blocks.fire.func_149840_c(1);
			mc.getTextureManager()
					.bindTexture(TextureMap.locationBlocksTexture);
			final float var6 = var5.getMinU();
			final float var7 = var5.getMaxU();
			final float var8 = var5.getMinV();
			final float var9 = var5.getMaxV();
			final float var10 = (0.0F - var3) / 2.0F;
			final float var11 = var10 + var3;
			final float var12 = 0.0F - var3 / 2.0F;
			final float var13 = var12 + var3;
			final float var14 = -0.5F;
			GL11.glTranslatef(-(var4 * 2 - 1) * 0.24F, -0.3F, 0.0F);
			GL11.glRotatef((var4 * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
			var2.startDrawingQuads();
			var2.addVertexWithUV(var10, var12, var14, var7, var9);
			var2.addVertexWithUV(var11, var12, var14, var6, var9);
			var2.addVertexWithUV(var11, var13, var14, var6, var8);
			var2.addVertexWithUV(var10, var13, var14, var7, var8);
			var2.draw();
			GL11.glPopMatrix();
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
	}

	/**
	 * Renders the texture of the block the player is inside as an overlay.
	 * Args: partialTickTime, blockTextureIndex
	 */
	private void renderInsideOfBlock(float p_78446_1_, IIcon p_78446_2_) {
		mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		final Tessellator var3 = Tessellator.instance;
		final float var4 = 0.1F;
		GL11.glColor4f(var4, var4, var4, 0.5F);
		GL11.glPushMatrix();
		final float var5 = -1.0F;
		final float var6 = 1.0F;
		final float var7 = -1.0F;
		final float var8 = 1.0F;
		final float var9 = -0.5F;
		final float var10 = p_78446_2_.getMinU();
		final float var11 = p_78446_2_.getMaxU();
		final float var12 = p_78446_2_.getMinV();
		final float var13 = p_78446_2_.getMaxV();
		var3.startDrawingQuads();
		var3.addVertexWithUV(var5, var7, var9, var11, var13);
		var3.addVertexWithUV(var6, var7, var9, var10, var13);
		var3.addVertexWithUV(var6, var8, var9, var10, var12);
		var3.addVertexWithUV(var5, var8, var9, var11, var12);
		var3.draw();
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Renders the item stack for being in an entity's hand Args: itemStack
	 */
	public void renderItem(EntityLivingBase p_78443_1_, ItemStack p_78443_2_,
			int p_78443_3_) {
		GL11.glPushMatrix();
		final TextureManager var4 = mc.getTextureManager();
		final Item var5 = p_78443_2_.getItem();
		final Block var6 = Block.getBlockFromItem(var5);

		if (p_78443_2_ != null && var6 != null
				&& var6.getRenderBlockPass() != 0) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_CULL_FACE);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		}

		if (p_78443_2_.getItemSpriteNumber() == 0 && var5 instanceof ItemBlock
				&& RenderBlocks.renderItemIn3d(var6.getRenderType())) {
			var4.bindTexture(var4.getResourceLocation(0));

			if (p_78443_2_ != null && var6 != null
					&& var6.getRenderBlockPass() != 0) {
				GL11.glDepthMask(false);
				renderBlocksIr.renderBlockAsItem(var6,
						p_78443_2_.getItemDamage(), 1.0F);
				GL11.glDepthMask(true);
			} else {
				renderBlocksIr.renderBlockAsItem(var6,
						p_78443_2_.getItemDamage(), 1.0F);
			}
		} else {
			final IIcon var7 = p_78443_1_.getItemIcon(p_78443_2_, p_78443_3_);

			if (var7 == null) {
				GL11.glPopMatrix();
				return;
			}

			var4.bindTexture(var4.getResourceLocation(p_78443_2_
					.getItemSpriteNumber()));
			TextureUtil.func_152777_a(false, false, 1.0F);
			final Tessellator var8 = Tessellator.instance;
			final float var9 = var7.getMinU();
			final float var10 = var7.getMaxU();
			final float var11 = var7.getMinV();
			final float var12 = var7.getMaxV();
			final float var13 = 0.0F;
			final float var14 = 0.3F;
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glTranslatef(-var13, -var14, 0.0F);
			final float var15 = 1.5F;
			GL11.glScalef(var15, var15, var15);
			GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
			renderItemIn2D(var8, var10, var11, var9, var12,
					var7.getIconWidth(), var7.getIconHeight(), 0.0625F);

			if (p_78443_2_.hasEffect() && p_78443_3_ == 0) {
				GL11.glDepthFunc(GL11.GL_EQUAL);
				GL11.glDisable(GL11.GL_LIGHTING);
				var4.bindTexture(RES_ITEM_GLINT);
				GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(768, 1, 1, 0);
				final float var16 = 0.76F;
				GL11.glColor4f(0.5F * var16, 0.25F * var16, 0.8F * var16, 1.0F);
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				GL11.glPushMatrix();
				final float var17 = 0.125F;
				GL11.glScalef(var17, var17, var17);
				float var18 = Minecraft.getSystemTime() % 3000L / 3000.0F * 8.0F;
				GL11.glTranslatef(var18, 0.0F, 0.0F);
				GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
				renderItemIn2D(var8, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				GL11.glScalef(var17, var17, var17);
				var18 = Minecraft.getSystemTime() % 4873L / 4873.0F * 8.0F;
				GL11.glTranslatef(-var18, 0.0F, 0.0F);
				GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
				renderItemIn2D(var8, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
				GL11.glPopMatrix();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glDepthFunc(GL11.GL_LEQUAL);
			}

			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			var4.bindTexture(var4.getResourceLocation(p_78443_2_
					.getItemSpriteNumber()));
			TextureUtil.func_147945_b();
		}

		if (p_78443_2_ != null && var6 != null
				&& var6.getRenderBlockPass() != 0) {
			GL11.glDisable(GL11.GL_BLEND);
		}

		GL11.glPopMatrix();
	}

	/**
	 * Renders the active item in the player's hand when in first person mode.
	 * Args: partialTickTime
	 */
	public void renderItemInFirstPerson(float p_78440_1_) {
		final float var2 = prevEquippedProgress
				+ (equippedProgress - prevEquippedProgress) * p_78440_1_;
		final EntityClientPlayerMP var3 = mc.thePlayer;
		final float var4 = var3.prevRotationPitch
				+ (var3.rotationPitch - var3.prevRotationPitch) * p_78440_1_;
		GL11.glPushMatrix();
		GL11.glRotatef(var4, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(var3.prevRotationYaw
				+ (var3.rotationYaw - var3.prevRotationYaw) * p_78440_1_, 0.0F,
				1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		final EntityPlayerSP var5 = var3;
		final float var6 = var5.prevRenderArmPitch
				+ (var5.renderArmPitch - var5.prevRenderArmPitch) * p_78440_1_;
		final float var7 = var5.prevRenderArmYaw
				+ (var5.renderArmYaw - var5.prevRenderArmYaw) * p_78440_1_;
		GL11.glRotatef((var3.rotationPitch - var6) * 0.1F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef((var3.rotationYaw - var7) * 0.1F, 0.0F, 1.0F, 0.0F);
		final ItemStack var8 = itemToRender;

		if (var8 != null && var8.getItem() instanceof ItemCloth) {
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		}

		final int var9 = mc.theWorld.getLightBrightnessForSkyBlocks(
				MathHelper.floor_double(var3.posX),
				MathHelper.floor_double(var3.posY),
				MathHelper.floor_double(var3.posZ), 0);
		final int var10 = var9 % 65536;
		final int var11 = var9 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,
				var10 / 1.0F, var11 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float var13;
		float var14;
		float var15;

		if (var8 != null) {
			final int var12 = var8.getItem().getColorFromItemStack(var8, 0);
			var13 = (var12 >> 16 & 255) / 255.0F;
			var14 = (var12 >> 8 & 255) / 255.0F;
			var15 = (var12 & 255) / 255.0F;
			GL11.glColor4f(var13, var14, var15, 1.0F);
		} else {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}

		float var16;
		float var17;
		float var18;
		float var22;
		Render var26;
		RenderPlayer var29;

		if (var8 != null && var8.getItem() == Items.filled_map) {
			GL11.glPushMatrix();
			var22 = 0.8F;
			var13 = var3.getSwingProgress(p_78440_1_);
			var14 = MathHelper.sin(var13 * (float) Math.PI);
			var15 = MathHelper.sin(MathHelper.sqrt_float(var13)
					* (float) Math.PI);
			GL11.glTranslatef(
					-var15 * 0.4F,
					MathHelper.sin(MathHelper.sqrt_float(var13)
							* (float) Math.PI * 2.0F) * 0.2F, -var14 * 0.2F);
			var13 = 1.0F - var4 / 45.0F + 0.1F;

			if (var13 < 0.0F) {
				var13 = 0.0F;
			}

			if (var13 > 1.0F) {
				var13 = 1.0F;
			}

			var13 = -MathHelper.cos(var13 * (float) Math.PI) * 0.5F + 0.5F;
			GL11.glTranslatef(0.0F, 0.0F * var22 - (1.0F - var2) * 1.2F - var13
					* 0.5F + 0.04F, -0.9F * var22);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(var13 * -85.0F, 0.0F, 0.0F, 1.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			mc.getTextureManager().bindTexture(var3.getLocationSkin());

			for (int var24 = 0; var24 < 2; ++var24) {
				final int var25 = var24 * 2 - 1;
				GL11.glPushMatrix();
				GL11.glTranslatef(-0.0F, -0.6F, 1.1F * var25);
				GL11.glRotatef(-45 * var25, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-65 * var25, 0.0F, 1.0F, 0.0F);
				var26 = RenderManager.instance
						.getEntityRenderObject(mc.thePlayer);
				var29 = (RenderPlayer) var26;
				var18 = 1.0F;
				GL11.glScalef(var18, var18, var18);
				var29.renderFirstPersonArm(mc.thePlayer);
				GL11.glPopMatrix();
			}

			var14 = var3.getSwingProgress(p_78440_1_);
			var15 = MathHelper.sin(var14 * var14 * (float) Math.PI);
			var16 = MathHelper.sin(MathHelper.sqrt_float(var14)
					* (float) Math.PI);
			GL11.glRotatef(-var15 * 20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var16 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-var16 * 80.0F, 1.0F, 0.0F, 0.0F);
			var17 = 0.38F;
			GL11.glScalef(var17, var17, var17);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-1.0F, -1.0F, 0.0F);
			var18 = 0.015625F;
			GL11.glScalef(var18, var18, var18);
			mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
			final Tessellator var30 = Tessellator.instance;
			GL11.glNormal3f(0.0F, 0.0F, -1.0F);
			var30.startDrawingQuads();
			final byte var31 = 7;
			var30.addVertexWithUV(0 - var31, 128 + var31, 0.0D, 0.0D, 1.0D);
			var30.addVertexWithUV(128 + var31, 128 + var31, 0.0D, 1.0D, 1.0D);
			var30.addVertexWithUV(128 + var31, 0 - var31, 0.0D, 1.0D, 0.0D);
			var30.addVertexWithUV(0 - var31, 0 - var31, 0.0D, 0.0D, 0.0D);
			var30.draw();
			final MapData var21 = Items.filled_map
					.getMapData(var8, mc.theWorld);

			if (var21 != null) {
				mc.entityRenderer.getMapItemRenderer().func_148250_a(var21,
						false);
			}

			GL11.glPopMatrix();
		} else if (var8 != null) {
			GL11.glPushMatrix();
			var22 = 0.8F;

			if (var3.getItemInUseCount() > 0) {
				final EnumAction var23 = var8.getItemUseAction();

				if (var23 == EnumAction.eat || var23 == EnumAction.drink) {
					var14 = var3.getItemInUseCount() - p_78440_1_ + 1.0F;
					var15 = 1.0F - var14 / var8.getMaxItemUseDuration();
					var16 = 1.0F - var15;
					var16 = var16 * var16 * var16;
					var16 = var16 * var16 * var16;
					var16 = var16 * var16 * var16;
					var17 = 1.0F - var16;
					GL11.glTranslatef(
							0.0F,
							MathHelper.abs(MathHelper.cos(var14 / 4.0F
									* (float) Math.PI) * 0.1F)
									* (var15 > 0.2D ? 1 : 0), 0.0F);
					GL11.glTranslatef(var17 * 0.6F, -var17 * 0.5F, 0.0F);
					GL11.glRotatef(var17 * 90.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(var17 * 10.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(var17 * 30.0F, 0.0F, 0.0F, 1.0F);
				}
			} else {
				var13 = var3.getSwingProgress(p_78440_1_);
				var14 = MathHelper.sin(var13 * (float) Math.PI);
				var15 = MathHelper.sin(MathHelper.sqrt_float(var13)
						* (float) Math.PI);
				GL11.glTranslatef(
						-var15 * 0.4F,
						MathHelper.sin(MathHelper.sqrt_float(var13)
								* (float) Math.PI * 2.0F) * 0.2F, -var14 * 0.2F);
			}

			GL11.glTranslatef(0.7F * var22, -0.65F * var22 - (1.0F - var2)
					* 0.6F, -0.9F * var22);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			var13 = var3.getSwingProgress(p_78440_1_);
			var14 = MathHelper.sin(var13 * var13 * (float) Math.PI);
			var15 = MathHelper.sin(MathHelper.sqrt_float(var13)
					* (float) Math.PI);
			GL11.glRotatef(-var14 * 20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var15 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-var15 * 80.0F, 1.0F, 0.0F, 0.0F);
			var16 = 0.4F;
			GL11.glScalef(var16, var16, var16);
			float var19;
			float var20;

			if (var3.getItemInUseCount() > 0) {
				final EnumAction var27 = var8.getItemUseAction();

				if (var27 == EnumAction.block) {
					GL11.glTranslatef(-0.5F, 0.2F, 0.0F);
					GL11.glRotatef(30.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-80.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(60.0F, 0.0F, 1.0F, 0.0F);
				} else if (var27 == EnumAction.bow) {
					GL11.glRotatef(-18.0F, 0.0F, 0.0F, 1.0F);
					GL11.glRotatef(-12.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-8.0F, 1.0F, 0.0F, 0.0F);
					GL11.glTranslatef(-0.9F, 0.2F, 0.0F);
					var18 = var8.getMaxItemUseDuration()
							- (var3.getItemInUseCount() - p_78440_1_ + 1.0F);
					var19 = var18 / 20.0F;
					var19 = (var19 * var19 + var19 * 2.0F) / 3.0F;

					if (var19 > 1.0F) {
						var19 = 1.0F;
					}

					if (var19 > 0.1F) {
						GL11.glTranslatef(0.0F,
								MathHelper.sin((var18 - 0.1F) * 1.3F) * 0.01F
										* (var19 - 0.1F), 0.0F);
					}

					GL11.glTranslatef(0.0F, 0.0F, var19 * 0.1F);
					GL11.glRotatef(-335.0F, 0.0F, 0.0F, 1.0F);
					GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glTranslatef(0.0F, 0.5F, 0.0F);
					var20 = 1.0F + var19 * 0.2F;
					GL11.glScalef(1.0F, 1.0F, var20);
					GL11.glTranslatef(0.0F, -0.5F, 0.0F);
					GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
				}
			}

			if (var8.getItem().shouldRotateAroundWhenRendering()) {
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			}

			if (var8.getItem().requiresMultipleRenderPasses()) {
				renderItem(var3, var8, 0);
				final int var28 = var8.getItem().getColorFromItemStack(var8, 1);
				var18 = (var28 >> 16 & 255) / 255.0F;
				var19 = (var28 >> 8 & 255) / 255.0F;
				var20 = (var28 & 255) / 255.0F;
				GL11.glColor4f(1.0F * var18, 1.0F * var19, 1.0F * var20, 1.0F);
				renderItem(var3, var8, 1);
			} else {
				renderItem(var3, var8, 0);
			}

			GL11.glPopMatrix();
		} else if (!var3.isInvisible()) {
			GL11.glPushMatrix();
			var22 = 0.8F;
			var13 = var3.getSwingProgress(p_78440_1_);
			var14 = MathHelper.sin(var13 * (float) Math.PI);
			var15 = MathHelper.sin(MathHelper.sqrt_float(var13)
					* (float) Math.PI);
			GL11.glTranslatef(
					-var15 * 0.3F,
					MathHelper.sin(MathHelper.sqrt_float(var13)
							* (float) Math.PI * 2.0F) * 0.4F, -var14 * 0.4F);
			GL11.glTranslatef(0.8F * var22, -0.75F * var22 - (1.0F - var2)
					* 0.6F, -0.9F * var22);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			var13 = var3.getSwingProgress(p_78440_1_);
			var14 = MathHelper.sin(var13 * var13 * (float) Math.PI);
			var15 = MathHelper.sin(MathHelper.sqrt_float(var13)
					* (float) Math.PI);
			GL11.glRotatef(var15 * 70.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var14 * 20.0F, 0.0F, 0.0F, 1.0F);
			mc.getTextureManager().bindTexture(var3.getLocationSkin());
			GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
			GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glTranslatef(5.6F, 0.0F, 0.0F);
			var26 = RenderManager.instance.getEntityRenderObject(mc.thePlayer);
			var29 = (RenderPlayer) var26;
			var18 = 1.0F;
			GL11.glScalef(var18, var18, var18);
			var29.renderFirstPersonArm(mc.thePlayer);
			GL11.glPopMatrix();
		}

		if (var8 != null && var8.getItem() instanceof ItemCloth) {
			GL11.glDisable(GL11.GL_BLEND);
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
	}

	/**
	 * Renders all the overlays that are in first person mode. Args:
	 * partialTickTime
	 */
	public void renderOverlays(float p_78447_1_) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);

		if (mc.thePlayer.isBurning()) {
			renderFireInFirstPerson(p_78447_1_);
		}

		if (mc.thePlayer.isEntityInsideOpaqueBlock()) {
			final int var2 = MathHelper.floor_double(mc.thePlayer.posX);
			final int var3 = MathHelper.floor_double(mc.thePlayer.posY);
			final int var4 = MathHelper.floor_double(mc.thePlayer.posZ);
			Block var5 = mc.theWorld.getBlock(var2, var3, var4);

			if (mc.theWorld.getBlock(var2, var3, var4).isNormalCube()) {
				renderInsideOfBlock(p_78447_1_, var5.getBlockTextureFromSide(2));
			} else {
				for (int var6 = 0; var6 < 8; ++var6) {
					final float var7 = ((var6 >> 0) % 2 - 0.5F)
							* mc.thePlayer.width * 0.9F;
					final float var8 = ((var6 >> 1) % 2 - 0.5F)
							* mc.thePlayer.height * 0.2F;
					final float var9 = ((var6 >> 2) % 2 - 0.5F)
							* mc.thePlayer.width * 0.9F;
					final int var10 = MathHelper.floor_float(var2 + var7);
					final int var11 = MathHelper.floor_float(var3 + var8);
					final int var12 = MathHelper.floor_float(var4 + var9);

					if (mc.theWorld.getBlock(var10, var11, var12)
							.isNormalCube()) {
						var5 = mc.theWorld.getBlock(var10, var11, var12);
					}
				}
			}

			if (var5.getMaterial() != Material.air) {
				renderInsideOfBlock(p_78447_1_, var5.getBlockTextureFromSide(2));
			}
		}

		if (mc.thePlayer.isInsideOfMaterial(Material.water)) {
			renderWarpedTextureOverlay(p_78447_1_);
		}

		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}

	/**
	 * Renders a texture that warps around based on the direction the player is
	 * looking. Texture needs to be bound before being called. Used for the
	 * water overlay. Args: parialTickTime
	 */
	private void renderWarpedTextureOverlay(float p_78448_1_) {
		mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
		final Tessellator var2 = Tessellator.instance;
		final float var3 = mc.thePlayer.getBrightness(p_78448_1_);
		GL11.glColor4f(var3, var3, var3, 0.5F);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glPushMatrix();
		final float var4 = 4.0F;
		final float var5 = -1.0F;
		final float var6 = 1.0F;
		final float var7 = -1.0F;
		final float var8 = 1.0F;
		final float var9 = -0.5F;
		final float var10 = -mc.thePlayer.rotationYaw / 64.0F;
		final float var11 = mc.thePlayer.rotationPitch / 64.0F;
		var2.startDrawingQuads();
		var2.addVertexWithUV(var5, var7, var9, var4 + var10, var4 + var11);
		var2.addVertexWithUV(var6, var7, var9, 0.0F + var10, var4 + var11);
		var2.addVertexWithUV(var6, var8, var9, 0.0F + var10, 0.0F + var11);
		var2.addVertexWithUV(var5, var8, var9, var4 + var10, 0.0F + var11);
		var2.draw();
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
	}

	/**
	 * Resets equippedProgress
	 */
	public void resetEquippedProgress() {
		equippedProgress = 0.0F;
	}

	/**
	 * Resets equippedProgress
	 */
	public void resetEquippedProgress2() {
		equippedProgress = 0.0F;
	}

	public void updateEquippedItem() {
		prevEquippedProgress = equippedProgress;
		final EntityClientPlayerMP var1 = mc.thePlayer;
		final ItemStack var2 = var1.inventory.getCurrentItem();
		boolean var3 = equippedItemSlot == var1.inventory.currentItem
				&& var2 == itemToRender;

		if (itemToRender == null && var2 == null) {
			var3 = true;
		}

		if (var2 != null && itemToRender != null && var2 != itemToRender
				&& var2.getItem() == itemToRender.getItem()
				&& var2.getItemDamage() == itemToRender.getItemDamage()) {
			itemToRender = var2;
			var3 = true;
		}

		final float var4 = 0.4F;
		final float var5 = var3 ? 1.0F : 0.0F;
		float var6 = var5 - equippedProgress;

		if (var6 < -var4) {
			var6 = -var4;
		}

		if (var6 > var4) {
			var6 = var4;
		}

		equippedProgress += var6;

		if (equippedProgress < 0.1F) {
			itemToRender = var2;
			equippedItemSlot = var1.inventory.currentItem;
		}
	}
}
