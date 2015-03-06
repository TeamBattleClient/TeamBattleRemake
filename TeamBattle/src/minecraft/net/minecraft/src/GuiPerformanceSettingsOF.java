package net.minecraft.src;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class GuiPerformanceSettingsOF extends GuiScreen {
	private static GameSettings.Options[] enumOptions = new GameSettings.Options[] {
			GameSettings.Options.SMOOTH_FPS, GameSettings.Options.SMOOTH_WORLD,
			GameSettings.Options.LOAD_FAR,
			GameSettings.Options.PRELOADED_CHUNKS,
			GameSettings.Options.CHUNK_UPDATES,
			GameSettings.Options.CHUNK_UPDATES_DYNAMIC,
			GameSettings.Options.FAST_MATH,
			GameSettings.Options.LAZY_CHUNK_LOADING,
			GameSettings.Options.FAST_RENDER };
	private int lastMouseX = 0;
	private int lastMouseY = 0;
	private long mouseStillTime = 0L;
	private final GuiScreen prevScreen;
	private final GameSettings settings;
	protected String title = "Performance Settings";

	public GuiPerformanceSettingsOF(GuiScreen guiscreen,
			GameSettings gamesettings) {
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
	public void drawScreen(int x, int y, float f) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, title, width / 2, 20, 16777215);
		super.drawScreen(x, y, f);

		if (Math.abs(x - lastMouseX) <= 5 && Math.abs(y - lastMouseY) <= 5) {
			final short activateDelay = 700;

			if (System.currentTimeMillis() >= mouseStillTime + activateDelay) {
				final int x1 = width / 2 - 150;
				int y1 = height / 6 - 5;

				if (y <= y1 + 98) {
					y1 += 105;
				}

				final int x2 = x1 + 150 + 150;
				final int y2 = y1 + 84 + 10;
				final GuiButton btn = getSelectedButton(x, y);

				if (btn != null) {
					final String s = getButtonName(btn.displayString);
					final String[] lines = getTooltipLines(s);

					if (lines == null)
						return;

					drawGradientRect(x1, y1, x2, y2, -536870912, -536870912);

					for (int i = 0; i < lines.length; ++i) {
						final String line = lines[i];
						fontRendererObj.drawStringWithShadow(line, x1 + 5, y1
								+ 5 + i * 11, 14540253);
					}
				}
			}
		} else {
			lastMouseX = x;
			lastMouseY = y;
			mouseStillTime = System.currentTimeMillis();
		}
	}

	private String getButtonName(String displayString) {
		final int pos = displayString.indexOf(58);
		return pos < 0 ? displayString : displayString.substring(0, pos);
	}

	private GuiButton getSelectedButton(int i, int j) {
		for (int k = 0; k < buttons.size(); ++k) {
			final GuiButton btn = (GuiButton) buttons.get(k);
			final int btnWidth = GuiVideoSettings.getButtonWidth(btn);
			final int btnHeight = GuiVideoSettings.getButtonHeight(btn);
			final boolean flag = i >= btn.field_146128_h
					&& j >= btn.field_146129_i
					&& i < btn.field_146128_h + btnWidth
					&& j < btn.field_146129_i + btnHeight;

			if (flag)
				return btn;
		}

		return null;
	}

	private String[] getTooltipLines(String btnName) {
		return btnName.equals("Smooth FPS") ? new String[] {
				"Stabilizes FPS by flushing the graphic driver buffers",
				"  OFF - no stabilization, FPS may fluctuate",
				"  ON - FPS stabilization",
				"This option is graphics driver dependant and its effect",
				"is not always visible" }
				: btnName.equals("Smooth World") ? new String[] {
						"Removes lag spikes caused by the internal server.",
						"  OFF - no stabilization, FPS may fluctuate",
						"  ON - FPS stabilization",
						"Stabilizes FPS by distributing the internal server load.",
						"Effective only for local worlds and single-core CPU." }
						: btnName.equals("Load Far") ? new String[] {
								"Loads the world chunks at distance Far.",
								"Switching the render distance does not cause all chunks ",
								"to be loaded again.",
								"  OFF - world chunks loaded up to render distance",
								"  ON - world chunks loaded at distance Far, allows",
								"       fast render distance switching" }
								: btnName.equals("Preloaded Chunks") ? new String[] {
										"Defines an area in which no chunks will be loaded",
										"  OFF - after 5m new chunks will be loaded",
										"  2 - after 32m  new chunks will be loaded",
										"  8 - after 128m new chunks will be loaded",
										"Higher values need more time to load all the chunks",
										"and may decrease the FPS." }
										: btnName.equals("Chunk Updates") ? new String[] {
												"Chunk updates",
												" 1 - (default) slower world loading, higher FPS",
												" 3 - faster world loading, lower FPS",
												" 5 - fastest world loading, lowest FPS",
												"Number of chunk updates per rendered frame,",
												"higher values may destabilize the framerate." }
												: btnName
														.equals("Dynamic Updates") ? new String[] {
														"Dynamic chunk updates",
														" OFF - (default) standard chunk updates per frame",
														" ON - more updates while the player is standing still",
														"Dynamic updates force more chunk updates while",
														"the player is standing still to load the world faster." }
														: btnName
																.equals("Lazy Chunk Loading") ? new String[] {
																"Lazy Chunk Loading",
																" OFF - default server chunk loading",
																" ON - lazy server chunk loading (smoother)",
																"Smooths the integrated server chunk loading by",
																"distributing the chunks over several ticks.",
																"Turn it OFF if parts of the world do not load correctly.",
																"Effective only for local worlds and single-core CPU." }
																: btnName
																		.equals("Fast Math") ? new String[] {
																		"Fast Math",
																		" OFF - standard math (default)",
																		" ON - faster math",
																		"Uses optimized sin() and cos() functions which can",
																		"better utilize the CPU cache and increase the FPS." }
																		: btnName
																				.equals("Fast Render") ? new String[] {
																				"Fast Render",
																				" OFF - standard rendering (default)",
																				" ON - faster rendering",
																				"Uses optimized rendering algorithm which decreases",
																				"the GPU load and may substantionally increase the FPS.",
																				"You can turn if OFF if you notice flickering textures",
																				"on some blocks." }
																				: null;
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

		buttons.add(new GuiButton(200, width / 2 - 100, height / 6 + 168 + 11,
				I18n.format("gui.done", new Object[0])));
	}
}
