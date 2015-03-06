package net.minecraft.client.gui;

import java.util.Iterator;

import net.minecraft.client.resources.I18n;

public class GuiYesNo extends GuiScreen {
	protected String field_146351_f;
	protected String field_146352_g;
	private int field_146353_s;
	private final String field_146354_r;
	protected GuiYesNoCallback field_146355_a;
	protected String field_146356_h;
	protected int field_146357_i;

	public GuiYesNo(GuiYesNoCallback p_i1082_1_, String p_i1082_2_,
			String p_i1082_3_, int p_i1082_4_) {
		field_146355_a = p_i1082_1_;
		field_146351_f = p_i1082_2_;
		field_146354_r = p_i1082_3_;
		field_146357_i = p_i1082_4_;
		field_146352_g = I18n.format("gui.yes", new Object[0]);
		field_146356_h = I18n.format("gui.no", new Object[0]);
	}

	public GuiYesNo(GuiYesNoCallback p_i1083_1_, String p_i1083_2_,
			String p_i1083_3_, String p_i1083_4_, String p_i1083_5_,
			int p_i1083_6_) {
		field_146355_a = p_i1083_1_;
		field_146351_f = p_i1083_2_;
		field_146354_r = p_i1083_3_;
		field_146352_g = p_i1083_4_;
		field_146356_h = p_i1083_5_;
		field_146357_i = p_i1083_6_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		field_146355_a.confirmClicked(p_146284_1_.id == 0, field_146357_i);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, field_146351_f, width / 2, 70,
				16777215);
		drawCenteredString(fontRendererObj, field_146354_r, width / 2, 90,
				16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	public void func_146350_a(int p_146350_1_) {
		field_146353_s = p_146350_1_;
		GuiButton var3;

		for (final Iterator var2 = buttons.iterator(); var2.hasNext(); var3.enabled = false) {
			var3 = (GuiButton) var2.next();
		}
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		buttons.add(new GuiOptionButton(0, width / 2 - 155, height / 6 + 96,
				field_146352_g));
		buttons.add(new GuiOptionButton(1, width / 2 - 155 + 160,
				height / 6 + 96, field_146356_h));
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();
		GuiButton var2;

		if (--field_146353_s == 0) {
			for (final Iterator var1 = buttons.iterator(); var1.hasNext(); var2.enabled = true) {
				var2 = (GuiButton) var1.next();
			}
		}
	}
}
