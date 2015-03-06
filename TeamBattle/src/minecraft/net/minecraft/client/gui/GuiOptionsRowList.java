package net.minecraft.client.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;

import com.google.common.collect.Lists;

public class GuiOptionsRowList extends GuiListExtended {
	public static class Row implements GuiListExtended.IGuiListEntry {
		private final GuiButton field_148323_b;
		private final GuiButton field_148324_c;
		private final Minecraft field_148325_a = Minecraft.getMinecraft();

		public Row(GuiButton p_i45014_1_, GuiButton p_i45014_2_) {
			field_148323_b = p_i45014_1_;
			field_148324_c = p_i45014_2_;
		}

		@Override
		public void func_148277_b(int p_148277_1_, int p_148277_2_,
				int p_148277_3_, int p_148277_4_, int p_148277_5_,
				int p_148277_6_) {
			if (field_148323_b != null) {
				field_148323_b.mouseReleased(p_148277_2_, p_148277_3_);
			}

			if (field_148324_c != null) {
				field_148324_c.mouseReleased(p_148277_2_, p_148277_3_);
			}
		}

		@Override
		public boolean func_148278_a(int p_148278_1_, int p_148278_2_,
				int p_148278_3_, int p_148278_4_, int p_148278_5_,
				int p_148278_6_) {
			if (field_148323_b.mousePressed(field_148325_a, p_148278_2_,
					p_148278_3_)) {
				if (field_148323_b instanceof GuiOptionButton) {
					field_148325_a.gameSettings.setOptionValue(
							((GuiOptionButton) field_148323_b).func_146136_c(),
							1);
					field_148323_b.displayString = field_148325_a.gameSettings
							.getKeyBinding(GameSettings.Options
									.getEnumOptions(field_148323_b.id));
				}

				return true;
			} else if (field_148324_c != null
					&& field_148324_c.mousePressed(field_148325_a, p_148278_2_,
							p_148278_3_)) {
				if (field_148324_c instanceof GuiOptionButton) {
					field_148325_a.gameSettings.setOptionValue(
							((GuiOptionButton) field_148324_c).func_146136_c(),
							1);
					field_148324_c.displayString = field_148325_a.gameSettings
							.getKeyBinding(GameSettings.Options
									.getEnumOptions(field_148324_c.id));
				}

				return true;
			} else
				return false;
		}

		@Override
		public void func_148279_a(int p_148279_1_, int p_148279_2_,
				int p_148279_3_, int p_148279_4_, int p_148279_5_,
				Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_,
				boolean p_148279_9_) {
			if (field_148323_b != null) {
				field_148323_b.field_146129_i = p_148279_3_;
				field_148323_b.drawButton(field_148325_a, p_148279_7_,
						p_148279_8_);
			}

			if (field_148324_c != null) {
				field_148324_c.field_146129_i = p_148279_3_;
				field_148324_c.drawButton(field_148325_a, p_148279_7_,
						p_148279_8_);
			}
		}
	}

	private final List field_148184_k = Lists.newArrayList();

	public GuiOptionsRowList(Minecraft p_i45015_1_, int p_i45015_2_,
			int p_i45015_3_, int p_i45015_4_, int p_i45015_5_, int p_i45015_6_,
			GameSettings.Options... p_i45015_7_) {
		super(p_i45015_1_, p_i45015_2_, p_i45015_3_, p_i45015_4_, p_i45015_5_,
				p_i45015_6_);
		field_148163_i = false;

		for (int var8 = 0; var8 < p_i45015_7_.length; var8 += 2) {
			final GameSettings.Options var9 = p_i45015_7_[var8];
			final GameSettings.Options var10 = var8 < p_i45015_7_.length - 1 ? p_i45015_7_[var8 + 1]
					: null;
			final GuiButton var11 = func_148182_a(p_i45015_1_,
					p_i45015_2_ / 2 - 155, 0, var9);
			final GuiButton var12 = func_148182_a(p_i45015_1_,
					p_i45015_2_ / 2 - 155 + 160, 0, var10);
			field_148184_k.add(new GuiOptionsRowList.Row(var11, var12));
		}
	}

	@Override
	protected int func_148137_d() {
		return super.func_148137_d() + 32;
	}

	@Override
	public int func_148139_c() {
		return 400;
	}

	@Override
	public GuiOptionsRowList.Row func_148180_b(int p_148180_1_) {
		return (GuiOptionsRowList.Row) field_148184_k.get(p_148180_1_);
	}

	private GuiButton func_148182_a(Minecraft p_148182_1_, int p_148182_2_,
			int p_148182_3_, GameSettings.Options p_148182_4_) {
		if (p_148182_4_ == null)
			return null;
		else {
			final int var5 = p_148182_4_.returnEnumOrdinal();
			return p_148182_4_.getEnumFloat() ? new GuiOptionSlider(var5,
					p_148182_2_, p_148182_3_, p_148182_4_)
					: new GuiOptionButton(var5, p_148182_2_, p_148182_3_,
							p_148182_4_,
							p_148182_1_.gameSettings.getKeyBinding(p_148182_4_));
		}
	}

	@Override
	protected int getSize() {
		return field_148184_k.size();
	}
}
