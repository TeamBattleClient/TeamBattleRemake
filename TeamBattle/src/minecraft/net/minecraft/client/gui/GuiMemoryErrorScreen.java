package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;

public class GuiMemoryErrorScreen extends GuiScreen {

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 0) {
			mc.displayGuiScreen(new GuiMainMenu());
		} else if (p_146284_1_.id == 1) {
			mc.shutdown();
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, "Out of memory!", width / 2,
				height / 4 - 60 + 20, 16777215);
		drawString(fontRendererObj, "Minecraft has run out of memory.",
				width / 2 - 140, height / 4 - 60 + 60 + 0, 10526880);
		drawString(fontRendererObj,
				"This could be caused by a bug in the game or by the",
				width / 2 - 140, height / 4 - 60 + 60 + 18, 10526880);
		drawString(fontRendererObj,
				"Java Virtual Machine not being allocated enough",
				width / 2 - 140, height / 4 - 60 + 60 + 27, 10526880);
		drawString(fontRendererObj, "memory.", width / 2 - 140, height / 4 - 60
				+ 60 + 36, 10526880);
		drawString(fontRendererObj,
				"To prevent level corruption, the current game has quit.",
				width / 2 - 140, height / 4 - 60 + 60 + 54, 10526880);
		drawString(fontRendererObj,
				"We\'ve tried to free up enough memory to let you go back to",
				width / 2 - 140, height / 4 - 60 + 60 + 63, 10526880);
		drawString(
				fontRendererObj,
				"the main menu and back to playing, but this may not have worked.",
				width / 2 - 140, height / 4 - 60 + 60 + 72, 10526880);
		drawString(fontRendererObj,
				"Please restart the game if you see this message again.",
				width / 2 - 140, height / 4 - 60 + 60 + 81, 10526880);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		buttons.clear();
		buttons.add(new GuiOptionButton(0, width / 2 - 155,
				height / 4 + 120 + 12, I18n.format("gui.toMenu", new Object[0])));
		buttons.add(new GuiOptionButton(1, width / 2 - 155 + 160,
				height / 4 + 120 + 12, I18n.format("menu.quit", new Object[0])));
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
	}
}
