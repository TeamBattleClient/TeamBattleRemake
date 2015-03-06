package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public class SoundPoolEntry {
	private double field_148653_d;
	private final boolean field_148654_b;
	private double field_148655_c;
	private final ResourceLocation field_148656_a;

	public SoundPoolEntry(ResourceLocation p_i45113_1_, double p_i45113_2_,
			double p_i45113_4_, boolean p_i45113_6_) {
		field_148656_a = p_i45113_1_;
		field_148655_c = p_i45113_2_;
		field_148653_d = p_i45113_4_;
		field_148654_b = p_i45113_6_;
	}

	public SoundPoolEntry(SoundPoolEntry p_i45114_1_) {
		field_148656_a = p_i45114_1_.field_148656_a;
		field_148655_c = p_i45114_1_.field_148655_c;
		field_148653_d = p_i45114_1_.field_148653_d;
		field_148654_b = p_i45114_1_.field_148654_b;
	}

	public void func_148647_b(double p_148647_1_) {
		field_148653_d = p_148647_1_;
	}

	public boolean func_148648_d() {
		return field_148654_b;
	}

	public double func_148649_c() {
		return field_148653_d;
	}

	public double func_148650_b() {
		return field_148655_c;
	}

	public void func_148651_a(double p_148651_1_) {
		field_148655_c = p_148651_1_;
	}

	public ResourceLocation func_148652_a() {
		return field_148656_a;
	}
}
