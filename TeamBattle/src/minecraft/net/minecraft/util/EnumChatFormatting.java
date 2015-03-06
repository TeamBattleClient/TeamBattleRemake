package net.minecraft.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public enum EnumChatFormatting {
	AQUA('b'), BLACK('0'), BLUE('9'), BOLD('l', true), DARK_AQUA('3'), DARK_BLUE(
			'1'), DARK_GRAY('8'), DARK_GREEN('2'), DARK_PURPLE('5'), DARK_RED(
			'4'), GOLD('6'), GRAY('7'), GREEN('a'), ITALIC('o', true), LIGHT_PURPLE(
			'd'), OBFUSCATED('k', true), RED('c'), RESET('r'), STRIKETHROUGH(
			'm', true), UNDERLINE('n', true), WHITE('f'), YELLOW('e');

	/**
	 * Maps a formatting code (e.g., 'f') to its corresponding enum value (e.g.,
	 * WHITE).
	 */
	private static final Map formattingCodeMapping = new HashMap();

	/**
	 * Matches formatting codes that indicate that the client should treat the
	 * following text as bold, recolored, obfuscated, etc.
	 */
	private static final Pattern formattingCodePattern = Pattern.compile("(?i)"
			+ String.valueOf('\u00a7') + "[0-9A-FK-OR]");

	/**
	 * Maps a name (e.g., 'underline') to its corresponding enum value (e.g.,
	 * UNDERLINE).
	 */
	private static final Map nameMapping = new HashMap();

	static {
		final EnumChatFormatting[] var0 = values();
		final int var1 = var0.length;

		for (int var2 = 0; var2 < var1; ++var2) {
			final EnumChatFormatting var3 = var0[var2];
			formattingCodeMapping.put(
					Character.valueOf(var3.getFormattingCode()), var3);
			nameMapping.put(var3.getFriendlyName(), var3);
		}
	}

	/**
	 * Returns a copy of the given string, with formatting codes stripped away.
	 */
	public static String getTextWithoutFormattingCodes(String p_110646_0_) {
		return p_110646_0_ == null ? null : formattingCodePattern.matcher(
				p_110646_0_).replaceAll("");
	}

	/**
	 * Gets all the valid values. Args: @param par0: Whether or not to include
	 * color values. @param par1: Whether or not to include fancy-styling values
	 * (anything that isn't a color value or the "reset" value).
	 */
	public static Collection getValidValues(boolean p_96296_0_,
			boolean p_96296_1_) {
		final ArrayList var2 = new ArrayList();
		final EnumChatFormatting[] var3 = values();
		final int var4 = var3.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			final EnumChatFormatting var6 = var3[var5];

			if ((!var6.isColor() || p_96296_0_)
					&& (!var6.isFancyStyling() || p_96296_1_)) {
				var2.add(var6.getFriendlyName());
			}
		}

		return var2;
	}

	/**
	 * Gets a value by its friendly name; null if the given name does not map to
	 * a defined value.
	 */
	public static EnumChatFormatting getValueByName(String p_96300_0_) {
		return p_96300_0_ == null ? null : (EnumChatFormatting) nameMapping
				.get(p_96300_0_.toLowerCase());
	}

	/**
	 * The control string (section sign + formatting code) that can be inserted
	 * into client-side text to display subsequent text in this format.
	 */
	private final String controlString;

	private final boolean fancyStyling;

	/** The formatting code that produces this format. */
	private final char formattingCode;

	private EnumChatFormatting(char p_i1336_3_) {
		this(p_i1336_3_, false);
	}

	private EnumChatFormatting(char p_i1337_3_, boolean p_i1337_4_) {
		formattingCode = p_i1337_3_;
		fancyStyling = p_i1337_4_;
		controlString = "\u00a7" + p_i1337_3_;
	}

	/**
	 * Gets the formatting code that produces this format.
	 */
	public char getFormattingCode() {
		return formattingCode;
	}

	/**
	 * Gets the friendly name of this value.
	 */
	public String getFriendlyName() {
		return name().toLowerCase();
	}

	/**
	 * Checks if typo is a color.
	 */
	public boolean isColor() {
		return !fancyStyling && this != RESET;
	}

	/**
	 * False if this is just changing the color or resetting; true otherwise.
	 */
	public boolean isFancyStyling() {
		return fancyStyling;
	}

	@Override
	public String toString() {
		return controlString;
	}
}
