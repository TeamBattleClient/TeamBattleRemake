package net.minecraft.realms;

import net.minecraft.client.gui.GuiSlotRealmsProxy;

public class RealmsScrolledSelectionList {
	private final GuiSlotRealmsProxy proxy;

	public RealmsScrolledSelectionList(int p_i1119_1_, int p_i1119_2_,
			int p_i1119_3_, int p_i1119_4_, int p_i1119_5_) {
		proxy = new GuiSlotRealmsProxy(this, p_i1119_1_, p_i1119_2_,
				p_i1119_3_, p_i1119_4_, p_i1119_5_);
	}

	public int getItemCount() {
		return 0;
	}

	public int getMaxPosition() {
		return 0;
	}

	public int getScrollbarPosition() {
		return proxy.func_154338_k() / 2 + 124;
	}

	public boolean isSelectedItem(int p_isSelectedItem_1_) {
		return false;
	}

	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		proxy.func_148128_a(p_render_1_, p_render_2_, p_render_3_);
	}

	public void renderBackground() {
	}

	public void renderItem(int p_renderItem_1_, int p_renderItem_2_,
			int p_renderItem_3_, int p_renderItem_4_, int p_renderItem_5_,
			int p_renderItem_6_) {
		this.renderItem(p_renderItem_1_, p_renderItem_2_, p_renderItem_3_,
				p_renderItem_4_, Tezzelator.instance, p_renderItem_5_,
				p_renderItem_6_);
	}

	protected void renderItem(int p_renderItem_1_, int p_renderItem_2_,
			int p_renderItem_3_, int p_renderItem_4_,
			Tezzelator p_renderItem_5_, int p_renderItem_6_, int p_renderItem_7_) {
	}

	public void selectItem(int p_selectItem_1_, boolean p_selectItem_2_,
			int p_selectItem_3_, int p_selectItem_4_) {
	}

	public int width() {
		return proxy.func_154338_k();
	}

	public int xm() {
		return proxy.func_154337_m();
	}

	public int ym() {
		return proxy.func_154339_l();
	}
}
