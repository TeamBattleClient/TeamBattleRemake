package net.minecraft.client.gui;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;

import org.lwjgl.input.Keyboard;

public class GuiScreenAddServer extends GuiScreen {
	private GuiTextField field_146308_f;
	private GuiTextField field_146309_g;
	private final GuiScreen field_146310_a;
	private final ServerData field_146311_h;
	private GuiButton field_152176_i;

	public GuiScreenAddServer(GuiScreen p_i1033_1_, ServerData p_i1033_2_) {
		field_146310_a = p_i1033_1_;
		field_146311_h = p_i1033_2_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 2) {
				field_146311_h
						.func_152584_a(ServerData.ServerResourceMode.values()[(field_146311_h
								.func_152586_b().ordinal() + 1)
								% ServerData.ServerResourceMode.values().length]);
				field_152176_i.displayString = I18n.format(
						"addServer.resourcePack", new Object[0])
						+ ": "
						+ field_146311_h.func_152586_b().func_152589_a()
								.getFormattedText();
			} else if (p_146284_1_.id == 1) {
				field_146310_a.confirmClicked(false, 0);
			} else if (p_146284_1_.id == 0) {
				field_146311_h.serverName = field_146309_g.getText();
				field_146311_h.serverIP = field_146308_f.getText();
				field_146310_a.confirmClicked(true, 0);
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
				I18n.format("addServer.title", new Object[0]), width / 2, 17,
				16777215);
		drawString(fontRendererObj,
				I18n.format("addServer.enterName", new Object[0]),
				width / 2 - 100, 53, 10526880);
		drawString(fontRendererObj,
				I18n.format("addServer.enterIp", new Object[0]),
				width / 2 - 100, 94, 10526880);
		field_146309_g.drawTextBox();
		field_146308_f.drawTextBox();
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		buttons.clear();
		buttons.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 18,
				I18n.format("addServer.add", new Object[0])));
		buttons.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 18,
				I18n.format("gui.cancel", new Object[0])));
		buttons.add(field_152176_i = new GuiButton(2, width / 2 - 100,
				height / 4 + 72, I18n.format("addServer.resourcePack",
						new Object[0])
						+ ": "
						+ field_146311_h.func_152586_b().func_152589_a()
								.getFormattedText()));
		field_146309_g = new GuiTextField(fontRendererObj, width / 2 - 100, 66,
				200, 20);
		field_146309_g.setFocused(true);
		field_146309_g.setText(field_146311_h.serverName);
		field_146308_f = new GuiTextField(fontRendererObj, width / 2 - 100,
				106, 200, 20);
		field_146308_f.func_146203_f(128);
		field_146308_f.setText(field_146311_h.serverIP);
		((GuiButton) buttons.get(0)).enabled = field_146308_f.getText()
				.length() > 0
				&& field_146308_f.getText().split(":").length > 0
				&& field_146309_g.getText().length() > 0;
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		field_146309_g.textboxKeyTyped(p_73869_1_, p_73869_2_);
		field_146308_f.textboxKeyTyped(p_73869_1_, p_73869_2_);

		if (p_73869_2_ == 15) {
			field_146309_g.setFocused(!field_146309_g.isFocused());
			field_146308_f.setFocused(!field_146308_f.isFocused());
		}

		if (p_73869_2_ == 28 || p_73869_2_ == 156) {
			actionPerformed((GuiButton) buttons.get(0));
		}

		((GuiButton) buttons.get(0)).enabled = field_146308_f.getText()
				.length() > 0
				&& field_146308_f.getText().split(":").length > 0
				&& field_146309_g.getText().length() > 0;
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		field_146308_f.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		field_146309_g.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
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
		field_146309_g.updateCursorCounter();
		field_146308_f.updateCursorCounter();
	}
}
