package net.minecraft.client.gui;

import java.util.Iterator;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.opengl.GL11;

public class GuiGameOver extends GuiScreen implements GuiYesNoCallback {
	private int field_146347_a;

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		switch (p_146284_1_.id) {
		case 0:
			mc.thePlayer.respawnPlayer();
			mc.displayGuiScreen((GuiScreen) null);
			break;

		case 1:
			final GuiYesNo var2 = new GuiYesNo(this, I18n.format(
					"deathScreen.quit.confirm", new Object[0]), "",
					I18n.format("deathScreen.titleScreen", new Object[0]),
					I18n.format("deathScreen.respawn", new Object[0]), 0);
			mc.displayGuiScreen(var2);
			var2.func_146350_a(20);
		}
	}

	@Override
	public void confirmClicked(boolean p_73878_1_, int p_73878_2_) {
		if (p_73878_1_) {
			mc.theWorld.sendQuittingDisconnectingPacket();
			mc.loadWorld((WorldClient) null);
			mc.displayGuiScreen(new GuiMainMenu());
		} else {
			mc.thePlayer.respawnPlayer();
			mc.displayGuiScreen((GuiScreen) null);
		}
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
		drawGradientRect(0, 0, width, height, 1615855616, -1602211792);
		GL11.glPushMatrix();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		final boolean var4 = mc.theWorld.getWorldInfo().isHardcoreModeEnabled();
		final String var5 = var4 ? I18n.format("deathScreen.title.hardcore",
				new Object[0]) : I18n
				.format("deathScreen.title", new Object[0]);
		drawCenteredString(fontRendererObj, var5, width / 2 / 2, 30, 16777215);
		GL11.glPopMatrix();

		if (var4) {
			drawCenteredString(fontRendererObj,
					I18n.format("deathScreen.hardcoreInfo", new Object[0]),
					width / 2, 144, 16777215);
		}

		drawCenteredString(fontRendererObj,
				I18n.format("deathScreen.score", new Object[0]) + ": "
						+ EnumChatFormatting.YELLOW + mc.thePlayer.getScore(),
				width / 2, 100, 16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		buttons.clear();

		if (mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
			if (mc.isIntegratedServerRunning()) {
				buttons.add(new GuiButton(1, width / 2 - 100, height / 4 + 96,
						I18n.format("deathScreen.deleteWorld", new Object[0])));
			} else {
				buttons.add(new GuiButton(1, width / 2 - 100, height / 4 + 96,
						I18n.format("deathScreen.leaveServer", new Object[0])));
			}
		} else {
			buttons.add(new GuiButton(0, width / 2 - 100, height / 4 + 72, I18n
					.format("deathScreen.respawn", new Object[0])));
			buttons.add(new GuiButton(1, width / 2 - 100, height / 4 + 96, I18n
					.format("deathScreen.titleScreen", new Object[0])));

			if (mc.getSession() == null) {
				((GuiButton) buttons.get(1)).enabled = false;
			}
		}

		GuiButton var2;

		for (final Iterator var1 = buttons.iterator(); var1.hasNext(); var2.enabled = false) {
			var2 = (GuiButton) var1.next();
		}
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
		super.updateScreen();
		++field_146347_a;
		GuiButton var2;

		if (field_146347_a == 20) {
			for (final Iterator var1 = buttons.iterator(); var1.hasNext(); var2.enabled = true) {
				var2 = (GuiButton) var1.next();
			}
		}
	}
}
