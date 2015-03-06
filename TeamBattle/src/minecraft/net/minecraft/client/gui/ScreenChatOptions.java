package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class ScreenChatOptions extends GuiScreen {
	private static final GameSettings.Options[] field_146395_f = new GameSettings.Options[] { GameSettings.Options.SHOW_CAPE };
	private static final GameSettings.Options[] field_146399_a = new GameSettings.Options[] {
			GameSettings.Options.CHAT_VISIBILITY,
			GameSettings.Options.CHAT_COLOR, GameSettings.Options.CHAT_LINKS,
			GameSettings.Options.CHAT_OPACITY,
			GameSettings.Options.CHAT_LINKS_PROMPT,
			GameSettings.Options.CHAT_SCALE,
			GameSettings.Options.CHAT_HEIGHT_FOCUSED,
			GameSettings.Options.CHAT_HEIGHT_UNFOCUSED,
			GameSettings.Options.CHAT_WIDTH };
	private final GuiScreen field_146396_g;
	private int field_146397_s;
	private String field_146398_r;
	private final GameSettings field_146400_h;
	private String field_146401_i;

	public ScreenChatOptions(GuiScreen p_i1023_1_, GameSettings p_i1023_2_) {
		field_146396_g = p_i1023_1_;
		field_146400_h = p_i1023_2_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id < 100 && p_146284_1_ instanceof GuiOptionButton) {
				field_146400_h.setOptionValue(
						((GuiOptionButton) p_146284_1_).func_146136_c(), 1);
				p_146284_1_.displayString = field_146400_h
						.getKeyBinding(GameSettings.Options
								.getEnumOptions(p_146284_1_.id));
			}

			if (p_146284_1_.id == 200) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(field_146396_g);
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, field_146401_i, width / 2, 20,
				16777215);
		drawCenteredString(fontRendererObj, field_146398_r, width / 2,
				field_146397_s + 7, 16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		int var1 = 0;
		field_146401_i = I18n.format("options.chat.title", new Object[0]);
		field_146398_r = I18n
				.format("options.multiplayer.title", new Object[0]);
		GameSettings.Options[] var2 = field_146399_a;
		int var3 = var2.length;
		int var4;
		GameSettings.Options var5;

		for (var4 = 0; var4 < var3; ++var4) {
			var5 = var2[var4];

			if (var5.getEnumFloat()) {
				buttons.add(new GuiOptionSlider(var5.returnEnumOrdinal(), width
						/ 2 - 155 + var1 % 2 * 160, height / 6 + 24
						* (var1 >> 1), var5));
			} else {
				buttons.add(new GuiOptionButton(var5.returnEnumOrdinal(), width
						/ 2 - 155 + var1 % 2 * 160, height / 6 + 24
						* (var1 >> 1), var5, field_146400_h.getKeyBinding(var5)));
			}

			++var1;
		}

		if (var1 % 2 == 1) {
			++var1;
		}

		field_146397_s = height / 6 + 24 * (var1 >> 1);
		var1 += 2;
		var2 = field_146395_f;
		var3 = var2.length;

		for (var4 = 0; var4 < var3; ++var4) {
			var5 = var2[var4];

			if (var5.getEnumFloat()) {
				buttons.add(new GuiOptionSlider(var5.returnEnumOrdinal(), width
						/ 2 - 155 + var1 % 2 * 160, height / 6 + 24
						* (var1 >> 1), var5));
			} else {
				buttons.add(new GuiOptionButton(var5.returnEnumOrdinal(), width
						/ 2 - 155 + var1 % 2 * 160, height / 6 + 24
						* (var1 >> 1), var5, field_146400_h.getKeyBinding(var5)));
			}

			++var1;
		}

		buttons.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, I18n
				.format("gui.done", new Object[0])));
	}
}
