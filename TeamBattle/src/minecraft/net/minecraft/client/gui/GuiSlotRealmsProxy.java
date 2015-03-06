package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.realms.RealmsScrolledSelectionList;

public class GuiSlotRealmsProxy extends GuiSlot {
	private final RealmsScrolledSelectionList field_154340_k;

	public GuiSlotRealmsProxy(RealmsScrolledSelectionList p_i1085_1_,
			int p_i1085_2_, int p_i1085_3_, int p_i1085_4_, int p_i1085_5_,
			int p_i1085_6_) {
		super(Minecraft.getMinecraft(), p_i1085_2_, p_i1085_3_, p_i1085_4_,
				p_i1085_5_, p_i1085_6_);
		field_154340_k = p_i1085_1_;
	}

	@Override
	protected void drawBackground() {
		field_154340_k.renderBackground();
	}

	@Override
	protected void drawSlot(int p_148126_1_, int p_148126_2_, int p_148126_3_,
			int p_148126_4_, Tessellator p_148126_5_, int p_148126_6_,
			int p_148126_7_) {
		field_154340_k.renderItem(p_148126_1_, p_148126_2_, p_148126_3_,
				p_148126_4_, p_148126_6_, p_148126_7_);
	}

	@Override
	protected void elementClicked(int p_148144_1_, boolean p_148144_2_,
			int p_148144_3_, int p_148144_4_) {
		field_154340_k.selectItem(p_148144_1_, p_148144_2_, p_148144_3_,
				p_148144_4_);
	}

	@Override
	protected int func_148137_d() {
		return field_154340_k.getScrollbarPosition();
	}

	@Override
	protected int func_148138_e() {
		return field_154340_k.getMaxPosition();
	}

	public int func_154337_m() {
		return super.field_148150_g;
	}

	public int func_154338_k() {
		return super.field_148155_a;
	}

	public int func_154339_l() {
		return super.field_148162_h;
	}

	@Override
	protected int getSize() {
		return field_154340_k.getItemCount();
	}

	@Override
	protected boolean isSelected(int p_148131_1_) {
		return field_154340_k.isSelectedItem(p_148131_1_);
	}
}
