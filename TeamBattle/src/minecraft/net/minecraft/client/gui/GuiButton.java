package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiButton extends Gui {
	protected static final ResourceLocation field_146122_a = new ResourceLocation(
			"textures/gui/widgets.png");
	/** The string displayed on this control. */
	public String displayString;
	public boolean enabled;
	protected int field_146120_f;
	protected int field_146121_g;

	protected boolean field_146123_n;
	public boolean field_146125_m;
	public int field_146128_h;
	public int field_146129_i;
	public int id;

	public GuiButton(int p_i1021_1_, int p_i1021_2_, int p_i1021_3_,
			int p_i1021_4_, int p_i1021_5_, String p_i1021_6_) {
		field_146120_f = 200;
		field_146121_g = 20;
		enabled = true;
		field_146125_m = true;
		id = p_i1021_1_;
		field_146128_h = p_i1021_2_;
		field_146129_i = p_i1021_3_;
		field_146120_f = p_i1021_4_;
		field_146121_g = p_i1021_5_;
		displayString = p_i1021_6_;
	}

	public GuiButton(int p_i1020_1_, int p_i1020_2_, int p_i1020_3_,
			String p_i1020_4_) {
		this(p_i1020_1_, p_i1020_2_, p_i1020_3_, 200, 20, p_i1020_4_);
	}

	/**
	 * Draws this button to the screen.
	 */
	public void drawButton(Minecraft p_146112_1_, int p_146112_2_,
			int p_146112_3_) {
		if (field_146125_m) {
			final FontRenderer var4 = p_146112_1_.fontRenderer;
			p_146112_1_.getTextureManager().bindTexture(field_146122_a);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			field_146123_n = p_146112_2_ >= field_146128_h
					&& p_146112_3_ >= field_146129_i
					&& p_146112_2_ < field_146128_h + field_146120_f
					&& p_146112_3_ < field_146129_i + field_146121_g;
			final int var5 = getHoverState(field_146123_n);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			drawTexturedModalRect(field_146128_h, field_146129_i, 0,
					46 + var5 * 20, field_146120_f / 2, field_146121_g);
			drawTexturedModalRect(field_146128_h + field_146120_f / 2,
					field_146129_i, 200 - field_146120_f / 2, 46 + var5 * 20,
					field_146120_f / 2, field_146121_g);
			mouseDragged(p_146112_1_, p_146112_2_, p_146112_3_);
			int var6 = 14737632;

			if (!enabled) {
				var6 = 10526880;
			} else if (field_146123_n) {
				var6 = 16777120;
			}

			drawCenteredString(var4, displayString, field_146128_h
					+ field_146120_f / 2, field_146129_i + (field_146121_g - 8)
					/ 2, var6);
		}
	}

	public void func_146111_b(int p_146111_1_, int p_146111_2_) {
	}

	public void func_146113_a(SoundHandler p_146113_1_) {
		p_146113_1_.playSound(PositionedSoundRecord.func_147674_a(
				new ResourceLocation("gui.button.press"), 1.0F));
	}

	public boolean func_146115_a() {
		return field_146123_n;
	}

	public int func_146117_b() {
		return field_146120_f;
	}

	public int func_154310_c() {
		return field_146121_g;
	}

	public int getHoverState(boolean p_146114_1_) {
		byte var2 = 1;

		if (!enabled) {
			var2 = 0;
		} else if (p_146114_1_) {
			var2 = 2;
		}

		return var2;
	}

	/**
	 * Fired when the mouse button is dragged. Equivalent of
	 * MouseListener.mouseDragged(MouseEvent e).
	 */
	protected void mouseDragged(Minecraft p_146119_1_, int p_146119_2_,
			int p_146119_3_) {
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of
	 * MouseListener.mousePressed(MouseEvent e).
	 */
	public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_,
			int p_146116_3_) {
		return enabled && field_146125_m && p_146116_2_ >= field_146128_h
				&& p_146116_3_ >= field_146129_i
				&& p_146116_2_ < field_146128_h + field_146120_f
				&& p_146116_3_ < field_146129_i + field_146121_g;
	}

	/**
	 * Fired when the mouse button is released. Equivalent of
	 * MouseListener.mouseReleased(MouseEvent e).
	 */
	public void mouseReleased(int p_146118_1_, int p_146118_2_) {
	}
}
