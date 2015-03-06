package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class GuiButtonLanguage extends GuiButton {

	public GuiButtonLanguage(int p_i1041_1_, int p_i1041_2_, int p_i1041_3_) {
		super(p_i1041_1_, p_i1041_2_, p_i1041_3_, 20, 20, "");
	}

	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft p_146112_1_, int p_146112_2_,
			int p_146112_3_) {
		if (field_146125_m) {
			p_146112_1_.getTextureManager().bindTexture(
					GuiButton.field_146122_a);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			final boolean var4 = p_146112_2_ >= field_146128_h
					&& p_146112_3_ >= field_146129_i
					&& p_146112_2_ < field_146128_h + field_146120_f
					&& p_146112_3_ < field_146129_i + field_146121_g;
			int var5 = 106;

			if (var4) {
				var5 += field_146121_g;
			}

			drawTexturedModalRect(field_146128_h, field_146129_i, 0, var5,
					field_146120_f, field_146121_g);
		}
	}
}
