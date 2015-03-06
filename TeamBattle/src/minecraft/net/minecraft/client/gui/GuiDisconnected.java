package net.minecraft.client.gui;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;

public class GuiDisconnected extends GuiScreen {
	private final IChatComponent field_146304_f;
	private List field_146305_g;
	private final String field_146306_a;
	private final GuiScreen field_146307_h;

	public GuiDisconnected(GuiScreen p_i45020_1_, String p_i45020_2_,
			IChatComponent p_i45020_3_) {
		field_146307_h = p_i45020_1_;
		field_146306_a = I18n.format(p_i45020_2_, new Object[0]);
		field_146304_f = p_i45020_3_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 0) {
			mc.displayGuiScreen(field_146307_h);
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, field_146306_a, width / 2,
				height / 2 - 50, 11184810);
		int var4 = height / 2 - 30;

		if (field_146305_g != null) {
			for (final Iterator var5 = field_146305_g.iterator(); var5
					.hasNext(); var4 += fontRendererObj.FONT_HEIGHT) {
				final String var6 = (String) var5.next();
				drawCenteredString(fontRendererObj, var6, width / 2, var4,
						16777215);
			}
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		buttons.clear();
		buttons.add(new GuiButton(0, width / 2 - 100, height / 4 + 120 + 12,
				I18n.format("gui.toMenu", new Object[0])));
		field_146305_g = fontRendererObj.listFormattedStringToWidth(
				field_146304_f.getFormattedText(), width - 50);
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
	}
}
