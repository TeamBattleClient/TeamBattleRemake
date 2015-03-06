package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class GuiControls extends GuiScreen {
	private static final GameSettings.Options[] field_146492_g = new GameSettings.Options[] {
			GameSettings.Options.INVERT_MOUSE,
			GameSettings.Options.SENSITIVITY, GameSettings.Options.TOUCHSCREEN };
	public KeyBinding field_146491_f = null;
	private GuiButton field_146493_s;
	private GuiKeyBindingList field_146494_r;
	protected String field_146495_a = "Controls";
	private final GuiScreen field_146496_h;
	private final GameSettings field_146497_i;
	public long field_152177_g;

	public GuiControls(GuiScreen p_i1027_1_, GameSettings p_i1027_2_) {
		field_146496_h = p_i1027_1_;
		field_146497_i = p_i1027_2_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 200) {
			mc.displayGuiScreen(field_146496_h);
		} else if (p_146284_1_.id == 201) {
			final KeyBinding[] var2 = mc.gameSettings.keyBindings;
			final int var3 = var2.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				final KeyBinding var5 = var2[var4];
				var5.setKeyCode(var5.getKeyCodeDefault());
			}

			KeyBinding.resetKeyBindingArrayAndHash();
		} else if (p_146284_1_.id < 100
				&& p_146284_1_ instanceof GuiOptionButton) {
			field_146497_i.setOptionValue(
					((GuiOptionButton) p_146284_1_).func_146136_c(), 1);
			p_146284_1_.displayString = field_146497_i
					.getKeyBinding(GameSettings.Options
							.getEnumOptions(p_146284_1_.id));
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		field_146494_r.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
		drawCenteredString(fontRendererObj, field_146495_a, width / 2, 8,
				16777215);
		boolean var4 = true;
		final KeyBinding[] var5 = field_146497_i.keyBindings;
		final int var6 = var5.length;

		for (int var7 = 0; var7 < var6; ++var7) {
			final KeyBinding var8 = var5[var7];

			if (var8.getKeyCode() != var8.getKeyCodeDefault()) {
				var4 = false;
				break;
			}
		}

		field_146493_s.enabled = !var4;
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		field_146494_r = new GuiKeyBindingList(this, mc);
		buttons.add(new GuiButton(200, width / 2 - 155, height - 29, 150, 20,
				I18n.format("gui.done", new Object[0])));
		buttons.add(field_146493_s = new GuiButton(201, width / 2 - 155 + 160,
				height - 29, 150, 20, I18n.format("controls.resetAll",
						new Object[0])));
		field_146495_a = I18n.format("controls.title", new Object[0]);
		int var1 = 0;
		final GameSettings.Options[] var2 = field_146492_g;
		final int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			final GameSettings.Options var5 = var2[var4];

			if (var5.getEnumFloat()) {
				buttons.add(new GuiOptionSlider(var5.returnEnumOrdinal(), width
						/ 2 - 155 + var1 % 2 * 160, 18 + 24 * (var1 >> 1), var5));
			} else {
				buttons.add(new GuiOptionButton(var5.returnEnumOrdinal(), width
						/ 2 - 155 + var1 % 2 * 160, 18 + 24 * (var1 >> 1),
						var5, field_146497_i.getKeyBinding(var5)));
			}

			++var1;
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (field_146491_f != null) {
			if (p_73869_2_ == 1) {
				field_146497_i.setKeyCodeSave(field_146491_f, 0);
			} else {
				field_146497_i.setKeyCodeSave(field_146491_f, p_73869_2_);
			}

			field_146491_f = null;
			field_152177_g = Minecraft.getSystemTime();
			KeyBinding.resetKeyBindingArrayAndHash();
		} else {
			super.keyTyped(p_73869_1_, p_73869_2_);
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		if (field_146491_f != null) {
			field_146497_i.setKeyCodeSave(field_146491_f, -100 + p_73864_3_);
			field_146491_f = null;
			KeyBinding.resetKeyBindingArrayAndHash();
		} else if (p_73864_3_ != 0
				|| !field_146494_r.func_148179_a(p_73864_1_, p_73864_2_,
						p_73864_3_)) {
			super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		}
	}

	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_,
			int p_146286_3_) {
		if (p_146286_3_ != 0
				|| !field_146494_r.func_148181_b(p_146286_1_, p_146286_2_,
						p_146286_3_)) {
			super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
		}
	}
}
