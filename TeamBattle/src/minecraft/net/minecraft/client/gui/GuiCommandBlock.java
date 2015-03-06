package net.minecraft.client.gui;

import io.netty.buffer.Unpooled;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GuiCommandBlock extends GuiScreen {
	private static final Logger field_146488_a = LogManager.getLogger();
	private GuiTextField field_146485_f;
	private GuiTextField field_146486_g;
	private GuiButton field_146487_r;
	private final CommandBlockLogic field_146489_h;
	private GuiButton field_146490_i;

	public GuiCommandBlock(CommandBlockLogic p_i45032_1_) {
		field_146489_h = p_i45032_1_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 1) {
				mc.displayGuiScreen((GuiScreen) null);
			} else if (p_146284_1_.id == 0) {
				final PacketBuffer var2 = new PacketBuffer(Unpooled.buffer());

				try {
					var2.writeByte(field_146489_h.func_145751_f());
					field_146489_h.func_145757_a(var2);
					var2.writeStringToBuffer(field_146485_f.getText());
					mc.getNetHandler().addToSendQueue(
							new C17PacketCustomPayload("MC|AdvCdm", var2));
				} catch (final Exception var7) {
					field_146488_a.error("Couldn\'t send command block info",
							var7);
				} finally {
					var2.release();
				}

				mc.displayGuiScreen((GuiScreen) null);
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj,
				I18n.format("advMode.setCommand", new Object[0]), width / 2,
				20, 16777215);
		drawString(fontRendererObj,
				I18n.format("advMode.command", new Object[0]), width / 2 - 150,
				37, 10526880);
		field_146485_f.drawTextBox();
		final byte var4 = 75;
		final byte var5 = 0;
		final FontRenderer var10001 = fontRendererObj;
		final String var10002 = I18n.format("advMode.nearestPlayer",
				new Object[0]);
		final int var10003 = width / 2 - 150;
		int var7 = var5 + 1;
		drawString(var10001, var10002, var10003, var4 + var5
				* fontRendererObj.FONT_HEIGHT, 10526880);
		drawString(fontRendererObj,
				I18n.format("advMode.randomPlayer", new Object[0]),
				width / 2 - 150, var4 + var7++ * fontRendererObj.FONT_HEIGHT,
				10526880);
		drawString(fontRendererObj,
				I18n.format("advMode.allPlayers", new Object[0]),
				width / 2 - 150, var4 + var7++ * fontRendererObj.FONT_HEIGHT,
				10526880);

		if (field_146486_g.getText().length() > 0) {
			final int var6 = var4 + var7 * fontRendererObj.FONT_HEIGHT + 20;
			drawString(fontRendererObj,
					I18n.format("advMode.previousOutput", new Object[0]),
					width / 2 - 150, var6, 10526880);
			field_146486_g.drawTextBox();
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		buttons.clear();
		buttons.add(field_146490_i = new GuiButton(0, width / 2 - 4 - 150,
				height / 4 + 120 + 12, 150, 20, I18n.format("gui.done",
						new Object[0])));
		buttons.add(field_146487_r = new GuiButton(1, width / 2 + 4,
				height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel",
						new Object[0])));
		field_146485_f = new GuiTextField(fontRendererObj, width / 2 - 150, 50,
				300, 20);
		field_146485_f.func_146203_f(32767);
		field_146485_f.setFocused(true);
		field_146485_f.setText(field_146489_h.func_145753_i());
		field_146486_g = new GuiTextField(fontRendererObj, width / 2 - 150,
				135, 300, 20);
		field_146486_g.func_146203_f(32767);
		field_146486_g.func_146184_c(false);
		field_146486_g.setText(field_146489_h.func_145753_i());

		if (field_146489_h.func_145749_h() != null) {
			field_146486_g.setText(field_146489_h.func_145749_h()
					.getUnformattedText());
		}

		field_146490_i.enabled = field_146485_f.getText().trim().length() > 0;
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		field_146485_f.textboxKeyTyped(p_73869_1_, p_73869_2_);
		field_146486_g.textboxKeyTyped(p_73869_1_, p_73869_2_);
		field_146490_i.enabled = field_146485_f.getText().trim().length() > 0;

		if (p_73869_2_ != 28 && p_73869_2_ != 156) {
			if (p_73869_2_ == 1) {
				actionPerformed(field_146487_r);
			}
		} else {
			actionPerformed(field_146490_i);
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		field_146485_f.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		field_146486_g.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	}

	/**
	 * "Called when the screen is unloaded. Used to disable keyboard repeat events."
	 */
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		field_146485_f.updateCursorCounter();
	}
}
