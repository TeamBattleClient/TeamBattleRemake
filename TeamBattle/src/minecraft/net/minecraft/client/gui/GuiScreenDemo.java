package net.minecraft.client.gui;

import java.net.URI;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class GuiScreenDemo extends GuiScreen {
	private static final ResourceLocation field_146348_f = new ResourceLocation(
			"textures/gui/demo_background.png");
	private static final Logger logger = LogManager.getLogger();

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		switch (p_146284_1_.id) {
		case 1:
			p_146284_1_.enabled = false;

			try {
				final Class var2 = Class.forName("java.awt.Desktop");
				final Object var3 = var2.getMethod("getDesktop", new Class[0])
						.invoke((Object) null, new Object[0]);
				var2.getMethod("browse", new Class[] { URI.class })
						.invoke(var3,
								new Object[] { new URI(
										"http://www.minecraft.net/store?source=demo") });
			} catch (final Throwable var4) {
				logger.error("Couldn\'t open link", var4);
			}

			break;

		case 2:
			mc.displayGuiScreen((GuiScreen) null);
			mc.setIngameFocus();
		}
	}

	@Override
	public void drawDefaultBackground() {
		super.drawDefaultBackground();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_146348_f);
		final int var1 = (width - 248) / 2;
		final int var2 = (height - 166) / 2;
		drawTexturedModalRect(var1, var2, 0, 0, 248, 166);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		final int var4 = (width - 248) / 2 + 10;
		int var5 = (height - 166) / 2 + 8;
		fontRendererObj.drawString(
				I18n.format("demo.help.title", new Object[0]), var4, var5,
				2039583);
		var5 += 12;
		final GameSettings var6 = mc.gameSettings;
		fontRendererObj.drawString(I18n.format(
				"demo.help.movementShort",
				new Object[] {
						GameSettings.getKeyDisplayString(var6.keyBindForward
								.getKeyCode()),
						GameSettings.getKeyDisplayString(var6.keyBindLeft
								.getKeyCode()),
						GameSettings.getKeyDisplayString(var6.keyBindBack
								.getKeyCode()),
						GameSettings.getKeyDisplayString(var6.keyBindRight
								.getKeyCode()) }), var4, var5, 5197647);
		fontRendererObj.drawString(
				I18n.format("demo.help.movementMouse", new Object[0]), var4,
				var5 + 12, 5197647);
		fontRendererObj.drawString(I18n.format("demo.help.jump",
				new Object[] { GameSettings
						.getKeyDisplayString(var6.keyBindJump.getKeyCode()) }),
				var4, var5 + 24, 5197647);
		fontRendererObj.drawString(I18n
				.format("demo.help.inventory",
						new Object[] { GameSettings
								.getKeyDisplayString(var6.keyBindInventory
										.getKeyCode()) }), var4, var5 + 36,
				5197647);
		fontRendererObj.drawSplitString(
				I18n.format("demo.help.fullWrapped", new Object[0]), var4,
				var5 + 68, 218, 2039583);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		buttons.clear();
		final byte var1 = -16;
		buttons.add(new GuiButton(1, width / 2 - 116, height / 2 + 62 + var1,
				114, 20, I18n.format("demo.help.buy", new Object[0])));
		buttons.add(new GuiButton(2, width / 2 + 2, height / 2 + 62 + var1,
				114, 20, I18n.format("demo.help.later", new Object[0])));
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();
	}
}
