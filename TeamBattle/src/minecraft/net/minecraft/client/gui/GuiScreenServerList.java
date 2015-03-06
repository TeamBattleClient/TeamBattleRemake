package net.minecraft.client.gui;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;

import org.lwjgl.input.Keyboard;

public class GuiScreenServerList extends GuiScreen {
	private final ServerData field_146301_f;
	private GuiTextField field_146302_g;
	private final GuiScreen field_146303_a;

	public GuiScreenServerList(GuiScreen p_i1031_1_, ServerData p_i1031_2_) {
		field_146303_a = p_i1031_1_;
		field_146301_f = p_i1031_2_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 1) {
				field_146303_a.confirmClicked(false, 0);
			} else if (p_146284_1_.id == 0) {
				field_146301_f.serverIP = field_146302_g.getText();
				field_146303_a.confirmClicked(true, 0);
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
				I18n.format("selectServer.direct", new Object[0]), width / 2,
				20, 16777215);
		drawString(fontRendererObj,
				I18n.format("addServer.enterIp", new Object[0]),
				width / 2 - 100, 100, 10526880);
		field_146302_g.drawTextBox();
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
				I18n.format("selectServer.select", new Object[0])));
		buttons.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12,
				I18n.format("gui.cancel", new Object[0])));
		field_146302_g = new GuiTextField(fontRendererObj, width / 2 - 100,
				116, 200, 20);
		field_146302_g.func_146203_f(128);
		field_146302_g.setFocused(true);
		field_146302_g.setText(mc.gameSettings.lastServer);
		((GuiButton) buttons.get(0)).enabled = field_146302_g.getText()
				.length() > 0 && field_146302_g.getText().split(":").length > 0;
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (field_146302_g.textboxKeyTyped(p_73869_1_, p_73869_2_)) {
			((GuiButton) buttons.get(0)).enabled = field_146302_g.getText()
					.length() > 0
					&& field_146302_g.getText().split(":").length > 0;
		} else if (p_73869_2_ == 28 || p_73869_2_ == 156) {
			actionPerformed((GuiButton) buttons.get(0));
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		field_146302_g.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	}

	/**
	 * "Called when the screen is unloaded. Used to disable keyboard repeat events."
	 */
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		mc.gameSettings.lastServer = field_146302_g.getText();
		mc.gameSettings.saveOptions();
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		field_146302_g.updateCursorCounter();
	}
}
