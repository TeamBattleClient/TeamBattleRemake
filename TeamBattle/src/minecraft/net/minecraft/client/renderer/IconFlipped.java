package net.minecraft.client.renderer;

import net.minecraft.util.IIcon;

public class IconFlipped implements IIcon {
	private final IIcon baseIcon;
	private final boolean flipU;
	private final boolean flipV;

	public IconFlipped(IIcon p_i1560_1_, boolean p_i1560_2_, boolean p_i1560_3_) {
		baseIcon = p_i1560_1_;
		flipU = p_i1560_2_;
		flipV = p_i1560_3_;
	}

	/**
	 * Returns the height of the icon, in pixels.
	 */
	@Override
	public int getIconHeight() {
		return baseIcon.getIconHeight();
	}

	@Override
	public String getIconName() {
		return baseIcon.getIconName();
	}

	/**
	 * Returns the width of the icon, in pixels.
	 */
	@Override
	public int getIconWidth() {
		return baseIcon.getIconWidth();
	}

	/**
	 * Gets a U coordinate on the icon. 0 returns uMin and 16 returns uMax.
	 * Other arguments return in-between values.
	 */
	@Override
	public float getInterpolatedU(double p_94214_1_) {
		final float var3 = getMaxU() - getMinU();
		return getMinU() + var3 * ((float) p_94214_1_ / 16.0F);
	}

	/**
	 * Gets a V coordinate on the icon. 0 returns vMin and 16 returns vMax.
	 * Other arguments return in-between values.
	 */
	@Override
	public float getInterpolatedV(double p_94207_1_) {
		final float var3 = getMaxV() - getMinV();
		return getMinV() + var3 * ((float) p_94207_1_ / 16.0F);
	}

	/**
	 * Returns the maximum U coordinate to use when rendering with this icon.
	 */
	@Override
	public float getMaxU() {
		return flipU ? baseIcon.getMinU() : baseIcon.getMaxU();
	}

	/**
	 * Returns the maximum V coordinate to use when rendering with this icon.
	 */
	@Override
	public float getMaxV() {
		return flipV ? baseIcon.getMinV() : baseIcon.getMaxV();
	}

	/**
	 * Returns the minimum U coordinate to use when rendering with this icon.
	 */
	@Override
	public float getMinU() {
		return flipU ? baseIcon.getMaxU() : baseIcon.getMinU();
	}

	/**
	 * Returns the minimum V coordinate to use when rendering with this icon.
	 */
	@Override
	public float getMinV() {
		return flipV ? baseIcon.getMinV() : baseIcon.getMinV();
	}
}
