package net.minecraft.client.gui.achievement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiAchievement extends Gui {
	private static final ResourceLocation field_146261_a = new ResourceLocation(
			"textures/gui/achievement/achievement_background.png");
	private final Minecraft field_146259_f;
	private int field_146260_g;
	private boolean field_146262_n;
	private long field_146263_l;
	private final RenderItem field_146264_m;
	private String field_146265_j;
	private Achievement field_146266_k;
	private int field_146267_h;
	private String field_146268_i;

	public GuiAchievement(Minecraft p_i1063_1_) {
		field_146259_f = p_i1063_1_;
		field_146264_m = new RenderItem();
	}

	public void func_146254_a() {
		if (field_146266_k != null && field_146263_l != 0L
				&& Minecraft.getMinecraft().thePlayer != null) {
			double var1 = (Minecraft.getSystemTime() - field_146263_l) / 3000.0D;

			if (!field_146262_n) {
				if (var1 < 0.0D || var1 > 1.0D) {
					field_146263_l = 0L;
					return;
				}
			} else if (var1 > 0.5D) {
				var1 = 0.5D;
			}

			func_146258_c();
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			double var3 = var1 * 2.0D;

			if (var3 > 1.0D) {
				var3 = 2.0D - var3;
			}

			var3 *= 4.0D;
			var3 = 1.0D - var3;

			if (var3 < 0.0D) {
				var3 = 0.0D;
			}

			var3 *= var3;
			var3 *= var3;
			final int var5 = field_146260_g - 160;
			final int var6 = 0 - (int) (var3 * 36.0D);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			field_146259_f.getTextureManager().bindTexture(field_146261_a);
			GL11.glDisable(GL11.GL_LIGHTING);
			drawTexturedModalRect(var5, var6, 96, 202, 160, 32);

			if (field_146262_n) {
				field_146259_f.fontRenderer.drawSplitString(field_146265_j,
						var5 + 30, var6 + 7, 120, -1);
			} else {
				field_146259_f.fontRenderer.drawString(field_146268_i,
						var5 + 30, var6 + 7, -256);
				field_146259_f.fontRenderer.drawString(field_146265_j,
						var5 + 30, var6 + 18, -1);
			}

			RenderHelper.enableGUIStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			GL11.glEnable(GL11.GL_LIGHTING);
			field_146264_m.renderItemAndEffectIntoGUI(
					field_146259_f.fontRenderer,
					field_146259_f.getTextureManager(),
					field_146266_k.theItemStack, var5 + 8, var6 + 8);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}
	}

	public void func_146255_b(Achievement p_146255_1_) {
		field_146268_i = p_146255_1_.func_150951_e().getUnformattedText();
		field_146265_j = p_146255_1_.getDescription();
		field_146263_l = Minecraft.getSystemTime() + 2500L;
		field_146266_k = p_146255_1_;
		field_146262_n = true;
	}

	public void func_146256_a(Achievement p_146256_1_) {
		field_146268_i = I18n.format("achievement.get", new Object[0]);
		field_146265_j = p_146256_1_.func_150951_e().getUnformattedText();
		field_146263_l = Minecraft.getSystemTime();
		field_146266_k = p_146256_1_;
		field_146262_n = false;
	}

	public void func_146257_b() {
		field_146266_k = null;
		field_146263_l = 0L;
	}

	private void func_146258_c() {
		GL11.glViewport(0, 0, field_146259_f.displayWidth,
				field_146259_f.displayHeight);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		field_146260_g = field_146259_f.displayWidth;
		field_146267_h = field_146259_f.displayHeight;
		final ScaledResolution var1 = new ScaledResolution(field_146259_f,
				field_146259_f.displayWidth, field_146259_f.displayHeight);
		field_146260_g = var1.getScaledWidth();
		field_146267_h = var1.getScaledHeight();
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, field_146260_g, field_146267_h, 0.0D, 1000.0D,
				3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	}
}
