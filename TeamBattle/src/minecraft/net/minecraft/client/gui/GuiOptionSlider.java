package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

import org.lwjgl.opengl.GL11;

public class GuiOptionSlider extends GuiButton {
	private final GameSettings.Options field_146133_q;
	private float field_146134_p;
	public boolean field_146135_o;

	public GuiOptionSlider(int p_i45016_1_, int p_i45016_2_, int p_i45016_3_,
			GameSettings.Options p_i45016_4_) {
		this(p_i45016_1_, p_i45016_2_, p_i45016_3_, p_i45016_4_, 0.0F, 1.0F);
	}

	public GuiOptionSlider(int p_i45017_1_, int p_i45017_2_, int p_i45017_3_,
			GameSettings.Options p_i45017_4_, float p_i45017_5_,
			float p_i45017_6_) {
		super(p_i45017_1_, p_i45017_2_, p_i45017_3_, 150, 20, "");
		field_146134_p = 1.0F;
		field_146133_q = p_i45017_4_;
		final Minecraft var7 = Minecraft.getMinecraft();
		field_146134_p = p_i45017_4_.normalizeValue(var7.gameSettings
				.getOptionFloatValue(p_i45017_4_));
		displayString = var7.gameSettings.getKeyBinding(p_i45017_4_);
	}

	@Override
	public int getHoverState(boolean p_146114_1_) {
		return 0;
	}

	/**
	 * Fired when the mouse button is dragged. Equivalent of
	 * MouseListener.mouseDragged(MouseEvent e).
	 */
	@Override
	protected void mouseDragged(Minecraft p_146119_1_, int p_146119_2_,
			int p_146119_3_) {
		if (field_146125_m) {
			if (field_146135_o) {
				field_146134_p = (float) (p_146119_2_ - (field_146128_h + 4))
						/ (float) (field_146120_f - 8);

				if (field_146134_p < 0.0F) {
					field_146134_p = 0.0F;
				}

				if (field_146134_p > 1.0F) {
					field_146134_p = 1.0F;
				}

				final float var4 = field_146133_q
						.denormalizeValue(field_146134_p);
				p_146119_1_.gameSettings.setOptionFloatValue(field_146133_q,
						var4);
				field_146134_p = field_146133_q.normalizeValue(var4);
				displayString = p_146119_1_.gameSettings
						.getKeyBinding(field_146133_q);
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			drawTexturedModalRect(field_146128_h
					+ (int) (field_146134_p * (field_146120_f - 8)),
					field_146129_i, 0, 66, 4, 20);
			drawTexturedModalRect(field_146128_h
					+ (int) (field_146134_p * (field_146120_f - 8)) + 4,
					field_146129_i, 196, 66, 4, 20);
		}
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of
	 * MouseListener.mousePressed(MouseEvent e).
	 */
	@Override
	public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_,
			int p_146116_3_) {
		if (super.mousePressed(p_146116_1_, p_146116_2_, p_146116_3_)) {
			field_146134_p = (float) (p_146116_2_ - (field_146128_h + 4))
					/ (float) (field_146120_f - 8);

			if (field_146134_p < 0.0F) {
				field_146134_p = 0.0F;
			}

			if (field_146134_p > 1.0F) {
				field_146134_p = 1.0F;
			}

			p_146116_1_.gameSettings.setOptionFloatValue(field_146133_q,
					field_146133_q.denormalizeValue(field_146134_p));
			displayString = p_146116_1_.gameSettings
					.getKeyBinding(field_146133_q);
			field_146135_o = true;
			return true;
		} else
			return false;
	}

	/**
	 * Fired when the mouse button is released. Equivalent of
	 * MouseListener.mouseReleased(MouseEvent e).
	 */
	@Override
	public void mouseReleased(int p_146118_1_, int p_146118_2_) {
		field_146135_o = false;
	}
}
