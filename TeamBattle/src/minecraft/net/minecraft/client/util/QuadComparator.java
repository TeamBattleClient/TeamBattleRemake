package net.minecraft.client.util;

import java.util.Comparator;

public class QuadComparator implements Comparator {
	private final int[] field_147627_d;
	private final float field_147628_b;
	private final float field_147629_c;
	private final float field_147630_a;

	public QuadComparator(int[] p_i45077_1_, float p_i45077_2_,
			float p_i45077_3_, float p_i45077_4_) {
		field_147627_d = p_i45077_1_;
		field_147630_a = p_i45077_2_;
		field_147628_b = p_i45077_3_;
		field_147629_c = p_i45077_4_;
	}

	public int compare(Integer p_compare_1_, Integer p_compare_2_) {
		final float var3 = Float.intBitsToFloat(field_147627_d[p_compare_1_
				.intValue()]) - field_147630_a;
		final float var4 = Float.intBitsToFloat(field_147627_d[p_compare_1_
				.intValue() + 1]) - field_147628_b;
		final float var5 = Float.intBitsToFloat(field_147627_d[p_compare_1_
				.intValue() + 2]) - field_147629_c;
		final float var6 = Float.intBitsToFloat(field_147627_d[p_compare_1_
				.intValue() + 8]) - field_147630_a;
		final float var7 = Float.intBitsToFloat(field_147627_d[p_compare_1_
				.intValue() + 9]) - field_147628_b;
		final float var8 = Float.intBitsToFloat(field_147627_d[p_compare_1_
				.intValue() + 10]) - field_147629_c;
		final float var9 = Float.intBitsToFloat(field_147627_d[p_compare_1_
				.intValue() + 16]) - field_147630_a;
		final float var10 = Float.intBitsToFloat(field_147627_d[p_compare_1_
				.intValue() + 17]) - field_147628_b;
		final float var11 = Float.intBitsToFloat(field_147627_d[p_compare_1_
				.intValue() + 18]) - field_147629_c;
		final float var12 = Float.intBitsToFloat(field_147627_d[p_compare_1_
				.intValue() + 24]) - field_147630_a;
		final float var13 = Float.intBitsToFloat(field_147627_d[p_compare_1_
				.intValue() + 25]) - field_147628_b;
		final float var14 = Float.intBitsToFloat(field_147627_d[p_compare_1_
				.intValue() + 26]) - field_147629_c;
		final float var15 = Float.intBitsToFloat(field_147627_d[p_compare_2_
				.intValue()]) - field_147630_a;
		final float var16 = Float.intBitsToFloat(field_147627_d[p_compare_2_
				.intValue() + 1]) - field_147628_b;
		final float var17 = Float.intBitsToFloat(field_147627_d[p_compare_2_
				.intValue() + 2]) - field_147629_c;
		final float var18 = Float.intBitsToFloat(field_147627_d[p_compare_2_
				.intValue() + 8]) - field_147630_a;
		final float var19 = Float.intBitsToFloat(field_147627_d[p_compare_2_
				.intValue() + 9]) - field_147628_b;
		final float var20 = Float.intBitsToFloat(field_147627_d[p_compare_2_
				.intValue() + 10]) - field_147629_c;
		final float var21 = Float.intBitsToFloat(field_147627_d[p_compare_2_
				.intValue() + 16]) - field_147630_a;
		final float var22 = Float.intBitsToFloat(field_147627_d[p_compare_2_
				.intValue() + 17]) - field_147628_b;
		final float var23 = Float.intBitsToFloat(field_147627_d[p_compare_2_
				.intValue() + 18]) - field_147629_c;
		final float var24 = Float.intBitsToFloat(field_147627_d[p_compare_2_
				.intValue() + 24]) - field_147630_a;
		final float var25 = Float.intBitsToFloat(field_147627_d[p_compare_2_
				.intValue() + 25]) - field_147628_b;
		final float var26 = Float.intBitsToFloat(field_147627_d[p_compare_2_
				.intValue() + 26]) - field_147629_c;
		final float var27 = (var3 + var6 + var9 + var12) * 0.25F;
		final float var28 = (var4 + var7 + var10 + var13) * 0.25F;
		final float var29 = (var5 + var8 + var11 + var14) * 0.25F;
		final float var30 = (var15 + var18 + var21 + var24) * 0.25F;
		final float var31 = (var16 + var19 + var22 + var25) * 0.25F;
		final float var32 = (var17 + var20 + var23 + var26) * 0.25F;
		final float var33 = var27 * var27 + var28 * var28 + var29 * var29;
		final float var34 = var30 * var30 + var31 * var31 + var32 * var32;
		return Float.compare(var34, var33);
	}

	@Override
	public int compare(Object p_compare_1_, Object p_compare_2_) {
		return this.compare((Integer) p_compare_1_, (Integer) p_compare_2_);
	}
}
