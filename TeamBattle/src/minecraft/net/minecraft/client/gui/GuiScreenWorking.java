package net.minecraft.client.gui;

import net.minecraft.util.IProgressUpdate;

public class GuiScreenWorking extends GuiScreen implements IProgressUpdate {
	private String field_146589_f = "";
	private int field_146590_g;
	private String field_146591_a = "";
	private boolean field_146592_h;

	/**
	 * "Saving level", or the loading,or downloading equivelent
	 */
	@Override
	public void displayProgressMessage(String p_73720_1_) {
		resetProgressAndMessage(p_73720_1_);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		if (field_146592_h) {
			mc.displayGuiScreen((GuiScreen) null);
		} else {
			drawDefaultBackground();
			drawCenteredString(fontRendererObj, field_146591_a, width / 2, 70,
					16777215);
			drawCenteredString(fontRendererObj, field_146589_f + " "
					+ field_146590_g + "%", width / 2, 90, 16777215);
			super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		}
	}

	@Override
	public void func_146586_a() {
		field_146592_h = true;
	}

	/**
	 * This is called with "Working..." by resetProgressAndMessage
	 */
	@Override
	public void resetProgresAndWorkingMessage(String p_73719_1_) {
		field_146589_f = p_73719_1_;
		setLoadingProgress(0);
	}

	/**
	 * this string, followed by "working..." and then the "% complete" are the 3
	 * lines shown. This resets progress to 0, and the WorkingString to
	 * "working...".
	 */
	@Override
	public void resetProgressAndMessage(String p_73721_1_) {
		field_146591_a = p_73721_1_;
		resetProgresAndWorkingMessage("Working...");
	}

	/**
	 * Updates the progress bar on the loading screen to the specified amount.
	 * Args: loadProgress
	 */
	@Override
	public void setLoadingProgress(int p_73718_1_) {
		field_146590_g = p_73718_1_;
	}
}
