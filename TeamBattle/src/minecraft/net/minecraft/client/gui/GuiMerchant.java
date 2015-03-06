package net.minecraft.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiMerchant extends GuiContainer {
	static class MerchantButton extends GuiButton {
		private final boolean field_146157_o;

		public MerchantButton(int p_i1095_1_, int p_i1095_2_, int p_i1095_3_,
				boolean p_i1095_4_) {
			super(p_i1095_1_, p_i1095_2_, p_i1095_3_, 12, 19, "");
			field_146157_o = p_i1095_4_;
		}

		@Override
		public void drawButton(Minecraft p_146112_1_, int p_146112_2_,
				int p_146112_3_) {
			if (field_146125_m) {
				p_146112_1_.getTextureManager().bindTexture(
						GuiMerchant.field_147038_v);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				final boolean var4 = p_146112_2_ >= field_146128_h
						&& p_146112_3_ >= field_146129_i
						&& p_146112_2_ < field_146128_h + field_146120_f
						&& p_146112_3_ < field_146129_i + field_146121_g;
				int var5 = 0;
				int var6 = 176;

				if (!enabled) {
					var6 += field_146120_f * 2;
				} else if (var4) {
					var6 += field_146120_f;
				}

				if (!field_146157_o) {
					var5 += field_146121_g;
				}

				drawTexturedModalRect(field_146128_h, field_146129_i, var6,
						var5, field_146120_f, field_146121_g);
			}
		}
	}

	private static final ResourceLocation field_147038_v = new ResourceLocation(
			"textures/gui/container/villager.png");
	private static final Logger logger = LogManager.getLogger();
	private final IMerchant field_147037_w;
	private final String field_147040_A;
	private int field_147041_z;
	private GuiMerchant.MerchantButton field_147042_y;

	private GuiMerchant.MerchantButton field_147043_x;

	public GuiMerchant(InventoryPlayer p_i1096_1_, IMerchant p_i1096_2_,
			World p_i1096_3_, String p_i1096_4_) {
		super(new ContainerMerchant(p_i1096_1_, p_i1096_2_, p_i1096_3_));
		field_147037_w = p_i1096_2_;
		field_147040_A = p_i1096_4_ != null && p_i1096_4_.length() >= 1 ? p_i1096_4_
				: I18n.format("entity.Villager.name", new Object[0]);
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		boolean var2 = false;

		if (p_146284_1_ == field_147043_x) {
			++field_147041_z;
			var2 = true;
		} else if (p_146284_1_ == field_147042_y) {
			--field_147041_z;
			var2 = true;
		}

		if (var2) {
			((ContainerMerchant) field_147002_h)
					.setCurrentRecipeIndex(field_147041_z);
			final ByteBuf var3 = Unpooled.buffer();

			try {
				var3.writeInt(field_147041_z);
				mc.getNetHandler().addToSendQueue(
						new C17PacketCustomPayload("MC|TrSel", var3));
			} catch (final Exception var8) {
				logger.error("Couldn\'t send trade info", var8);
			} finally {
				var3.release();
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		final MerchantRecipeList var4 = field_147037_w.getRecipes(mc.thePlayer);

		if (var4 != null && !var4.isEmpty()) {
			final int var5 = (width - field_146999_f) / 2;
			final int var6 = (height - field_147000_g) / 2;
			final int var7 = field_147041_z;
			final MerchantRecipe var8 = (MerchantRecipe) var4.get(var7);
			GL11.glPushMatrix();
			final ItemStack var9 = var8.getItemToBuy();
			final ItemStack var10 = var8.getSecondItemToBuy();
			final ItemStack var11 = var8.getItemToSell();
			RenderHelper.enableGUIStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			GL11.glEnable(GL11.GL_LIGHTING);
			itemRender.zLevel = 100.0F;
			itemRender.renderItemAndEffectIntoGUI(fontRendererObj,
					mc.getTextureManager(), var9, var5 + 36, var6 + 24);
			itemRender.renderItemOverlayIntoGUI(fontRendererObj,
					mc.getTextureManager(), var9, var5 + 36, var6 + 24);

			if (var10 != null) {
				itemRender.renderItemAndEffectIntoGUI(fontRendererObj,
						mc.getTextureManager(), var10, var5 + 62, var6 + 24);
				itemRender.renderItemOverlayIntoGUI(fontRendererObj,
						mc.getTextureManager(), var10, var5 + 62, var6 + 24);
			}

			itemRender.renderItemAndEffectIntoGUI(fontRendererObj,
					mc.getTextureManager(), var11, var5 + 120, var6 + 24);
			itemRender.renderItemOverlayIntoGUI(fontRendererObj,
					mc.getTextureManager(), var11, var5 + 120, var6 + 24);
			itemRender.zLevel = 0.0F;
			GL11.glDisable(GL11.GL_LIGHTING);

			if (func_146978_c(36, 24, 16, 16, p_73863_1_, p_73863_2_)) {
				func_146285_a(var9, p_73863_1_, p_73863_2_);
			} else if (var10 != null
					&& func_146978_c(62, 24, 16, 16, p_73863_1_, p_73863_2_)) {
				func_146285_a(var10, p_73863_1_, p_73863_2_);
			} else if (func_146978_c(120, 24, 16, 16, p_73863_1_, p_73863_2_)) {
				func_146285_a(var11, p_73863_1_, p_73863_2_);
			}

			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			RenderHelper.enableStandardItemLighting();
		}
	}

	@Override
	protected void func_146976_a(float p_146976_1_, int p_146976_2_,
			int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_147038_v);
		final int var4 = (width - field_146999_f) / 2;
		final int var5 = (height - field_147000_g) / 2;
		drawTexturedModalRect(var4, var5, 0, 0, field_146999_f, field_147000_g);
		final MerchantRecipeList var6 = field_147037_w.getRecipes(mc.thePlayer);

		if (var6 != null && !var6.isEmpty()) {
			final int var7 = field_147041_z;
			final MerchantRecipe var8 = (MerchantRecipe) var6.get(var7);

			if (var8.isRecipeDisabled()) {
				mc.getTextureManager().bindTexture(field_147038_v);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glDisable(GL11.GL_LIGHTING);
				drawTexturedModalRect(field_147003_i + 83, field_147009_r + 21,
						212, 0, 28, 21);
				drawTexturedModalRect(field_147003_i + 83, field_147009_r + 51,
						212, 0, 28, 21);
			}
		}
	}

	@Override
	protected void func_146979_b(int p_146979_1_, int p_146979_2_) {
		fontRendererObj.drawString(field_147040_A, field_146999_f / 2
				- fontRendererObj.getStringWidth(field_147040_A) / 2, 6,
				4210752);
		fontRendererObj.drawString(
				I18n.format("container.inventory", new Object[0]), 8,
				field_147000_g - 96 + 2, 4210752);
	}

	public IMerchant func_147035_g() {
		return field_147037_w;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		super.initGui();
		final int var1 = (width - field_146999_f) / 2;
		final int var2 = (height - field_147000_g) / 2;
		buttons.add(field_147043_x = new GuiMerchant.MerchantButton(1,
				var1 + 120 + 27, var2 + 24 - 1, true));
		buttons.add(field_147042_y = new GuiMerchant.MerchantButton(2,
				var1 + 36 - 19, var2 + 24 - 1, false));
		field_147043_x.enabled = false;
		field_147042_y.enabled = false;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();
		final MerchantRecipeList var1 = field_147037_w.getRecipes(mc.thePlayer);

		if (var1 != null) {
			field_147043_x.enabled = field_147041_z < var1.size() - 1;
			field_147042_y.enabled = field_147041_z > 0;
		}
	}
}
