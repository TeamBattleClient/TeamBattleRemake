package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;

public class GuiConfirmOpenLink extends GuiYesNo {
	private boolean field_146360_u = true;
	private final String field_146361_t;
	private final String field_146362_s;
	private final String field_146363_r;

	public GuiConfirmOpenLink(GuiYesNoCallback p_i1084_1_, String p_i1084_2_,
			int p_i1084_3_, boolean p_i1084_4_) {
		super(p_i1084_1_, I18n.format(p_i1084_4_ ? "chat.link.confirmTrusted"
				: "chat.link.confirm", new Object[0]), p_i1084_2_, p_i1084_3_);
		field_146352_g = I18n.format(p_i1084_4_ ? "chat.link.open" : "gui.yes",
				new Object[0]);
		field_146356_h = I18n.format(p_i1084_4_ ? "gui.cancel" : "gui.no",
				new Object[0]);
		field_146362_s = I18n.format("chat.copy", new Object[0]);
		field_146363_r = I18n.format("chat.link.warning", new Object[0]);
		field_146361_t = p_i1084_2_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 2) {
			func_146359_e();
		}

		field_146355_a.confirmClicked(p_146284_1_.id == 0, field_146357_i);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);

		if (field_146360_u) {
			drawCenteredString(fontRendererObj, field_146363_r, width / 2, 110,
					16764108);
		}
	}

	public void func_146358_g() {
		field_146360_u = false;
	}

	public void func_146359_e() {
		setClipboardString(field_146361_t);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		buttons.add(new GuiButton(0, width / 3 - 83 + 0, height / 6 + 96, 100,
				20, field_146352_g));
		buttons.add(new GuiButton(2, width / 3 - 83 + 105, height / 6 + 96,
				100, 20, field_146362_s));
		buttons.add(new GuiButton(1, width / 3 - 83 + 210, height / 6 + 96,
				100, 20, field_146356_h));
	}
}
