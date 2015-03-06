package net.minecraft.world;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class ChunkPosition {
	public final int field_151327_b;
	public final int field_151328_c;
	public final int field_151329_a;

	public ChunkPosition(int p_i45363_1_, int p_i45363_2_, int p_i45363_3_) {
		field_151329_a = p_i45363_1_;
		field_151327_b = p_i45363_2_;
		field_151328_c = p_i45363_3_;
	}

	public ChunkPosition(Vec3 p_i45364_1_) {
		this(MathHelper.floor_double(p_i45364_1_.xCoord), MathHelper
				.floor_double(p_i45364_1_.yCoord), MathHelper
				.floor_double(p_i45364_1_.zCoord));
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (!(p_equals_1_ instanceof ChunkPosition))
			return false;
		else {
			final ChunkPosition var2 = (ChunkPosition) p_equals_1_;
			return var2.field_151329_a == field_151329_a
					&& var2.field_151327_b == field_151327_b
					&& var2.field_151328_c == field_151328_c;
		}
	}

	@Override
	public int hashCode() {
		return field_151329_a * 8976890 + field_151327_b * 981131
				+ field_151328_c;
	}
}
