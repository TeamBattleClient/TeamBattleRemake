package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.src.GuiAnimationSettingsOF;
import net.minecraft.src.GuiDetailSettingsOF;
import net.minecraft.src.GuiOtherSettingsOF;
import net.minecraft.src.GuiPerformanceSettingsOF;
import net.minecraft.src.GuiQualitySettingsOF;

public class GuiVideoSettings extends GuiScreen {
	private static GameSettings.Options[] field_146502_i = new GameSettings.Options[] {
			GameSettings.Options.GRAPHICS,
			GameSettings.Options.RENDER_DISTANCE,
			GameSettings.Options.AMBIENT_OCCLUSION,
			GameSettings.Options.FRAMERATE_LIMIT,
			GameSettings.Options.AO_LEVEL, GameSettings.Options.VIEW_BOBBING,
			GameSettings.Options.GUI_SCALE,
			GameSettings.Options.ADVANCED_OPENGL, GameSettings.Options.GAMMA,
			GameSettings.Options.CHUNK_LOADING, GameSettings.Options.FOG_FANCY,
			GameSettings.Options.FOG_START, GameSettings.Options.ANAGLYPH };

	public static int getButtonHeight(GuiButton btn) {
		return btn.field_146121_g;
	}

	public static int getButtonWidth(GuiButton btn) {
		return btn.field_146120_f;
	}

	private final GuiScreen field_146498_f;
	private final GameSettings field_146499_g;

	protected String field_146500_a = "Video Settings";
	private boolean is64bit;
	private int lastMouseX = 0;

	private int lastMouseY = 0;

	private long mouseStillTime = 0L;

	public GuiVideoSettings(GuiScreen par1GuiScreen,
			GameSettings par2GameSettings) {
		field_146498_f = par1GuiScreen;
		field_146499_g = par2GameSettings;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			final int var2 = field_146499_g.guiScale;

			if (par1GuiButton.id < 200
					&& par1GuiButton instanceof GuiOptionButton) {
				field_146499_g.setOptionValue(
						((GuiOptionButton) par1GuiButton).func_146136_c(), 1);
				par1GuiButton.displayString = field_146499_g
						.getKeyBinding(GameSettings.Options
								.getEnumOptions(par1GuiButton.id));
			}

			if (par1GuiButton.id == 200) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(field_146498_f);
			}

			if (field_146499_g.guiScale != var2) {
				final ScaledResolution scr = new ScaledResolution(mc,
						mc.displayWidth, mc.displayHeight);
				final int var4 = scr.getScaledWidth();
				final int var5 = scr.getScaledHeight();
				setWorldAndResolution(mc, var4, var5);
			}

			if (par1GuiButton.id == 201) {
				mc.gameSettings.saveOptions();
				final GuiDetailSettingsOF scr1 = new GuiDetailSettingsOF(this,
						field_146499_g);
				mc.displayGuiScreen(scr1);
			}

			if (par1GuiButton.id == 202) {
				mc.gameSettings.saveOptions();
				final GuiQualitySettingsOF scr2 = new GuiQualitySettingsOF(
						this, field_146499_g);
				mc.displayGuiScreen(scr2);
			}

			if (par1GuiButton.id == 211) {
				mc.gameSettings.saveOptions();
				final GuiAnimationSettingsOF scr3 = new GuiAnimationSettingsOF(
						this, field_146499_g);
				mc.displayGuiScreen(scr3);
			}

			if (par1GuiButton.id == 212) {
				mc.gameSettings.saveOptions();
				final GuiPerformanceSettingsOF scr4 = new GuiPerformanceSettingsOF(
						this, field_146499_g);
				mc.displayGuiScreen(scr4);
			}

			if (par1GuiButton.id == 222) {
				mc.gameSettings.saveOptions();
				final GuiOtherSettingsOF scr5 = new GuiOtherSettingsOF(this,
						field_146499_g);
				mc.displayGuiScreen(scr5);
			}

