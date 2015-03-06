package net.minecraft.client.gui;

import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;

public class GuiIngameMenu extends GuiScreen {
	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		switch (p_146284_1_.id) {
		case 0:
			mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
			break;

		case 1:
			p_146284_1_.enabled = false;
			mc.theWorld.sendQuittingDisconnectingPacket();
			mc.loadWorld((WorldClient) null);
			mc.displayGuiScreen(new GuiMainMenu());

		case 2:
		case 3:
		default:
			break;

		case 4:
			mc.displayGuiScreen((GuiScreen) null);
			mc.setIngameFocus();
			break;

		case 5:
			mc.displayGuiScreen(new GuiAchievements(this, mc.thePlayer
					.func_146107_m()));
			break;

		case 6:
			mc.displayGuiScreen(new GuiStats(this, mc.thePlayer.func_146107_m()));
			break;

		case 7:
			mc.displayGuiScreen(new GuiShareToLan(this));
			break;
		case 8:
			final ServerData server = mc.func_147104_D();
			if (server != null) {
				mc.theWorld.sendQuittingDisconnectingPacket();
				mc.loadWorld((WorldClient) null);
				mc.displayGuiScreen(new GuiConnecting(null, mc, server));
			}
			break;
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj,
				I18n.format("menu.game", new Object[0]), width / 2, 40,
				16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		buttons.clear();
		final byte var1 = -16;
		if (!mc.isIntegratedServerRunning()) {
			buttons.add(new GuiButton(1, width / 2 - 100, height / 4 + 120
					+ var1, 98, 20, I18n.format("menu.disconnect",
					new Object[0])));
			buttons.add(new GuiButton(8, width / 2 + 2,
					height / 4 + 120 + var1, 98, 20, "Reconnect"));
		} else {
			buttons.add(new GuiButton(1, width / 2 - 100, height / 4 + 120
					+ var1, I18n.format("menu.returnToMenu", new Object[0])));
		}

		buttons.add(new GuiButton(4, width / 2 - 100, height / 4 + 24 + var1,
				I18n.format("menu.returnToGame", new Object[0])));
		buttons.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + var1,
				98, 20, I18n.format("menu.options", new Object[0])));
		GuiButton var3;
		buttons.add(var3 = new GuiButton(7, width / 2 + 2, height / 4 + 96
				+ var1, 98, 20, I18n.format("menu.shareToLan", new Object[0])));
		buttons.add(new GuiButton(5, width / 2 - 100, height / 4 + 48 + var1,
				98, 20, I18n.format("gui.achievements", new Object[0])));
		buttons.add(new GuiButton(6, width / 2 + 2, height / 4 + 48 + var1, 98,
				20, I18n.format("gui.stats", new Object[0])));
		var3.enabled = mc.isSingleplayer()
				&& !mc.getIntegratedServer().getPublic();
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();
	}
}
