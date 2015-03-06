package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiScreenOptionsSounds extends GuiScreen {
	class Button extends GuiButton {
		private final String field_146152_s;
		private final SoundCategory field_146153_r;
		public boolean field_146155_p;
		public float field_146156_o = 1.0F;

		public Button(int p_i45024_2_, int p_i45024_3_, int p_i45024_4_,
				SoundCategory p_i45024_5_, boolean p_i45024_6_) {
			super(p_i45024_2_, p_i45024_3_, p_i45024_4_, p_i45024_6_ ? 310
					: 150, 20, "");
			field_146153_r = p_i45024_5_;
			field_146152_s = I18n.format(
					"soundCategory." + p_i45024_5_.getCategoryName(),
					new Object[0]);
			displayString = field_146152_s + ": " + func_146504_a(p_i45024_5_);
			field_146156_o = field_146506_g.getSoundLevel(p_i45024_5_);
		}

		@Override
		public void func_146113_a(SoundHandler p_146113_1_) {
		}

		@Override
		public int getHoverState(boolean p_146114_1_) {
			return 0;
		}

		@Override
		protected void mouseDragged(Minecraft p_146119_1_, int p_146119_2_,
				int p_146119_3_) {
			if (field_146125_m) {
				if (field_146155_p) {
					field_146156_o = (float) (p_146119_2_ - (field_146128_h + 4))
							/ (float) (field_146120_f - 8);

					if (field_146156_o < 0.0F) {
						field_146156_o = 0.0F;
					}

					if (field_146156_o > 1.0F) {
						field_146156_o = 1.0F;
					}

					p_146119_1_.gameSettings.setSoundLevel(field_146153_r,
							field_146156_o);
					p_146119_1_.gameSettings.saveOptions();
					displayString = field_146152_s + ": "
							+ func_146504_a(field_146153_r);
				}

				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				drawTexturedModalRect(field_146128_h
						+ (int) (field_146156_o * (field_146120_f - 8)),
						field_146129_i, 0, 66, 4, 20);
				drawTexturedModalRect(field_146128_h
						+ (int) (field_146156_o * (field_146120_f - 8)) + 4,
						field_146129_i, 196, 66, 4, 20);
			}
		}

		@Override
		public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_,
				int p_146116_3_) {
			if (super.mousePressed(p_146116_1_, p_146116_2_, p_146116_3_)) {
				field_146156_o = (float) (p_146116_2_ - (field_146128_h + 4))
						/ (float) (field_146120_f - 8);

				if (field_146156_o < 0.0F) {
					field_146156_o = 0.0F;
				}

				if (field_146156_o > 1.0F) {
					field_146156_o = 1.0F;
				}

				p_146116_1_.gameSettings.setSoundLevel(field_146153_r,
						field_146156_o);
				p_146116_1_.gameSettings.saveOptions();
				displayString = field_146152_s + ": "
						+ func_146504_a(field_146153_r);
				field_146155_p = true;
				return true;
			} else
				return false;
		}

		@Override
		public void mouseReleased(int p_146118_1_, int p_146118_2_) {
			if (field_146155_p) {
				if (field_146153_r == SoundCategory.MASTER) {
				} else {
					field_146506_g.getSoundLevel(field_146153_r);
				}

				GuiScreenOptionsSounds.this.mc.getSoundHandler()
						.playSound(
								PositionedSoundRecord
										.func_147674_a(new ResourceLocation(
												"gui.button.press"), 1.0F));
			}

			field_146155_p = false;
		}
	}

	private final GuiScreen field_146505_f;
	private final GameSettings field_146506_g;
	protected String field_146507_a = "Options";

	private String field_146508_h;

	public GuiScreenOptionsSounds(GuiScreen p_i45025_1_,
			GameSettings p_i45025_2_) {
		field_146505_f = p_i45025_1_;
		field_146506_g = p_i45025_2_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 200) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(field_146505_f);
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, field_146507_a, width / 2, 15,
				16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	protected String func_146504_a(SoundCategory p_146504_1_) {
		final float var2 = field_146506_g.getSoundLevel(p_146504_1_);
		return var2 == 0.0F ? field_146508_h : (int) (var2 * 100.0F) + "%";
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		final byte var1 = 0;
		field_146507_a = I18n.format("options.sounds.title", new Object[0]);
		field_146508_h = I18n.format("options.off", new Object[0]);
		buttons.add(new GuiScreenOptionsSounds.Button(SoundCategory.MASTER
				.getCategoryId(), width / 2 - 155 + var1 % 2 * 160, height / 6
				- 12 + 24 * (var1 >> 1), SoundCategory.MASTER, true));
		int var6 = var1 + 2;
		final SoundCategory[] var2 = SoundCategory.values();
		final int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			final SoundCategory var5 = var2[var4];

			if (var5 != SoundCategory.MASTER) {
				buttons.add(new GuiScreenOptionsSounds.Button(var5
						.getCategoryId(), width / 2 - 155 + var6 % 2 * 160,
						height / 6 - 12 + 24 * (var6 >> 1), var5, false));
				++var6;
			}
		}

		buttons.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, I18n
				.format("gui.done", new Object[0])));
	}
}