			if (par1GuiButton.id == GameSettings.Options.AO_LEVEL.ordinal())
				return;
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int x, int y, float z) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, field_146500_a, width / 2,
				is64bit ? 20 : 5, 16777215);

		if (!is64bit && field_146499_g.renderDistanceChunks > 8) {
			;
		}

		super.drawScreen(x, y, z);

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
			final boolean flag = i >= btn.field_146128_h
					&& j >= btn.field_146129_i
					&& i < btn.field_146128_h + btn.field_146120_f
					&& j < btn.field_146129_i + btn.field_146121_g;

			if (flag)
				return btn;
		}

		return null;
	}

	private String[] getTooltipLines(String btnName) {
		return btnName.equals("Graphics") ? new String[] { "Visual quality",
				"  Fast  - lower quality, faster",
				"  Fancy - higher quality, slower",
				"Changes the appearance of clouds, leaves, water,",
				"shadows and grass sides." }
				: btnName.equals("Render Distance") ? new String[] {
						"Visible distance",
						"  2 Tiny - 32m (fastest)",
						"  4 Short - 64m (faster)",
						"  8 Normal - 128m",
						"  16 Far - 256m (slower)",
						"  32 Extreme - 512m (slowest!)",
						"The Extreme view distance is very resource demanding!",
						"Values over 16 Far are only effective in local worlds." }
						: btnName.equals("Smooth Lighting") ? new String[] {
								"Smooth lighting",
								"  OFF - no smooth lighting (faster)",
								"  Minimum - simple smooth lighting (slower)",
								"  Maximum - complex smooth lighting (slowest)" }
								: btnName.equals("Smooth Lighting Level") ? new String[] {
										"Smooth lighting level",
										"  OFF - no smooth lighting (faster)",
										"  1% - light smooth lighting (slower)",
										"  100% - dark smooth lighting (slower)" }
										: btnName.equals("Max Framerate") ? new String[] {
												"Max framerate",
												"  VSync - limit to monitor framerate (60, 30, 20)",
												"  5-255 - variable",
												"  Unlimited - no limit (fastest)",
												"The framerate limit decreases the FPS even if",
												"the limit value is not reached." }
												: btnName
														.equals("View Bobbing") ? new String[] {
														"More realistic movement.",
														"When using mipmaps set it to OFF for best results." }
														: btnName
																.equals("GUI Scale") ? new String[] {
																"GUI Scale",
																"Smaller GUI might be faster" }
																: btnName
																		.equals("Server Textures") ? new String[] {
																		"Server textures",
																		"Use the resource pack recommended by the server" }
																		: btnName
																				.equals("Advanced OpenGL") ? new String[] {
																				"Detect and render only visible geometry",
																				"  OFF - all geometry is rendered (slower)",
																				"  Fast - only visible geometry is rendered (fastest)",
																				"  Fancy - conservative, avoids visual artifacts (faster)",
																				"The option is available only if it is supported by the ",
																				"graphic card." }
																				: btnName
																						.equals("Fog") ? new String[] {
																						"Fog type",
																						"  Fast - faster fog",
																						"  Fancy - slower fog, looks better",
																						"  OFF - no fog, fastest",
																						"The fancy fog is available only if it is supported by the ",
																						"graphic card." }
																						: btnName
																								.equals("Fog Start") ? new String[] {
																								"Fog start",
																								"  0.2 - the fog starts near the player",
																								"  0.8 - the fog starts far from the player",
																								"This option usually does not affect the performance." }
																								: btnName
																										.equals("Brightness") ? new String[] {
																										"Increases the brightness of darker objects",
																										"  OFF - standard brightness",
																										"  100% - maximum brightness for darker objects",
																										"This options does not change the brightness of ",
																										"fully black objects" }
																										: btnName
																												.equals("Chunk Loading") ? new String[] {
																												"Chunk Loading",
																												"  Default - unstable FPS when loading chunks",
																												"  Smooth - stable FPS",
																												"  Multi-Core - stable FPS, 3x faster world loading",
																												"Smooth and Multi-Core remove the stuttering and ",
																												"freezes caused by chunk loading.",
																												"Multi-Core can speed up 3x the world loading and",
																												"increase FPS by using a second CPU core." }
																												: null;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		field_146500_a = I18n.format("options.videoTitle", new Object[0]);
		buttons.clear();
		is64bit = false;
		final String[] var1 = new String[] { "sun.arch.data.model",
				"com.ibm.vm.bitmode", "os.arch" };
		final String[] var2 = var1;
		final int var3 = var1.length;

		for (int var8 = 0; var8 < var3; ++var8) {
			final String var9 = var2[var8];
			final String var10 = System.getProperty(var9);

			if (var10 != null && var10.contains("64")) {
				is64bit = true;
				break;
			}
		}

		final GameSettings.Options[] var13 = field_146502_i;
		final int var14 = var13.length;
		int x;
		int var15;

		for (var15 = 0; var15 < var14; ++var15) {
			final GameSettings.Options y = var13[var15];
			x = width / 2 - 155 + var15 % 2 * 160;
			final int y1 = height / 6 + 21 * (var15 / 2) - 10;

			if (y.getEnumFloat()) {
				buttons.add(new GuiOptionSlider(y.returnEnumOrdinal(), x, y1, y));
			} else {
				buttons.add(new GuiOptionButton(y.returnEnumOrdinal(), x, y1,
						y, field_146499_g.getKeyBinding(y)));
			}
		}

		int var16 = height / 6 + 21 * (var15 / 2) - 10;
		x = width / 2 - 155 + 160;
		buttons.add(new GuiOptionButton(202, x, var16, "Quality..."));
		var16 += 21;
		x = width / 2 - 155 + 0;
		buttons.add(new GuiOptionButton(201, x, var16, "Details..."));
		x = width / 2 - 155 + 160;
		buttons.add(new GuiOptionButton(212, x, var16, "Performance..."));
		var16 += 21;
		x = width / 2 - 155 + 0;
		buttons.add(new GuiOptionButton(211, x, var16, "Animations..."));
		x = width / 2 - 155 + 160;
		buttons.add(new GuiOptionButton(222, x, var16, "Other..."));
		buttons.add(new GuiButton(200, width / 2 - 100, height / 6 + 168 + 11,
				I18n.format("gui.done", new Object[0])));
	}
}
