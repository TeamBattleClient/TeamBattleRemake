package net.minecraft.block.material;

public class MaterialTransparent extends Material {

	public MaterialTransparent(MapColor p_i2113_1_) {
		super(p_i2113_1_);
		setReplaceable();
	}

	/**
	 * Returns if this material is considered solid or not
	 */
	@Override
	public boolean blocksMovement() {
		return false;
	}

	/**
	 * Will prevent grass from growing on dirt underneath and kill any grass
	 * below it if it returns true
	 */
	@Override
	public boolean getCanBlockGrass() {
		return false;
	}

	@Override
	public boolean isSolid() {
		return false;
	}
}
