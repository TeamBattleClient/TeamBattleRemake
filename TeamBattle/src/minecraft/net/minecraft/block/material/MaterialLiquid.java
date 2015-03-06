package net.minecraft.block.material;

public class MaterialLiquid extends Material {

	public MaterialLiquid(MapColor p_i2114_1_) {
		super(p_i2114_1_);
		setReplaceable();
		setNoPushMobility();
	}

	/**
	 * Returns if this material is considered solid or not
	 */
	@Override
	public boolean blocksMovement() {
		return false;
	}

	/**
	 * Returns if blocks of these materials are liquids.
	 */
	@Override
	public boolean isLiquid() {
		return true;
	}

	@Override
	public boolean isSolid() {
		return false;
	}
}
