package net.minecraft.client.gui;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.C00PacketKeepAlive;

public class GuiDownloadTerrain extends GuiScreen {
	private int field_146593_f;
	private final NetHandlerPlayClient field_146594_a;

	public GuiDownloadTerrain(NetHandlerPlayClient p_i45023_1_) {
		field_146594_a = p_i45023_1_;
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		func_146278_c(0);
		drawCenteredString(fontRendererObj,
				I18n.format("multiplayer.downloadingTerrain", new Object[0]),
				width / 2, height / 2 - 50, 16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		buttons.clear();
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		++field_146593_f;

		if (field_146593_f % 20 == 0) {
			field_146594_a.addToSendQueue(new C00PacketKeepAlive());
		}

		if (field_146594_a != null) {
			field_146594_a.onNetworkTick();
		}
	}
}
