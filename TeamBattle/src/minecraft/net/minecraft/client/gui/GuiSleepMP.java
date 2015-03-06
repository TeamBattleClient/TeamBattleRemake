package net.minecraft.client.gui;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class GuiSleepMP extends GuiChat {

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 1) {
			func_146418_g();
		} else {
			super.actionPerformed(p_146284_1_);
		}
	}

	private void func_146418_g() {
		final NetHandlerPlayClient var1 = mc.thePlayer.sendQueue;
		var1.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, 3));
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		super.initGui();
		buttons.add(new GuiButton(1, width / 2 - 100, height - 40, I18n.format(
				"multiplayer.stopSleeping", new Object[0])));
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (p_73869_2_ == 1) {
			func_146418_g();
		} else if (p_73869_2_ != 28 && p_73869_2_ != 156) {
			super.keyTyped(p_73869_1_, p_73869_2_);
		} else {
			final String var3 = field_146415_a.getText().trim();

			if (!var3.isEmpty()) {
				mc.thePlayer.sendChatMessage(var3);
			}

			field_146415_a.setText("");
			mc.ingameGUI.getChatGUI().resetScroll();
		}
	}
}
