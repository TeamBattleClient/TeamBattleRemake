package net.minecraft.client.gui;

import net.minecraft.util.IChatComponent;

public class ChatLine {
	/**
	 * int value to refer to existing Chat Lines, can be 0 which means
	 * unreferrable
	 */
	private final int chatLineID;
	private final IChatComponent lineString;

	/** GUI Update Counter value this Line was created at */
	private final int updateCounterCreated;

	public ChatLine(int p_i45000_1_, IChatComponent p_i45000_2_, int p_i45000_3_) {
		lineString = p_i45000_2_;
		updateCounterCreated = p_i45000_1_;
		chatLineID = p_i45000_3_;
	}

	public IChatComponent func_151461_a() {
		return lineString;
	}

	public int getChatLineID() {
		return chatLineID;
	}

	public int getUpdatedCounter() {
		return updateCounterCreated;
	}
}
