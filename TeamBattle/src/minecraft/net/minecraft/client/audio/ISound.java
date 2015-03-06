package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public interface ISound {
	public static enum AttenuationType {
		LINEAR("LINEAR", 1, 2), NONE("NONE", 0, 0);
		private final int field_148589_c;

		private AttenuationType(String p_i45110_1_, int p_i45110_2_,
				int p_i45110_3_) {
			field_148589_c = p_i45110_3_;
		}

		public int func_148586_a() {
			return field_148589_c;
		}
	}

	float func_147649_g();

	ResourceLocation func_147650_b();

	float func_147651_i();

	int func_147652_d();

	float func_147653_e();

	float func_147654_h();

	float func_147655_f();

	ISound.AttenuationType func_147656_j();

	boolean func_147657_c();
}
