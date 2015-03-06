package net.minecraft.client.gui;

import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;

import org.apache.commons.lang3.ArrayUtils;

public class GuiKeyBindingList extends GuiListExtended {
	public class CategoryEntry implements GuiListExtended.IGuiListEntry {
		private final String field_148285_b;
		private final int field_148286_c;

		public CategoryEntry(String p_i45028_2_) {
			field_148285_b = I18n.format(p_i45028_2_, new Object[0]);
			field_148286_c = field_148189_l.fontRenderer
					.getStringWidth(field_148285_b);
		}

		@Override
		public void func_148277_b(int p_148277_1_, int p_148277_2_,
				int p_148277_3_, int p_148277_4_, int p_148277_5_,
				int p_148277_6_) {
		}

		@Override
		public boolean func_148278_a(int p_148278_1_, int p_148278_2_,
				int p_148278_3_, int p_148278_4_, int p_148278_5_,
				int p_148278_6_) {
			return false;
		}

		@Override
		public void func_148279_a(int p_148279_1_, int p_148279_2_,
				int p_148279_3_, int p_148279_4_, int p_148279_5_,
				Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_,
				boolean p_148279_9_) {
			field_148189_l.fontRenderer
					.drawString(field_148285_b,
							field_148189_l.currentScreen.width / 2
									- field_148286_c / 2, p_148279_3_
									+ p_148279_5_
									- field_148189_l.fontRenderer.FONT_HEIGHT
									- 1, 16777215);
		}
	}

	public class KeyEntry implements GuiListExtended.IGuiListEntry {
		private final GuiButton field_148280_d;
		private final GuiButton field_148281_e;
		private final KeyBinding field_148282_b;
		private final String field_148283_c;

		private KeyEntry(KeyBinding p_i45029_2_) {
			field_148282_b = p_i45029_2_;
			field_148283_c = I18n.format(p_i45029_2_.getKeyDescription(),
					new Object[0]);
			field_148280_d = new GuiButton(0, 0, 0, 75, 18, I18n.format(
					p_i45029_2_.getKeyDescription(), new Object[0]));
			field_148281_e = new GuiButton(0, 0, 0, 50, 18, I18n.format(
					"controls.reset", new Object[0]));
		}

		KeyEntry(KeyBinding p_i45030_2_, Object p_i45030_3_) {
			this(p_i45030_2_);
		}

		@Override
		public void func_148277_b(int p_148277_1_, int p_148277_2_,
				int p_148277_3_, int p_148277_4_, int p_148277_5_,
				int p_148277_6_) {
			field_148280_d.mouseReleased(p_148277_2_, p_148277_3_);
			field_148281_e.mouseReleased(p_148277_2_, p_148277_3_);
		}

		@Override
		public boolean func_148278_a(int p_148278_1_, int p_148278_2_,
				int p_148278_3_, int p_148278_4_, int p_148278_5_,
				int p_148278_6_) {
			if (field_148280_d.mousePressed(field_148189_l, p_148278_2_,
					p_148278_3_)) {
				field_148191_k.field_146491_f = field_148282_b;
				return true;
			} else if (field_148281_e.mousePressed(field_148189_l, p_148278_2_,
					p_148278_3_)) {
				field_148189_l.gameSettings.setKeyCodeSave(field_148282_b,
						field_148282_b.getKeyCodeDefault());
				KeyBinding.resetKeyBindingArrayAndHash();
				return true;
			} else
				return false;
		}

		@Override
		public void func_148279_a(int p_148279_1_, int p_148279_2_,
				int p_148279_3_, int p_148279_4_, int p_148279_5_,
				Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_,
				boolean p_148279_9_) {
			final boolean var10 = field_148191_k.field_146491_f == field_148282_b;
			field_148189_l.fontRenderer.drawString(field_148283_c, p_148279_2_
					+ 90 - field_148188_n, p_148279_3_ + p_148279_5_ / 2
					- field_148189_l.fontRenderer.FONT_HEIGHT / 2, 16777215);
			field_148281_e.field_146128_h = p_148279_2_ + 190;
			field_148281_e.field_146129_i = p_148279_3_;
			field_148281_e.enabled = field_148282_b.getKeyCode() != field_148282_b
					.getKeyCodeDefault();
			field_148281_e.drawButton(field_148189_l, p_148279_7_, p_148279_8_);
			field_148280_d.field_146128_h = p_148279_2_ + 105;
			field_148280_d.field_146129_i = p_148279_3_;
			field_148280_d.displayString = GameSettings
					.getKeyDisplayString(field_148282_b.getKeyCode());
			boolean var11 = false;

			if (field_148282_b.getKeyCode() != 0) {
				final KeyBinding[] var12 = field_148189_l.gameSettings.keyBindings;
				final int var13 = var12.length;

				for (int var14 = 0; var14 < var13; ++var14) {
					final KeyBinding var15 = var12[var14];

					if (var15 != field_148282_b
							&& var15.getKeyCode() == field_148282_b
									.getKeyCode()) {
						var11 = true;
						break;
					}
				}
			}

			if (var10) {
				field_148280_d.displayString = EnumChatFormatting.WHITE + "> "
						+ EnumChatFormatting.YELLOW
						+ field_148280_d.displayString
						+ EnumChatFormatting.WHITE + " <";
			} else if (var11) {
				field_148280_d.displayString = EnumChatFormatting.RED
						+ field_148280_d.displayString;
			}

			field_148280_d.drawButton(field_148189_l, p_148279_7_, p_148279_8_);
		}
	}

	private int field_148188_n = 0;
	private final Minecraft field_148189_l;

	private final GuiListExtended.IGuiListEntry[] field_148190_m;

	private final GuiControls field_148191_k;

	public GuiKeyBindingList(GuiControls p_i45031_1_, Minecraft p_i45031_2_) {
		super(p_i45031_2_, p_i45031_1_.width, p_i45031_1_.height, 63,
				p_i45031_1_.height - 32, 20);
		field_148191_k = p_i45031_1_;
		field_148189_l = p_i45031_2_;
		final KeyBinding[] var3 = ArrayUtils
				.clone(p_i45031_2_.gameSettings.keyBindings);
		field_148190_m = new GuiListExtended.IGuiListEntry[var3.length
				+ KeyBinding.func_151467_c().size()];
		Arrays.sort(var3);
		int var4 = 0;
		String var5 = null;
		final KeyBinding[] var6 = var3;
		final int var7 = var3.length;

		for (int var8 = 0; var8 < var7; ++var8) {
			final KeyBinding var9 = var6[var8];
			final String var10 = var9.getKeyCategory();

			if (!var10.equals(var5)) {
				var5 = var10;
				field_148190_m[var4++] = new GuiKeyBindingList.CategoryEntry(
						var10);
			}

			final int var11 = p_i45031_2_.fontRenderer.getStringWidth(I18n
					.format(var9.getKeyDescription(), new Object[0]));

			if (var11 > field_148188_n) {
				field_148188_n = var11;
			}

			field_148190_m[var4++] = new GuiKeyBindingList.KeyEntry(var9, null);
		}
	}

	@Override
	protected int func_148137_d() {
		return super.func_148137_d() + 15;
	}

	@Override
	public int func_148139_c() {
		return super.func_148139_c() + 32;
	}

	@Override
	public GuiListExtended.IGuiListEntry func_148180_b(int p_148180_1_) {
		return field_148190_m[p_148180_1_];
	}

	@Override
	protected int getSize() {
		return field_148190_m.length;
	}
}
