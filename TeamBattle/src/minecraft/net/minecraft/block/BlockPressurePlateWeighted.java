package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockPressurePlateWeighted extends BlockBasePressurePlate {
	private final int field_150068_a;

	protected BlockPressurePlateWeighted(String p_i45436_1_,
			Material p_i45436_2_, int p_i45436_3_) {
		super(p_i45436_1_, p_i45436_2_);
		field_150068_a = p_i45436_3_;
	}

	@Override
	public int func_149738_a(World p_149738_1_) {
		return 10;
	}

	@Override
	protected int func_150060_c(int p_150060_1_) {
		return p_150060_1_;
	}

	@Override
	protected int func_150065_e(World p_150065_1_, int p_150065_2_,
			int p_150065_3_, int p_150065_4_) {
		final int var5 = Math.min(
				p_150065_1_.getEntitiesWithinAABB(Entity.class,
						func_150061_a(p_150065_2_, p_150065_3_, p_150065_4_))
						.size(), field_150068_a);

		if (var5 <= 0)
			return 0;
		else {
			final float var6 = (float) Math.min(field_150068_a, var5)
					/ (float) field_150068_a;
			return MathHelper.ceiling_float_int(var6 * 15.0F);
		}
	}

	@Override
	protected int func_150066_d(int p_150066_1_) {
		return p_150066_1_;
	}
}
