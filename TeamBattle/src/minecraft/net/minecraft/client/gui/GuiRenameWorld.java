package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;

import org.lwjgl.input.Keyboard;

public class GuiRenameWorld extends GuiScreen {
	private GuiTextField field_146583_f;
	private final String field_146584_g;
	private final GuiScreen field_146585_a;

	public GuiRenameWorld(GuiScreen p_i1050_1_, String p_i1050_2_) {
		field_146585_a = p_i1050_1_;
		field_146584_g = p_i1050_2_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 1) {
				mc.displayGuiScreen(field_146585_a);
			} else if (p_146284_1_.id == 0) {
				final ISaveFormat var2 = mc.getSaveLoader();
				var2.renameWorld(field_146584_g, field_146583_f.getText()
						.trim());
				mc.displayGuiScreen(field_146585_a);
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj,
				I18n.format("selectWorld.renameTitle", new Object[0]),
				width / 2, 20, 16777215);
		drawString(fontRendererObj,
				I18n.format("selectWorld.enterName", new Object[0]),
				width / 2 - 100, 47, 10526880);
		field_146583_f.drawTextBox();
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		buttons.clear();
		buttons.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12,
				I18n.format("selectWorld.renameButton", new Object[0])));
		buttons.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12,
				I18n.format("gui.cancel", new Object[0])));
		final ISaveFormat var1 = mc.getSaveLoader();
		final WorldInfo var2 = var1.getWorldInfo(field_146584_g);
		final String var3 = var2.getWorldName();
		field_146583_f = new GuiTextField(fontRendererObj, width / 2 - 100, 60,
				200, 20);
		field_146583_f.setFocused(true);
		field_146583_f.setText(var3);
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		field_146583_f.textboxKeyTyped(p_73869_1_, p_73869_2_);
		((GuiButton) buttons.get(0)).enabled = field_146583_f.getText().trim()
				.length() > 0;

		if (p_73869_2_ == 28 || p_73869_2_ == 156) {
			actionPerformed((GuiButton) buttons.get(0));
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		field_146583_f.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	}

	/**
	 * "Called when the screen is unloaded. Used to disable keyboard repeat events."
	 */
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		field_146583_f.updateCursorCounter();
	}
}
