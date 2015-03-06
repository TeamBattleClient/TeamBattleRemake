package net.minecraft.util;

import java.util.Iterator;

public class ChatComponentText extends ChatComponentStyle {
	private final String text;

	public ChatComponentText(String p_i45159_1_) {
		text = p_i45159_1_;
	}

	/**
	 * Creates a copy of this component. Almost a deep copy, except the style is
	 * shallow-copied.
	 */
	@Override
	public ChatComponentText createCopy() {
		final ChatComponentText var1 = new ChatComponentText(text);
		var1.setChatStyle(getChatStyle().createShallowCopy());
		final Iterator var2 = getSiblings().iterator();

		while (var2.hasNext()) {
			final IChatComponent var3 = (IChatComponent) var2.next();
			var1.appendSibling(var3.createCopy());
		}

		return var1;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_)
			return true;
		else if (!(p_equals_1_ instanceof ChatComponentText))
			return false;
		else {
			final ChatComponentText var2 = (ChatComponentText) p_equals_1_;
			return text.equals(var2.getChatComponentText_TextValue())
					&& super.equals(p_equals_1_);
		}
	}

	/**
	 * Gets the text value of this ChatComponentText. TODO: what are
	 * getUnformattedText and getUnformattedTextForChat missing that made
	 * someone decide to create a third equivalent method that only
	 * ChatComponentText can implement?
	 */
	public String getChatComponentText_TextValue() {
		return text;
	}

	/**
	 * Gets the text of this component, without any special formatting codes
	 * added, for chat. TODO: why is this two different methods?
	 */
	@Override
	public String getUnformattedTextForChat() {
		return text;
	}

	@Override
	public String toString() {
		return "TextComponent{text=\'" + text + '\'' + ", siblings=" + siblings
				+ ", style=" + getChatStyle() + '}';
	}
}
