package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class GuiSnooper extends GuiScreen {
	class List extends GuiSlot {

		public List() {
			super(GuiSnooper.this.mc, GuiSnooper.this.width,
					GuiSnooper.this.height, 80, GuiSnooper.this.height - 40,
					GuiSnooper.this.fontRendererObj.FONT_HEIGHT + 1);
		}

		@Override
		protected void drawBackground() {
		}

		@Override
		protected void drawSlot(int p_148126_1_, int p_148126_2_,
				int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_,
				int p_148126_6_, int p_148126_7_) {
			GuiSnooper.this.fontRendererObj.drawString(
					(String) field_146604_g.get(p_148126_1_), 10, p_148126_3_,
					16777215);
			GuiSnooper.this.fontRendererObj.drawString(
					(String) field_146609_h.get(p_148126_1_), 230, p_148126_3_,
					16777215);
		}

		@Override
		protected void elementClicked(int p_148144_1_, boolean p_148144_2_,
				int p_148144_3_, int p_148144_4_) {
		}

		@Override
		protected int func_148137_d() {
			return field_148155_a - 10;
		}

		@Override
		protected int getSize() {
			return field_146604_g.size();
		}

		@Override
		protected boolean isSelected(int p_148131_1_) {
			return false;
		}
	}

	private final GameSettings field_146603_f;
	private final java.util.List field_146604_g = new ArrayList();
	private GuiButton field_146605_t;
	private GuiSnooper.List field_146606_s;
	private String[] field_146607_r;
	private final GuiScreen field_146608_a;
	private final java.util.List field_146609_h = new ArrayList();

	private String field_146610_i;

	public GuiSnooper(GuiScreen p_i1061_1_, GameSettings p_i1061_2_) {
		field_146608_a = p_i1061_1_;
		field_146603_f = p_i1061_2_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 2) {
				field_146603_f.saveOptions();
				field_146603_f.saveOptions();
				mc.displayGuiScreen(field_146608_a);
			}

			if (p_146284_1_.id == 1) {
				field_146603_f.setOptionValue(
						GameSettings.Options.SNOOPER_ENABLED, 1);
				field_146605_t.displayString = field_146603_f
						.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED);
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		field_146606_s.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
		drawCenteredString(fontRendererObj, field_146610_i, width / 2, 8,
				16777215);
		int var4 = 22;
		final String[] var5 = field_146607_r;
		final int var6 = var5.length;

		for (int var7 = 0; var7 < var6; ++var7) {
			final String var8 = var5[var7];
			drawCenteredString(fontRendererObj, var8, width / 2, var4, 8421504);
			var4 += fontRendererObj.FONT_HEIGHT;
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		field_146610_i = I18n.format("options.snooper.title", new Object[0]);
		final String var1 = I18n.format("options.snooper.desc", new Object[0]);
		final ArrayList var2 = new ArrayList();
		final Iterator var3 = fontRendererObj.listFormattedStringToWidth(var1,
				width - 30).iterator();

		while (var3.hasNext()) {
			final String var4 = (String) var3.next();
			var2.add(var4);
		}

		field_146607_r = (String[]) var2.toArray(new String[0]);
		field_146604_g.clear();
		field_146609_h.clear();
		buttons.add(field_146605_t = new GuiButton(1, width / 2 - 152,
				height - 30, 150, 20, field_146603_f
						.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED)));
		buttons.add(new GuiButton(2, width / 2 + 2, height - 30, 150, 20, I18n
				.format("gui.done", new Object[0])));
		final boolean var6 = mc.getIntegratedServer() != null
				&& mc.getIntegratedServer().getPlayerUsageSnooper() != null;
		Iterator var7 = new TreeMap(mc.getPlayerUsageSnooper()
				.getCurrentStats()).entrySet().iterator();
		Entry var5;

		while (var7.hasNext()) {
			var5 = (Entry) var7.next();
			field_146604_g.add((var6 ? "C " : "") + (String) var5.getKey());
			field_146609_h.add(fontRendererObj.trimStringToWidth(
					(String) var5.getValue(), width - 220));
		}

		if (var6) {
			var7 = new TreeMap(mc.getIntegratedServer().getPlayerUsageSnooper()
					.getCurrentStats()).entrySet().iterator();

			while (var7.hasNext()) {
				var5 = (Entry) var7.next();
				field_146604_g.add("S " + (String) var5.getKey());
				field_146609_h.add(fontRendererObj.trimStringToWidth(
						(String) var5.getValue(), width - 220));
			}
		}

		field_146606_s = new GuiSnooper.List();
	}
}
