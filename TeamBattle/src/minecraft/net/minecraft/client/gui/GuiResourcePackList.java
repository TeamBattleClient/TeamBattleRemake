package net.minecraft.client.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.util.EnumChatFormatting;

public abstract class GuiResourcePackList extends GuiListExtended {
	protected final List field_148204_l;
	protected final Minecraft field_148205_k;

	public GuiResourcePackList(Minecraft p_i45055_1_, int p_i45055_2_,
			int p_i45055_3_, List p_i45055_4_) {
		super(p_i45055_1_, p_i45055_2_, p_i45055_3_, 32, p_i45055_3_ - 55 + 4,
				36);
		field_148205_k = p_i45055_1_;
		field_148204_l = p_i45055_4_;
		field_148163_i = false;
		func_148133_a(true, (int) (p_i45055_1_.fontRenderer.FONT_HEIGHT * 1.5F));
	}

	@Override
	protected void func_148129_a(int p_148129_1_, int p_148129_2_,
			Tessellator p_148129_3_) {
		final String var4 = EnumChatFormatting.UNDERLINE + ""
				+ EnumChatFormatting.BOLD + func_148202_k();
		field_148205_k.fontRenderer.drawString(
				var4,
				p_148129_1_ + field_148155_a / 2
						- field_148205_k.fontRenderer.getStringWidth(var4) / 2,
				Math.min(field_148153_b + 3, p_148129_2_), 16777215);
	}

	@Override
	protected int func_148137_d() {
		return field_148151_d - 6;
	}

	@Override
	public int func_148139_c() {
		return field_148155_a;
	}

	@Override
	public ResourcePackListEntry func_148180_b(int p_148180_1_) {
		return (ResourcePackListEntry) func_148201_l().get(p_148180_1_);
	}

	public List func_148201_l() {
		return field_148204_l;
	}

	protected abstract String func_148202_k();

	@Override
	protected int getSize() {
		return func_148201_l().size();
	}
}
