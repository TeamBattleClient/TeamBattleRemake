package net.minecraft.util;

public class MouseFilter {
	private float field_76334_b;
	private float field_76335_c;
	private float field_76336_a;

	/**
	 * Smooths mouse input
	 */
	public float smooth(float p_76333_1_, float p_76333_2_) {
		field_76336_a += p_76333_1_;
		p_76333_1_ = (field_76336_a - field_76334_b) * p_76333_2_;
		field_76335_c += (p_76333_1_ - field_76335_c) * 0.5F;

		if (p_76333_1_ > 0.0F && p_76333_1_ > field_76335_c
				|| p_76333_1_ < 0.0F && p_76333_1_ < field_76335_c) {
			p_76333_1_ = field_76335_c;
		}

		field_76334_b += p_76333_1_;
		return p_76333_1_;
	}
}
