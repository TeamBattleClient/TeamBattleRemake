package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;

public class GuiShareToLan extends GuiScreen {
	private GuiButton field_146596_f;
	private GuiButton field_146597_g;
	private final GuiScreen field_146598_a;
	private String field_146599_h = "survival";
	private boolean field_146600_i;

	public GuiShareToLan(GuiScreen p_i1055_1_) {
		field_146598_a = p_i1055_1_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 102) {
			mc.displayGuiScreen(field_146598_a);
		} else if (p_146284_1_.id == 104) {
			if (field_146599_h.equals("survival")) {
				field_146599_h = "creative";
			} else if (field_146599_h.equals("creative")) {
				field_146599_h = "adventure";
			} else {
				field_146599_h = "survival";
			}

			func_146595_g();
		} else if (p_146284_1_.id == 103) {
			field_146600_i = !field_146600_i;
			func_146595_g();
		} else if (p_146284_1_.id == 101) {
			mc.displayGuiScreen((GuiScreen) null);
			final String var2 = mc.getIntegratedServer().shareToLAN(
					WorldSettings.GameType.getByName(field_146599_h),
					field_146600_i);
			Object var3;

			if (var2 != null) {
				var3 = new ChatComponentTranslation("commands.publish.started",
						new Object[] { var2 });
			} else {
				var3 = new ChatComponentText("commands.publish.failed");
			}

			mc.ingameGUI.getChatGUI().func_146227_a((IChatComponent) var3);
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj,
				I18n.format("lanServer.title", new Object[0]), width / 2, 50,
				16777215);
		drawCenteredString(fontRendererObj,
				I18n.format("lanServer.otherPlayers", new Object[0]),
				width / 2, 82, 16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	private void func_146595_g() {
		field_146597_g.displayString = I18n.format("selectWorld.gameMode",
				new Object[0])
				+ " "
				+ I18n.format("selectWorld.gameMode." + field_146599_h,
						new Object[0]);
		field_146596_f.displayString = I18n.format("selectWorld.allowCommands",
				new Object[0]) + " ";

		if (field_146600_i) {
			field_146596_f.displayString = field_146596_f.displayString
					+ I18n.format("options.on", new Object[0]);
		} else {
			field_146596_f.displayString = field_146596_f.displayString
					+ I18n.format("options.off", new Object[0]);
		}
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		buttons.clear();
		buttons.add(new GuiButton(101, width / 2 - 155, height - 28, 150, 20,
				I18n.format("lanServer.start", new Object[0])));
		buttons.add(new GuiButton(102, width / 2 + 5, height - 28, 150, 20,
				I18n.format("gui.cancel", new Object[0])));
		buttons.add(field_146597_g = new GuiButton(104, width / 2 - 155, 100,
				150, 20, I18n.format("selectWorld.gameMode", new Object[0])));
		buttons.add(field_146596_f = new GuiButton(103, width / 2 + 5, 100,
				150, 20, I18n
						.format("selectWorld.allowCommands", new Object[0])));
		func_146595_g();
	}
}
