package net.minecraft.client.gui;

import java.util.Random;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;

import org.lwjgl.input.Keyboard;

public class GuiCreateWorld extends GuiScreen {
	private static final String[] field_146327_L = new String[] { "CON", "COM",
			"PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4",
			"COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3",
			"LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9" };

	public static String func_146317_a(ISaveFormat p_146317_0_,
			String p_146317_1_) {
		p_146317_1_ = p_146317_1_.replaceAll("[\\./\"]", "_");
		final String[] var2 = field_146327_L;
		final int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			final String var5 = var2[var4];

			if (p_146317_1_.equalsIgnoreCase(var5)) {
				p_146317_1_ = "_" + p_146317_1_ + "_";
			}
		}

		while (p_146317_0_.getWorldInfo(p_146317_1_) != null) {
			p_146317_1_ = p_146317_1_ + "-";
		}

		return p_146317_1_;
	}

	private GuiButton field_146320_D;
	private GuiButton field_146321_E;
	private GuiButton field_146322_F;
	private String field_146323_G;
	private GuiButton field_146324_A;
	private GuiButton field_146325_B;
	private GuiButton field_146326_C;
	private String field_146328_H;
	private String field_146329_I;
	private String field_146330_J;
	private int field_146331_K;
	private final GuiScreen field_146332_f;
	private GuiTextField field_146333_g;
	public String field_146334_a = "";
	private GuiTextField field_146335_h;
	private String field_146336_i;
	private boolean field_146337_w;
	private boolean field_146338_v;
	private boolean field_146339_u;
	private boolean field_146340_t;
	private boolean field_146341_s = true;
	private String field_146342_r = "survival";
	private GuiButton field_146343_z;
	private boolean field_146344_y;

	private boolean field_146345_x;

	public GuiCreateWorld(GuiScreen p_i1030_1_) {
		field_146332_f = p_i1030_1_;
		field_146329_I = "";
		field_146330_J = I18n.format("selectWorld.newWorld", new Object[0]);
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 1) {
				mc.displayGuiScreen(field_146332_f);
			} else if (p_146284_1_.id == 0) {
				mc.displayGuiScreen((GuiScreen) null);

				if (field_146345_x)
					return;

				field_146345_x = true;
				long var2 = new Random().nextLong();
				final String var4 = field_146335_h.getText();

				if (!MathHelper.stringNullOrLengthZero(var4)) {
					try {
						final long var5 = Long.parseLong(var4);

						if (var5 != 0L) {
							var2 = var5;
						}
					} catch (final NumberFormatException var7) {
						var2 = var4.hashCode();
					}
				}

				final WorldSettings.GameType var8 = WorldSettings.GameType
						.getByName(field_146342_r);
				final WorldSettings var6 = new WorldSettings(var2, var8,
						field_146341_s, field_146337_w,
						WorldType.worldTypes[field_146331_K]);
				var6.func_82750_a(field_146334_a);

				if (field_146338_v && !field_146337_w) {
					var6.enableBonusChest();
				}

				if (field_146340_t && !field_146337_w) {
					var6.enableCommands();
				}

				mc.launchIntegratedServer(field_146336_i, field_146333_g
						.getText().trim(), var6);
			} else if (p_146284_1_.id == 3) {
				func_146315_i();
			} else if (p_146284_1_.id == 2) {
				if (field_146342_r.equals("survival")) {
					if (!field_146339_u) {
						field_146340_t = false;
					}

					field_146337_w = false;
					field_146342_r = "hardcore";
					field_146337_w = true;
					field_146321_E.enabled = false;
					field_146326_C.enabled = false;
					func_146319_h();
				} else if (field_146342_r.equals("hardcore")) {
					if (!field_146339_u) {
						field_146340_t = true;
					}

					field_146337_w = false;
					field_146342_r = "creative";
					func_146319_h();
					field_146337_w = false;
					field_146321_E.enabled = true;
					field_146326_C.enabled = true;
				} else {
					if (!field_146339_u) {
						field_146340_t = false;
					}

					field_146342_r = "survival";
					func_146319_h();
					field_146321_E.enabled = true;
					field_146326_C.enabled = true;
					field_146337_w = false;
				}

				func_146319_h();
			} else if (p_146284_1_.id == 4) {
				field_146341_s = !field_146341_s;
				func_146319_h();
			} else if (p_146284_1_.id == 7) {
				field_146338_v = !field_146338_v;
				func_146319_h();
			} else if (p_146284_1_.id == 5) {
				++field_146331_K;

				if (field_146331_K >= WorldType.worldTypes.length) {
					field_146331_K = 0;
				}

				while (WorldType.worldTypes[field_146331_K] == null
						|| !WorldType.worldTypes[field_146331_K]
								.getCanBeCreated()) {
					++field_146331_K;

					if (field_146331_K >= WorldType.worldTypes.length) {
						field_146331_K = 0;
					}
				}

				field_146334_a = "";
				func_146319_h();
				func_146316_a(field_146344_y);
			} else if (p_146284_1_.id == 6) {
				field_146339_u = true;
				field_146340_t = !field_146340_t;
				func_146319_h();
			} else if (p_146284_1_.id == 8) {
				mc.displayGuiScreen(new GuiCreateFlatWorld(this, field_146334_a));
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
				I18n.format("selectWorld.create", new Object[0]), width / 2,
				20, -1);

		if (field_146344_y) {
			drawString(fontRendererObj,
					I18n.format("selectWorld.enterSeed", new Object[0]),
					width / 2 - 100, 47, -6250336);
			drawString(fontRendererObj,
					I18n.format("selectWorld.seedInfo", new Object[0]),
					width / 2 - 100, 85, -6250336);
			drawString(fontRendererObj,
					I18n.format("selectWorld.mapFeatures.info", new Object[0]),
					width / 2 - 150, 122, -6250336);
			drawString(fontRendererObj, I18n.format(
					"selectWorld.allowCommands.info", new Object[0]),
					width / 2 - 150, 172, -6250336);
			field_146335_h.drawTextBox();

			if (WorldType.worldTypes[field_146331_K].func_151357_h()) {
				fontRendererObj.drawSplitString(I18n.format(
						WorldType.worldTypes[field_146331_K].func_151359_c(),
						new Object[0]), field_146320_D.field_146128_h + 2,
						field_146320_D.field_146129_i + 22, field_146320_D
								.func_146117_b(), 10526880);
			}
		} else {
			drawString(fontRendererObj,
					I18n.format("selectWorld.enterName", new Object[0]),
					width / 2 - 100, 47, -6250336);
			drawString(fontRendererObj,
					I18n.format("selectWorld.resultFolder", new Object[0])
							+ " " + field_146336_i, width / 2 - 100, 85,
					-6250336);
			field_146333_g.drawTextBox();
			drawString(fontRendererObj, field_146323_G, width / 2 - 100, 137,
					-6250336);
			drawString(fontRendererObj, field_146328_H, width / 2 - 100, 149,
					-6250336);
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	private void func_146314_g() {
		field_146336_i = field_146333_g.getText().trim();
		final char[] var1 = ChatAllowedCharacters.allowedCharacters;
		final int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			final char var4 = var1[var3];
			field_146336_i = field_146336_i.replace(var4, '_');
		}

		if (MathHelper.stringNullOrLengthZero(field_146336_i)) {
			field_146336_i = "World";
		}

		field_146336_i = func_146317_a(mc.getSaveLoader(), field_146336_i);
	}

	private void func_146315_i() {
		func_146316_a(!field_146344_y);
	}

	private void func_146316_a(boolean p_146316_1_) {
		field_146344_y = p_146316_1_;
		field_146343_z.field_146125_m = !field_146344_y;
		field_146325_B.field_146125_m = field_146344_y;
		field_146326_C.field_146125_m = field_146344_y;
		field_146320_D.field_146125_m = field_146344_y;
		field_146321_E.field_146125_m = field_146344_y;
		field_146322_F.field_146125_m = field_146344_y
				&& WorldType.worldTypes[field_146331_K] == WorldType.FLAT;

		if (field_146344_y) {
			field_146324_A.displayString = I18n.format("gui.done",
					new Object[0]);
		} else {
			field_146324_A.displayString = I18n.format(
					"selectWorld.moreWorldOptions", new Object[0]);
		}
	}

	public void func_146318_a(WorldInfo p_146318_1_) {
		field_146330_J = I18n.format("selectWorld.newWorld.copyOf",
				new Object[] { p_146318_1_.getWorldName() });
		field_146329_I = p_146318_1_.getSeed() + "";
		field_146331_K = p_146318_1_.getTerrainType().getWorldTypeID();
		field_146334_a = p_146318_1_.getGeneratorOptions();
		field_146341_s = p_146318_1_.isMapFeaturesEnabled();
		field_146340_t = p_146318_1_.areCommandsAllowed();

		if (p_146318_1_.isHardcoreModeEnabled()) {
			field_146342_r = "hardcore";
		} else if (p_146318_1_.getGameType().isSurvivalOrAdventure()) {
			field_146342_r = "survival";
		} else if (p_146318_1_.getGameType().isCreative()) {
			field_146342_r = "creative";
		}
	}

	private void func_146319_h() {
		field_146343_z.displayString = I18n.format("selectWorld.gameMode",
				new Object[0])
				+ " "
				+ I18n.format("selectWorld.gameMode." + field_146342_r,
						new Object[0]);
		field_146323_G = I18n.format("selectWorld.gameMode." + field_146342_r
				+ ".line1", new Object[0]);
		field_146328_H = I18n.format("selectWorld.gameMode." + field_146342_r
				+ ".line2", new Object[0]);
		field_146325_B.displayString = I18n.format("selectWorld.mapFeatures",
				new Object[0]) + " ";

		if (field_146341_s) {
			field_146325_B.displayString = field_146325_B.displayString
					+ I18n.format("options.on", new Object[0]);
		} else {
			field_146325_B.displayString = field_146325_B.displayString
					+ I18n.format("options.off", new Object[0]);
		}

		field_146326_C.displayString = I18n.format("selectWorld.bonusItems",
				new Object[0]) + " ";

		if (field_146338_v && !field_146337_w) {
			field_146326_C.displayString = field_146326_C.displayString
					+ I18n.format("options.on", new Object[0]);
		} else {
			field_146326_C.displayString = field_146326_C.displayString
					+ I18n.format("options.off", new Object[0]);
		}

		field_146320_D.displayString = I18n.format("selectWorld.mapType",
				new Object[0])
				+ " "
				+ I18n.format(
						WorldType.worldTypes[field_146331_K].getTranslateName(),
						new Object[0]);
		field_146321_E.displayString = I18n.format("selectWorld.allowCommands",
				new Object[0]) + " ";

		if (field_146340_t && !field_146337_w) {
			field_146321_E.displayString = field_146321_E.displayString
					+ I18n.format("options.on", new Object[0]);
		} else {
			field_146321_E.displayString = field_146321_E.displayString
					+ I18n.format("options.off", new Object[0]);
		}
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		buttons.clear();
		buttons.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20,
				I18n.format("selectWorld.create", new Object[0])));
		buttons.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, I18n
				.format("gui.cancel", new Object[0])));
		buttons.add(field_146343_z = new GuiButton(2, width / 2 - 75, 115, 150,
				20, I18n.format("selectWorld.gameMode", new Object[0])));
		buttons.add(field_146324_A = new GuiButton(3, width / 2 - 75, 187, 150,
				20, I18n.format("selectWorld.moreWorldOptions", new Object[0])));
		buttons.add(field_146325_B = new GuiButton(4, width / 2 - 155, 100,
				150, 20, I18n.format("selectWorld.mapFeatures", new Object[0])));
		field_146325_B.field_146125_m = false;
		buttons.add(field_146326_C = new GuiButton(7, width / 2 + 5, 151, 150,
				20, I18n.format("selectWorld.bonusItems", new Object[0])));
		field_146326_C.field_146125_m = false;
		buttons.add(field_146320_D = new GuiButton(5, width / 2 + 5, 100, 150,
				20, I18n.format("selectWorld.mapType", new Object[0])));
		field_146320_D.field_146125_m = false;
		buttons.add(field_146321_E = new GuiButton(6, width / 2 - 155, 151,
				150, 20, I18n
						.format("selectWorld.allowCommands", new Object[0])));
		field_146321_E.field_146125_m = false;
		buttons.add(field_146322_F = new GuiButton(8, width / 2 + 5, 120, 150,
				20, I18n.format("selectWorld.customizeType", new Object[0])));
		field_146322_F.field_146125_m = false;
		field_146333_g = new GuiTextField(fontRendererObj, width / 2 - 100, 60,
				200, 20);
		field_146333_g.setFocused(true);
		field_146333_g.setText(field_146330_J);
		field_146335_h = new GuiTextField(fontRendererObj, width / 2 - 100, 60,
				200, 20);
		field_146335_h.setText(field_146329_I);
		func_146316_a(field_146344_y);
		func_146314_g();
		func_146319_h();
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (field_146333_g.isFocused() && !field_146344_y) {
			field_146333_g.textboxKeyTyped(p_73869_1_, p_73869_2_);
			field_146330_J = field_146333_g.getText();
		} else if (field_146335_h.isFocused() && field_146344_y) {
			field_146335_h.textboxKeyTyped(p_73869_1_, p_73869_2_);
			field_146329_I = field_146335_h.getText();
		}

		if (p_73869_2_ == 28 || p_73869_2_ == 156) {
			actionPerformed((GuiButton) buttons.get(0));
		}

		((GuiButton) buttons.get(0)).enabled = field_146333_g.getText()
				.length() > 0;
		func_146314_g();
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);

		if (field_146344_y) {
			field_146335_h.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		} else {
			field_146333_g.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		}
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
		field_146333_g.updateCursorCounter();
		field_146335_h.updateCursorCounter();
	}
}
