package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;

public class GuiErrorScreen extends GuiScreen {
	private final String field_146312_f;
	private final String field_146313_a;

	public GuiErrorScreen(String p_i1034_1_, String p_i1034_2_) {
		field_146313_a = p_i1034_1_;
		field_146312_f = p_i1034_2_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		mc.displayGuiScreen((GuiScreen) null);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawGradientRect(0, 0, width, height, -12574688, -11530224);
		drawCenteredString(fontRendererObj, field_146313_a, width / 2, 90,
				16777215);
		drawCenteredString(fontRendererObj, field_146312_f, width / 2, 110,
				16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		super.initGui();
		buttons.add(new GuiButton(0, width / 2 - 100, 140, I18n.format(
				"gui.cancel", new Object[0])));
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
	}
}
