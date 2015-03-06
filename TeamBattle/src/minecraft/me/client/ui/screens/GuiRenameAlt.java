package me.client.ui.screens;

import me.client.Client;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiRenameAlt extends GuiScreen {
	private final GuiAltManager manager;
	private GuiTextField nameField;
	private String status = "Waiting...";

	public GuiRenameAlt(GuiAltManager manager) {
		this.manager = manager;
	}

	@Override
	public void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 1:
			mc.displayGuiScreen(manager);
			break;
		case 0:
			manager.selectedAlt.setMask(nameField.getText());
			status = "\247aRenamed!";
			Client.getFileManager().getFileByName("alts").saveFile();
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, "Rename Alt", width / 2, 10,
				0xFFFFFFFF);
		drawCenteredString(fontRendererObj, status, width / 2, 20, 0xFFFFFFFF);
		nameField.drawTextBox();
		super.drawScreen(par1, par2, par3);
	}

	@Override
	public void initGui() {
		this.buttons.add(new GuiButton(0, width / 2 - 100,
				height / 4 + 92 + 12, "Rename"));
		this.buttons.add(new GuiButton(1, width / 2 - 100,
				height / 4 + 116 + 12, "Cancel"));
		this.nameField = new GuiTextField(fontRendererObj, width / 2 - 100, 76,
				200, 20);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		nameField.textboxKeyTyped(par1, par2);
		if (par1 == '\t' && nameField.isFocused()) {
			nameField.setFocused(!nameField.isFocused());
		}

		if (par1 == '\r') {
			actionPerformed((GuiButton) buttons.get(0));
		}
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		nameField.mouseClicked(par1, par2, par3);
	}
}