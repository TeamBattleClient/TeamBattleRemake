package net.minecraft.dispenser;

public class PositionImpl implements IPosition {
	protected final double x;
	protected final double y;
	protected final double z;

	public PositionImpl(double p_i1368_1_, double p_i1368_3_, double p_i1368_5_) {
		x = p_i1368_1_;
		y = p_i1368_3_;
		z = p_i1368_5_;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public double getZ() {
		return z;
	}
}
