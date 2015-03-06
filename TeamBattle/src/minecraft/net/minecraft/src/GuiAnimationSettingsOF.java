package net.minecraft.src;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class GuiAnimationSettingsOF extends GuiScreen {
	private static GameSettings.Options[] enumOptions = new GameSettings.Options[] {
			GameSettings.Options.ANIMATED_WATER,
			GameSettings.Options.ANIMATED_LAVA,
			GameSettings.Options.ANIMATED_FIRE,
			GameSettings.Options.ANIMATED_PORTAL,
			GameSettings.Options.ANIMATED_REDSTONE,
			GameSettings.Options.ANIMATED_EXPLOSION,
			GameSettings.Options.ANIMATED_FLAME,
			GameSettings.Options.ANIMATED_SMOKE,
			GameSettings.Options.VOID_PARTICLES,
			GameSettings.Options.WATER_PARTICLES,
			GameSettings.Options.RAIN_SPLASH,
			GameSettings.Options.PORTAL_PARTICLES,
			GameSettings.Options.POTION_PARTICLES,
			GameSettings.Options.DRIPPING_WATER_LAVA,
			GameSettings.Options.ANIMATED_TERRAIN,
			GameSettings.Options.ANIMATED_ITEMS,
			GameSettings.Options.ANIMATED_TEXTURES,
			GameSettings.Options.PARTICLES };
	private final GuiScreen prevScreen;
	private final GameSettings settings;
	protected String title = "Animation Settings";

	public GuiAnimationSettingsOF(GuiScreen guiscreen, GameSettings gamesettings) {
		prevScreen = guiscreen;
		settings = gamesettings;
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.enabled) {
			if (guibutton.id < 200 && guibutton instanceof GuiOptionButton) {
				settings.setOptionValue(
						((GuiOptionButton) guibutton).func_146136_c(), 1);
				guibutton.displayString = settings
						.getKeyBinding(GameSettings.Options
								.getEnumOptions(guibutton.id));
			}

			if (guibutton.id == 200) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(prevScreen);
			}

			if (guibutton.id == 210) {
				mc.gameSettings.setAllAnimations(true);
			}

			if (guibutton.id == 211) {
				mc.gameSettings.setAllAnimations(false);
			}

			if (guibutton.id != GameSettings.Options.CLOUD_HEIGHT.ordinal()) {
				final ScaledResolution scaledresolution = new ScaledResolution(
						mc, mc.displayWidth, mc.displayHeight);
				final int i = scaledresolution.getScaledWidth();
				final int j = scaledresolution.getScaledHeight();
				setWorldAndResolution(mc, i, j);
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, title, width / 2, 20, 16777215);
		super.drawScreen(i, j, f);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		int i = 0;
		final GameSettings.Options[] aenumoptions = enumOptions;
		final int j = aenumoptions.length;

		for (int k = 0; k < j; ++k) {
			final GameSettings.Options enumoptions = aenumoptions[k];
			final int x = width / 2 - 155 + i % 2 * 160;
			final int y = height / 6 + 21 * (i / 2) - 10;

			if (!enumoptions.getEnumFloat()) {
				buttons.add(new GuiOptionButton(
						enumoptions.returnEnumOrdinal(), x, y, enumoptions,
						settings.getKeyBinding(enumoptions)));
			} else {
				buttons.add(new GuiOptionSlider(
						enumoptions.returnEnumOrdinal(), x, y, enumoptions));
			}

			++i;
		}

		buttons.add(new GuiButton(210, width / 2 - 155, height / 6 + 168 + 11,
				70, 20, "All ON"));
		buttons.add(new GuiButton(211, width / 2 - 155 + 80,
				height / 6 + 168 + 11, 70, 20, "All OFF"));
		buttons.add(new GuiOptionButton(200, width / 2 + 5,
				height / 6 + 168 + 11, I18n.format("gui.done", new Object[0])));
	}
}
