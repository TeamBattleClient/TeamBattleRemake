package net.minecraft.item;

import net.minecraft.util.EnumChatFormatting;

public enum EnumRarity {
	common(EnumChatFormatting.WHITE, "Common"), epic(
			EnumChatFormatting.LIGHT_PURPLE, "Epic"), rare(
			EnumChatFormatting.AQUA, "Rare"), uncommon(
			EnumChatFormatting.YELLOW, "Uncommon");

	/**
	 * A decimal representation of the hex color codes of a the color assigned
	 * to this rarity type. (13 becomes d as in \247d which is light purple)
	 */
	public final EnumChatFormatting rarityColor;

	/** Rarity name. */
	public final String rarityName;

	private EnumRarity(EnumChatFormatting p_i45349_3_, String p_i45349_4_) {
		rarityColor = p_i45349_3_;
		rarityName = p_i45349_4_;
	}
}
