package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public abstract class PositionedSound implements ISound {
	protected float field_147658_f;
	protected boolean field_147659_g = false;
	protected float field_147660_d;
	protected float field_147661_e;
	protected float field_147662_b = 1.0F;
	protected float field_147663_c = 1.0F;
	protected final ResourceLocation field_147664_a;
	protected int field_147665_h = 0;
	protected ISound.AttenuationType field_147666_i;

	protected PositionedSound(ResourceLocation p_i45103_1_) {
		field_147666_i = ISound.AttenuationType.LINEAR;
		field_147664_a = p_i45103_1_;
	}

	@Override
	public float func_147649_g() {
		return field_147660_d;
	}

	@Override
	public ResourceLocation func_147650_b() {
		return field_147664_a;
	}

	@Override
	public float func_147651_i() {
		return field_147658_f;
	}

	@Override
	public int func_147652_d() {
		return field_147665_h;
	}

	@Override
	public float func_147653_e() {
		return field_147662_b;
	}

	@Override
	public float func_147654_h() {
		return field_147661_e;
	}

	@Override
	public float func_147655_f() {
		return field_147663_c;
	}

	@Override
	public ISound.AttenuationType func_147656_j() {
		return field_147666_i;
	}

	@Override
	public boolean func_147657_c() {
		return field_147659_g;
	}
}
