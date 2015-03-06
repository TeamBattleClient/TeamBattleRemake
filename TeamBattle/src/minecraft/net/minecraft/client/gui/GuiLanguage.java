package net.minecraft.client.gui;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class GuiLanguage extends GuiScreen {
	class List extends GuiSlot {
		private final java.util.List field_148176_l = Lists.newArrayList();
		private final Map field_148177_m = Maps.newHashMap();

		public List() {
			super(GuiLanguage.this.mc, GuiLanguage.this.width,
					GuiLanguage.this.height, 32,
					GuiLanguage.this.height - 65 + 4, 18);
			final Iterator var2 = field_146454_h.getLanguages().iterator();

			while (var2.hasNext()) {
				final Language var3 = (Language) var2.next();
				field_148177_m.put(var3.getLanguageCode(), var3);
				field_148176_l.add(var3.getLanguageCode());
			}
		}

		@Override
		protected void drawBackground() {
			drawDefaultBackground();
		}

		@Override
		protected void drawSlot(int p_148126_1_, int p_148126_2_,
				int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_,
				int p_148126_6_, int p_148126_7_) {
			GuiLanguage.this.fontRendererObj.setBidiFlag(true);
			drawCenteredString(GuiLanguage.this.fontRendererObj,
					((Language) field_148177_m.get(field_148176_l
							.get(p_148126_1_))).toString(), field_148155_a / 2,
					p_148126_3_ + 1, 16777215);
			GuiLanguage.this.fontRendererObj.setBidiFlag(field_146454_h
					.getCurrentLanguage().isBidirectional());
		}

		@Override
		protected void elementClicked(int p_148144_1_, boolean p_148144_2_,
				int p_148144_3_, int p_148144_4_) {
			final Language var5 = (Language) field_148177_m.get(field_148176_l
					.get(p_148144_1_));
			field_146454_h.setCurrentLanguage(var5);
			field_146451_g.language = var5.getLanguageCode();
			GuiLanguage.this.mc.refreshResources();
			GuiLanguage.this.fontRendererObj.setUnicodeFlag(field_146454_h
					.isCurrentLocaleUnicode()
					|| field_146451_g.forceUnicodeFont);
			GuiLanguage.this.fontRendererObj.setBidiFlag(field_146454_h
					.isCurrentLanguageBidirectional());
			field_146452_r.displayString = I18n.format("gui.done",
					new Object[0]);
			field_146455_i.displayString = field_146451_g
					.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
			field_146451_g.saveOptions();
		}

		@Override
		protected int func_148138_e() {
			return getSize() * 18;
		}

		@Override
		protected int getSize() {
			return field_148176_l.size();
		}

		@Override
		protected boolean isSelected(int p_148131_1_) {
			return ((String) field_148176_l.get(p_148131_1_))
					.equals(field_146454_h.getCurrentLanguage()
							.getLanguageCode());
		}
	}

	private GuiLanguage.List field_146450_f;
	private final GameSettings field_146451_g;
	private GuiOptionButton field_146452_r;
	protected GuiScreen field_146453_a;
	private final LanguageManager field_146454_h;

	private GuiOptionButton field_146455_i;

	public GuiLanguage(GuiScreen p_i1043_1_, GameSettings p_i1043_2_,
			LanguageManager p_i1043_3_) {
		field_146453_a = p_i1043_1_;
		field_146451_g = p_i1043_2_;
		field_146454_h = p_i1043_3_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			switch (p_146284_1_.id) {
			case 5:
				break;

			case 6:
				mc.displayGuiScreen(field_146453_a);
				break;

			case 100:
				if (p_146284_1_ instanceof GuiOptionButton) {
					field_146451_g.setOptionValue(
							((GuiOptionButton) p_146284_1_).func_146136_c(), 1);
					p_146284_1_.displayString = field_146451_g
							.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
					final ScaledResolution var2 = new ScaledResolution(mc,
							mc.displayWidth, mc.displayHeight);
					final int var3 = var2.getScaledWidth();
					final int var4 = var2.getScaledHeight();
					setWorldAndResolution(mc, var3, var4);
				}

				break;

			default:
				field_146450_f.func_148147_a(p_146284_1_);
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		field_146450_f.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
		drawCenteredString(fontRendererObj,
				I18n.format("options.language", new Object[0]), width / 2, 16,
				16777215);
		drawCenteredString(fontRendererObj,
				"(" + I18n.format("options.languageWarning", new Object[0])
						+ ")", width / 2, height - 56, 8421504);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		if (field_146455_i != null) {
			;
		}

		buttons.add(field_146455_i = new GuiOptionButton(100, width / 2 - 155,
				height - 38, GameSettings.Options.FORCE_UNICODE_FONT,
				field_146451_g
						.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT)));
		buttons.add(field_146452_r = new GuiOptionButton(6,
				width / 2 - 155 + 160, height - 38, I18n.format("gui.done",
						new Object[0])));
		field_146450_f = new GuiLanguage.List();
		field_146450_f.func_148134_d(7, 8);
	}
}
