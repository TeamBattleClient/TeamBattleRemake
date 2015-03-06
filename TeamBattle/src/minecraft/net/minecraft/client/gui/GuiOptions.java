package net.minecraft.client.gui;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.stream.GuiStreamOptions;
import net.minecraft.client.gui.stream.GuiStreamUnavailable;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.stream.IStream;

public class GuiOptions extends GuiScreen implements GuiYesNoCallback {
	private static final GameSettings.Options[] field_146440_f = new GameSettings.Options[] {
			GameSettings.Options.FOV, GameSettings.Options.DIFFICULTY };
	private final GuiScreen field_146441_g;
	protected String field_146442_a = "Options";
	private final GameSettings field_146443_h;

	public GuiOptions(GuiScreen p_i1046_1_, GameSettings p_i1046_2_) {
		field_146441_g = p_i1046_1_;
		field_146443_h = p_i1046_2_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id < 100 && p_146284_1_ instanceof GuiOptionButton) {
				field_146443_h.setOptionValue(
						((GuiOptionButton) p_146284_1_).func_146136_c(), 1);
				p_146284_1_.displayString = field_146443_h
						.getKeyBinding(GameSettings.Options
								.getEnumOptions(p_146284_1_.id));
			}

			if (p_146284_1_.id == 8675309) {
				mc.entityRenderer.activateNextShader();
			}

			if (p_146284_1_.id == 101) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(new GuiVideoSettings(this, field_146443_h));
			}

			if (p_146284_1_.id == 100) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(new GuiControls(this, field_146443_h));
			}

			if (p_146284_1_.id == 102) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(new GuiLanguage(this, field_146443_h, mc
						.getLanguageManager()));
			}

			if (p_146284_1_.id == 103) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(new ScreenChatOptions(this, field_146443_h));
			}

			if (p_146284_1_.id == 104) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(new GuiSnooper(this, field_146443_h));
			}

			if (p_146284_1_.id == 200) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(field_146441_g);
			}

			if (p_146284_1_.id == 105) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(new GuiScreenResourcePacks(this));
			}

			if (p_146284_1_.id == 106) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(new GuiScreenOptionsSounds(this,
						field_146443_h));
			}

			if (p_146284_1_.id == 107) {
				mc.gameSettings.saveOptions();
				final IStream var2 = mc.func_152346_Z();

				if (var2.func_152936_l() && var2.func_152928_D()) {
					mc.displayGuiScreen(new GuiStreamOptions(this,
							field_146443_h));
				} else {
					GuiStreamUnavailable.func_152321_a(this);
				}
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, field_146442_a, width / 2, 15,
				16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		int var1 = 0;
		field_146442_a = I18n.format("options.title", new Object[0]);
		final GameSettings.Options[] var2 = field_146440_f;
		final int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			final GameSettings.Options var5 = var2[var4];

			if (var5.getEnumFloat()) {
				buttons.add(new GuiOptionSlider(var5.returnEnumOrdinal(), width
						/ 2 - 155 + var1 % 2 * 160, height / 6 - 12 + 24
						* (var1 >> 1), var5));
			} else {
				final GuiOptionButton var6 = new GuiOptionButton(
						var5.returnEnumOrdinal(), width / 2 - 155 + var1 % 2
								* 160, height / 6 - 12 + 24 * (var1 >> 1),
						var5, field_146443_h.getKeyBinding(var5));

				if (var5 == GameSettings.Options.DIFFICULTY
						&& mc.theWorld != null
						&& mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
					var6.enabled = false;
					var6.displayString = I18n.format("options.difficulty",
							new Object[0])
							+ ": "
							+ I18n.format("options.difficulty.hardcore",
									new Object[0]);
				}

				buttons.add(var6);
			}

			++var1;
		}

		buttons.add(new GuiButton(8675309, width / 2 + 5, height / 6 + 48 - 6,
				150, 20, "Super Secret Settings...") {

			@Override
			public void func_146113_a(SoundHandler p_146113_1_) {
				final SoundEventAccessorComposite var2 = p_146113_1_
						.func_147686_a(new SoundCategory[] {
								SoundCategory.ANIMALS, SoundCategory.BLOCKS,
								SoundCategory.MOBS, SoundCategory.PLAYERS,
								SoundCategory.WEATHER });

				if (var2 != null) {
					p_146113_1_.playSound(PositionedSoundRecord.func_147674_a(
							var2.func_148729_c(), 0.5F));
				}
			}
		});
		buttons.add(new GuiButton(106, width / 2 - 155, height / 6 + 72 - 6,
				150, 20, I18n.format("options.sounds", new Object[0])));
		buttons.add(new GuiButton(107, width / 2 + 5, height / 6 + 72 - 6, 150,
				20, I18n.format("options.stream", new Object[0])));
		buttons.add(new GuiButton(101, width / 2 - 155, height / 6 + 96 - 6,
				150, 20, I18n.format("options.video", new Object[0])));
		buttons.add(new GuiButton(100, width / 2 + 5, height / 6 + 96 - 6, 150,
				20, I18n.format("options.controls", new Object[0])));
		buttons.add(new GuiButton(102, width / 2 - 155, height / 6 + 120 - 6,
				150, 20, I18n.format("options.language", new Object[0])));
		buttons.add(new GuiButton(103, width / 2 + 5, height / 6 + 120 - 6,
				150, 20, I18n
						.format("options.multiplayer.title", new Object[0])));
		buttons.add(new GuiButton(105, width / 2 - 155, height / 6 + 144 - 6,
				150, 20, I18n.format("options.resourcepack", new Object[0])));
		buttons.add(new GuiButton(104, width / 2 + 5, height / 6 + 144 - 6,
				150, 20, I18n.format("options.snooper.view", new Object[0])));
		buttons.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, I18n
				.format("gui.done", new Object[0])));
	}
}
